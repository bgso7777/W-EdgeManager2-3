package com.inswave.appplatform.service.rdbdao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.LanguagePack;
import com.inswave.appplatform.core.security.DomainUserDetails;
import com.inswave.appplatform.dao.Domain;
import com.inswave.appplatform.dao.RdbDaoPackage;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.domain.*;
import com.inswave.appplatform.service.ExternalRdbDaoService;
import com.inswave.appplatform.service.dao.StandardDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableTransactionManagement
@Service
@Transactional
public class StandardService extends StandardServiceHelper implements ExternalRdbDaoService {

    private Map<String, Map<String, Object>> insertAsyncEntitiesMap          = new ConcurrentHashMap<>();
    private int                              asyncCheckPointMaxEntityCount   = 1000;
    private int                              asyncCheckPointMaxPeriodSeconds = 30;
    private EntityManager                    entityManager;

    public StandardService(@Value("${wedgemanager.service.asyncCheckPointMaxEntityCount:1000}") int asyncCheckPointMaxEntityCount,
                           @Value("${wedgemanager.service.asyncCheckPointMaxPeriodSeconds:30}") int asyncCheckPointMaxPeriodSeconds,
                           @Qualifier("wemEntityManager") EntityManager entityManager
    ) {
        this.asyncCheckPointMaxEntityCount = asyncCheckPointMaxEntityCount;
        this.asyncCheckPointMaxPeriodSeconds = asyncCheckPointMaxPeriodSeconds;
        this.entityManager = entityManager;
    }

