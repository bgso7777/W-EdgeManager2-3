package com.inswave.appplatform.transaver.elasticsearch.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.repository.query.Param;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "#{T(com.inswave.appplatform.transaver.elasticsearch.domain.DynamicIndexBean).getIndexName()}", shards=2, replicas=1, refreshInterval="-1", createIndex=true)

@Getter 
@Setter
public class IntegrityLogOriginal extends Document {

    private String indexName;
    public void setIndexName(@Param("indexName") String indexName) {
        if (indexName.equals("")) {
            indexName = this.getClass().getSimpleName().toLowerCase();
            this.indexName = this.getClass().getSimpleName().toLowerCase();
        } else {
            this.indexName = indexName;
        }
        super.setIndexName(indexName);
    }
    public String getIndexName() {
        return this.indexName;
    }

    @Field(type = FieldType.Integer, name = "result")
    private Integer result;

    @Field(type = FieldType.Nested, name = "integrity")
    private IntegrityLogData integrity;


    /*
[{"integrity":{
    "startTime":"2021-09-08T21:17:46.532Z",
    "endTime":"2021-09-08T21:17:46.563Z",
    "elapsedTime":"00:00:00.031",
    "result":0,

    info : { "netclient5": { "UID": {x86:"" x64: "" }       ??????????????????

    "integrityData":[{

        pcType : 재책 vpc 업무 pc
        remotecode : 원격코드
        defauleReg : ?

        "name":"commonIntegrity",
        "isTargetHome":true,
        "isAlways":true,
        "startTime":"2021-09-08T21:17:46.548Z",
        "endTime":"2021-09-08T21:17:46.563Z",
        "elapsedTime":"00:00:00.015",
        "result":0,
        "rules":[{
                "id":"000001",
                "startTime":"2021-09-08T21:17:46.548Z",
                "endTime":"2021-09-08T21:17:46.548Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000002",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000003",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000004",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,"message":"script is empty"
             }
        ]}]},"source":"WEdgeAgent","service":"IntegrityLog","deviceId":"Windows.00330-80000-00000-AA476.Inswave.Prod","appId":"Windows.Inswave.Prod","osType":"Windows","ip":"192.168.1.7","hostName":"DESKTOP-ADUENBC","timeCreated":"2021-09-08T21:17:46.563Z"}]




[
        "source":"WEdgeAgent",
        "service":"IntegrityLog",
        "deviceId":"Windows.00330-80000-00000-AA476.Inswave.Prod",
        "appId":"Windows.Inswave.Prod","osType":"Windows","ip":"192.168.1.7",
        "hostName":"DESKTOP-ADUENBC",
        "timeCreated":"2021-09-08T21:17:46.563Z",
        "startTime":"2021-09-08T21:17:46.532Z",
        "endTime":"2021-09-08T21:17:46.563Z",
        "elapsedTime":"00:00:00.031",
        "result":0,
         "name" : "SHB_OP"
        "isTargetHome":true,
        "isAlways":true,
        "startTime":"2021-09-08T21:17:46.548Z",
        "endTime":"2021-09-08T21:17:46.563Z",
        "elapsedTime":"00:00:00.015",
        "result_sub":0,
        "rules":[{
                "id":"000001",
                "startTime":"2021-09-08T21:17:46.548Z",
                "endTime":"2021-09-08T21:17:46.548Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000002",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000003",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000004",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,"message":"script is empty"
             }
          } ,  {


         "source":"WEdgeAgent",
        "service":"IntegrityLog",
        "deviceId":"Windows.00330-80000-00000-AA476.Inswave.Prod",
        "appId":"Windows.Inswave.Prod","osType":"Windows","ip":"192.168.1.7",
        "hostName":"DESKTOP-ADUENBC",
        "timeCreated":"2021-09-08T21:17:46.563Z",
        "startTime":"2021-09-08T21:17:46.532Z",
        "endTime":"2021-09-08T21:17:46.563Z",

        "userdefine" : {
        "isTargetHome":true,
        "isAlways":true,
        "startTime":"2021-09-08T21:17:46.548Z",
        "endTime":"2021-09-08T21:17:46.563Z",
        "elapsedTime":"00:00:00.015",
        "result":0,
        "rules":[{
                "id":"000001",
                "startTime":"2021-09-08T21:17:46.548Z",
                "endTime":"2021-09-08T21:17:46.548Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000002",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000003",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,
                "message":"script is empty"
            },{
                "id":"000004",
                "startTime":"2021-09-08T21:17:46.563Z",
                "endTime":"2021-09-08T21:17:46.563Z",
                "elapsedTime":"00:00:00.000",
                "result":2,"message":"script is empty"
             }
          }

        }]},"source":"WEdgeAgent","service":"IntegrityLog","deviceId":"Windows.00330-80000-00000-AA476.Inswave.Prod","appId":"Windows.Inswave.Prod","osType":"Windows","ip":"192.168.1.7","hostName":"DESKTOP-ADUENBC","timeCreated":"2021-09-08T21:17:46.563Z"}]

     */
}