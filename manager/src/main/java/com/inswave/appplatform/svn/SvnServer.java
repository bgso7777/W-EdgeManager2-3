package com.inswave.appplatform.svn;

import com.inswave.appplatform.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jutils.jprocesses.model.ProcessInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Start a svn server
 */
@Slf4j
public class SvnServer {

    private static SvnServer singleton = null;

    private Command command;

    final private int listenPort;

    private File repository;

    public static boolean isRunning() {
        return singleton != null;
    }

    public static SvnServer startServer(int listenPort,
                                        File repository) throws IOException {
        singleton = new SvnServer(listenPort, repository);
        singleton.start();
        return singleton;
    }

    public static void stopServer(SvnServer instance) {
        singleton.kill();
        singleton = null;
    }

    private SvnServer(int listenPort, File repository) {
        super();
        this.listenPort = listenPort;
        this.repository = repository;
        if (listenPort == 0) {
            throw new IllegalArgumentException("listenPort must not be 0 !");
        }
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null !");
        }
    }

    /**
     * @return Returns the listenPort.
     */
    public int getListenPort() {
        return listenPort;
    }

    /**
     * @return Returns the repository.
     */
    public File getRepository() {
        return repository;
    }

    public void clearProcess() throws IOException {
        // svnserve pid가 살아 있다면 kill한다.
        Path pathPid = SvnManager.getServerPid();
        if (Files.exists(pathPid)) {
            String sPid = new String(Files.readAllBytes(pathPid));
            if (!StringUtils.isEmpty(sPid)) {
                ProcessInfo processInfo = ProcessUtil.getProcess(Integer.valueOf(sPid));
                if (processInfo != null) {
                    if (processInfo.getName().indexOf("svnserve") != -1) {
                        log.info("Kill svnserve. already svnserve is running. (PID: {})", sPid);
                        log.info("Kill process PID: {} result is: {}.", ProcessUtil.killPid(Integer.valueOf(sPid)));
                    }
                }
            }
        }
    }

    public void start() throws IOException {
        clearProcess();
        Path pathPid = SvnManager.getServerPid();

        command = getSvnServerCommmand();
        command.setParameters(new String[] {
        "-d",
        "--foreground",
        "--listen-port", Integer.toString(listenPort),
        "-r", repository.toString()
        });
        command.exec();
        int pid = ProcessUtil.getPid(command.getProcess());
        Files.write(pathPid, String.valueOf(pid).getBytes());

        log.info("[Success] Now svnserve is running. (PID: {})", pid);
    }

    public void kill() {
        if (command != null) {
            command.kill();
        }
    }

    protected Command getSvnServerCommmand() {
        Path svnServePath = SvnManager.getBinPathSvnserve();
        return new Command(svnServePath.toString());
    }
}