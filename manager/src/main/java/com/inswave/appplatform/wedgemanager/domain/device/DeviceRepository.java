package com.inswave.appplatform.wedgemanager.domain.device;

import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
@Primary
public class DeviceRepository extends SimpleJpaRepository<Device, DeviceKey> {
	EntityManager entityManager;

	public DeviceRepository(@Qualifier("wemEntityManager") EntityManager entityManager) {
		super(Device.class, entityManager);
		this.entityManager = entityManager;
	}

//	public List<Device> findAllByUserId(List<String> userId){
//		List<Device> device = null;
//
//		try{
//			device = this.entityManager.createQuery(
//					"SELECT t FROM Device t where t.userId=:userId")
//					.setParameter("userId",userId).getResultList();
//		}catch (NoResultException noresult){
//
//		}catch (NonUniqueResultException noUnique){
//
//		}
//
//		return device;
//	}

	public List<Device> findByDepartCode(String departCode, String appId){
		try {
			String stringQuery = "SELECT dv from Device dv " +
					"where dv.departCode = :departCode " +
					"and dv.appId = :appId";

			Query query = entityManager.createQuery(stringQuery)
					.setParameter("departCode", departCode)
					.setParameter("appId", appId);

			return query.getResultList();
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	public Device findOneByIp(String ip) {
		Device device = null;
		
		try {
			device = this.entityManager.createQuery(
					"SELECT t FROM Device t where t.ip = :ip", Device.class)
	        .setParameter("ip", ip).getSingleResult();
		} catch(NoResultException noresult) {
		    // if there is no result
		} catch(NonUniqueResultException notUnique) {
		    // if more than one result
		}
		return device;
	}

	public List<Device> findAllByIp(String ip, String appId) {
		List<Device> device = null;

		try {
			device = this.entityManager.createQuery(
					"SELECT t FROM Device t where t.ip like :ip and t.appId =:appId", Device.class)
					.setParameter("ip", ip)
					.setParameter("appId", appId)
					.getResultList();
		} catch(NoResultException noresult) {
			// if there is no result
		} catch(NonUniqueResultException notUnique) {
			// if more than one result
		}
		return device;
	}

	public Iterable<Device> findConnectSessionUserId(String userId){
		String stringQuery = "SELECT dv from Device dv " +
				"where dv.userId = :userId ";

		Query query = entityManager.createQuery(stringQuery)
				.setParameter("userId", userId);

		return query.getResultList();
	}

	public void initWebSocketState() {
		String updateQuery = "UPDATE Device dv " +
				"SET dv.sessionState = :sessionState ";

		entityManager.createQuery(updateQuery)
				.setParameter("sessionState", WebsocketState.CL_WEBSOCKET_STATE_INACTIVE)
				.executeUpdate();
	}

	public List<Device> getDelegateList(String ip, String distVersion, Integer numMaxResult) {

		Query query = entityManager.createQuery(
				"SELECT dv"
				+ " FROM Device dv"
				+ " WHERE dv.distVersion = :distVersion"
				+ " AND dv.state='DistributionCompleted'"
				+ " AND dv.ip LIKE :likeIp"
				+ " AND dv.ip <> :ip"
				+ " ORDER BY dv.lastDate desc", Device.class)
				.setParameter("distVersion", distVersion)
				.setParameter("likeIp", ip.substring(0, ip.lastIndexOf(".") + 1) + "%")
				.setParameter("ip", ip)
				.setMaxResults(numMaxResult);

		return query.getResultList();
	}

	public List<Device> getLoginDeviceList(WebsocketState sessionState){
		List<Device> deviceList = null;

		Query query = entityManager.createQuery(
				"SELECT dv"
				+ " FROM Device dv"
				+ " WHERE dv.sessionState = :sessionState", Device.class)
				.setParameter("sessionState", sessionState);
		deviceList = query.getResultList();

		return deviceList;
	}
}
