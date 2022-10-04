package com.inswave.appplatform.wedgemanager.service.scm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.dao.Domain;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.service.ExternalService;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.svn.domain.*;
import com.inswave.appplatform.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.subversion.javahl.SubversionException;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tigris.subversion.javahl.ClientException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RepositoryService implements ExternalService {

    private SvnManager svnManager;

    @PostConstruct
    public void postConstruct() {
        svnManager = SvnManager.getInstance();
    }

    @Override
    public IData excuteGet(HashMap<String, Object> params) {
        return null;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData, Object object) {
        return null;
    }

    public IData addDirectory(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");
        String dirName = (String) reqIData.getBodyValue("directoryName");

        try {
            svnManager.addDirectory(repoName, dirName);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData("Directory '" + dirName + "' has been added."));
        } catch (ClientException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData getDirectories(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");

        try {
            JSONObject jsonObj = new JSONObject();
            List<DirectoryVO> directoryVOs = svnManager.getDirectories(repoName);
            List<BusinessVO> businessVOs = directoryVOs.stream().map(BusinessVO::from).collect(Collectors.toList());
            List<Object> data = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(businessVOs)));
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (ClientException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData delRepository(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");

        try {
            svnManager.removeRepository(repoName);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData("Repository '" + repoName + "' has been deleted."));
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            //            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            // TODO. IData -> JSON 형변환 시 Syntax 지원하도록 수정해야할듯. : JSON.parse 오류 발생
        }

        return resIData;
    }

    public IData createDirectories(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");
        String solutionPath = (String) reqIData.getBodyValue("solutionPath");
        List<Map<String, Object>> businessList = (ArrayList) reqIData.getBodyValue("business");

        businessList.forEach(busi -> {
            List<Map<String, Object>> authList = (ArrayList) busi.get("auth");
            authList.forEach(auth -> {
                try {
                    svnManager.addDirectory(repoName, "Directory added by administrator.", solutionPath, (String) busi.get("id"), (String) auth.get("id"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            });
        });

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));

        return resIData;
    }

    public IData addRepository(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");

        try {
            if (!svnManager.existsRepository(repoName)) {
                svnManager.createRepository(repoName);
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
                body.put(Constants.TAG_RESULT_MSG, new SimpleData("Repository '" + repoName + "' has been created."));
                return resIData;
            } else {
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData("Repository '" + repoName + "' is already exists."));
            }
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (org.apache.subversion.javahl.ClientException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
        body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));

        return resIData;
    }

    public IData getRepositories(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        List<RepositoryVO> repositories = svnManager.getRepositories();

        try {
            JSONObject jsonObj = new JSONObject();
            List<Object> data = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(repositories)));
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        return resIData;
    }

    public IData setAccessMap(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repositoryName = (String) reqIData.getBodyValue("repositoryName");
        String authz = (String) reqIData.getBodyValue("authz").toString();
        String passwd = (String) reqIData.getBodyValue("passwd").toString();
        String accessMap = (String) reqIData.getBodyValue("accessMap").toString();

        try {
            svnManager.setConfFile(repositoryName, authz, "authz");
            svnManager.setConfFile(repositoryName, passwd, "passwd");
            svnManager.setConfFile(repositoryName, accessMap, "accessMap.json");
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            return resIData;
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
        body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));

        return resIData;
    }

    public IData getAccessMap(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String repoName = (String) reqIData.getBodyValue("repositoryName");

        try {
            String accessMap = svnManager.getAccessMap(repoName);
            body.put("data", new SimpleData(new Parse().getJSONObject(new StringBuffer(accessMap))));
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            return resIData;
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));

        return resIData;
    }

    public IData setLock(IData reqIData, IData resIData) {
        String repositoryName = reqIData.getBodyValueString("repositoryName");
        String comments = reqIData.getBodyValueString("comments");
        List<String> paths = (ArrayList<String>) reqIData.getBodyValue("paths");
        IData body = new NodeData();
        try {
            svnManager.setLock(repositoryName, paths, comments, true);
            updateCacheForce(repositoryName);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData(paths.size() + "files has been locked."));
        } catch (org.apache.subversion.javahl.ClientException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (SubversionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resIData.put(Constants.TAG_BODY, body);

        return resIData;
    }

    public IData setUnlock(IData reqIData, IData resIData) {
        String repositoryName = reqIData.getBodyValueString("repositoryName");
        List<String> paths = (ArrayList<String>) reqIData.getBodyValue("paths");
        IData body = new NodeData();
        try {
            svnManager.setUnlock(repositoryName, paths, true);
            updateCacheForce(repositoryName);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData(paths.size() + "files has been unlocked."));
        } catch (org.apache.subversion.javahl.ClientException | ClientException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (SubversionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        resIData.put(Constants.TAG_BODY, body);

        return resIData;
    }

    public IData getRepositoryListItem(IData reqIData, IData resIData) throws org.apache.subversion.javahl.ClientException, IOException {
        String repositoryName = reqIData.getBodyValueString("repositoryName");
        Boolean onlySolution = reqIData.getBodyValueBoolean("onlySolution");
        boolean onlyDir = reqIData.getBodyValue("onlyDir") != null ? reqIData.getBodyValueBoolean("onlyDir") : false;

        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        try {
            JSONObject jsonObj = new JSONObject();
            //            List<SvnItemVO> svnItemVOs = svnManager.getRepositoryListItemCache(repositoryName);
            //            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(svnItemVOs)));

            List<SvnItemVO> svnItemVOs = getSolutionListItem(repositoryName, onlySolution, onlyDir);
            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(svnItemVOs)));

            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        return resIData;
    }

    public IData getBusinessAuth(IData reqIData, IData resIData) {

        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        List<RepositoryVO> repositories = svnManager.getRepositories();
        repositories.forEach(repositoryVO -> {
            try {
                JSONObject jsonObj = new JSONObject();
                List<Map<String, String>> businessAuths = getBusinessAuth(repositoryVO.getName());
                List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(businessAuths)));
                jsonObj.put(repositoryVO.getName(), new Parse().parseList(data));
                body.setObject(jsonObj);
            } catch (IOException e) {
                e.printStackTrace();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            }
        });

        return resIData;
    }

    public List<Map<String, String>> getBusinessAuth(String repositoryName) throws IOException {
        JsonNode accessMap = svnManager.getAccessMapJson(repositoryName);
        ArrayNode business = (ArrayNode) accessMap.at("/business"); // id, name, auth []
        ArrayNode businessAuth = (ArrayNode) accessMap.at("/businessAuth");     // business, name, id

        Map<String, String> businessInfoMap = new HashMap<>();
        List<Map<String, String>> authInfoList = new ArrayList();
        business.forEach(node -> {
            businessInfoMap.put(node.get("id").asText(), node.get("name").asText());
        });

        businessAuth.forEach(node -> {
            authInfoList.add(
            new HashMap<String, String>() {
                {
                    put("repositoryName", repositoryName);
                    put("path", StringUtil.rebuildPath("/", node.get("business").asText(""), node.get("id").asText("")));
                    put("businessId", node.get("business").asText(""));  //업무
                    put("businessName", businessInfoMap.get(node.get("business").asText("")));  //업무명
                    put("authId", node.get("id").asText(""));        //권한코드
                    put("authName", node.get("name").asText(""));       //권한명
                }
            });
        });
        return authInfoList;
    }

    public List<SvnItemVO> getSolutionListItem(String repositoryName, boolean onlySolution, boolean onlyDir) throws org.apache.subversion.javahl.ClientException, IOException {
        List<SvnItemVO> svnItemVOs = svnManager.getRepositoryListItemCache(repositoryName);
        return svnItemVOs.stream()
                         .filter(svnItemVO -> onlySolution ? Objects.nonNull(svnItemVO.getBusiness()) : true)
                         .filter(svnItemVO -> onlyDir ? svnItemVO.getNodeKind().equals("dir") : true)
                         .collect(Collectors.toList());
    }

    public IData getHistory(IData reqIData, IData resIData) {
        String repositoryName = reqIData.getBodyValueString("repositoryName");
        String path = reqIData.getBodyValueString("path");

        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        try {
            JSONObject jsonObj = new JSONObject();

            //            List<BlameVO> blameVOs = svnManager.getHistory(repositoryName, path);
            //            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(blameVOs)));
            List<LogMessageVO> logMessageVOs = svnManager.getLogMessage(repositoryName, false, path);
            fillUserName(repositoryName, logMessageVOs);
            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(logMessageVOs)));

            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
        } catch (JsonProcessingException | ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        return resIData;
    }

    public void fillUserName(String repositoryName, List<LogMessageVO> logMessageVOs) throws IOException {
        JsonNode accessMap = svnManager.getAccessMapJson(repositoryName);
        ArrayNode user = (ArrayNode) accessMap.at("/user");  // id, name, email, role

        Map<String, String[]> userInfoMap = new HashMap<>();
        user.forEach(node -> {
            userInfoMap.put(node.get("id").asText(""),
                            new String[] {
                            node.has("name") ? node.get("name").asText("") : null,
                            node.has("email") ? node.get("email").asText("") : null,
                            node.has("role") ? node.get("role").asText("") : null
                            });
        });
        logMessageVOs.stream()
                     .forEach(logMessageVO -> {
                         String[] userInfo = userInfoMap.get(logMessageVO.getCommitAuthor());
                         if (userInfo != null) {
                             logMessageVO.setCommitAuthorName(userInfo[0]);
                             logMessageVO.setCommitAuthorEmail(userInfo[1]);
                             logMessageVO.setCommitAuthorRole(userInfo[2]);
                         }
                     });

    }

    public IData getLogMessage(IData reqIData, IData resIData) {
        String repositoryName = reqIData.getBodyValueString("repositoryName");
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        try {
            JSONObject jsonObj = new JSONObject();

            //            List<BlameVO> blameVOs = svnManager.getHistory(repositoryName, path);
            //            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(blameVOs)));
            List<LogMessageVO> logMessageVOs = svnManager.getLogMessage(repositoryName, true);
            fillUserName(repositoryName, logMessageVOs);
            List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(logMessageVOs)));

            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
        } catch (JsonProcessingException | ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        return resIData;
    }

    public void updateCacheForce(String repositoryName) throws SubversionException, IOException {
        long revisionLatest = svnManager.getLatestRevision(repositoryName);
        long accessMapVersionLatest = svnManager.getAccessMapJson(repositoryName).at("/version").asLong();
        svnManager.updateRepositoryCache(repositoryName, revisionLatest, accessMapVersionLatest);
    }
    @Scheduled(fixedDelay = 60000)  // 1분 간격 캐시 갱신
    public void updateCache() {
        if(SvnManager.isConfigured()) {
            List<RepositoryVO> repositoryVOS = svnManager.getRepositories();
            repositoryVOS.forEach(repositoryVO -> {
                try {
                    String repositoryName = repositoryVO.getName();
                    long revisionLatest = svnManager.getLatestRevision(repositoryName);
                    long accessMapVersionLatest = svnManager.getAccessMapJson(repositoryName).at("/version").asLong();

                    RepositoryListItemCache repositoryListItemCache = svnManager.getRepositoryListItemCacheMap(repositoryName);

                    if (Objects.nonNull(repositoryListItemCache)) {
                        long revisionCached = repositoryListItemCache.getRevision();
                        long accessMapVersionCached = repositoryListItemCache.getAccessMapVersion();

                        if (revisionLatest > revisionCached || accessMapVersionLatest > accessMapVersionCached) {
                            svnManager.updateRepositoryCache(repositoryName, revisionLatest, accessMapVersionLatest);
                        }
                    } else {
                        svnManager.updateRepositoryCache(repositoryName, revisionLatest, accessMapVersionLatest);
                    }
                } catch (SubversionException e) {
                    log.debug("{}", e.getMessage());
                } catch (IOException e) {
                    log.debug("{}", e.getMessage());
                }
            });
        }
    }
}
