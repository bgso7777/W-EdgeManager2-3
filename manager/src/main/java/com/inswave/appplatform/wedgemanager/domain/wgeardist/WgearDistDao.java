package com.inswave.appplatform.wedgemanager.domain.wgeardist;

import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceATM;
import com.inswave.appplatform.wedgemanager.dto.*;

import java.util.Collection;
import java.util.List;

public interface WgearDistDao {

    void insert(WgearDistDTO dto);

//    void endDist(WgearDistDTO dto);

//    void endDistByScope(WgearDistDTO dto);

    void insertTargets(Collection<WgearDistDepartments> wgearDistDepartments);

    ClientUpdateCheckDTO checkAdvance(Device device);

    ClientUpdateCheckDTO checkAdvance(DeviceATM device);

    ClientUpdateCheckDTO getLatestDist(ClientUpdateCheckDTO dto);

    void insertFiles(Collection<WgearDistFiles> wgearDistFiles);

    List<WgearDistFiles> getFileListByVersion(ClientUpdateCheckDTO checkUpdate);

    List<DeployFileListDTO> getFilesByVersion(ClientUpdateCheckDTO checkUpdate);

    List<WgearDistFiles> getFileListByVersionAndDeviceType(ClientUpdateCheckDTO checkUpdate);
	
	List<WgearDistFiles> getLastestFileList();

	List<WgearDist> findAllByOrderByCreateDateAsc(String appId);

    //scopeLit 추가 시작
    List<WgearDist> getScopeList(WgearDistDTO param);
    List<WgearDist> getAllDistList(WgearDistDTO param);
    //scopeLit 추가 끝

    //이력 추가 시작
    List<WgearDist> getDeployList(WgearDistDTO param);
    //이력 추가 끝

    WgearDistFilesInfo checkFilesInfo(WgearDistFilesInfo param);

    List<DeployStateDTO> getDeployStateByVersion(DataHeader dataHeader);

    List<DeployStateDTO> getDeployStateByDepartment(DataHeader dataHeader);

    DeployStateDTO getTotalAndCompleteByDepartment(DeployStateDTO deployStateDTO);

    List<DeployStateDTO> getATMDeployStateByVersion(DataHeader dataHeader);

    List<DeployStateDTO> getATMDeployStateByDepartment(DataHeader dataHeader);

    DeployStateDTO getATMTotalAndCompleteByDepartment(DeployStateDTO deployStateDTO);

    List<DeployFileListDTO> getFileList(String appId, String fileName);

    TotalDeviceDTO getTotalDevice(String totalDeviceDTO);

    TotalDeviceDTO getTotalATMDevice(String totalDeviceDTO);

    DistFiles getLastVersion(String appId, String fileName);

    //void addFilesInfo(Collection<WgearDistFilesInfo> wgearDistFilesInfo);

    //admin 관련
    List<AdminUserDTO> getAdminUSer(String roleId);
    List<WgearDistFiles> getFileListByVersion(String appId, String version);
    List<WgearDistDepartments> getDepartmentListByVersion(String appId, String version);

    //배포승인관련
    List<Dist> getAdmissionByAdminId(String appId, String adminId);
    List<Dist> getAdmissionCompleteByAdminId(String appId, String adminId);

    //챠트
    List<DeployStateDTO> getDistInfoByDepartCode(String appId, String version);
    List<DeployStateDTO> getDistInfoATMByDepartCode(String appId, String version);
    List<Device> getDeviceByVersionAndDepartCode(String appId, String version, String departCode);
    List<DeviceATM> getDeviceATMByVersionAndDepartCode(String appId, String version, String departCode);
}
