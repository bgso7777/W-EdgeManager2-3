package com.inswave.appplatform.requestcontroller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping(value = "/api/log")
public class ApiElasticsearchDaoController {
	
   private static final Logger logger = LoggerFactory.getLogger(Class.class);

   /**
    * 
    * @param indexName
    * @param documents
    * @return
    */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/c/{indexName}")
	@ResponseBody
	public Object logInsert(@PathVariable("indexName") String indexName, @RequestBody List<Object> documents) {
		IData iData = new NodeData();
		try {
			logger.debug("[ApiDaoController][logInsert] indexName -->>"+indexName);
			Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE + indexName);
			Constructor<?> constructor = cls.getConstructor();
			Object node = constructor.newInstance();
			Method method = cls.getMethod("insert", String.class, List.class);
			iData = (IData)method.invoke(node, indexName, documents);
		} catch (Throwable e) {
			e.printStackTrace();
			iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
		}
		return iData.toString();
	}
	
	/**
	 * 
	 * @param indexName
	 * @param id
	 * @return
	 */
	@CrossOrigin(origins="*", allowedHeaders="*")
   	@GetMapping(value = "/r/{indexName}/{id}")
   	@ResponseBody
   	public Object select(@PathVariable("indexName") String indexName, @PathVariable("id") Long id) {
		IData iData = new NodeData();
		try {
			logger.debug("[ApiDaoController][logSelect] indexName -->>"+indexName);
			Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE + indexName);
			Constructor<?> constructor = cls.getConstructor();
			Object node = constructor.newInstance();
			Method method = cls.getMethod("select", String.class, Long.TYPE);
			iData = (IData)method.invoke(node, indexName, id);
		} catch (Throwable e) {
			e.printStackTrace();
			iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
		}
		return iData.toString();
   	}
   
	@CrossOrigin(origins="*", allowedHeaders="*")
    @DeleteMapping(value = "/d/{indexName}/{id}")
    @ResponseBody
    public Object delete(@PathVariable("indexName") String indexName, @PathVariable("id") Long id) {
		IData iData = new NodeData();
		try {
			logger.debug("[ApiDaoController][logDelete] indexName -->>"+indexName);
			Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE + indexName);
			Constructor<?> constructor = cls.getConstructor();
			Object node = constructor.newInstance();
			Method method = cls.getMethod("delete", String.class, Long.TYPE);
			iData = (IData)method.invoke(node, indexName, id);
		} catch (Throwable e) {
			e.printStackTrace();
			iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
		}
		return iData.toString();
	}
	
	@CrossOrigin(origins="*", allowedHeaders="*")
	//@PutMapping(value = "/client/log/u/{indexName}") 
	@PatchMapping(value = "/u/{indexName}")
	@ResponseBody
	public Object update(@PathVariable("indexName") String indexName, @RequestBody List<Object> documents) {
		IData iData = new NodeData();
		try {
			logger.debug("[ApiDaoController][logUpdate] indexName -->>"+indexName);
			Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE + indexName);
			Constructor<?> constructor = cls.getConstructor();
			Object node = constructor.newInstance();
            Method method = cls.getMethod("update", String.class, List.class);
            iData = (IData)method.invoke(node, indexName, documents);
			
		} catch (Throwable e) {
			e.printStackTrace();
			iData.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
		}
		return iData.toString();
	}


	@CrossOrigin(origins="*", allowedHeaders="*")
	@PostMapping(value = "/dao")
	@ResponseBody
	public String reauestDao(@RequestBody JSONObject jSONObject) {
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
			Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE+reqService);
			Object node = BeanUtils.getBean(cls);
			Method method = cls.getMethod(reqDaoOperation, IData.class, IData.class);
			Object resObject = method.invoke(node, reqIData, resIData);
			resIData = (IData) resObject;
		} catch(NoSuchBeanDefinitionException e ) {
			try {
				Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE + reqService);
				Constructor<?> constructor = cls.getConstructor();
				Object node = constructor.newInstance();
				Method method = cls.getMethod(reqDaoOperation, IData.class, IData.class);
				Object resObject = method.invoke(node, reqIData, resIData);
				resIData = (IData) resObject;
			} catch (Throwable ee) {
				e.printStackTrace();
				IData body = new NodeData();
				body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
				body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
				body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_SERVICE_PROCESS)));
				resIData.put(Constants.TAG_BODY,body);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			IData body = new NodeData();
			body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
			body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
			body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_SERVICE_PROCESS)));
			resIData.put(Constants.TAG_BODY,body);
		}
		return resIData.toString();
	}
}
