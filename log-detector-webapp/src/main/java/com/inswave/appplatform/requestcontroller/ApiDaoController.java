package com.inswave.appplatform.requestcontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.LanguagePack;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.Parse;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.transaver.util.BeanUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping(value = "/api/wem")
public class ApiDaoController {

	private static final Logger logger = LoggerFactory.getLogger(Class.class);

    @GetMapping(value = "/health")
    @ResponseBody
    public ResponseEntity health() {
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @GetMapping(value = "/r/{tableName}/{id}")
    @ResponseBody
    public List<Object> select(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
		logger.debug("[ApiDaoController][select]");
		logger.debug("tableName==>>"+tableName);
		logger.debug("id==>>"+id);
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("select", String.class, Long.TYPE);
            Object obj = method.invoke(node, tableName, id);
            return (List<Object>) obj;

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // error code return
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/ra/{tableName}")
    @ResponseBody
    public String selectAll(@PathVariable("tableName") String tableName, @RequestBody JsonNode jsonNode, Authentication authentication) {
        logger.debug("[ApiDaoController][select with specification & pagable & sort");
        Long ret = 0L; // 1< sucess,  0>= error
        logger.debug("tableName==>>"+tableName);
        logger.debug("jsonNode.size()==>>"+jsonNode.size());
        IData resIData = new NodeData();
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("selectAll", String.class, JsonNode.class);
            Object obj = method.invoke(node, tableName, jsonNode);
            resIData = (IData) obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return resIData.toString();
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/sa/{tableName}")
    @ResponseBody
    public String saveAll(@PathVariable("tableName") String tableName, @RequestBody JsonNode jsonNode, Authentication authentication) {
        logger.debug("[ApiDaoController][save(C,U,D]");
        Long ret = 0L; // 1< sucess,  0>= error
        logger.debug("tableName==>>"+tableName);
        logger.debug("jsonNode.size()==>>"+jsonNode.size());
        IData resIData = new NodeData();
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("saveAll", String.class, JsonNode.class, Authentication.class);
            Object obj = method.invoke(node, tableName, jsonNode, authentication);
            resIData = (IData) obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return resIData.toString();
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/insert-async/{tableName}")
    @ResponseBody
    public String insertAsync(@PathVariable("tableName") String tableName, @RequestBody JsonNode jsonNode, Authentication authentication) {
        Long ret = 0L; // 1< sucess,  0>= error
        IData resIData = new NodeData();
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("insertAsync", String.class, JsonNode.class, Authentication.class);
            Object obj = method.invoke(node, tableName, jsonNode, authentication);
            resIData = (IData) obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return resIData.toString();
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/c/{tableName}")
    @ResponseBody
    public Long insert(@PathVariable("tableName") String tableName, @RequestBody List<Object> objects) {
		logger.debug("[ApiDaoController][insert]");
        Long ret = 0L; // 1< sucess,  0>= error
        logger.debug("tableName==>>"+tableName);
        logger.debug("objects.size()==>>"+objects.size());
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("insert", String.class, List.class);
            Object obj = method.invoke(node, tableName, objects);
            return (Long)obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     *
     * @param tableName
     * @param objects
     * @return
     */
	@CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/u/{tableName}")
    @ResponseBody
    public Object update(@PathVariable("tableName") String tableName, @RequestBody List<Object> objects) {
		logger.debug("[ApiDaoController][update]");
        Long ret = 0L; // 1< sucess,  0>= error
        logger.debug("tableName==>>"+tableName);
        logger.debug("objects.size()==>>"+objects);
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("update", String.class, List.class);
            Object obj = method.invoke(node, tableName, objects);
            return (Long)obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

	@CrossOrigin(origins="*", allowedHeaders="*")
    @DeleteMapping(value = "/d/{tableName}/{idx}")
    @ResponseBody
    public Long delete(@PathVariable("tableName") String tableName, @PathVariable("idx") Long id) {
		logger.debug("[ApiDaoController][delete]");
        Long ret = 0L; // 1< sucess,  0>= error
        logger.debug("tableName==>>"+tableName);
        logger.debug("id==>>"+id);
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_RDBDAO_STANDARD_SERVICE);
            Object node = BeanUtils.getBean(cls);
            Method method = cls.getMethod("delete", String.class, Long.TYPE );
            Object obj = method.invoke(node, tableName, id);
            return (Long)obj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/{runMode2}/dao")
    @ResponseBody
    public String reauestDao(@PathVariable("runMode2") String runMode2, @RequestBody JSONObject jSONObject) {
        logger.debug("[ApiDaoController][reauestDoa]");

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource = "", reqService = "", reqDaoOperation = "", reqDestination = "";
        Long countryId=null;
        try {
            reqIData.setObject(jSONObject);
            resIData = parse.copyHeader(reqIData);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            reqDaoOperation = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DAO_OPERATION);
            reqDestination = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY, body);
            return resIData.toString();
        }

        try {
            Class<?> cls = Class.forName(Constants.PACKATE_OF_SERVICE + reqService);
            Object node = BeanUtils.getBean(cls);

            Method method = cls.getMethod(reqDaoOperation, IData.class, IData.class);
            Object resObject = method.invoke(node, reqIData, resIData); // -1 all
            resIData = (IData) resObject;
        } catch(NoSuchBeanDefinitionException e ) {
            try {
                Class<?> cls = Class.forName(Constants.PACKATE_OF_SERVICE + reqService);
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();

                Method method = cls.getMethod("excutePost", IData.class, IData.class);
                Object resObject = method.invoke(node, reqIData, resIData); // -1 all
                resIData = (IData) resObject;
            } catch (Throwable ee) {
                e.printStackTrace(); // he
                IData body = new NodeData();
                body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
                body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
                body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_SERVICE_PROCESS)));
                resIData.put(Constants.TAG_BODY,body);
            }
        } catch (Throwable e) {
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_SERVICE_PROCESS)));
            resIData.put(Constants.TAG_BODY,body);
        }
        return resIData.toString();
    }

}