package com.inswave.appplatform.wedgemanager.domain.device;

import com.inswave.appplatform.wedgemanager.domain.user.UserService;
import com.inswave.appplatform.wedgemanager.dto.ClientUpdateCheckDTO;
import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceATMService {
    private Integer maxDelegateResult = 5;
    private Integer maxNumberExpression = 1000;
    private DeviceATMRepository dr;

    @Autowired
    UserService userService;

    @Autowired
    public void setDrRepository(DeviceATMRepository dr) {
        this.dr = dr;
    }

    public List<DeviceATM> findAll() { return dr.findAll(); }

    public Iterable<DeviceATM> findAllByIds(Iterable<DeviceKey> ids) {
        Iterable<DeviceATM> devices = dr.findAllById(ids);
        return devices;
        //return dr.findAllById(ids);
    }

    public DeviceATM findOne(DeviceKey key) {
        DeviceATM device = null;
        try {
            device = dr.findById(key).get();
        }catch (Exception e){
            device = null;
        }

        return device;
    }

    public List<DeviceATM> findAllByIp(String ip, String appId) { return dr.findAllByIp(ip, appId); }

//	public List<Device> findAllByUserId(List<String> userId) { return  dr.findAllByUserId(userId); }

    public List<DeviceATM> findByDepartCode(String departCode, String appId) { return dr.findByDepartCode(departCode, appId); }

    public DeviceATM findOneByIp(String ip) {
        return dr.findOneByIp(ip);
    }

    @Transactional
    public void initWebSocketState() {
        dr.initWebSocketState();
    }

    @Transactional
    public DeviceATM deviceConnect(DeviceKey key) {
        DeviceATM device = dr.findById(key).get();
        if(device ==null) {
            return null;
        }

        device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE);
        return dr.save(device);
    }

    @Transactional
    public DeviceATM deviceConnectInfo(DeviceATM device) {
        device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE);
        return dr.save(device);
    }

    @Transactional
    public DeviceATM deviceDisconnect(DeviceKey key) {
        DeviceATM device = dr.findById(key).get();
        if(device ==null) {
            return null;
        }

        device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_INACTIVE);
        return dr.save(device);
    }

    @Transactional
    public DeviceATM save(DeviceATM device) { return dr.save(device); }

    public Iterable<DeviceATM> findConnectSessionAllByUserId(ArrayList<String> userIds){
        ArrayList<DeviceATM> result = new ArrayList<>();
        for(String userId : userIds){
            Iterable<DeviceATM> devices = dr.findConnectSessionUserId(userId);
            for(DeviceATM d : devices){
                if(d.getSessionState().equals(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE)){
                    result.add(d);
                }
            }
        }
        return result;
    }

    public DeviceATM findConnectSessionUserId(String userId) {
        Iterable<DeviceATM> devices = dr.findConnectSessionUserId(userId);
        DeviceATM device = null;

        for(DeviceATM d : devices){
            if(d.getSessionState() == WebsocketState.CL_WEBSOCKET_STATE_ACTIVE){
                device = d;
            }
        }

        return device;
    }


    @Transactional
    public DeviceATM setDistVersion(DeviceATM updateDevice) {
        return dr.save(updateDevice);
    }

    public List<DeviceATM> getDelegateList(ClientUpdateCheckDTO checkUpdate) {
        String ip = checkUpdate.getIp();
        return dr.getDelegateList(ip, checkUpdate.getDistVersion(), maxDelegateResult);
    }

    //�߰�
    public List<DeviceATM> getLoginDeviceList(WebsocketState websocketState){
        return dr.getLoginDeviceList(websocketState);
    }
}
