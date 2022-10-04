package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inswave.appplatform.svn.SvnManager;
import com.inswave.appplatform.svn.domain.SvnItemVO;
import com.inswave.appplatform.util.StringUtil;
import com.inswave.appplatform.wedgemanager.dao.TerminalMenuDao;
import com.inswave.appplatform.wedgemanager.domain.organization.Organization;
import com.inswave.appplatform.wedgemanager.domain.organization.OrganizationRepository;
import com.inswave.appplatform.wedgemanager.domain.terminal.TerminalMenu;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.subversion.javahl.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/menu")
public class TerminalMenuController {

    private       TerminalMenuDao        terminalMenuDao;
    private       OrganizationRepository organizationRepository;
    private final int                    ORGANIZATION_ID_MENU_JSON = 500;
    private       String                 menuJsonSystemPath;

    public TerminalMenuController(TerminalMenuDao terminalMenuDao,
                                  OrganizationRepository organizationRepository,
                                  @Value("${wedgemanager.terminal.menuJsonSystemPath:#{null}}") String menuJsonSystemPath) {
        this.terminalMenuDao = terminalMenuDao;
        this.organizationRepository = organizationRepository;
        this.menuJsonSystemPath = menuJsonSystemPath;
    }

    @PostMapping(path = "/update-json")
    public ResponseEntity update(@RequestBody JsonNode param) throws ClientException, IOException {
        String repositoryName = param.get("repositoryName").asText();

        List<SvnItemVO> svnItemVOs = SvnManager.getInstance().getRepositoryListItemCache(repositoryName);
        List<TerminalMenu> roots = terminalMenuDao.findByParentIdIsNull(Sort.by("ord"));

        List<TerminalMenuVO> flatList = new ArrayList<>();
        roots.forEach(terminalMenu -> {
            addFlatList(flatList, terminalMenu, 1, svnItemVOs, new ArrayList<>());
        });

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode deptTreeNode = mapper.createObjectNode();
        deptTreeNode.set("dlt_terminalMenu", mapper.valueToTree(flatList));
        String deptTreeNodeStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deptTreeNode);
        //        organizationRepository.save(Organization.builder().id(ORGANIZATION_ID_MENU_JSON).org(deptTreeNodeStr).build());

        if(SvnManager.configured) {
            JsonNode accessMap = SvnManager.getInstance().getAccessMapJson(repositoryName);
            JsonNode solutionPathNode = accessMap.at("/config/menuPath");
            Path menuJsonPath = SvnManager.getSvnWorkingCopyPath(repositoryName).resolve(StringUtil.rebuildPath("/", solutionPathNode.asText(""))).resolve("menu.json");

            FileUtils.write(menuJsonPath.toFile(), deptTreeNodeStr, "UTF-8");

            SvnManager.getInstance().updateWorkingCopy(repositoryName);
            SvnManager.getInstance().commit(menuJsonPath, "menu.json updated.");

            log.info("menu.json updated. svn commit. : {}", menuJsonPath.toAbsolutePath());
        }

        if (menuJsonSystemPath != null) {
            File file = Paths.get(menuJsonSystemPath).toFile();
            FileUtils.write(file, deptTreeNodeStr, "UTF-8");
            //                Files.setPosixFilePermissions(file.toPath(), PosixFilePermissions.fromString("rwxrwxrwx"));
            file.setReadable(true, false);          //r
            file.setWritable(true, false);          //w
            file.setExecutable(true, false);      //x
            log.info("menu.json updated. fileSystem. : {}", file.getAbsolutePath());
        }

        return ResponseEntity.ok().build();
    }

    public void addFlatList(List<TerminalMenuVO> flatList,
                            TerminalMenu terminalMenu,
                            int depth,
                            List<SvnItemVO> svnItemVOs,
                            List<String> menuFullPath) {
        menuFullPath.add(terminalMenu.getMenuName());

        String screenSolutionPath = "";
        String screenSolutionSvnPath = "";
        if (terminalMenu.getScreenId() != null) {
            screenSolutionPath = String.format("/Solution/%s/%s/%s.xml",
                                               terminalMenu.getScreenId().substring(0, 2),
                                               terminalMenu.getScreenId().substring(0, 3),
                                               terminalMenu.getScreenId());

            Optional<SvnItemVO> optionalSvnItemVO = svnItemVOs.stream()
                                                              .filter(svnItemVO -> svnItemVO.getPath().endsWith(terminalMenu.getScreenId() + ".xml"))
                                                              .findFirst();
            if (optionalSvnItemVO.isPresent()) {
                screenSolutionSvnPath = optionalSvnItemVO.get().getPath();
            }
        }

        TerminalMenuVO terminalMenuVO = TerminalMenuVO.from(terminalMenu, depth, screenSolutionPath, screenSolutionSvnPath, menuFullPath);
        flatList.add(terminalMenuVO);

        if (terminalMenu.getChildren() != null && terminalMenu.getChildren().size() > 0) {
            depth++;
            for (int i = 0; i < terminalMenu.getChildren().size(); i++) {
                List<String> menuFullPathCopy = menuFullPath.stream().collect(Collectors.toList());
                addFlatList(flatList, terminalMenu.getChildren().get(i), depth, svnItemVOs, menuFullPathCopy);
            }
        }
    }

    @GetMapping(path = "/select-json")
    public ResponseEntity<Organization> select() {
        return ResponseEntity.ok(organizationRepository.findById(ORGANIZATION_ID_MENU_JSON).orElse(null));
    }

    @PostMapping(path = "/screen-info")
    public ResponseEntity<TerminalMenu> findOne(@RequestBody JsonNode param) throws ClientException, IOException {
        String screenId = param.get("screenId").asText();
        return ResponseEntity.ok(terminalMenuDao.findByScreenId(screenId));
    }
}
