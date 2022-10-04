package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import com.inswave.appplatform.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter 
@Setter
public class IntegrityLogDataRule {

    @Field(type = FieldType.Keyword, name = "id")
    private String id;

    @Field(type = FieldType.Keyword, name = "name")
    private String name;

    @Field(type = FieldType.Keyword, name = "description")
    private String description;

    @Field(type = FieldType.Date, name = "startTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date startTime;

    @Field(type = FieldType.Date, name = "endTime", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date endTime;

    @Field(type = FieldType.Keyword, name = "elapsedTime")
    private String elapsedTime;

    @Field(type = FieldType.Integer, name = "result")
    private Integer result;

    @Field(type = FieldType.Boolean, name = "isFollowUp")
    private Boolean isFollowUp=false; /* 수동 조치 여부 */

    @Field(type = FieldType.Date, name = "timeFollowUp", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeFollowUp = DateUtil.getDate("yyyy-MM-dd HH:mm:ss","0000-00-00 00:00:00"); /* 수동 조치 일시 */

    @Field(type = FieldType.Keyword, name = "message")
    private String message;


    /*
[{
	"integrity": {
		"startTime": "2021-09-08T09:10:38.629Z",
		"endTime": "2021-09-08T09:14:46.942Z",
		"elapsedTime": "00:04:08.312",
		"result": 0,
		"commonIntegrity": {
			"isTargetHome": true,
			"isAlways": true,
			"startTime": "2021-09-08T09:10:38.692Z",
			"endTime": "2021-09-08T09:14:46.942Z",
			"elapsedTime": "00:04:08.250",
			"result": 0,
			"rules": [{
				"id": "000001",
				"startTime": "2021-09-08T09:10:38.692Z",
				"endTime": "2021-09-08T09:10:38.707Z",
				"elapsedTime": "00:00:00.015",
				"result": 2,
				"message": "script is empty"
			}, {
				"id": "000002",
				"startTime": "2021-09-08T09:10:38.707Z",
				"endTime": "2021-09-08T09:10:38.707Z",
				"elapsedTime": "00:00:00.000",
				"result": 2,
				"message": "script is empty"
			}, {
				"id": "000003",
				"startTime": "2021-09-08T09:10:38.707Z",
				"endTime": "2021-09-08T09:10:38.707Z",
				"elapsedTime": "00:00:00.000",
				"result": 2,
				"message": "script is empty"
			}, {
				"id": "000004",
				"startTime": "2021-09-08T09:10:38.707Z",
				"endTime": "2021-09-08T09:14:46.942Z",
				"elapsedTime": "00:04:08.234",
				"result": 1,
				"message": ""
			}, {
				"id": "000005",
				"startTime": "2021-09-08T09:14:46.942Z",
				"endTime": "2021-09-08T09:14:46.942Z",
				"elapsedTime": "00:00:00.000",
				"result": 2,
				"message": "script is empty"
			}]
		}
	},
	"source": "WEdgeAgent",
	"service": "IntegrityLog",
	"deviceId": "Windows.00330-80108-12863-AA096.Inswave.Prod",
	"appId": "Windows.Inswave.Prod",
	"osType": "Windows",
	"ip": "192.168.151.143",
	"hostName": "DESKTOP-LRVIN22",
	"timeCreated": "2021-09-08T09:14:46.942Z"
}]
     */

}