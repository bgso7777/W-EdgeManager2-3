package com.inswave.appplatform.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.entity.SystemInfo;
import com.inswave.appplatform.util.SystemUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Parse {

    private IData reqIData;
    private IData resIData;

    public Parse() {
    }

    public Parse(IData reqIData, IData resIData) {
        this.reqIData = reqIData;
        this.resIData = resIData;
    }

    public IData copyHeader(IData reqIData) {
        IData iData = new NodeData();
        IData header = new NodeData();
        header.put(Constants.TAG_HEADER_MSGID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_MSGID)));
        header.put(Constants.TAG_HEADER_SOURCE, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SOURCE)));
        header.put(Constants.TAG_HEADER_SERVICE, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SERVICE)));
        header.put(Constants.TAG_HEADER_DESTINATION, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_DESTINATION)));
        header.put(Constants.TAG_HEADER_DAO_OPERATION, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_DAO_OPERATION)));
        header.put(Constants.TAG_HEADER_DEVICEID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_DEVICEID)));
        header.put(Constants.TAG_HEADER_APPID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_APPID)));
        header.put(Constants.TAG_HEADER_OSTYPE, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_OSTYPE)));
        header.put(Constants.TAG_HEADER_SITE_ID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SITE_ID)));
        header.put(Constants.TAG_HEADER_INSTALL_ID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_INSTALL_ID)));
        header.put(Constants.TAG_HEADER_SITE_NAME, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SITE_NAME)));

        header.put(Constants.TAG_HEADER_USER_ID, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_USER_ID)));
        header.put(Constants.TAG_HEADER_TERM_NO, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_TERM_NO)));
        header.put(Constants.TAG_HEADER_SSO_BR_NO, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SSO_BR_NO)));
        header.put(Constants.TAG_HEADER_BR_NO, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_BR_NO)));
        header.put(Constants.TAG_HEADER_DEPT_NAME, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_DEPT_NAME)));
        header.put(Constants.TAG_HEADER_HWN_NO, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_HWN_NO)));
        header.put(Constants.TAG_HEADER_USER_NAME, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_USER_NAME)));
        header.put(Constants.TAG_HEADER_SSO_TYPE, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_SSO_TYPE)));
        header.put(Constants.TAG_HEADER_PC_NAME, new SimpleData((String) reqIData.getHeaderValue(Constants.TAG_HEADER_PC_NAME)));

        iData.put(Constants.TAG_HEADER, header);
        return iData;
    }

    public IData createHeader(String msgId, String source, String service, String destination, String daoOperation, String deviceId, String appId, String osType, Long siteId, Long installId, String siteName,
                              String userId, String termNo, String ssoBrNo, String brNo, String deptName, String hwnNo, String userName, String ssoType, String pcName ) {
        IData iData = new NodeData();
        IData header = new NodeData();
        header.put(Constants.TAG_HEADER_MSGID, new SimpleData(msgId));
        header.put(Constants.TAG_HEADER_SOURCE, new SimpleData(source));
        header.put(Constants.TAG_HEADER_SERVICE, new SimpleData(service));
        header.put(Constants.TAG_HEADER_DESTINATION, new SimpleData(destination));
        header.put(Constants.TAG_HEADER_DAO_OPERATION, new SimpleData(daoOperation));
        header.put(Constants.TAG_HEADER_DEVICEID, new SimpleData(deviceId));
        header.put(Constants.TAG_HEADER_APPID, new SimpleData(appId));
        header.put(Constants.TAG_HEADER_OSTYPE, new SimpleData(osType));
        header.put(Constants.TAG_HEADER_SITE_ID, new SimpleData(siteId));
        header.put(Constants.TAG_HEADER_INSTALL_ID, new SimpleData(installId));
        header.put(Constants.TAG_HEADER_SITE_NAME, new SimpleData(siteName));

        header.put(Constants.TAG_HEADER_USER_ID, new SimpleData(userId));
        header.put(Constants.TAG_HEADER_TERM_NO, new SimpleData(termNo));
        header.put(Constants.TAG_HEADER_SSO_BR_NO, new SimpleData(ssoBrNo));
        header.put(Constants.TAG_HEADER_BR_NO, new SimpleData(brNo));
        header.put(Constants.TAG_HEADER_DEPT_NAME, new SimpleData(deptName));
        header.put(Constants.TAG_HEADER_HWN_NO, new SimpleData(hwnNo));
        header.put(Constants.TAG_HEADER_USER_NAME, new SimpleData(userName));
        header.put(Constants.TAG_HEADER_SSO_TYPE, new SimpleData(ssoType));
        header.put(Constants.TAG_HEADER_PC_NAME, new SimpleData(pcName));

        iData.put(Constants.TAG_HEADER, header);

        return iData;
    }

    public String toString(Object object) {
        String ret = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ret = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String toString(List<Object> arrayList) {
        String ret = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ret = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * JSON 문자열 데이터를 ObjectNode로 변환하여 반환한다.
     *
     * @param jsonData JSON 문자열
     * @return 변환된 ObjectNode
     */
    public static ObjectNode getObjectNode(String jsonData) {
        ObjectMapper om = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = om.readValue(jsonData, JsonNode.class);
            for (Iterator<JsonNode> i = rootNode.iterator(); i.hasNext(); ) {
                JsonNode j = i.next();
                if (j.isObject()) {
                    ObjectNode obj = om.convertValue(j, ObjectNode.class);
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    /*
    StringBuffer to  ?? guaba
     */
    public JSONArray getJSONArray(StringBuffer stringBuffer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jSONArray = mapper.readValue(cleanUpJson(stringBuffer).toString(), JSONArray.class);
        return jSONArray;
    }

    public JSONObject getJSONObject(StringBuffer stringBuffer) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jSONObject = mapper.readValue(cleanUpJson(stringBuffer).toString(), JSONObject.class);
        return jSONObject;
    }

    private StringBuffer cleanUpJson(StringBuffer jsonString) {
        return new StringBuffer(jsonString.toString().trim().replaceFirst("\ufeff", ""));
    }

    public Object parse(Object object) throws JsonProcessingException {
        String jsonStr = toString(object);

        ObjectMapper mapper = new ObjectMapper();
        JSONObject obj = mapper.readValue(jsonStr, JSONObject.class);

        return obj;
    }

    public List<Object> parseList(List<Object> objectList) throws JsonProcessingException {
        String jsonStr = toString(objectList);

        ObjectMapper mapper = new ObjectMapper();
        JSONArray arr = mapper.readValue(jsonStr, JSONArray.class);

        List<Object> arr0 = (List<Object>) arr.get(0);
        return (List<Object>) arr0.get(0);
    }

    public static void main_(String[] argv) {

        IData resIData = new NodeData();

        IData body = new NodeData();

        IData arrayData11 = new ArrayData();

        IData subdata11 = new NodeData();
        subdata11.put("city", new SimpleData("pusan"));
        subdata11.put("street", new SimpleData("nampo-dong"));
        subdata11.put("idnum", new SimpleData(78));
        arrayData11.put(null, subdata11);
        IData subdata12 = new NodeData();
        subdata12.put("city", new SimpleData("pusan3"));
        subdata12.put("street", new SimpleData("nampo-dong3"));
        subdata12.put("idnum", new SimpleData(783));
        arrayData11.put(null, subdata12);

        body.put("infos", arrayData11);

        resIData.put("body", body);

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        resIData.put("body", body);

        System.out.println(resIData.toString());

    }

    public static void main1(String[] argv) throws JsonProcessingException {

        TestInfo tableInfo1 = new TestInfo();
        tableInfo1.id = 1L;
        tableInfo1.name = "name1";
        tableInfo1.description = "description1";

        TestInfo tableInfo2 = new TestInfo();
        tableInfo2.id = 2L;
        tableInfo2.name = "name2";
        tableInfo2.description = "description2";

        //List<Object> tempArrayList = new ArrayList<Object>();
        List<Object> tempArrayList = null;
        tempArrayList.add(tableInfo1);
        tempArrayList.add(tableInfo2);

        Parse parse = new Parse();
        String info = parse.toString(tempArrayList);
        //System.out.println(info);

        IData resData = new NodeData();
        IData body = new NodeData();

        ObjectMapper mapper = new ObjectMapper();
        JSONArray jSONArray = mapper.readValue(info, JSONArray.class);
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("tableInfos", jSONArray);

        body.setObject(jsonObj1);
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        resData.put("body", body);

        System.out.println("=======================================================");
        System.out.println(resData.toString());
        System.out.println("=======================================================");

    }

    public static void main_3(String[] argv) throws JsonProcessingException {

        SystemInfo systemInfo = SystemUtil.getSystemInfo();

        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("c:/home/inswave/temp/SystemInfo.json");

        //String jSONArray = objectMapper.writeValueAsString(file, SystemInfo.class);
        String jsonString = objectMapper.writeValueAsString(systemInfo);
        System.out.println();
        //JSONObject jsonObj1 = new JSONObject();
        //jsonObj1.put("tableInfos",jSONArray);

    }

};

class TestInfo {
    public Long   id;
    public String name;
    public String description;
};
