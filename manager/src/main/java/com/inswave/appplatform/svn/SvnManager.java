package com.inswave.appplatform.svn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.svn.domain.*;
import com.inswave.appplatform.util.DateUtil;
import com.inswave.appplatform.util.PathUtil;
import com.inswave.appplatform.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.subversion.javahl.types.Revision;
import org.tigris.subversion.javahl.Depth;
import org.tigris.subversion.javahl.DirEntry;
import org.tigris.subversion.javahl.RevisionRange;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Locks 변환 시 정상인지 여부 : Map<String, Lock>
 * Lock 상황을 만드려면 Commit을 해야함. rw
 * Commit 하려면 계정설정이 필요함. authz, passwd
 * 위 작업을 하려면 Repository 생성 시 초기 svnserve 설정을 해야함.
 * ...
 */
@Slf4j
public class SvnManager {
    public static  boolean                                configured                 = false;
    private static int                                    port                       = 3690;
    private static String                                 svnAdminAccount;
    private static String                                 svnAdminPassword;
    private static Path                                   svnRepository;
    private static Path                                   serverPid;
    private static Path                                   binPath;
    private static Path                                   binPathSvnserve;
    private static Path                                   binPathSvnadmin;
    private static Path                                   binPathJavahl;
    private static SvnServer                              svnServer;
    private static SvnManager                             svnManager;
    private static org.tigris.subversion.javahl.SVNClient svnClientTigris;
    private static SVNClient                              svnClientApache;
    private        Map<String, RepositoryListItemCache>   repositoryListItemCacheMap = new ConcurrentHashMap<>();

    public static SvnManager getInstance() {
        if (svnManager == null) {
            //            if (configured) {
            try {
                svnManager = new SvnManager();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.tigris.subversion.javahl.ClientException e) {
                e.printStackTrace();
            }
            //            } else {
            //                log.error("SVN is not configured.");
            //            }
        }
        return svnManager;
    }

    private SvnManager() throws IOException, org.tigris.subversion.javahl.ClientException {
        init();
    }

    public void init() throws IOException, org.tigris.subversion.javahl.ClientException {
        configured = initSvnConfig();
        if (configured) {
            startSvnServer();
            startSvnClient();
            initSvnRepository();
            log.info("[Success] SvnManager is initialized.");
        } else {
            log.error("[Failed] SvnManager is not initialized.");
        }
    }

    private void initSvnRepository() {
        String DEFAULT_REPO_NAME = "default";
        if (getRepositories().size() == 0) {
            try {
                createRepository(DEFAULT_REPO_NAME);
                log.info("[Success] Default SVN Repository has been created. (Repository Name : {})", DEFAULT_REPO_NAME);
            } catch (IOException e) {
                log.error("[Failed] Default SVN Repository creation is failed. (Repository Name : {})", DEFAULT_REPO_NAME);
                e.printStackTrace();
            } catch (ClientException e) {
                log.error("[Failed] Default SVN Repository creation is failed. (Repository Name : {})", DEFAULT_REPO_NAME);
                e.printStackTrace();
            }
        }
    }

