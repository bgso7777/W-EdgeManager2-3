package com.inswave.appplatform.log.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.log.monitor.JavaMelodyMonitor;
import com.inswave.appplatform.service.InternalService;
import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.transaver.elasticsearch.ElasticsearchHighLevelClient;
import com.inswave.appplatform.transaver.util.BeanUtils;
import com.inswave.appplatform.util.FileUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@Service
public class ElasticsearchTemplateCreator implements InternalService {

    @Override
    public Object excute() {
        Integer result = Constants.RESULT_SUCESS;
        try{
            createAllTemplate();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            result = Constants.RESULT_FAIL;
        } catch (IOException e) {
            e.printStackTrace();
            result = Constants.RESULT_FAIL;
        } catch (Exception e) {
            e.printStackTrace();
            result = Constants.RESULT_FAIL;
        }
        return result;
    }

    /**
     * resource 내의 모든 파일을 읽어 template를 디폴트로 생성한다.
     * @return
     */
    private void createAllTemplate() throws URISyntaxException, IOException {
        URL url = ElasticsearchTemplateCreator.class.getResource(ConstantsTranSaver.PATH_TEMPLATE_FILE);
        File dir = new File(url.toURI());
        for(File file : dir.listFiles()) {
            if(file.getName().indexOf(ConstantsTranSaver.FILE_TEMPLATE)!=-1) {
                String templateName = ((file.getName().substring(0,file.getName().indexOf(ConstantsTranSaver.FILE_TEMPLATE))).toLowerCase())+"template";
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"templateName-->>"+templateName);
                String index_patterns = ((file.getName().substring(0,file.getName().indexOf(ConstantsTranSaver.FILE_TEMPLATE))).toLowerCase())+"_*";
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"index_patterns-->>"+index_patterns);
                StringBuffer source = FileUtil.getJarResource(this.getClass(),ConstantsTranSaver.PATH_TEMPLATE_FILE+"/"+file.getName());
                JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"source-->>"+source);
                if(source==null || source.toString().equals("") ) {
                    JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"not found source");
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> jsonMap = objectMapper.readValue(source.toString(), Map.class);
                    ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
                    if (elasticsearchHighLevelClient != null) {
                        elasticsearchHighLevelClient.createTemplate(templateName, index_patterns, jsonMap, 0, 0);
                        JavaMelodyMonitor.printInfo(this.getClass().getSimpleName(),"sucess");
                    }
                }
            }
        }
    }

    /**
     * jsp에서 파라미터로 받아서 생성
     * @param fileName
     * @param templateName
     * @param index_patterns
     * @param shards
     * @param replicas
     * @return
     */
    public Object createTemplate(String fileName, String templateName, String index_patterns, int shards, int replicas) {
        String result = "sucess";
        try{
            StringBuffer source = FileUtil.getJarResource(this.getClass(),ConstantsTranSaver.PATH_TEMPLATE_FILE+"/"+fileName+".json");
            if(source==null || source.toString().equals("") ) {
                result = "not found source";
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(source.toString(), Map.class);
                ElasticsearchHighLevelClient elasticsearchHighLevelClient = BeanUtils.getBean(ElasticsearchHighLevelClient.class);
                if (elasticsearchHighLevelClient != null) {
                    elasticsearchHighLevelClient.createTemplate(templateName, index_patterns, jsonMap, shards, replicas);
                    result = "sucess";
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = "fail";
        }
        return result;
    }

    @Override
    public Object sendLog() {
        return null;
    }

}
