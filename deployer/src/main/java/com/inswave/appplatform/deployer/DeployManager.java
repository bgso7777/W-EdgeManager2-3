package com.inswave.appplatform.deployer;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.deployer.cache.DeploySegmentCache;
import com.inswave.appplatform.deployer.dao.DeployTransferDao;
import com.inswave.appplatform.deployer.dao.DeployTransferHistoryDao;
import com.inswave.appplatform.deployer.domain.Deploy;
import com.inswave.appplatform.deployer.domain.DeployTransfer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeployManager {

    private static DeployManager            deployManager = null;
    @Autowired
    private        DeployTransferDao        deployTransferDao;
    @Autowired
    private        DeployTransferHistoryDao deployTransferHistoryDao;
    @Autowired
    private        DeploySegmentCache       deploySegmentCache;
    @Value("${wdeployer.multicast.ip:#{'232.1.5.1'}}")
    private        String                   multicastIp;
    @Value("${wdeployer.multicast.port:#{27016}}")
    private        int                      multicastPort;

    public static DeployManager getInstance() {
        if (deployManager == null)
            deployManager = new DeployManager();
        return deployManager;
    }

    // DeployTransfer ID, Thread
    private static Map<Long, MulticastSender> multicastSenders = new ConcurrentHashMap<Long, MulticastSender>();

    public void deploy(Deploy deploy, DeployTransfer deployTransfer) throws IOException, NoSuchAlgorithmException {
        MulticastSender multicastSender = new MulticastSender(this, deploy, deployTransfer, deploySegmentCache);
        multicastSender.setDeployTransferDao(deployTransferDao);
        multicastSender.setDeployTransferHistoryDao(deployTransferHistoryDao);

        multicastSenders.put(deployTransfer.getDeployTransferId(), multicastSender);
        multicastSender.setUncaughtExceptionHandler((t, e) -> {
            remove(deployTransfer.getDeployTransferId());   // 예외 발생 시 목록에서 제거 (Thread에서 NullPointer가 catch되지 않는 문제)
        });
        multicastSender.start();
    }

    public static void pause(DeployTransfer deployTransfer) {
        MulticastSender multicastSender = multicastSenders.get(deployTransfer.getDeployTransferId());
        // TODO 일시정지 기능이 필요한가??
    }

    public void remove(Long deployTransferId) {
        multicastSenders.remove(deployTransferId);
    }

    public void stop(Long deployTransferId) {
        MulticastSender multicastSender = multicastSenders.get(deployTransferId);
        multicastSender.setStop();
    }

    public Map<Long, MulticastSender> getMulticastSenders() {
        return multicastSenders;
    }

    public Path getFile(Long deployId) {
        return Paths.get(Config.getInstance().getDeployerRepoPath(), deployId.toString());
    }

    public Path moveFile(Path from, Long deployId) throws IOException {
        File src = from.toFile();
        File dest = Paths.get(Config.getInstance().getDeployerRepoPath()).toFile();
        File re_src = Paths.get(Config.getInstance().getDeployerRepoPath(), src.getName()).toFile();
        File re_dest = Paths.get(Config.getInstance().getDeployerRepoPath(), deployId.toString()).toFile();

        FileUtils.moveFileToDirectory(src, dest, true);
        if (Files.exists(re_dest.toPath())) {
            Files.delete(re_dest.toPath());
        }
        FileUtils.moveFile(re_src, re_dest);
        return re_dest.toPath();
    }

    public Integer getStatus(Long deployId) {
        MulticastSender multicastSender = getMulticastSenders().get(deployId);
        if (multicastSender.isStop()) {
            return Constants.TAG_DEPLOYER_STATUS_STOPPING;  // 진행중
        } else {
            return Constants.TAG_DEPLOYER_STATUS_SENDING;  // 중지중
        }
    }

    public String getMulticastIp() {
        return multicastIp;
    }

    public int getMulticastPort() {
        return multicastPort;
    }
}