    private Object[] getTableEntity(String tableEntityName, List<Domain> reqTableEntities) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] ret = new Object[0];
        if (tableEntityName.equals(AdminUser.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<AdminUser>>() {
            })).toArray();
        else if (tableEntityName.equals(License.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<License>>() {
            })).toArray();
        else if (tableEntityName.equals(Role.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<Role>>() {
            })).toArray();
        else if (tableEntityName.equals(Site.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<Site>>() {
            })).toArray();
        else if (tableEntityName.equals(ConfigManager.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<ConfigManager>>() {
            })).toArray();
        else if (tableEntityName.equals(ConfigLog.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<ConfigLog>>() {
            })).toArray();
        else if (tableEntityName.equals(Menu.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<Menu>>() {
            })).toArray();
        else if (tableEntityName.equals(MenuRole.class.getSimpleName()))
            ret = (objectMapper.convertValue(reqTableEntities, new TypeReference<List<MenuRole>>() {
            })).toArray();

        else {
            if (com.inswave.appplatform.Config.getInstance().getRunMode2().equals(Constants.RUN_MODE2_LOG)) {
                try {
                    Class<?> cls = Class.forName(Constants.CLASS_OF_LOG_TABLEENTITYPROVIDER);
                    Constructor<?> constructor = cls.getConstructor();
                    Object node = constructor.newInstance();
                    Method method = cls.getMethod(Constants.METHOD_OF_LOG_TABLEENTITYPROVIDER, String.class, List.class);
                    Object object = method.invoke(node, tableEntityName, reqTableEntities);
                    ret = (Object[]) object;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    @Override
    public IData select(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        List<Object> selectResult = null;
        Long id = -1L;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            jpaRepository = findJpaRepository(tableEntityName);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        long sucessCount = 0;
        try {
            Parse parse = new Parse();
            if (id > 0)
                selectResult = Collections.singletonList((List<Domain>) (List) Collections.singletonList(jpaRepository.findById(id)));
            else
                selectResult = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(jpaRepository.findAll())));
            String info = parse.toString(selectResult);
            ObjectMapper mapper = new ObjectMapper();
            JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
            sucessCount = jSONArray.size();
            List<Object> jSONArray0 = (List<Object>) jSONArray.get(0);
            List<Object> jSONArray1 = (List<Object>) jSONArray0.get(0);

            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS, jSONArray1);
            body.setObject(jsonObj1);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
        }
        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    @Transactional
    @Override
    public IData select2(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        Integer sucessCount = 0;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
            jpaRepository = findJpaRepository(tableEntityName);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        try {
            JSONObject jSONObject = (JSONObject) reqIData.getObject();
            HashMap hashMap = (HashMap) jSONObject.get(Constants.TAG_BODY);

            StringBuffer selectColumns = new StringBuffer();
            LinkedHashMap selectColumnsLinkedHashMap = (LinkedHashMap) reqIData.getBodyValue("selectColumns");
            for (Object object : selectColumnsLinkedHashMap.entrySet()) {
                Entry mapEntry = (Entry) object;
                String key = (String) mapEntry.getKey();
                selectColumns.append(" a." + key + ",");
            }
            if (selectColumns.equals("") || selectColumnsLinkedHashMap.size() <= 0) {
                //                body.put(Constants.TAG_RESULT, new SimpleData(Constants.ERROR_RDB_SELECT_QUERY_CODE));
                //                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_SELECT_QUERY_CODE));
                //                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_RDB_SELECT_QUERY)));
                //                resIData.put(Constants.TAG_BODY,body);
                //                return resIData;
                selectColumns = new StringBuffer("*");
            } else {
                selectColumns = new StringBuffer(selectColumns.substring(0, selectColumns.length() - 1));
            }

            StringBuffer whereColumnAndValue = new StringBuffer();
            LinkedHashMap whereColumns = (LinkedHashMap) reqIData.getBodyValue("whereColumns");
            for (Object object : whereColumns.entrySet()) {
                Entry mapEntry = (Entry) object;
                String key = (String) mapEntry.getKey();
                if (tableEntityName.equals("MenuRole") && key.equals("roleId"))
                    key = "ROLE_ID";
                else if (tableEntityName.equals("MenuRole") && key.equals("menuId"))
                    key = "MENU_ID";

                Object value = mapEntry.getValue();
                if (value instanceof String)
                    //whereColumnAndValue.append(" a."+key+"='"+value+"'AND");
                    whereColumnAndValue.append(" " + key + "='" + value + "'AND");
                else
                    //whereColumnAndValue.append(" a."+key+"="+value+"AND");
                    whereColumnAndValue.append(" " + key + "=" + value + "AND");
            }
            if (whereColumnAndValue.toString().equals("")) {
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DATA_NOT_FOUND));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DATA_NOT_FOUND)));
                resIData.put(Constants.TAG_BODY, body);
                return resIData;
            } else {
                whereColumnAndValue = new StringBuffer(whereColumnAndValue.substring(0, whereColumnAndValue.length() - 3));
            }

            //StringBuffer queryString = new StringBuffer("SELECT "+selectColumns+" FROM "+tableEntityName+" WHERE "+whereColumnAndValue);
            StringBuffer queryString = new StringBuffer("FROM " + tableEntityName + " WHERE " + whereColumnAndValue);

            Object objectDomain = RdbDaoPackage.getInstance().getDomain(tableEntityName);

            Session session = (Session) RdbDaoPackage.getInstance().getDao(Session.class.getSimpleName());
            try {
                Transaction transaction = session.beginTransaction();
            } catch (Exception e) {
            }
            //Query query = session.createQuery(queryString.toString(), objectDomain.getClass());
            Query query = session.createQuery(queryString.toString());
            //sucessCount = query.getResultList().size();

            Parse parse = new Parse();
            Object obj = query.getResultList();
            String info = parse.toString(query.getResultList());
            ObjectMapper mapper = new ObjectMapper();
            JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
            sucessCount = jSONArray.size();
            //            List<Object> jSONArray0 = (List<Object>) jSONArray.get(0);
            //            List<Object> jSONArray1 = (List<Object>) jSONArray0.get(0);

            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS, jSONArray);
            body.setObject(jsonObj1);

            //session.close(); // close하면 안됨
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
        }
        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    @Override
    public IData insert(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        List<Domain> reqTableEntities = null;
        Long sucessCount = 0L;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            jpaRepository = (JpaRepository) RdbDaoPackage.getInstance().getDao(tableEntityName);
            reqTableEntities = (List<Domain>) reqIData.getBodyValue(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        try {
            Object[] objects = getTableEntity(tableEntityName, reqTableEntities);
            for (Object object : objects) {
                try {
                    jpaRepository.save(object);
                    sucessCount++;
                } catch (Exception e) {
                    e.printStackTrace(); // row의 에러 처리 ??????????????
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
        }
        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    @Override
    public IData update(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        List<Domain> reqTableEntities = null;
        Long sucessCount = 0L;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            jpaRepository = findJpaRepository(tableEntityName);
            reqTableEntities = (List<Domain>) reqIData.getBodyValue(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        try {
            Object[] objects = getTableEntity(tableEntityName, reqTableEntities);
            for (Object object : objects) {
                try {
                    jpaRepository.save(object);
                    sucessCount++;
                } catch (Exception e) {
                    e.printStackTrace(); // row의 에러 처리 ??????????????
                    body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
        }
        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    //    @Transactional
    //    @Override
    //    public IData updateColumn(IData reqIData, IData resIData) {
    //
    //        IData body = new NodeData();
    //        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
    //
    //        String tableEntityName = "";
    //        JpaRepository jpaRepository = null;
    //        List<Domain> reqTableEntities = null;
    //        long sucessCount=0;
    //        try{
    //            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
    //            jpaRepository = (JpaRepository) RdbDaoPackage.getInstance().getDao(tableEntityName);
    //            reqTableEntities = (List<Domain>) reqIData.getBodyValue(tableEntityName+Constants.TAG_TABLE_ENTITY_ROWS);
    //        } catch(Exception e) {
    //            e.printStackTrace();
    //            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
    //            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT));
    //            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_DESCRIPTION));
    //            resIData.put(Constants.TAG_BODY,body);
    //            return resIData;
    //        }
    //
    //        try{
    ////            Object[] objects =  getTableEntity(tableEntityName,reqTableEntities);
    ////            for (Object object: objects) {
    ////                try{
    ////                    AdminUserDao adminUserDao = (AdminUserDao)jpaRepository;
    ////
    ////                    //EntityManager entityManager = (EntityManager) RdbDaoPackage.getInstance().getDao(EntityManager.class.getSimpleName());
    ////                    EntityManagerFactory entityManagerFactory = (EntityManagerFactory) RdbDaoPackage.getInstance().getDao(EntityManagerFactory.class.getSimpleName());
    ////                    EntityManager entityManager = null;
    ////                    if(entityManagerFactory==null)
    ////                        entityManager = (EntityManager)RdbDaoPackage.getInstance().getDao(EntityManager.class.getSimpleName());
    ////
    ////                    EntityTransaction entityTransaction = entityManager.getTransaction();
    ////                    entityTransaction.begin();
    ////                    entityManager.createNativeQuery("UPDATE AdminUser a  SET a.password=?1 WHERE a.adminUserId=?2").setParameter(1,"").setParameter(2,10L).executeUpdate();
    ////                    entityTransaction.commit();
    ////                    entityManager.close();
    ////
    ////                    System.out.println();
    ////                    sucessCount++;
    ////                } catch(Exception e) {
    ////                    e.printStackTrace(); // row의 에러 처리 ??????????????
    ////                    e.getMessage();
    ////                    e.printStackTrace();
    ////                    body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
    ////                }
    ////            }
    //
    //            Session session = (Session)RdbDaoPackage.getInstance().getDao(Session.class.getSimpleName());
    //            Transaction transaction = session.beginTransaction();
    //            String sql = "UPDATE AdminUser SET password=1111 WHERE adminUserId=2346";
    //            Query query = session.createQuery(sql);
    //            query.executeUpdate();
    //            transaction.commit();
    //            session.close();
    //
    //        } catch(Exception e) {
    //            e.printStackTrace();
    //            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
    //            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY));
    //            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(Constants.ERROR_RDB_QUERY_DESCRIPTION));
    //        }
    //        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
    //        resIData.put(Constants.TAG_BODY,body);
    //        return resIData;
    //    }

    @Transactional
    @Override
    public IData updateColumn(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        List<Domain> reqTableEntities = null;
        Long sucessCount = 0L;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
            jpaRepository = findJpaRepository(tableEntityName);
            reqTableEntities = (List<Domain>) reqIData.getBodyValue(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        int queryResult = -1;
        try {
            JSONObject jSONObject = (JSONObject) reqIData.getObject();
            HashMap hashMap = (HashMap) jSONObject.get(Constants.TAG_BODY);
            String idColumn = "";
            Integer idColumnValue = 0;
            for (Object object : hashMap.entrySet()) {
                Entry mapEntry = (Entry) object;
                String key = (String) mapEntry.getKey();
                if (key.lastIndexOf("Id") != -1) {
                    idColumn = key;
                    idColumnValue = (Integer) mapEntry.getValue();
                    break;
                }
            }
            if (idColumn.equals("") || idColumnValue <= 0) {
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_ID_NOT_FOUND));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_ID_NOT_FOUND));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_ID_NOT_FOUND)));
                resIData.put(Constants.TAG_BODY, body);
                return resIData;
            }

            StringBuffer columnValue = new StringBuffer();
            LinkedHashMap columns = (LinkedHashMap) reqIData.getBodyValue("columns");
            for (Object object : columns.entrySet()) {
                Entry mapEntry = (Entry) object;
                String key = (String) mapEntry.getKey();
                Object value = mapEntry.getValue();
                if (value instanceof String)
                    columnValue.append(" " + key + "='" + value + "',");
                else
                    columnValue.append(" " + key + "=" + value + ",");
            }
            if (columnValue.toString().equals("")) {
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_DATA_NOT_FOUND));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DATA_NOT_FOUND));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DATA_NOT_FOUND)));
                resIData.put(Constants.TAG_BODY, body);
                return resIData;
            } else {
                columnValue = new StringBuffer(columnValue.substring(0, columnValue.length() - 1));
            }
            //StringBuffer queryString = new StringBuffer("UPDATE "+tableEntityName+" SET password=1111 WHERE adminUserId=2346");// ""UPDATE "+tableEntityName+" SET password=1111 WHERE adminUserId=2346";
            StringBuffer queryString = new StringBuffer("UPDATE " + tableEntityName + " SET " + columnValue.toString() + " WHERE " + idColumn + "=" + idColumnValue);

            Session session = (Session) RdbDaoPackage.getInstance().getDao(Session.class.getSimpleName());
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
            } catch (Exception e) {
            }
            Query query = session.createQuery(queryString.toString());
            queryResult = query.executeUpdate();
            transaction.commit();
            //session.close(); // close하면 안됨
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_RDB_QUERY_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_RDB_QUERY)));
        }
        body.put(Constants.TAG_QUERY_RESULT, new SimpleData(queryResult));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    @Override
    public IData delete(IData reqIData, IData resIData) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        String tableEntityName = "";
        JpaRepository jpaRepository = null;
        List<Domain> reqTableEntities = null;
        Long sucessCount = 0L;
        Long countryId = null;
        try {
            tableEntityName = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            jpaRepository = findJpaRepository(tableEntityName);
            reqTableEntities = (List<Domain>) reqIData.getBodyValue(tableEntityName + Constants.TAG_TABLE_ENTITY_ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(""));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));
            resIData.put(Constants.TAG_BODY, body);
            return resIData;
        }

        try {
            for (int i = 0; i < reqTableEntities.size(); i++) {
                try {
                    LinkedHashMap linkedHashMap = (LinkedHashMap) reqTableEntities.get(i);
                    Integer id = (Integer) linkedHashMap.get("id");
                    jpaRepository.deleteById(id.longValue());
                    sucessCount++;
                } catch (Exception e) {
                    body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                    e.printStackTrace(); // row의 에러 처리 ??????????????
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(""));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));
        }
        body.put(Constants.TAG_SUCESS_COUNT, new SimpleData(sucessCount));
        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    @Override
    public List<Domain> select(String tableEntityName, long id) {
        JpaRepository jpaRepository = findJpaRepository(tableEntityName);
        if (id > 0)
            return (List<Domain>) (List) Collections.singletonList(jpaRepository.findById(id));
        return (List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(jpaRepository.findAll()));
    }

    @Override
    public long insert(String tableEntityName, List<Domain> reqTableEntities) {
        long ret = 0;
        JpaRepository jpaRepository = findJpaRepository(tableEntityName);
        Object[] objects = getTableEntity(tableEntityName, reqTableEntities);
        for (Object object : objects) {
            jpaRepository.save(object);
            ret++;
        }
        return ret;
    }

    @Override
    public long update(String tableEntityName, List<Domain> reqTableEntities) {
        long ret = 0;
        JpaRepository jpaRepository = findJpaRepository(tableEntityName);
        Object[] objects = getTableEntity(tableEntityName, reqTableEntities);
        for (Object object : objects) {
            jpaRepository.save(object);
            ret++;
        }
        return ret;
    }

    @Override
    public long delete(String tableEntityName, long id) {
        long ret = 0;
        JpaRepository jpaRepository = findJpaRepository(tableEntityName);
        jpaRepository.deleteById(id);
        ret++;
        return ret;
    }

    //    @Scheduled(fixedDelay = 60000) // 1분 간격, 누적 건 bulk insert (체크포인트 발생 시점 (1만 건 누적 시 or 2분 초과 시)
    @Scheduled(fixedDelay = 5000) // 5초 간격, 누적 건 bulk insert (체크포인트 발생 시점 (100 건 누적 시 or 5초 초과 시)
    public void insertAsyncSchedule() {
        insertAsyncEntitiesMap.entrySet().forEach(stringMapEntry -> {
            List entities = (List) stringMapEntry.getValue().get("entities");
            ZonedDateTime lastInsertedDatetime = (ZonedDateTime) stringMapEntry.getValue().get("lastInsertedDatetime");
            if (entities.size() >= asyncCheckPointMaxEntityCount
                || (entities.size() > 0 && (lastInsertedDatetime.toEpochSecond() + asyncCheckPointMaxPeriodSeconds > ZonedDateTime.now().toEpochSecond()))) {
                StopWatch stopWatch = new StopWatch("insertAsyncSchedule");
                stopWatch.start();
                log.info("st insertAsyncSchedule ({}) :  sttime = {}, size = {}", stringMapEntry.getKey(), ZonedDateTime.now().toEpochSecond(), entities.size());
                stringMapEntry.getValue().put("entities", new ArrayList<>());
                stringMapEntry.getValue().put("lastInsertedDatetime", ZonedDateTime.now());
                StandardDao standardDao = (StandardDao) findJpaRepository(stringMapEntry.getKey());
                standardDao.saveAll(entities);

                stopWatch.stop();
                log.info("ed insertAsyncSchedule ({}) :  edtime = {}, TotalTimeSeconds = {}", stringMapEntry.getKey(), ZonedDateTime.now().toEpochSecond(), stopWatch.getTotalTimeSeconds());
            }
        });
    }

    public synchronized IData insertAsync(String tableEntityName, JsonNode jsonNode, Authentication authentication) {
        IData resIData = new NodeData();
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        Long countryId = null;
        try {
            List<Object> inserted = jsonNode.has("inserted") ? toEntityList(tableEntityName, jsonNode.get("inserted")) : new ArrayList<>();

            String userName = "unknown";
            String userId = "unknown";
            if (Objects.nonNull(authentication)) {
                DomainUserDetails user = (DomainUserDetails) authentication.getPrincipal(); //  UserDetails를 상속받아 UserName, UserId를 모두 포함하도록 처리필요
                userName = user.getRealName();
                userId = user.getUsername();
            }
            insertAudit(inserted, userId, userName);

            Map<String, Object> insertAsyncEntities = new HashMap<>();
            if (insertAsyncEntitiesMap.get(tableEntityName) != null) {
                insertAsyncEntities = insertAsyncEntitiesMap.get(tableEntityName);
                ((List) insertAsyncEntities.get("entities")).addAll(inserted);
            } else {
                insertAsyncEntities.put("entities", inserted);
                insertAsyncEntities.put("lastInsertedDatetime", ZonedDateTime.now());
                insertAsyncEntitiesMap.put(tableEntityName, insertAsyncEntities);
            }

            body.put(Constants.TAG_INSERT_REQUEST_COUNT, new SimpleData(inserted.size()));

        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            return resIData;
        }

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public IData saveAll(String tableEntityName, JsonNode jsonNode, Authentication authentication) {
        IData resIData = new NodeData();
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        Long countryId = null;
        try {
            boolean deleteAll = jsonNode.has("deleteAll") ? jsonNode.get("deleteAll").asBoolean(false) : false;
            List<Object> deleted = jsonNode.has("deleted") ? toEntityList(tableEntityName, jsonNode.get("deleted")) : new ArrayList<>();
            List<Object> updated = jsonNode.has("updated") ? toEntityList(tableEntityName, jsonNode.get("updated")) : new ArrayList<>();
            List<Object> inserted = jsonNode.has("inserted") ? toEntityList(tableEntityName, jsonNode.get("inserted")) : new ArrayList<>();

            List<Object> updatedDone = new ArrayList<>();
            List<Object> insertedDone = inserted;
            List<Object> deletedDone = deleted;

            String userName = "unknown";
            String userId = "unknown";
            if (Objects.nonNull(authentication)) {
                DomainUserDetails user = (DomainUserDetails) authentication.getPrincipal(); //  UserDetails를 상속받아 UserName, UserId를 모두 포함하도록 처리필요
                userName = user.getRealName();
                userId = user.getUsername();
            }
            insertAudit(inserted, userId, userName);
            updateAudit(updated, userId, userName);

            StandardDao standardDao = (StandardDao) findJpaRepository(tableEntityName);
            if (deleteAll) {
                standardDao.deleteAll();
            } else {
                standardDao.deleteAll(deleted);
            }
            updatedDone = standardDao.saveAll(updated);

            //            standardDao.saveAll(inserted);
            for (int i = 0; i < inserted.size(); i++) {
                entityManager.persist(inserted.get(i));
            }
            ObjectMapper mapper = new ObjectMapper();
            Parse parse = new Parse();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(Constants.TAG_DELETED_ROWS, mapper.readValue(parse.toString(deletedDone), JSONArray.class));
            jsonObj.put(Constants.TAG_INSERTED_ROWS, mapper.readValue(parse.toString(insertedDone), JSONArray.class));
            jsonObj.put(Constants.TAG_UPDATED_ROWS, mapper.readValue(parse.toString(updatedDone), JSONArray.class));
            body.setObject(jsonObj);
            body.put(Constants.TAG_DELETED_COUNT, new SimpleData(deleted.size()));
            body.put(Constants.TAG_UPDATED_COUNT, new SimpleData(updated.size()));
            body.put(Constants.TAG_INSERTED_COUNT, new SimpleData(inserted.size()));

        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            return resIData;
        }

        resIData.put(Constants.TAG_BODY, body);
        return resIData;
    }

    public IData selectAll(String tableEntityName, JsonNode jsonNode) {
        IData resIData = new NodeData();
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));

        Long countryId = null;
        try {
            Specification specification = toSpecification(jsonNode.get("where"));
            PageRequest pageRequest = toPageRequest(jsonNode.get("pageable"));

            StandardDao standardDao = (StandardDao) findJpaRepository(tableEntityName);

            JSONObject jsonObj1 = new JSONObject();
            Page page = standardDao.findAll(specification, pageRequest);

            Parse parse = new Parse();
            String info = parse.toString(page.getContent());
            ObjectMapper mapper = new ObjectMapper();
            JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
            jsonObj1.put(Constants.TAG_TABLE_ENTITY_ROWS, jSONArray);
            body.setObject(jsonObj1);
            body.put(Constants.TAG_PAGE_ROW_COUNT, new SimpleData(page.getTotalElements()));
            body.put(Constants.TAG_PAGE_NUMBER, new SimpleData(page.getNumber()));
            body.put(Constants.TAG_PAGE_SIZE, new SimpleData(page.getSize()));
            body.put(Constants.TAG_PAGE_COUNT, new SimpleData(page.getTotalPages()));

        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_DOMAIN_FORMAT_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId, Constants.ERROR_DOMAIN_FORMAT)));
            return resIData;
        }

        return resIData;
    }
}