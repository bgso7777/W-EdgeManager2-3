package com.inswave.appplatform.transaver.elasticsearch.domain;

import com.inswave.appplatform.transaver.ConstantsTranSaver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ClientPerformanceResourceLogData {

    /*{
        ""cpuUsage"": 10.537,
        ""threadCount"": 3679.0,
        ""handleCount"": 123434.0,
        ""memoryTotal"": 16505688.0,
        ""memoryUsed"": 9236988.0,
        ""memoryUsage"": 55.962,
        ""poolPaged"": 675512.0,
        ""poolNonpaged"": 462932.0,
        ""diskRead"": 10.436,
        ""diskWrite"": 1413.732,
        ""diskTotal"": 998954196.0,
        ""diskUsed"": 1424.128,
        ""diskUsage"": 0.0,
        ""networkSent"": 2.558,
        ""networkReceived"": 4.451,
        ""cpuTemperature"": 31.85,
        ""memoryVirtualTotal"": 20699992.0,
        "diskInfo": [{
            "instanceName": "0 C: E: D:",
            "isSystemDrive": true,
            "diskReadIndividual": 0.0,
            "diskWriteIndividual": 0.0,
            "diskTimeIndividual": 0.0
        }]
      }"
     */
    @Field(type = FieldType.Double, name = "cpuUsage")
    public Double cpuUsage;//CPU 사용률 (%)

    @Field(type = FieldType.Long, name = "threadCount")
    public Long threadCount;//thread 갯수

    @Field(type = FieldType.Long, name = "handleCount")
    public Long handleCount;//핸들 갯수

    @Field(type = FieldType.Long, name = "memoryUsed")
    public Long memoryUsed;//메모리 사용량

    @Field(type = FieldType.Long, name = "memoryTotal")
    public Long memoryTotal;//메모리 총 사용량

    @Field(type = FieldType.Long, name = "memoryUsage")
    public Long memoryUsage;//메모리 사용률 (%)  ( Used / Total ) * 100

    @Field(type = FieldType.Long, name = "poolPaged")
    public Long poolPaged;//메모리 페이징 풀

    @Field(type = FieldType.Long, name = "poolNonpaged")
    public Long poolNonpaged;//메모리 비페이징 풀

    @Field(type = FieldType.Long, name = "diskUsage")
    public Long diskUsage;//Disk 사용률 (%) ( Used / Total ) * 100

    @Field(type = FieldType.Long, name = "diskTotal")
    public Long diskTotal;//Disk 전체 크기

    @Field(type = FieldType.Double, name = "diskUsed")
    public Double diskUsed;//Disk 사용 크기 ( diskRead + diskWrite )

    @Field(type = FieldType.Double, name = "diskTime")
    public Double diskTime;

    @Field(type = FieldType.Double, name = "diskRead")
    public Double diskRead;

    @Field(type = FieldType.Double, name = "diskWrite")
    public Double diskWrite;

    @Field(type = FieldType.Double, name = "networkSent")
    public Double networkSent;//BytesSentPerSec

    @Field(type = FieldType.Double, name = "networkReceived")
    public Double networkReceived;//BytesReceivedPerSec

    @Field(type = FieldType.Double, name = "cpuTemperature")
    public Double cpuTemperature;//CPU 온도

    @Field(type = FieldType.Long, name = "networkUsed")
    public Long networkUsed;//BytesReceivedPerSec

    @Field(type = FieldType.Long, name = "memoryVirtualTotal")
    public Long memoryVirtualTotal;//가상메모리 크기

    @Field(type = FieldType.Nested, name = "diskInfo")
    private List<ClientPerformanceResourceLogDataDiskInfo> diskInfo;

    @Field(type = FieldType.Date, name = "timeCreated", format = DateFormat.custom, pattern = ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z)
    private Date timeCreated;
}