package com.inswave.appplatform.deployer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.dao.*;
import com.inswave.appplatform.data.*;
import com.inswave.appplatform.deployer.DeployManager;
import com.inswave.appplatform.deployer.MulticastSender;
import com.inswave.appplatform.deployer.cache.DeploySegmentCache;
import com.inswave.appplatform.deployer.dao.DeployDao;
import com.inswave.appplatform.deployer.dao.DeployTransferDao;
import com.inswave.appplatform.deployer.dao.DeployTransferHistoryDao;
import com.inswave.appplatform.deployer.dao.DeployTransferReceiveStatusDao;
import com.inswave.appplatform.deployer.data.DeployStatusVO;
import com.inswave.appplatform.deployer.data.DeployTransferHistoryVO;
import com.inswave.appplatform.deployer.data.DeployTransferVO;
import com.inswave.appplatform.deployer.data.DeployVO;
import com.inswave.appplatform.deployer.domain.Deploy;
import com.inswave.appplatform.deployer.domain.DeployTransfer;
import com.inswave.appplatform.deployer.data.DeployTransferReceiveStatusVO;
import com.inswave.appplatform.deployer.domain.DeployTransferHistory;
import com.inswave.appplatform.deployer.domain.DeployTransferReceiveStatus;
import com.inswave.appplatform.service.ExternalService;
import com.inswave.appplatform.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class DeployService implements ExternalService {

    private DeployDao                        deployDao;
    private DeployTransferDao                deployTransferDao;
    private DeployTransferReceiveStatusDao   deployTransferReceiveStatusDao;
    private DeployTransferHistoryDao         deployTransferHistoryDao;
    private DeployManager                    deployManager;
    private DeploySegmentCache               deploySegmentCache;
    private Map<String, Map<String, Object>> reportStatusMap  = new ConcurrentHashMap<>();
    private Map<String, Object>              clientProperties = new HashMap<>();

    public DeployService(DeployDao deployDao,
                         DeployTransferDao deployTransferDao,
                         DeployTransferReceiveStatusDao deployTransferReceiveStatusDao,
                         DeployTransferHistoryDao deployTransferHistoryDao,
                         DeployManager deployManager,
                         DeploySegmentCache deploySegmentCache,
                         @Value("#{${wdeployer.multicast.client-properites}}") Map<String, Object> clientProperties) {
        this.deployDao = deployDao;
        this.deployTransferDao = deployTransferDao;
        this.deployTransferReceiveStatusDao = deployTransferReceiveStatusDao;
        this.deployTransferHistoryDao = deployTransferHistoryDao;
        this.deployManager = deployManager;
        this.deploySegmentCache = deploySegmentCache;
        this.clientProperties = clientProperties;
    }

    @Override public IData excuteGet(HashMap<String, Object> params) {
        return null;
    }

    @Override public IData excutePost(IData reqIData, IData resIData) {
        return null;
    }

    @Override public IData excutePost(IData reqIData, IData resIData, Object object) {
        return null;
    }

    @Scheduled(fixedDelay = 5000)
    public void receiveStatusUpdate() {
        reportStatusMap.keySet().forEach(deployTransferReceiveStatusPK -> {
            StopWatch stopWatch = new StopWatch("receiveStatusUpdate");
            stopWatch.start();

            Map deployTransferReceiveStatusMap = reportStatusMap.put(deployTransferReceiveStatusPK, new ConcurrentHashMap<>());

            if (deployTransferReceiveStatusMap.size() > 0) {
                log.info("st receiveStatusUpdate ({}) :  sttime = {}, size(req) = {}", deployTransferReceiveStatusPK, ZonedDateTime.now().toEpochSecond(), deployTransferReceiveStatusMap.size());
                Optional<DeployTransferReceiveStatus> optionalDeployTransferReceiveStatus = deployTransferReceiveStatusDao.findById(Long.valueOf(deployTransferReceiveStatusPK.split("-")[1]));
                optionalDeployTransferReceiveStatus.ifPresent(deployTransferReceiveStatus -> {
                    List<DeployTransferReceiveStatusVO> deployTransferReceiveStatusVOS = new ArrayList<>();
                    if (!StringUtils.isEmpty(deployTransferReceiveStatus.getReceiveStatus())) {
                        deployTransferReceiveStatusVOS = JsonUtil.toList(deployTransferReceiveStatus.getReceiveStatus(), DeployTransferReceiveStatusVO.class);
                    }

                    Map deployTransferReceiveStatusVOMap = deployTransferReceiveStatusVOS.stream()
                                                                                         .collect(Collectors.toMap(i1 -> String.format("%s-%s", i1.getDeviceid(), i1.getAppid()),
                                                                                                                   i2 -> i2));
                    deployTransferReceiveStatusMap.keySet().forEach(key -> {
                        deployTransferReceiveStatusVOMap.put(key, deployTransferReceiveStatusMap.get(key));
                    });
                    List<Map> list = (List<Map>) deployTransferReceiveStatusVOMap.keySet()
                                                                                 .stream()
                                                                                 .map(key -> deployTransferReceiveStatusVOMap.get(key))
                                                                                 .collect(Collectors.toCollection(ArrayList::new));
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String reportStatusValue = objectMapper.writeValueAsString(list);
                        deployTransferReceiveStatus.setReceiveStatus(reportStatusValue);
                        deployTransferReceiveStatusDao.save(deployTransferReceiveStatus);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    stopWatch.stop();
                    log.info("ed receiveStatusUpdate ({}) :  edtime = {}, TotalTimeSeconds = {}, size(save) = {}", deployTransferReceiveStatusPK, ZonedDateTime.now().toEpochSecond(), stopWatch.getTotalTimeSeconds(), list.size());
                });
            }
        });
    }

    public IData infoMassDeploy(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        clientProperties.entrySet().forEach(entry -> {
            body.put(entry.getKey(), new SimpleData(entry.getValue()));
        });
        return resIData;
    }

    //client API
    public IData testApi(IData reqIData, IData resIData) throws IOException {

        String bitString = reqIData.getBodyValueString("bitString");
        BitSet b = new BitSet();
        for (int i = 0; i < bitString.length(); i++) {
            b.set(i, bitString.substring(i, i + 1).equals("0") ? false : true);
        }

        final StringBuilder buffer = new StringBuilder(bitString.length());
        IntStream.range(0, bitString.length()).mapToObj(i -> b.get(i) ? "1" : "0").forEach(buffer::append);
        buffer.toString();

        log.info("toString : {}", buffer.toString());
        log.info("toString.length : {}", buffer.toString().length());

        log.info("toByteArray : {}", b.toByteArray());
        log.info("toByteArray.length : {}", b.toByteArray().length);

        byte[] byteArray = b.toByteArray();
        String base64encoded = Crypto.base64Encode(byteArray);
        log.info("base64Encode : {}", base64encoded);
        log.info("base64Encode.length : {}", base64encoded.length());

        final BitSet bo = ByteUtil.base64ToBitSet(base64encoded);
        final String strO = ByteUtil.base64ToBinaryString(base64encoded);
        log.info("toStringO : {}", strO);
        log.info("toString.lengthO : {}", strO.length());

        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);
        resIData.put("rtn", new SimpleData(new ObjectMapper().writeValueAsString(new HashMap<String, String>() {
            {
                put("base64", base64encoded);
                put("str", strO);
            }
        })));
        return resIData;

    }

    public IData reportStatus(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String deviceId = reqIData.getHeaderValueString(Constants.TAG_HEADER_DEVICEID);
        String appId = reqIData.getHeaderValueString(Constants.TAG_HEADER_APPID);
        String ip = reqIData.getHeaderValueString(Constants.TAG_IP);

        int signal = reqIData.getBodyValueInteger("signal");
        Long deployId = reqIData.getBodyValueLong("deployId");
        Long deployTransferId = reqIData.getBodyValueLong("deployTransferId");

        DeployTransferReceiveStatusVO deployTransferReceiveStatusVO = DeployTransferReceiveStatusVO.builder()
                                                                                                   .deviceid(deviceId)
                                                                                                   .appid(appId)
                                                                                                   .ip(ip)
                                                                                                   .receiveStatus(reqIData.getBodyValueString("receiveStatus"))
                                                                                                   .build();
        String deployTransferReceiveStatusPK = String.format("%d-%d", deployId, deployTransferId);
        String deployTransferReceiveStatusDevicePK = String.format("%s-%s", deployTransferReceiveStatusVO.getDeviceid(), deployTransferReceiveStatusVO.getAppid());

        if (reportStatusMap.containsKey(deployTransferReceiveStatusPK)) {
            Map deployTransferReceiveStatus = reportStatusMap.get(deployTransferReceiveStatusPK);
            deployTransferReceiveStatus.put(deployTransferReceiveStatusDevicePK, deployTransferReceiveStatusVO);
        } else {
            Map deployTransferReceiveStatusNew = new ConcurrentHashMap();
            deployTransferReceiveStatusNew.put(deployTransferReceiveStatusDevicePK, deployTransferReceiveStatusVO);
            reportStatusMap.put(deployTransferReceiveStatusPK, deployTransferReceiveStatusNew);
        }

        try {
            String bs = ByteUtil.base64ToBinaryString(deployTransferReceiveStatusVO.getReceiveStatus());
            BitSet b = ByteUtil.base64ToBitSet(deployTransferReceiveStatusVO.getReceiveStatus());

            Path targetFile = Paths.get(Config.getInstance().getDeployerRepoPath(), deployId.toString());
            int chunkCount = (int) Math.ceil((double) Files.size(targetFile) / (double) Constants.TAG_DEPLOYER_CHUNK_BYTE_SIZE);
            int lastIndex = signal == 2 ? chunkCount : bs.lastIndexOf("1") + 1;
            boolean hasMissedSegments = bs.substring(0, lastIndex).contains("0");
            int receivedSegmentsCount = StringUtils.countMatches(bs.substring(0, lastIndex), "1");
            int missedSegmentsCount = StringUtils.countMatches(bs.substring(0, lastIndex), "0");

            log.info("deployTransferId: {}, deviceId:{}, appId:{}, ip:{}, received/missed/total:{}/{}/{}", deployTransferId, deviceId, appId, ip, receivedSegmentsCount, missedSegmentsCount, chunkCount);

            if (hasMissedSegments) {
                List<Object> arrayData = new ArrayList<>();

                for (int i = 0, iL = lastIndex; i < iL; i++) {
                    if (!b.get(i)) {
                        Map<String, Object> posData = new HashMap<>();
                        posData.put("pos", i);
                        posData.put("base64", deploySegmentCache.get(deployId, i));
                        arrayData.add(posData);
                    }
                    if (arrayData.size() == Constants.TAG_DEPLOYER_MISSED_SEGMENT_FILL_LIMIT) {
                        break;  // 누락패킷 회신 수 제한
                    }
                }
                String jsonStr = new Parse().toString(arrayData);
                ObjectMapper mapper = new ObjectMapper();
                JSONArray arr = mapper.readValue(jsonStr, JSONArray.class);

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("fillData", arr);
                body.setObject(jsonObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        return resIData;
    }

    public IData getChunks(IData reqIData, IData resIData) {
        return resIData;
    }

    public IData getFile(IData reqIData, IData resIData) {
        return resIData;
    }

    public IData delFile(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String fileName = (String) reqIData.getBodyValue("fileName");
        String filePath = (String) reqIData.getBodyValue("filePath");
        try {
            Files.delete(Paths.get(filePath, fileName));
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            body.put(Constants.TAG_RESULT_MSG, new SimpleData("File '" + fileName + "' has been deleted."));
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        return resIData;
    }

    //admin API
    public IData getStatus(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        Map<Long, MulticastSender> multicastSenders = deployManager.getMulticastSenders();
        List<DeployStatusVO> deployStatusVOS = new ArrayList<>();

        multicastSenders.forEach((k, v) -> {
            deployStatusVOS.add(DeployStatusVO.builder()
                                              .deployTransferId(v.getDeployTransfer().getDeployTransferId())
                                              .deployId(v.getDeploy().getDeployId())
                                              .isStop(v.isStop())
                                              .repeatCount(v.getRepeatCount())
                                              .repeatPos(v.getRepeatPos())
                                              .chunkCount(v.getChunkCount())
                                              .chunkPos(v.getChunkPos())
                                              .build());
        });
        List<Object> data = Collections.singletonList(Collections.synchronizedList(Collections.singletonList(deployStatusVOS)));

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData getDeployTransferHistory(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        Long deployTransferId = reqIData.getBodyValueLong("deployTransferId");

        List<DeployTransferHistory> deployTransferHistories = deployTransferHistoryDao.findByDeployTransferId(deployTransferId, Sort.by("deployTransferHistoryId").descending());
        List<DeployTransferHistoryVO> deployTransferHistoryVOs = deployTransferHistories.stream()
                                                                                        .map(deployTransferHistory -> {
                                                                                            return DeployTransferHistoryVO.builder()
                                                                                                                          .deployTransferHistoryId(deployTransferHistory.getDeployTransferHistoryId())
                                                                                                                          .deployTransferId(deployTransferHistory.getDeployTransferId())
                                                                                                                          .seq(deployTransferHistory.getSeq())
                                                                                                                          .repeatPos(deployTransferHistory.getRepeatPos())
                                                                                                                          .sentDate(DateUtil.getDate(deployTransferHistory.getSentDate(), "yyyy-MM-dd hh:mm:ss"))
                                                                                                                          .chunkSizeOrg(deployTransferHistory.getChunkSizeOrg())
                                                                                                                          .chunkSizeSent(deployTransferHistory.getChunkSizeSent())
                                                                                                                          .useCompress(deployTransferHistory.getUseCompress())
                                                                                                                          .compressRate((int) Math.ceil(deployTransferHistory.getChunkSizeSent() / deployTransferHistory.getChunkSizeOrg()))
                                                                                                                          .lossCount(deployTransferHistory.getLossCount())
                                                                                                                          .build();
                                                                                        })
                                                                                        .sorted((o1, o2) -> o2.getSeq().compareTo(o1.getSeq()))
                                                                                        .collect(Collectors.toList());
        List<Object> data = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(deployTransferHistoryVOs)));

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData getDeployTransfer(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        Long deployId = reqIData.getBodyValueLong("deployId");

        List<DeployTransfer> deployTransferList = deployTransferDao.findByDeployId(deployId, Sort.by("deployTransferId").descending());
        List<DeployTransferVO> deployTransferVOs = deployTransferList.stream()
                                                                     .map(deployTransfer -> {
                                                                         MulticastSender multicastSender = deployManager.getMulticastSenders().get(deployId);
                                                                         return DeployTransferVO.builder()
                                                                                                .deployTransferId(deployTransfer.getDeployTransferId())
                                                                                                .deployId(deployTransfer.getDeployId())
                                                                                                .bandwidthKbps(deployTransfer.getBandwidthKbps())
                                                                                                .useCompress(deployTransfer.getUseCompress())
                                                                                                .multicastGroupAddress(deployTransfer.getMulticastGroupAddress())
                                                                                                .multicastGroupPort(deployTransfer.getMulticastGroupPort())
                                                                                                .status(deployTransfer.getStatus())
                                                                                                .repeatCount(deployTransfer.getRepeatCount())
                                                                                                .transferCount(deployTransfer.getTransferCount())
                                                                                                .transferDoneCount(multicastSender == null ? -1 : multicastSender.getChunkPos())
                                                                                                .transferRate(multicastSender == null ? -1 : multicastSender.getChunkPos() / multicastSender.getChunkCount())

                                                                                                .reactiveClientCount(deployTransfer.getReactiveClientCount())
                                                                                                .lossCount(-1)  // History에서 취합해야함.
                                                                                                .lossRate(-1)   // History에서 취합해야함.
                                                                                                .receiveDoneCount(-1)
                                                                                                .receiveRate(-1)

                                                                                                .executor(deployTransfer.getCreator())
                                                                                                .executedDate(DateUtil.getDate(deployTransfer.getCreatedDate(), "yyyy-MM-dd hh:mm:ss"))
                                                                                                .finishedDate(DateUtil.getDate(deployTransfer.getFinishedDate(), "yyyy-MM-dd hh:mm:ss"))
                                                                                                .executeInstallDate(DateUtil.getDate(deployTransfer.getExecuteInstallDate(), "yyyy-MM-dd hh:mm:ss"))
                                                                                                .build();
                                                                     })
                                                                     .collect(Collectors.toList());
        List<Object> data = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(deployTransferVOs)));

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData getDeploy(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        List<Deploy> deployList = deployDao.findAll(Sort.by("deployId").descending());
        List<DeployVO> deployVOs = deployList.stream()
                                             .map(deploy -> {
                                                 MulticastSender multicastSender = deployManager.getMulticastSenders().get(deploy.getDeployId());
                                                 return DeployVO.builder()
                                                                .deployId(deploy.getDeployId())
                                                                .deployName(deploy.getDeployName())
                                                                .deployType(deploy.getDeployType())
                                                                .fileName(deploy.getFileName())
                                                                .fileSize(deploy.getFileSize())
                                                                .createdDate(DateUtil.getDate(deploy.getCreatedDate(), "yyyy-MM-dd hh:mm:ss"))
                                                                .status(multicastSender == null ? Constants.TAG_DEPLOYER_STATUS_READY : multicastSender.getStatus())
                                                                .statusChunkCount(multicastSender == null ? -1 : multicastSender.getChunkCount())
                                                                .statusChunkPos(multicastSender == null ? -1 : multicastSender.getChunkPos())
                                                                .build();
                                             })
                                             .collect(Collectors.toList());
        List<Object> data = Collections.singletonList((List<Domain>) (List) Collections.synchronizedList(Collections.singletonList(deployVOs)));

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("data", new Parse().parseList(data));
            body.setObject(jsonObj);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData createDeploy(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        String deployName = (String) reqIData.getBodyValue("deployName");
        Integer deployType = Integer.valueOf((String) reqIData.getBodyValue("deployType"));
        String fileName = (String) reqIData.getBodyValue("fileName");
        String filePath = (String) reqIData.getBodyValue("filePath");
        Long fileSize = reqIData.getBodyValueLong("fileSize");

        Deploy deployNew = deployDao.save(Deploy.builder()
                                                .deployName(deployName)
                                                .deployType(deployType)
                                                .fileSize(fileSize.longValue())
                                                .fileName(fileName)
                                                .createdDate(new Date())    // 임시추가 : @CreatedDate가 동작하지 않는 이유는?
                                                .build());
        try {
            deployManager.moveFile(Paths.get(filePath, fileName), deployNew.getDeployId());
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (IOException e) {
            e.printStackTrace();
            deployDao.delete(deployNew);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        return resIData;
    }

    public IData modifyDeploy(IData reqIData, IData resIData) {
        return resIData;
    }

    public IData deleteDeploy(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        Long deployId = reqIData.getBodyValueLong("deployId");

        List<DeployTransfer> deployTransferList = deployTransferDao.findByDeployId(deployId, null);
        List<Long> deployTransferIds = deployTransferList.stream().map(deployTransfer -> deployTransfer.getDeployTransferId()).collect(Collectors.toList());
        deployTransferHistoryDao.deleteWithDeployIds(deployTransferIds);
        deployTransferDao.deleteAll(deployTransferList);
        deployDao.deleteById(deployId);
        File delTarget = Paths.get(Config.getInstance().getDeployerRepoPath(), deployId.toString()).toFile();
        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        if (Files.exists(delTarget.toPath())) {
            try {
                Files.delete(delTarget.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resIData;
    }

    public IData uploadFile(IData reqIData, IData resIData) {
        return resIData;
    }

    public IData deleteFile(IData reqIData, IData resIData) {
        return resIData;
    }

    //admin Command API
    public IData startDeployTransfer(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        DeployTransfer deployTransferNew = deployTransferDao.save(DeployTransfer.builder()
                                                                                .deployId(reqIData.getBodyValueLong("deployId"))
                                                                                .bandwidthKbps(Integer.parseInt(reqIData.getBodyValueString("bandwidthKbps")))
                                                                                .useCompress(Boolean.valueOf(reqIData.getBodyValueString("useCompress")))
                                                                                .multicastGroupAddress(reqIData.getBodyValueString("multicastGroupAddress"))
                                                                                .multicastGroupPort(Integer.valueOf(reqIData.getBodyValueString("multicastGroupPort")))
                                                                                .repeatCount(Integer.valueOf(reqIData.getBodyValueString("repeatCount")))
                                                                                .executeInstallDate(DateUtil.getDate("yyyyMMddHHmm", reqIData.getBodyValue("executeInstallDate").toString()))
                                                                                .creator(reqIData.getBodyValueString("creator"))
                                                                                .createdDate(new Date())
                                                                                .build());

        try {
            deployManager.deploy(deployDao.findById(reqIData.getBodyValueLong("deployId")).get(), deployTransferNew);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            deployTransferDao.delete(deployTransferNew);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }
        return resIData;
    }

    //    public IData pauseDeployTransfer(IData reqIData, IData resIData) { return resIData; }

    public IData stopDeployTransfer(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        Long deployId = reqIData.getBodyValueLong("deployId");
        Map<Long, MulticastSender> multicastSenders = deployManager.getMulticastSenders();
        multicastSenders.forEach((k, v) -> {
            if (v.getDeploy().getDeployId().equals(deployId)) {
                deployManager.stop(v.getDeployTransfer().getDeployTransferId());
            }
        });

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        return resIData;
    }

    public IData sendData(IData reqIData, IData resIData) {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        int pos = reqIData.getBodyValueInteger("pos");
        Long deployId = reqIData.getBodyValueLong("deployId");
        Long deployTransferId = reqIData.getBodyValueLong("deployTransferId");

        Deploy deploy = deployDao.getOne(deployId);
        DeployTransfer deployTransfer = deployTransferDao.getOne(deployTransferId);
        try {
            MulticastSender multicastSender = new MulticastSender(deployManager, deploy, deployTransfer, deploySegmentCache);
            multicastSender.sendData(pos, false);
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
            return resIData;
        } catch (IOException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            body.put(Constants.TAG_ERROR_DESCRIPTION, new SimpleData(e.getMessage()));
        }

        body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
        body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));

        return resIData;
    }

    public IData downData(IData reqIData, IData resIData) throws IOException {
        IData body = new NodeData();
        resIData.put(Constants.TAG_BODY, body);

        List<Integer> posList = (ArrayList) reqIData.getBodyValue("posList");
        Long deployId = reqIData.getBodyValueLong("deployId");
        Long deployTransferId = reqIData.getBodyValueLong("deployTransferId");

        Deploy deploy = deployDao.getOne(deployId);
        DeployTransfer deployTransfer = deployTransferDao.getOne(deployTransferId);

        try {
            MulticastSender multicastSender = new MulticastSender(deployManager, deploy, deployTransfer, deploySegmentCache);

            List<Object> arrayData = new ArrayList<>();
            posList.forEach(pos -> {
                try {
                    Map<String, Object> posData = new HashMap<>();
                    posData.put("pos", pos);
                    posData.put("fileName", deploy.getFileName());
                    posData.put("fileDataBase64", Crypto.base64Encode(multicastSender.getData(pos)));
                    arrayData.add(posData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            String jsonStr = new Parse().toString(arrayData);
            ObjectMapper mapper = new ObjectMapper();
            JSONArray arr = mapper.readValue(jsonStr, JSONArray.class);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("data", arr);
            body.setObject(jsonObj);

            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_SUCESS));
        } catch (Exception e) {
            e.printStackTrace();
            body.put(Constants.TAG_RESULT, new SimpleData(Constants.RESULT_FAIL));
            body.put(Constants.TAG_ERROR, new SimpleData(Constants.ERROR_SERVICE_PROCESS_CODE));
        }

        return resIData;
    }

    public ResponseEntity<Resource> downData(IData reqIData) throws IOException {

        int pos = reqIData.getBodyValueInteger("pos");
        Long deployId = reqIData.getBodyValueLong("deployId");
        Long deployTransferId = reqIData.getBodyValueLong("deployTransferId");

        Deploy deploy = deployDao.getOne(deployId);
        DeployTransfer deployTransfer = deployTransferDao.getOne(deployTransferId);

        MulticastSender multicastSender = null;
        try {
            multicastSender = new MulticastSender(deployManager, deploy, deployTransfer, deploySegmentCache);
            Resource resource = new InputStreamResource(new ByteArrayInputStream(multicastSender.getData(pos)));
            //            Resource resource = new InputStreamResource(new ByteArrayInputStream(Files.readAllBytes(Paths.get("C:\\Users\\ingyu\\Downloads\\FileZilla_3.54.1_win64-setup.exe"))));
            return new ResponseEntity<>(resource, getHeader(pos + "_" + deploy.getFileName()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpHeaders getHeader(String fileName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        //        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                                                        .filename(fileName, StandardCharsets.UTF_8)
                                                        .build());
        return headers;
    }

}
