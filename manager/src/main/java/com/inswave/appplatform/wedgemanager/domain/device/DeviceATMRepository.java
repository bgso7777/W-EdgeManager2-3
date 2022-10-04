package com.inswave.appplatform.wedgemanager.domain.device;

import com.inswave.appplatform.wedgemanager.enums.WebsocketState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class DeviceATMRepository extends SimpleJpaRepository<DeviceATM, DeviceKey> {
    EntityManager entityManager;

    public DeviceATMRepository(@Qualifier("wemEntityManager") EntityManager entityManager) {
        super(DeviceATM.class, entityManager);
        this.entityManager = entityManager;
    }

    public DeviceATM findOneByIp(String ip) {
        DeviceATM device = null;

        try {
            device = this.entityManager.createQuery(
                    "SELECT t FROM DeviceATM t where t.ip = :ip", DeviceATM.class)
                    .setParameter("ip", ip).getSingleResult();
        } catch (NoResultException noresult) {
            // if there is no result
        } catch (NonUniqueResultException notUnique) {
            // if more than one result
        }
        return device;
    }

    public List<DeviceATM> findAllByIp(String ip, String appId) {
        List<DeviceATM> device = null;

        try {
            device = this.entityManager.createQuery(
                    "SELECT t FROM DeviceATM t where t.ip like :ip and t.appId =:appId", DeviceATM.class)
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

    public Iterable<DeviceATM> findConnectSessionUserId(String userId){
        String stringQuery = "SELECT dv from Device dv " +
                "where dv.userId = :userId ";

        Query query = entityManager.createQuery(stringQuery)
                .setParameter("userId", userId);

        return query.getResultList();
    }

    public List<DeviceATM> findByDepartCode(String departCode, String appId){
        String stringQuery = "SELECT dv from DeviceATM dv " +
                "where dv.departCode = :departCode " +
                "and dv.appId =:appId";

        Query query = entityManager.createQuery(stringQuery)
                .setParameter("departCode", departCode)
                .setParameter("appId", appId);

        return query.getResultList();
    }

    public void initWebSocketState() {
        String updateQuery = "UPDATE DeviceATM dv " +
                "SET dv.sessionState = :sessionState ";

        entityManager.createQuery(updateQuery)
                .setParameter("sessionState", WebsocketState.CL_WEBSOCKET_STATE_INACTIVE)
                .executeUpdate();
    }

    public List<DeviceATM> getDelegateList(String ip, String distVersion, Integer numMaxResult) {

        Query query = entityManager.createQuery(
                "SELECT dv"
                        + " FROM DeviceATM dv"
                        + " WHERE dv.distVersion = :distVersion"
                        + " AND dv.distState IN ('S', 'R')"
                        + " AND dv.ip LIKE :likeIp"
                        + " AND dv.ip <> :ip"
                        + " ORDER BY dv.lastDate desc", DeviceATM.class)
                .setParameter("distVersion", distVersion)
                .setParameter("likeIp", ip.substring(0, ip.lastIndexOf(".") + 1) + "%")
                .setParameter("ip", ip)
                .setMaxResults(numMaxResult);

        return query.getResultList();
    }

    public List<DeviceATM> getLoginDeviceList(WebsocketState sessionState) {
        List<DeviceATM> deviceList = null;

        Query query = entityManager.createQuery(
                "SELECT dv"
                        + " FROM DeviceATM dv"
                        + " WHERE dv.sessionState = :sessionState", DeviceATM.class)
                .setParameter("sessionState", sessionState);
        deviceList = query.getResultList();

        return deviceList;
    }
}