package com.inswave.appplatform.wedgemanager.domain.wgeardist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inswave.appplatform.wedgemanager.common.CommonUtil;
import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceATM;
import com.inswave.appplatform.wedgemanager.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class WgearDistService {

    @Value("${whub.distribution.file.deployResourcePath}")
    private String deployResourcePathStr;

    private int baseResourcePathLength;

    private Path deployResourcePath;

    @Value("${whub.distribution.file.deployStoragePath}")
    private String deployStoragePathStr;

    private int baseStoragePathLength;

    private Path deployStoragePath;

//    @Autowired
//    WgearDistRepository wgearDistRepository;

    @Autowired
    WgearDistDao wgearDistDao;

    @Autowired
    WgearDistFilesInfoService wgearDistFilesInfoService;

    @PostConstruct
    public void initDeploy() {

        deployResourcePath = setDeployPath(deployResourcePathStr);
        deployStoragePath = setDeployPath(deployStoragePathStr);
        baseResourcePathLength = deployResourcePath.toString().length();
        baseStoragePathLength = deployStoragePath.toString().length() + 1;
    }

    public List<DeployStateDTO> getDeployStateByVersion(DataHeader dataHeader){
        List<DeployStateDTO> stateDTOList = wgearDistDao.getDeployStateByVersion(dataHeader);
        List<DeployStateDTO> resultStateDTOList = new LinkedList<>();
        for(DeployStateDTO stateDTO : stateDTOList){
            String[] targets = stateDTO.getTargets().split(",");
            DeployStateDTO newStateDTO = new DeployStateDTO();
            int totalCount =0;
            int completeCount=0;
            for(String target:targets){
                newStateDTO.setDepartCode(target);
                newStateDTO.setDistVersion(stateDTO.getVersion());
                newStateDTO.setAppId(dataHeader.getAppId());
                newStateDTO = wgearDistDao.getTotalAndCompleteByDepartment(newStateDTO);

                totalCount += Integer.parseInt(newStateDTO.getTotalDevice());
                completeCount += Integer.parseInt(newStateDTO.getCompleteCount());
            }

            stateDTO.setTotalDevice(Integer.toString(totalCount));
            stateDTO.setCompleteCount(Integer.toString(completeCount));

            resultStateDTOList.add(stateDTO);
        }

        return resultStateDTOList;
    }

    public List<DeployFileListDTO> getFileList(String appId, String fileName){
        return wgearDistDao.getFileList(appId, fileName);
    }

    public DistFiles getLastVersion(String appId, String fileName){
        return wgearDistDao.getLastVersion(appId, fileName);
    }

    public List<DeployStateDTO> getDeployStateByDepartment(DataHeader dataHeader){
        return wgearDistDao.getDeployStateByDepartment(dataHeader);
    }

    public List<DeployStateDTO> getATMDeployStateByVersion(DataHeader dataHeader){
        return wgearDistDao.getATMDeployStateByVersion(dataHeader);
    }

    public List<DeployStateDTO> getATMDeployStateByDepartment(DataHeader dataHeader){
        return wgearDistDao.getATMDeployStateByDepartment(dataHeader);
    }

    public TotalDeviceDTO getTotalDevice(String appId){ return wgearDistDao.getTotalDevice(appId); }

    public TotalDeviceDTO getTotalATMDevice(String appId){ return wgearDistDao.getTotalATMDevice(appId); };

    public List<WgearDist> findAll(String appId) {
        return wgearDistDao.findAllByOrderByCreateDateAsc(appId);
//        return new ArrayList<>();
    }

    public WgearDist findOneByVersion(String version) {
//        return wgearDistRepository.findOneByVersion(version);
        return new WgearDist();
    }

    public WgearDist findOneByCreatedAt(String createdAt) {
//        Date createdDate = new Date(Long.parseLong(createdAt));
//        return wgearDistRepository.findOneByCreateDate(createdDate);
        return new WgearDist();
    }

    public List<WgearDistFiles> setDeployFiles(String appId, String version) {

        File sourceFolder = deployResourcePath.toFile();
        File destinationFolder = deployStoragePath.resolve(version).toFile();

        List<WgearDistFiles> filesList = new ArrayList<>();

        return CommonUtil.copyFolder(sourceFolder, destinationFolder, baseStoragePathLength,appId, version, filesList);
    }

    @Transactional
    public void addDist(WgearDistDTO dto) {

        wgearDistDao.insertTargets(dto.getDepartmentsList());

        wgearDistDao.insertFiles(dto.getFilesList());

        wgearDistDao.insert(dto);
    }

    public ObjectNode getFileList() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode()
                .put("title", "root")
                .put("key", "root_1")
                .put("expanded", true);

        File rootFile = deployResourcePath.toFile();

        if(!rootFile.exists()) {
            return null;
        }

        ArrayNode rootChildrenNode = mapper.createArrayNode();
        makeFileList(rootFile, rootChildrenNode);
        rootNode.set("children", rootChildrenNode);

        return rootNode;
    }

    private ArrayNode makeFileList(File rootFile, ArrayNode childrenNode) {
        File[] files = rootFile.listFiles();
        //recently created files are on the top in the list
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        ObjectMapper mapper = new ObjectMapper();

        for (File file : files) {
            if (file.isDirectory()) {
                ObjectNode node = mapper.createObjectNode();
                node.put("title", file.getName());
                node.put("key", file.getPath().substring(baseResourcePathLength));
                node.put("folder", true);
                node.put("hideCheckbox", true);
                node.put("expanded", true);

                ArrayNode newArrayNode = mapper.createArrayNode();
                ArrayNode subChildren = makeFileList(file, newArrayNode);
                node.set("children", subChildren);
                childrenNode.add(node);
            } else {
                ObjectNode node = mapper.createObjectNode();
                node.put("title", file.getName());
                node.put("key", file.getPath().substring(baseResourcePathLength));
                childrenNode.add(node);
            }
        }
        return childrenNode;
    }

    public WgearDistFilesInfo checkFilesInfo(WgearDistFilesInfo wgearDistFilesInfo) { return wgearDistDao.checkFilesInfo(wgearDistFilesInfo); }

    public void addFilesInfo(List<WgearDistFilesInfo> wgearDistFilesInfo){
        wgearDistFilesInfoService.setFilesInfoList(wgearDistFilesInfo);
    }

    public ClientUpdateCheckDTO checkAdvance(Device device) {
        return wgearDistDao.checkAdvance(device);
    }

    public ClientUpdateCheckDTO checkAdvance(DeviceATM device) {
        return wgearDistDao.checkAdvance(device);
    }

    public ClientUpdateCheckDTO getLatestDist(ClientUpdateCheckDTO dto) {
        return wgearDistDao.getLatestDist(dto);
    }

    public List<WgearDistFiles> getFileListByVersion(ClientUpdateCheckDTO checkUpdate) {
        return wgearDistDao.getFileListByVersion(checkUpdate);
    }

    public List<DeployFileListDTO> getFilesByVersion(ClientUpdateCheckDTO checkUpdate) {
        return wgearDistDao.getFilesByVersion(checkUpdate);
    }

    public List<WgearDistFiles> getFileListByVersionAndDeviceType(ClientUpdateCheckDTO checkUpdate) {
        return wgearDistDao.getFileListByVersionAndDeviceType(checkUpdate);
    }

    private Path setDeployPath( String pathStr ) {
        Path path = Paths.get(pathStr);

        if (!path.isAbsolute()) {
            ClassPathResource resource = new ClassPathResource(pathStr);
            path = Paths.get(resource.getPath()).toAbsolutePath();
        }

        return path;
    }

    //추가 시작 : 대상 폴더 treeViewList에 출력될 데이타 조합
    public List<WgearDistFiles> getLastestFileList() {

        return wgearDistDao.getLastestFileList();
    }

    private List makeFileTreeList(File rootFile, ArrayList list, int depth, Map compareData) {
        File[] files = rootFile.listFiles();
        //recently created files are on the top in the list
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File file : files) {
            if (file.isDirectory()) {

                Map data = new HashMap();
                data.put("title", file.getName());
                data.put("key", file.getPath().substring(baseResourcePathLength));
                data.put("folder", true);
                data.put("hideCheckbox", true);
                data.put("expanded", true);
                data.put("depth", depth +1 );
                data.put("type", "1");
                list.add( data );
                makeFileTreeList(file, list, depth+1, compareData);
            } else {
                Map data = new HashMap();
                String key = file.getPath().substring(baseResourcePathLength);
                data.put("title", file.getName());
                data.put("key", key);
                data.put("depth", depth +1 );
                data.put("type", "2");
                data.put("fileStatus", "R");

                String fileHashKey = CommonUtil.makeHash(file).toUpperCase();
                String compareKey = key;

                if (CommonUtil.isWindows) {
                    compareKey = key.replaceAll("\\\\", "/");
                }
                String tempHash = (String)compareData.get(compareKey.substring(1));
                if ( tempHash == null) {
                    data.put("fileStatus", "N");
                } else {
                    if ( !fileHashKey.equals(tempHash)) {
                        data.put("fileStatus", "U");
                    }
                    data.put("fileStatus","E");
                }

                list.add(data);

            }
        }

        return list;
    }

    public List getFileTreeList(Map compareData) {
        ArrayList list = new ArrayList();
        Map data = new HashMap();
        data.put("title", "Deploy File");
        data.put("key", "");
        data.put("expanded", true);
        data.put("depth", 1);
        data.put("type", "1");
        File rootFile = deployResourcePath.toFile();
        list.add(data);

        if(!rootFile.exists()) {
            return null;
        }

        list = (ArrayList)makeFileTreeList(rootFile, list, 1, compareData);

        return list;
    }
    //추가 끝 : 대상 폴더 treeViewList에 출력될 데이타 조합

    //추가 시작 scopeList
    public List<WgearDist> getScopeList(WgearDistDTO param) {
        return wgearDistDao.getScopeList(param);
    }
    //추가 끝 scopeList

    //이력 추가 시작
    public List<WgearDist> getDeployList(WgearDistDTO param) {
        return wgearDistDao.getDeployList(param);
    }

    public List<WgearDist> getAllDistList(WgearDistDTO param) {
        return wgearDistDao.getAllDistList(param);
    }
    //이력 추가 끝


    //admin user
    public List<AdminUserDTO> getAdminUser(String roleId) { return wgearDistDao.getAdminUSer(roleId); }

    public List<WgearDistFiles> getFileListByVersion(String appId, String version) { return wgearDistDao.getFileListByVersion(appId, version); }

    public List<WgearDistDepartments> getDepartmentListByVersion(String appId, String version) { return wgearDistDao.getDepartmentListByVersion(appId, version); }

    //배포승인관련
    public List<Dist> getAdmissionByAdminId(String appId, String adminId) { return wgearDistDao.getAdmissionByAdminId(appId, adminId); }

    public List<Dist> getAdmissionCompleteByAdminId(String appId, String adminId) { return wgearDistDao.getAdmissionCompleteByAdminId(appId, adminId); }

    @Transactional
    public List<DeployStateDTO> getDistInfoByDepartCode(String appId, String version) {
        return wgearDistDao.getDistInfoByDepartCode(appId, version);
    }

    @Transactional
    public List<DeployStateDTO> getDistInfoATMByDepartCode(String appId, String version) {
        return wgearDistDao.getDistInfoATMByDepartCode(appId, version);
    }

    @Transactional
    public List<Device> getDeviceByVersionAndDepartCode(String appId, String version, String departCode) {
        return wgearDistDao.getDeviceByVersionAndDepartCode(appId, version, departCode);
    }

    @Transactional
    public List<DeviceATM> getDeviceATMByVersionAndDepartCode(String appId, String version, String departCode) {
        return wgearDistDao.getDeviceATMByVersionAndDepartCode(appId, version, departCode);
    }
}
