package com.inswave.appplatform.wedgemanager.domain.device;

import com.inswave.appplatform.wedgemanager.domain.user.UserService;
import com.inswave.appplatform.wedgemanager.dto.ClientUpdateCheckDTO;
import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class DeviceService {

	private Integer maxDelegateResult = 5;
	private Integer maxNumberExpression = 1000;
	private DeviceRepository dr;

	@Autowired
	UserService userService;

	@Autowired
	public void setDrRepository(DeviceRepository dr) {
		this.dr = dr;
	}

	public List<Device> findAll() { return dr.findAll(); }

	public Iterable<Device> findAllByIds(Iterable<DeviceKey> ids) {
		Iterable<Device> devices = dr.findAllById(ids);
	    return devices;
		//return dr.findAllById(ids);
	}

	public Device findOne(DeviceKey key) {
		Device device = null;
		try {
			device = dr.findById(key).get();
		}catch (Exception e){
			device = null;
		}

		return device;
	}

//	public List<Device> findAllByUserId(List<String> userId) { return  dr.findAllByUserId(userId); }

	public Device findOneByIp(String ip) {
		return dr.findOneByIp(ip);
	}

	public List<Device> findAllByIp(String ip, String appId) { return dr.findAllByIp(ip, appId); }

	@Transactional
	public void initWebSocketState() {
		dr.initWebSocketState();
	}

	@Transactional
	public Device deviceConnect(DeviceKey key) {
		Device device = dr.findById(key).get();
		if(device ==null) {
			return null;
		}

		device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE);
		return dr.save(device);
	}

	@Transactional
	public Device deviceConnectInfo(Device device) {
		device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE);
		return dr.save(device);
	}

	@Transactional
	public Device deviceDisconnect(DeviceKey key) {
		Device device = dr.findById(key).get();
		if(device ==null) {
			return null;
		}

		device.setSessionState(WebsocketState.CL_WEBSOCKET_STATE_INACTIVE);
		return dr.save(device);
	}

	@Transactional
	public Device save(Device device) { return dr.save(device); }

//	public Device createOrUpdate(Device device) {
//		User user = userService.findById(device.getUserId()).get();
//		if (user == null) {
//			return null;
//		}
//
//		DeviceKey key = new DeviceKey();
//		key.appId = device.getAppId();
//		key.userId = user.getUserId();
//		key.osType = device.getOsType();
//
//		Device baseDevice = dr.findById(key).get();
//
//		if (baseDevice == null) {
//			device.setAppId(device.getAppId());
//			device.setUserId(user.getUserId());
//			device.setOsType(device.getOsType());
//			dr.save(device);
//			return device;
//		} else {
//
//			BeanUtils.copyProperties(device, baseDevice, CommonUtil.getNullPropertyNames(device));
//			dr.save(baseDevice);
//			return baseDevice;
//		}
//	}

	public Iterable<Device> findConnectSessionAllByUserId(ArrayList<String> userIds){
		ArrayList<Device> result = new ArrayList<>();
		for(String userId : userIds){
			Iterable<Device> devices = dr.findConnectSessionUserId(userId);
			for(Device d : devices){
				if(d.getSessionState().equals(WebsocketState.CL_WEBSOCKET_STATE_ACTIVE)){
					result.add(d);
				}
			}
		}
		return result;
	}

	public Device findConnectSessionUserId(String userId) {
		Iterable<Device> devices = dr.findConnectSessionUserId(userId);
		Device device = null;

		for(Device d : devices){
			if(d.getSessionState() == WebsocketState.CL_WEBSOCKET_STATE_ACTIVE){
				device = d;
			}
		}

		return device;
	}

	public List<Device> findByDepartCode(String departCode, String appId) { return dr.findByDepartCode(departCode, appId); }

	@Transactional
	public Device setDistVersion(Device updateDevice) {
		return dr.save(updateDevice);
	}

	public List<Device> getDelegateList(ClientUpdateCheckDTO checkUpdate) {
		String ip = checkUpdate.getIp();
		return dr.getDelegateList(ip, checkUpdate.getDistVersion(), maxDelegateResult);
	}

	//�߰�
	public List<Device> getLoginDeviceList(WebsocketState websocketState){
		return dr.getLoginDeviceList(websocketState);
	}
}
