package com.inswave.appplatform.service;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.data.IData;
import com.inswave.appplatform.data.NodeData;
import com.inswave.appplatform.data.SimpleData;
import com.inswave.appplatform.legacy.ShinhanbankGoldWing;
import com.inswave.appplatform.service.ExternalService;
import com.inswave.appplatform.util.HttpClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;

public class SimpleTest implements ExternalService {

    @Override
    public IData excuteGet(HashMap<String, Object> params) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData("notfound method"));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData("check your method parameter"));

        String method = (String) params.get("method");
        if(method.equals("mailGoldwing"))
            return mailGoldwing(params);
        else if (method.equals("messageGoldwing"))
            return messageGoldwing(params);
        else if (method.equals("messageGoldwing2"))
            return messageGoldwing2(params);
        else if (method.equals("checkDocument"))
            return checkDocument(params);

        return body;
    }

    public IData mailGoldwing(HashMap<String, Object> params) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String email = (String) params.get("email");

        try {
            String charSet = "UTF-8";
            ShinhanbankGoldWing shinhanbankGoldWing = new ShinhanbankGoldWing();
            shinhanbankGoldWing.sendMailExchangeServer(email, new String((charSet+"제목").getBytes(),charSet), new String("내용".getBytes(),charSet));
        } catch(UnsupportedEncodingException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("UnsupportedEncodingException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(MessagingException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("MessagingException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        return body;
    }

    public IData messageGoldwing(HashMap<String, Object> params) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        String uniqueId = (String) params.get("uniqueId");
        String content = (String) params.get("content");
        String senderId = (String) params.get("senderId");
        String senderName = (String) params.get("senderName");

        HttpClient httpClient = new HttpClient();

        String rcvData = "";
        try {
            String charSet = "EUC-KR";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#",uniqueId);
            urlData = urlData.replace("#content#",charSet+" 1 한글 a");
            urlData = urlData.replace("#senderId#",senderId);
            urlData = urlData.replace("#senderName#",senderName);

            urlData = new String(urlData.getBytes(),charSet);
            rcvData = httpClient.excuteGet(Config.getInstance().getUrl()+urlData, charSet);
            body.put("rcvData", new SimpleData(rcvData));
        } catch(IOException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("IOException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "UTF-8";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#",uniqueId);
            urlData = urlData.replace("#content#",charSet+" 2 한글 a");
            urlData = urlData.replace("#senderId#",senderId);
            urlData = urlData.replace("#senderName#",senderName);

            urlData = new String(urlData.getBytes(),charSet);
            rcvData = httpClient.excuteGet(Config.getInstance().getUrl()+urlData, charSet);
            body.put("rcvData", new SimpleData(rcvData));
        } catch(IOException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("IOException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "MS949";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#",uniqueId);
            urlData = urlData.replace("#content#",charSet+" 3 한글 a");
            urlData = urlData.replace("#senderId#",senderId);
            urlData = urlData.replace("#senderName#",senderName);

            urlData = new String(urlData.getBytes(),charSet);
            rcvData = httpClient.excuteGet(Config.getInstance().getUrl()+urlData, charSet);
            body.put("rcvData", new SimpleData(rcvData));
        } catch(IOException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("IOException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "MS949";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#",uniqueId);
            urlData = urlData.replace("#content#",charSet+" 4 한글 a");
            urlData = urlData.replace("#senderId#",senderId);
            urlData = urlData.replace("#senderName#",senderName);

            urlData = new String(urlData.getBytes(),charSet);
            rcvData = httpClient.excuteGet(Config.getInstance().getUrl()+urlData,charSet);
            body.put("rcvData", new SimpleData(rcvData));
        } catch(IOException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("IOException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#",uniqueId);
            urlData = urlData.replace("#content#",charSet+" 5 한글 a");
            urlData = urlData.replace("#senderId#",senderId);
            urlData = urlData.replace("#senderName#",senderName);

            rcvData = httpClient.excuteGet(Config.getInstance().getUrl()+urlData,charSet);
            body.put("rcvData", new SimpleData(rcvData));
        } catch(IOException e) {
            body.put(Constants.TAG_ERROR, new SimpleData("IOException"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        } catch(Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        return body;
    }


    public static IData messageGoldwing2(HashMap<String, Object> params) {

        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));

        String uniqueId = (String) params.get("uniqueId");
        String content = (String) params.get("content");
        String senderId = (String) params.get("senderId");
        String senderName = (String) params.get("senderName");

        try {
            String charSet = "euc-kr";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#", uniqueId);
            urlData = urlData.replace("#content#", charSet+" 11 가 b");
            urlData = urlData.replace("#senderId#", senderId);
            urlData = urlData.replace("#senderName#", senderName);

            String url = Config.getInstance().getUrlData() + URLEncoder.encode(urlData, charSet);
            RestTemplate restOperations = new RestTemplate();

            DefaultUriTemplateHandler uriTemplateHandler = (DefaultUriTemplateHandler) restOperations.getUriTemplateHandler();
            uriTemplateHandler.setStrictEncoding(true);
            restOperations.getForObject(url, String.class);

        } catch (Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "utf-8";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#", uniqueId);
            urlData = urlData.replace("#content#", charSet+" 12 가 b");
            urlData = urlData.replace("#senderId#", senderId);
            urlData = urlData.replace("#senderName#", senderName);

            String url = Config.getInstance().getUrlData() + URLEncoder.encode(urlData, charSet);
            RestTemplate restOperations = new RestTemplate();

            DefaultUriTemplateHandler uriTemplateHandler = (DefaultUriTemplateHandler) restOperations.getUriTemplateHandler();
            uriTemplateHandler.setStrictEncoding(true);
            restOperations.getForObject(url, String.class);

        } catch (Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        try {
            String charSet = "MS949";
            String urlData = Config.getInstance().getUrlData();
            urlData = urlData.replace("#uniqueId#", uniqueId);
            urlData = urlData.replace("#content#", charSet+" 13 가 b");
            urlData = urlData.replace("#senderId#", senderId);
            urlData = urlData.replace("#senderName#", senderName);

            String url = Config.getInstance().getUrlData() + URLEncoder.encode(urlData, charSet);
            RestTemplate restOperations = new RestTemplate();

            DefaultUriTemplateHandler uriTemplateHandler = (DefaultUriTemplateHandler) restOperations.getUriTemplateHandler();
            uriTemplateHandler.setStrictEncoding(true);
            restOperations.getForObject(url, String.class);

        } catch (Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }

        return body;
    }

    public static IData checkDocument(HashMap<String, Object> params) {
        IData body = new NodeData();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        body.put(Constants.TAG_ERROR, new SimpleData(""));
        body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(""));
        try{
            try {
                Class<?> cls = Class.forName("com.inswave.appplatform.log.service.batch.DocumentCheck");
                Constructor<?> constructor = cls.getConstructor();
                Object node = constructor.newInstance();
                Method method = cls.getMethod(Constants.METHOD_OF_INTERNAL_EXCUTE_SERVICE);
                Object object = method.invoke(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            body.put(Constants.TAG_ERROR, new SimpleData("Exception"));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData) {
        return null;
    }

    @Override
    public IData excutePost(IData reqIData, IData resIData, Object object) {
        return null;
    }
}