    /**
     * SVN을 사용하기 위한 설정값이 모두 존재하는지 검증
     */
    public synchronized boolean initSvnConfig() {
        Config config = Config.getInstance();
        port = config.getSvnServerPort();
        String repo = config.getSvnServerRepo();
        String binPathSvnserve = config.getSvnPathBinSvnserve();
        String binPathSvnadmin = config.getSvnPathBinSvnadmin();
        String binPathJavahl = config.getSvnPathBinJavahl();
        String svnServerAdminUsername = config.getSvnServerAdminUsername();
        String svnServerAdminPassword = config.getSvnServerAdminPassword();

        //입력값 존재여부&유효성 검증
        try {
            if (!StringUtils.isEmpty(repo) && Files.exists(Paths.get(repo))) {
                this.svnRepository = Paths.get(repo);
                this.serverPid = Paths.get(repo, "pid");
            } else {
                log.error("Repository path is not configured.");
                throw new Exception("Repository path is not configured.");
            }
            if (!StringUtils.isEmpty(binPathSvnserve) && Files.exists(Paths.get(binPathSvnserve))) {
                this.binPathSvnserve = Paths.get(binPathSvnserve);
            } else {
                log.error("svnserve path is not configured.");
                throw new Exception("svnserve path is not configured.");
            }
            if (!StringUtils.isEmpty(binPathSvnadmin) && Files.exists(Paths.get(binPathSvnadmin))) {
                this.binPathSvnadmin = Paths.get(binPathSvnadmin);
            } else {
                log.error("svnadmin path is not configured.");
                throw new Exception("svnadmin path is not configured.");
            }
            if (!StringUtils.isEmpty(binPathJavahl) && Files.exists(Paths.get(binPathJavahl))) {
                this.binPathJavahl = Paths.get(binPathJavahl);
                this.binPath = this.binPathJavahl.getParent();
            } else {
                log.error("javahl path is not configured.");
                throw new Exception("javahl path is not configured.");
            }
            if (!StringUtils.isEmpty(svnServerAdminUsername) || !StringUtils.isEmpty(svnServerAdminPassword)) {
                svnAdminAccount = svnServerAdminUsername;
                svnAdminPassword = svnServerAdminPassword;
            } else {
                log.error("Admin account(username or password) is not configured.");
                throw new Exception("Admin account(username or password) is not configured.");
            }
        } catch (Exception e) {
            //            e.printStackTrace();
            return false;
        }

        String javaLibraryPath = System.getProperty("java.library.path");
        javaLibraryPath = javaLibraryPath != null ? javaLibraryPath + ";" + this.binPath.toAbsolutePath().toString() : this.binPath.toAbsolutePath().toString();
        System.setProperty("java.library.path", javaLibraryPath + ";");
        log.info("java.library.path : {}", javaLibraryPath);

        String subversionNativeLibrary = System.getProperty("subversion.native.library");
        subversionNativeLibrary = subversionNativeLibrary != null ? subversionNativeLibrary + ";" + this.binPathJavahl.toAbsolutePath().toString() : this.binPathJavahl.toAbsolutePath().toString();
        System.setProperty("subversion.native.library", subversionNativeLibrary + ";");
        log.info("subversion.native.library : {}", subversionNativeLibrary);

        log.info("SVN is successfully configured.");
        log.info("SVN config - port : {}", port);
        log.info("SVN config - repo : {}", this.svnRepository);
        log.info("SVN config - binPath : {}", this.binPath);
        log.info("SVN config - binPathSvnserve : {}", this.binPathSvnserve);
        log.info("SVN config - binPathSvnadmin : {}", this.binPathSvnadmin);
        log.info("SVN config - binPathJavahl : {}", this.binPathJavahl);
        log.info("SVN config - svnAdminAccount : {}", this.svnAdminAccount);
        log.info("SVN config - svnAdminPassword : ...", this.svnAdminPassword);

        return true;
    }

    public void startSvnServer() throws IOException {
        svnServer = SvnServer.startServer(port, svnRepository.toFile());
    }

    @PreDestroy
    public void stopSvnServer() throws Exception {
        svnServer.kill();
        svnClientTigris.dispose();
    }

