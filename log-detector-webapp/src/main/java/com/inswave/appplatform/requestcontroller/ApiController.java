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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

//import com.inswave.appplatform.core.security.jwt.JwtTokenProvider;

@CrossOrigin(origins="*", allowedHeaders="*")
@Controller
@RequestMapping(value = "/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 전 서비스
     * @return
     */
    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/wem")
    @ResponseBody
    public Object openService(@RequestBody JSONObject jSONObject) {

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource="",reqService="";
        Long countryId=null;

        try {
            reqIData.setObject(jSONObject);
            resIData = parse.copyHeader(reqIData);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData.toString();
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls = Class.forName(Constants.PACKATE_OF_SERVICE+reqService);
            Object node = BeanUtils.getBean(cls);

            Method method = null;
            Object resObject = null;
            if(reqService.toLowerCase().equals("login")) { // login config 공통으로 처리..
//                method = cls.getMethod("excutePost", IData.class, IData.class, Object.class);
//                resObject = method.invoke(node, reqIData,resIData,jwtTokenProvider);
            } else {
                method = cls.getMethod("excutePost", IData.class, IData.class);
                resObject = method.invoke(node, reqIData,resIData);
            }
            resIData = (IData)resIData;
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

    /**
     * 로그인 후 기존 메니져 서비스
     * @param jSONObject
     * @return
     */
    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = {"/wem/manager/external"})
    @ResponseBody
    public String requestAdminManagerExternal(@RequestBody JSONObject jSONObject) {
        return requestAdmin(jSONObject, Constants.PACKATE_OF_MANAGER_EXTERNAL_SERVICE);
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = {"/wem/manager"})
    @ResponseBody
    public String requestAdminManager(@RequestBody JSONObject jSONObject) {
        return requestAdmin(jSONObject, Constants.PACKATE_OF_MANAGER_SERVICE);
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = {"/wem/deployer/resource"})
    public ResponseEntity<Resource> requestAdminDeployerResource(@RequestBody JSONObject jSONObject) {
        logger.debug("[ApiController][requestAdminDeployerResource]");

        IData reqIData = new NodeData();
        String reqSource="",reqService="",reqDestination="";
        String servicePackage = Constants.PACKATE_OF_DEPLOYER_SERVICE;

        try {
            reqIData.setObject(jSONObject);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            try{ reqDestination = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION); } catch (Exception e) { }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls;
            if(Constants.PACKATE_OF_DEPLOYER_SERVICE.equals(servicePackage)) {
                cls = Class.forName(servicePackage+reqService);
            }else{
                cls = Class.forName(servicePackage+reqSource.toLowerCase()+"."+reqService);
            }
            Object node = BeanUtils.getBean(cls);

            Method method = cls.getMethod(reqDestination, IData.class);
            return (ResponseEntity<Resource>) method.invoke(node, reqIData);
        } catch (Throwable e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = {"/wem/deployer"})
    @ResponseBody
    public String requestAdminDeployer(@RequestBody JSONObject jSONObject) {
        return requestAdmin(jSONObject, Constants.PACKATE_OF_DEPLOYER_SERVICE);
    }

    public String requestAdmin(JSONObject jSONObject, String servicePackage) {
        logger.debug("[ApiController][requestAdmin]");

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource="",reqService="",reqDestination="";
        Long countryId=null;

        try {
            reqIData.setObject(jSONObject);
            resIData = parse.copyHeader(reqIData);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            // bgso 20210729 필수 아님으로 null일 수 있음..
            ///////////////////////////////////////////
            try{ countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID); } catch (Exception e) { }
            try{ reqDestination = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION); } catch (Exception e) { }
            ///////////////////////////////////////////
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData.toString();
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls;
            if (Constants.PACKATE_OF_DEPLOYER_SERVICE.equals(servicePackage)) {
                cls = Class.forName(servicePackage + reqService);
            } else if (Constants.PACKATE_OF_MANAGER_EXTERNAL_SERVICE.equals(servicePackage)) {
                cls = Class.forName(servicePackage + reqService);
                reqDestination = null;
            } else {
                cls = Class.forName(servicePackage + reqSource.toLowerCase() + "." + reqService);
            }
            Object node = BeanUtils.getBean(cls);

            String strMethod = "excutePost";
            if (reqDestination != null && !reqDestination.equals(""))
                strMethod = reqDestination;

            Method method = cls.getMethod(strMethod, IData.class, IData.class);
            Object resObject = method.invoke(node, reqIData,resIData);
            resIData = (IData)resIData;
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

    /**
     * 로그인 후 log서비스
     * @param jSONObject
     * @return
     */
    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/wem/log")
    @ResponseBody
    public String requestLog(@RequestBody JSONObject jSONObject) {
        logger.debug("[ApiController][requestAdmin]");

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource="",reqService="",reqDaoOperation="", reqDestination="";
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
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData.toString();
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls = Class.forName(Constants.PACKATE_OF_LOG_SERVICE+reqSource+"."+reqService);
            Constructor<?> constructor = cls.getConstructor();
            Object node = constructor.newInstance();
            String methodStr = "excutePost";
            if(reqDestination!=null&&!reqDestination.equals(""))
                methodStr=reqDestination;
            Method method = cls.getMethod(methodStr, IData.class, IData.class);
            Object object = method.invoke(node, reqIData,resIData);
            resIData = (IData)resIData;
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

    /**
     * 로그인 필요없는 wgear 서비스
     * @param jSONObject
     * @return
     */
    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/wem/wgear")
    @ResponseBody
    public String requestWgear(@RequestBody JSONObject jSONObject) {
        logger.debug("[ApiController][requestWhubServiceData]");

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource="",reqService="";
        Long countryId=null;

        try {
            reqIData.setObject(jSONObject);
            resIData = parse.copyHeader(reqIData);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData.toString();
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls = Class.forName(Constants.PACKATE_OF_MANAGER_SERVICE+reqSource+"."+reqService);
            Constructor<?> constructor = cls.getConstructor();
            Object node = constructor.newInstance();
            Method method = cls.getMethod("excutePost", IData.class, IData.class);
            Object object = method.invoke(node, reqIData,resIData);
            resIData = (IData)resIData;
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

    /**
     * 로그인 필요없는 whybrid 서비스
     * @param jSONObject
     * @return
     */
    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping(value = "/wem/whybrid")
    @ResponseBody
    public String requestWhybrid(@RequestBody JSONObject jSONObject) {
        logger.debug("[ApiController][requestWhybrid]");

        Parse parse = new Parse();
        IData resIData = new NodeData();
        IData reqIData = new NodeData();
        String reqSource="",reqService="";
        Long countryId=null;

        try {
            reqIData.setObject(jSONObject);
            resIData = parse.copyHeader(reqIData);
            reqSource = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE);
            reqService = (String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE);
            countryId = (Long) reqIData.getHeaderValue(Constants.TAG_HEADER_COUNTRY_ID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); // he
            IData body = new NodeData();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_HEADER_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(LanguagePack.getInstance().get(countryId,Constants.ERROR_HEADER)));
            resIData.put(Constants.TAG_BODY,body);
            return resIData.toString();
        }

        try {
            // header 값에 따라 분기...
            Class<?> cls = Class.forName(Constants.PACKATE_OF_MANAGER_SERVICE+reqSource+"."+reqService);
            Constructor<?> constructor = cls.getConstructor();
            Object node = constructor.newInstance();
            Method method = cls.getMethod("excutePost", IData.class, IData.class);
            Object object = method.invoke(node, reqIData,resIData);
            resIData = (IData)resIData;
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

    @CrossOrigin(origins="*", allowedHeaders="*")
    @RequestMapping(value="/SimpleTest", method = RequestMethod.GET)
    @ResponseBody
    public String simpleTest(@RequestParam HashMap<String, Object> params) throws Exception {

        Class<?> cls = Class.forName(Constants.PACKATE_OF_SERVICE+"SimpleTest");
        Constructor<?> constructor = cls.getConstructor();
        Object node = constructor.newInstance();
        Method method = cls.getMethod("excuteGet", HashMap.class);
        Object object = method.invoke(node, params);

        IData resIData = (IData)object;

        return resIData.toString();
    }
}