    public synchronized void startSvnClient() throws org.tigris.subversion.javahl.ClientException {
        svnClientTigris = new org.tigris.subversion.javahl.SVNClient();
        svnClientApache = svnClientTigris.getSVNClient();
        svnClientTigris.username(svnAdminAccount);
        svnClientTigris.password(svnAdminPassword);
        log.info("SVN client - getAdminDirectoryName() : {}", svnClientTigris.getAdminDirectoryName());
        log.info("SVN client - getConfigDirectory() : {}", svnClientTigris.getConfigDirectory());
        log.info("SVN client - getVersion() : {}", svnClientTigris.getVersion());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized long getLatestRevision(String repositoryName) throws SubversionException {
        ISVNRemote remote = svnClientApache.openRemoteSession(getSvnServerUrl(repositoryName));
        Long revision = remote.getLatestRevision();
        if (remote != null) {
            remote.dispose();
        }
        return revision;
    }

    public synchronized boolean existsRepository(String repoNameTarget) throws IOException {
        AtomicBoolean isExists = new AtomicBoolean(false);
        Optional<Path> result = Files.walk(svnRepository, 1)
                                     .filter(path -> Files.isDirectory(path)
                                                     && !svnRepository.equals(path)
                                                     && path.getFileName().toString().equals(repoNameTarget))
                                     .findFirst();
        return result.isPresent();
    }

    public synchronized List<RepositoryVO> getRepositories() {
        List<RepositoryVO> repos = new ArrayList<>();
        try {
            Files.walk(svnRepository, 1)
                 .filter(path -> Files.isDirectory(path) && !svnRepository.equals(path))
                 .filter(path -> !path.getFileName().toString().endsWith("-local"))
                 .filter(path -> Files.exists(path.resolve("conf").resolve("accessMap.json")))
                 .forEach(path -> {
                     ISVNRemote remote = null;
                     try {
                         String repoName = path.getFileName().toString();
                         remote = svnClientApache.openRemoteSession(getSvnServerUrl(repoName));

                         //                         BlameVO blameCallback = new BlameVO();
                         //
                         //                         svnClientTigris.blame(
                         //                         getSvnServerUrl(repoName)+"/testd/test1.txt", //String path
                         //                         Revision.HEAD, //Revision pegRevision
                         //                         Revision.HEAD, //Revision revisionStart
                         //                         Revision.HEAD, //Revision revisionEnd
                         //                         true, //boolean ignoreMimeType
                         //                         true, //boolean includeMergedRevisions
                         //                         blameCallback//BlameCallback3 callback
                         //                         );

                         RepositoryVO repository = RepositoryVO.builder()
                                                               .name(repoName)
                                                               .path(path.toString())
                                                               .uuid(remote.getReposUUID())
                                                               .revision(remote.getLatestRevision())
                                                               //                                                               .locks(remote.getLocks("/", org.apache.subversion.javahl.types.Depth.infinity)
                                                               //                                                                            .values()
                                                               //                                                                            .stream()
                                                               //                                                                            .map(LockVO::from)
                                                               //                                                                            .collect(Collectors.toList()))
                                                               .createdDate(DateUtil.getDateFromPath(path, "yyyy-MM-dd hh:mm:ss"))
                                                               .build();
                         repos.add(repository);
                     } catch (SubversionException e) {
                         log.debug("{}", e.getMessage());
                     } catch (IOException e) {
                         log.debug("{}", e.getMessage());
                     } finally {
                         if (remote != null) {
                             remote.dispose();
                         }
                     }
                 });
        } catch (IOException e) {
            log.debug("{}", e.getMessage());
        }
        return repos;
    }

    //Repo: 생성,삭제
    public synchronized void createRepository(String repositoryName) throws IOException, ClientException {
        Path repositoryPath = svnRepository.resolve(repositoryName);
        if (!Files.exists(repositoryPath)) {
            Files.createDirectories(repositoryPath);
            SVNRepos repo = new SVNRepos();
            repo.create(repositoryPath.toFile(), true, true, null, SVNRepos.FSFS);
            String warning = "### Do not edit this file your self.\n"
                             + "### System update this file Automatied.\n"
                             + "### Please use Web Admin UI.\n";
            String svnserveConf = warning + "[general]\n"
                                  + "anon-access = none\n"
                                  + "auth-access = write\n"
                                  + "password-db = passwd\n"
                                  + "authz-db = authz";
            String authz = warning + "[/]\n" + svnAdminAccount + " = rw";
            String passwd = warning + "[users]\n" + svnAdminAccount + " = " + svnAdminPassword;
            String accessMap = "{ \"config\": "
                               + "{ "
                               + "\"solutionPath\": \"Solution\","
                               + "\"distributePathSrc\": \"Solution\","
                               + "\"distributePath\": \"Solution\","
                               + "\"menuPath\": \"Solution\","
                               + "\"adminId\": \"" + svnAdminAccount + "\","
                               + "\"adminPw\": \"" + svnAdminPassword + "\""
                               + "},"
                               + "\"business\":[],"
                               + "\"businessAuth\":[],"
                               + "\"directoryAuth\":[],"
                               + "\"user\":[],"
                               + "\"version\":" + ZonedDateTime.now().toEpochSecond()
                               + "}";

            // Repository 초기 설정
            Files.write(repositoryPath.resolve("conf").resolve("svnserve.conf"), Collections.singleton(svnserveConf));
            Files.write(repositoryPath.resolve("conf").resolve("passwd"), Collections.singleton(passwd));
            Files.write(repositoryPath.resolve("conf").resolve("authz"), Collections.singleton(authz));
            Files.write(repositoryPath.resolve("conf").resolve("accessMap.json"), Collections.singleton(accessMap));
        }
    }

    public synchronized void removeRepository(String repositoryName) throws IOException {
        Path sourceRepositoryPath = svnRepository.resolve(repositoryName);
        Path deletedRepositoryPath = svnRepository.getParent().resolve(svnRepository.getFileName().toString() + "-deleted");
        Path targetRepositoryPath = deletedRepositoryPath.resolve(repositoryName + DateUtil.getCurrentDate("-yyyyMMddhhmmss"));
        if (Files.exists(sourceRepositoryPath)) {
            if (!Files.exists(deletedRepositoryPath)) {
                Files.createDirectory(deletedRepositoryPath);
            }
            //            FileUtils.forceDelete(repositoryPath.toFile()); // 삭제 O
            Files.move(sourceRepositoryPath, targetRepositoryPath); // 삭제 X : 백업
        }
    }

    public synchronized JsonNode getAccessMapJson(String repoName) throws IOException {
        String accessMap = svnManager.getAccessMap(repoName);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(accessMap, JsonNode.class);
    }

    public synchronized String getAccessMap(String repositoryName) throws IOException {
        Path repositoryPath = svnRepository.resolve(repositoryName);
        Path accessMapPath = repositoryPath.resolve("conf").resolve("accessMap.json");

        return FileUtils.readFileToString(accessMapPath.toFile(), "UTF-8");//write(filePath.toFile(), contents, "UTF-8");

        //        return new String(Files.readAllBytes(accessMapPath));
    }

    public synchronized void setConfFile(String repositoryName, String contents, String fileName) throws IOException {
        //        String warning = "### Do not edit this file your self.\n"
        //                         + "### System update this file Automatied.\n"
        //                         + "### Please use Web Admin UI.\n";
        //        contents = warning + contents;
        Path repositoryPath = svnRepository.resolve(repositoryName);
        Path filePath = repositoryPath.resolve("conf").resolve(fileName);
        FileUtils.write(filePath.toFile(), contents, "UTF-8");
        //        Files.write(filePath, Collections.singleton(contents));
    }

    public synchronized List<DirectoryVO> getDirectories(String repositoryName) throws org.tigris.subversion.javahl.ClientException {
        Path repositoryPath = svnRepository.resolve(repositoryName);
        Path solutionPath;
        DirectoryCallbackVO dirVO = new DirectoryCallbackVO();
        svnClientTigris.list(getSvnServerUrl(repositoryName),//String url,
                             org.tigris.subversion.javahl.Revision.COMMITTED,//Revision revision,
                             org.tigris.subversion.javahl.Revision.COMMITTED,//Revision pegRevision,
                             Depth.immediates,//int depth,
                             DirEntry.Fields.all,//int direntFields,
                             false,//boolean fetchLocks,
                             dirVO//ListCallback callback
        );
        return dirVO.getDirectoryVOs();
    }

    public synchronized void addDirectory(String repoName, String message, String... dir) throws org.tigris.subversion.javahl.ClientException {
        svnClientTigris.mkdir(
        new String[] { getSvnServerUrl(repoName) + "/" + String.join("/", dir) },
        message,
        true,
        null
        );
    }

    public synchronized void addDirectories(String[] dirs, String message) throws org.tigris.subversion.javahl.ClientException {
        svnClientTigris.mkdir(
        dirs,
        message,
        true,
        null
        );
    }

    public void delDirectory(String repoName, String dirName) throws org.tigris.subversion.javahl.ClientException {
        // TODO. 디렉토리 삭제는 SVN에서 직접수행한다.
    }

/*
    //디렉토리(업무): 추가,제거,동기화
    public void addDirectory(String repoName, String dirName) throws SubversionException, org.tigris.subversion.javahl.ClientException {
        svnClientTigris.checkout(getSvnServerUrl(repoName),
                                 svnRepository.resolve(repoName + "-local").toString(),
                                 org.tigris.subversion.javahl.Revision.HEAD,
                                 org.tigris.subversion.javahl.Revision.HEAD,
                                 Depth.infinity,
                                 true,
                                 true);
        svnClientTigris.mkdir(new String[] { svnRepository.resolve(repoName + "-local").resolve("Solution").resolve(dirName).toString() },
                              "Add Solution/" + dirName + " dir.1",
                              true,
                              null);
        svnClientTigris.commit(new String[] { svnRepository.resolve(repoName + "-local").resolve("Solution").toString() },
                               "Add Solution/" + dirName + " dir.2",
                               Depth.infinity,
                               true,
                               false,
                               null,
                               null);
        svnClientTigris.commit(new String[] { svnRepository.resolve(repoName + "-local").resolve("Solution").resolve(dirName).toString() },
                               "Add Solution/" + dirName + " dir.2",
                               Depth.infinity,
                               true,
                               false,
                               null,
                               null);
        //        log.info("client.getAdminDirectoryName() : {}", client.getAdminDirectoryName());
        //        log.info("client.getConfigDirectory() : {}", client.getConfigDirectory());
        //        log.info("client.getVersion() : {}", client.getVersion());
        //        //        client.mkdir(new String[] { Paths.get("Solution", dirName).toString() }, "");
        //        client.mkdir(new String[] { "svn://localhost/REPO3/Solution/" + dirName }, "Add AA dir.");

        //        ISVNRemote remote = factory.openRemoteSession("svn://localhost/REPO3");
        //        log.info("remote.getReposRootUrl() : {}", remote.getReposRootUrl());
        //        log.info("remote.getLatestRevision() : {}", remote.getLatestRevision());
        //        log.info("remote.getSessionUrl() : {}", remote.getSessionUrl());
        //
        //        ISVNEditor editor = remote.getCommitEditor(null, null, null, false);
        //        editor.addDirectory("/Solution/" + dirName, null, null, Revision.SVN_INVALID_REVNUM);
        //        editor.complete();

        //        Path dir = Paths.get("Solution", dirName);
        //        SVNClient client = new SVNClient();
        //        client.checkout();
        //        Set<String> paths = new HashSet<>();
        //        paths.add(dir.toFile().getPath().replace('\\', '/'));
        //
        //        client.mkdir(paths, false, null, null, null);
    }
*/
    //사용자: 추가,제거,동기화
    //권한: 추가,제거,동기화

    public static boolean isConfigured() {
        return configured;
    }

    public static int getPort() {
        return port;
    }

    public static Path getSvnRepository() {
        return svnRepository;
    }

    public static Path getServerPid() {
        return serverPid;
    }

    public static Path getBinPathSvnserve() {
        return binPathSvnserve;
    }

    public static Path getBinPathSvnadmin() {
        return binPathSvnadmin;
    }

    public static Path getBinPathJavahl() {
        return binPathJavahl;
    }

    public static SvnServer getSvnServer() {
        return svnServer;
    }

    public static String getSvnServerUrl() {
        return "svn://localhost:" + Config.getInstance().getSvnServerPort();
    }

    public static String getSvnServerUrl(String repoName) {
        //        log.debug("getSvnServerUrl : {}", getSvnServerUrl() + "/" + repoName);
        return getSvnServerUrl() + "/" + repoName;
    }

    public static Path getSvnWorkingCopyPath(String repoName) {
        return svnRepository.resolve(repoName + "-local");
    }

    public synchronized void setLock(String repoName, List<String> paths, String comments, Boolean force) throws ClientException {
        updateWorkingCopy(repoName);

        List<String> pathParam = new ArrayList<>();
        paths.forEach(path -> {
            pathParam.add(PathUtil.concat(getSvnWorkingCopyPath(repoName), path, "/").toString());
        });
        svnClientApache.lock(new HashSet<>(pathParam), comments, force);
    }

    public synchronized void setUnlock(String repoName, List<String> paths, Boolean force) throws ClientException, org.tigris.subversion.javahl.ClientException {
        updateWorkingCopy(repoName);

        List<String> pathParam = new ArrayList<>();
        paths.forEach(path -> {
            pathParam.add(PathUtil.concat(getSvnWorkingCopyPath(repoName), path, "/").toString());
        });
        svnClientApache.unlock(new HashSet<>(pathParam), force);
    }

    public RepositoryListItemCache getRepositoryListItemCacheMap(String repositoryName) {
        return repositoryListItemCacheMap.get(repositoryName);
    }

    public List<SvnItemVO> getRepositoryListItemCache(String repositoryName) {
        RepositoryListItemCache repositoryListItemCache = repositoryListItemCacheMap.get(repositoryName);
        if (Objects.nonNull(repositoryListItemCache)) {
            return repositoryListItemCache.getCache();
        }
        return new ArrayList();
    }

    public RepositoryListItemCache setRepositoryListItemCache(String repositoryName, long revision, long accessMapVersion, List<SvnItemVO> entityList) {
        return repositoryListItemCacheMap.put(repositoryName, RepositoryListItemCache.builder()
                                                                                     .cache(entityList)
                                                                                     .revision(revision)
                                                                                     .accessMapVersion(accessMapVersion)
                                                                                     .build());
    }

    public void updateRepositoryCache(String repoName, long revision, long accessMapVersion) throws ClientException, IOException {
        log.info("updateRepositoryCache : repoName({}) / revision({}) / accessMapversion({})", repoName, revision, accessMapVersion);
        setRepositoryListItemCache(repoName, revision, accessMapVersion, getRepositoryListItem(repoName));
    }

    public synchronized List<SvnItemVO> getRepositoryListItem(String repoName) throws ClientException, IOException {
        return getRepositoryListItem(repoName, Revision.HEAD, Revision.HEAD);
    }

    public synchronized List<SvnItemVO> getRepositoryListItem(String repoName, Revision revision, Revision pegRevision) throws ClientException, IOException {
        JsonNode accessMap = getAccessMapJson(repoName);
        String solutionPath = accessMap.at("/config/solutionPath").asText();
        ArrayNode business = (ArrayNode) accessMap.at("/business"); // id, name, auth []
        ArrayNode businessAuth = (ArrayNode) accessMap.at("/businessAuth");     // business, name, id
        ArrayNode user = (ArrayNode) accessMap.at("/user");  // id, name, email, role

        Map<String, String[]> userInfoMap = new HashMap<>();
        Map<String, String> businessInfoMap = new HashMap<>();
        List<String[]> authInfoList = new ArrayList();
        business.forEach(node -> {
            businessInfoMap.put(node.get("id").asText(), node.get("name").asText());
        });
        user.forEach(node -> {
            userInfoMap.put(node.get("id").asText(""),
                            new String[] {
                            node.get("name") != null ? node.get("name").asText("") : "",
                            node.get("email") != null ? node.get("email").asText("") : "",
                            node.get("role") != null ? node.get("role").asText("") : ""
                            });
        });
        businessAuth.forEach(node -> {
            authInfoList.add(
            new String[] {
            StringUtil.rebuildPath("/", solutionPath, node.get("business").asText(""), node.get("id").asText("")),
            node.get("business").asText(""),  //업무
            businessInfoMap.get(node.get("business").asText("")),  //업무명
            node.get("id").asText(""),        //권한코드
            node.get("name").asText("")       //권한명
            });
        });

        ListItemVO listItemVO = new ListItemVO();
        svnClientApache.list(getSvnServerUrl(repoName),                                      // String url,
                             revision,                                                       // Revision revision,
                             pegRevision,                                                    // Revision pegRevision,
                             null,                                                    // List<String> patterns,
                             org.apache.subversion.javahl.types.Depth.infinity,              // Depth depth,
                             DirEntry.Fields.all,                                            // int direntFields,
                             true,                                                  // boolean fetchLocks,
                             true,                                              // boolean includeExternals,
                             listItemVO                                                      // ListItemCallback callback)
        );
        List<LogMessageVO> logMessageVOS = new ArrayList<>();
        try {
            logMessageVOS = getLogMessage(repoName,
                                          true,
                                          org.tigris.subversion.javahl.Revision.START,
                                          org.tigris.subversion.javahl.Revision.START);
        } catch (org.tigris.subversion.javahl.ClientException e) {
            e.printStackTrace();
        }

        Map<String, Integer> commitCountMap = new HashMap<>();
        logMessageVOS.forEach(logMessageVO -> {
            logMessageVO.getChangedFiles().forEach(logMessageFileVO -> {
                Integer count = commitCountMap.put(logMessageFileVO.getPath(), 1);
                if (count != null) {
                    commitCountMap.put(logMessageFileVO.getPath(), count + 1);
                }
            });
        });

        List<SvnItemVO> result = SvnItemVO.from(listItemVO.getList())
                                          .stream()
                                          .map(svnItemVO -> {
                                              String[] userInfo = userInfoMap.get(svnItemVO.getLastAuthor());
                                              if (userInfo != null) {
                                                  svnItemVO.setLastAuthorName(userInfo[0]);
                                                  svnItemVO.setLastAuthorEmail(userInfo[1]);
                                                  svnItemVO.setLastAuthorRole(userInfo[2]);
                                              }
                                              Optional<String[]> authInfo = authInfoList.stream().filter(info -> svnItemVO.getPath().startsWith(info[0])).findFirst();
                                              if (authInfo.isPresent()) {
                                                  svnItemVO.setBusiness(authInfo.get()[1]);
                                                  svnItemVO.setBusinessName(authInfo.get()[2]);
                                                  svnItemVO.setBusinessAuth(authInfo.get()[3]);
                                                  svnItemVO.setBusinessAuthName(authInfo.get()[4]);
                                                  svnItemVO.setCommitCount(commitCountMap.get("/" + svnItemVO.getPath()));
                                                  if (svnItemVO.getNodeKind().equals("file")) {
                                                      String[] fileNames = svnItemVO.getPath().split("/");
                                                      String fileName = fileNames[fileNames.length - 1];
                                                      svnItemVO.setMenuPath(String.format("/Solution/%s/%s/%s", authInfo.get()[1], authInfo.get()[3], fileName));
                                                  }
                                              }
                                              return svnItemVO;
                                          })
                                          .collect(Collectors.toList());

        return result;
    }

    public synchronized long updateWorkingCopy(String repoName) {
        try {
            return svnClientApache.checkout(getSvnServerUrl(repoName),
                                            getSvnWorkingCopyPath(repoName).toString(),
                                            Revision.HEAD,
                                            Revision.HEAD,
                                            org.apache.subversion.javahl.types.Depth.infinity,
                                            true,
                                            true);
        } catch (ClientException e) {
            log.warn("[{}] SVN Checkout 오류 (message={})", repoName, e.getMessage());
            e.printStackTrace();
            try {
                svnClientApache.cleanup(getSvnWorkingCopyPath(repoName).toString());
            } catch (ClientException e2) {
                log.warn("[{}] SVN cleanup 오류 (message={})", repoName, e.getMessage());
                e2.printStackTrace();
            }
        }
        return -1;
    }

    public synchronized List<LogMessageVO> getLogMessage(String repoName, boolean includeChangeFiles) throws org.tigris.subversion.javahl.ClientException {
        return getLogMessage(repoName, includeChangeFiles, org.tigris.subversion.javahl.Revision.START, org.tigris.subversion.javahl.Revision.HEAD);
    }

    public synchronized List<LogMessageVO> getLogMessage(String repoName,
                                                         boolean includeChangeFiles,
                                                         org.tigris.subversion.javahl.Revision startRevision,
                                                         org.tigris.subversion.javahl.Revision endRevision) throws org.tigris.subversion.javahl.ClientException {
        RevisionRange revisionRange = new RevisionRange(startRevision, endRevision);

        LogMessageVO logMessageVO = new LogMessageVO();
        logMessageVO.setUseChangedFiles(includeChangeFiles);

        svnClientTigris.logMessages(
        getSvnServerUrl(repoName),
        null,   //        org.tigris.subversion.javahl.Revision.START,
        new RevisionRange[] { revisionRange },    //RevisionRange[] ranges,
        false,  //boolean stopOnCopy,
        true,    //boolean discoverPath,
        true,   //boolean includeMergedRevisions,
        null,               //String[] revProps,
        Integer.MAX_VALUE,           //long limit,
        logMessageVO    //LogMessageCallback callback
        );
        List<LogMessageVO> list = logMessageVO.getList();
        return list;
    }

    public synchronized List<LogMessageVO> getLogMessage(String repoName, boolean includeChangeFiles, String path) throws org.tigris.subversion.javahl.ClientException {
        RevisionRange revisionRange = new RevisionRange(
        //        org.tigris.subversion.javahl.Revision.getInstance(1),
        org.tigris.subversion.javahl.Revision.START,
        org.tigris.subversion.javahl.Revision.START
        );

        LogMessageVO logMessageVO = new LogMessageVO();
        logMessageVO.setUseChangedFiles(includeChangeFiles);

        svnClientTigris.logMessages(
        getSvnServerUrl(repoName) + "/" + String.join("/", path.split("\\\\")),
        null,   //        org.tigris.subversion.javahl.Revision.START,
        new RevisionRange[] { revisionRange },    //RevisionRange[] ranges,
        false,  //boolean stopOnCopy,
        true,    //boolean discoverPath,
        true,   //boolean includeMergedRevisions,
        null,               //String[] revProps,
        Integer.MAX_VALUE,           //long limit,
        logMessageVO    //LogMessageCallback callback
        );
        List<LogMessageVO> list = logMessageVO.getList();
        return list;
    }

    public String getFileContent(String repoName, String filePath, long fileRevision) throws ClientException {

        String targetPath = getSvnServerUrl(repoName) + "/" + StringUtil.rebuildPath("/", filePath);
        Revision revision = Revision.getInstance(fileRevision);
        Revision pegRevision = revision;

        return new String(svnClientApache.fileContent(targetPath, revision, pegRevision));
    }

    public void commit(Path path, String commitMessage) {
        try {
            svnClientTigris.add(path.toString(),//            String path,
                                0,//            int depth,
                                true,//            boolean force,
                                true,//            boolean noIgnores,
                                true//            boolean addParents
            );
        } catch (org.tigris.subversion.javahl.ClientException e) {

        }
        try {
            svnClientTigris.commit(new String[] { path.toString() },//        String[] path,
                                   commitMessage,//        String message,
                                   0, //        int depth,
                                   false, //        boolean noUnlock,
                                   false, //        boolean keepChangelist,
                                   null, //        String[] changelists,
                                   null //        Map revpropTable
            );
        } catch (org.tigris.subversion.javahl.ClientException e) {
            e.printStackTrace();
        }
    }
}
