package com.inswave.appplatform.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

import java.io.IOException;
import java.lang.reflect.Field;

@Slf4j
public class ProcessUtil {

    interface Kernel32 extends Library {

        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        int GetProcessId(Long hProcess);
    }

    public static void main(String[] args) {
        try {
            Process p = null;

            if (Platform.isWindows())
                p = Runtime.getRuntime().exec("cmd /C ping msn.de");
            else if (Platform.isLinux())
                p = Runtime.getRuntime().exec("cmd /C ping msn.de");

            System.out.println("The PID: " + getPid(p));

            int x = p.waitFor();
            System.out.println("Exit with exitcode: " + x);

        } catch (Exception ex) {
            //            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            log.error("{}", ex.getMessage());
        }
    }

    public static int getPid(Process p) {
        Field f;

        if (Platform.isWindows()) {
            try {
                f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                int pid = Kernel32.INSTANCE.GetProcessId((Long) f.get(p));
                return pid;
            } catch (Exception ex) {
                //                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                log.error("{}", ex.getMessage());
            }
        } else if (Platform.isLinux()) {
            try {
                f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                int pid = (Integer) f.get(p);
                return pid;
            } catch (Exception ex) {
                //                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                log.error("{}", ex.getMessage());
            }
        } else {
        }
        return 0;
    }

    public static ProcessInfo getProcess(int pid) {
        return JProcesses.getProcess(pid);
    }

    public static String getProcessName(int pid) {
        ProcessInfo processInfo = JProcesses.getProcess(pid);
        if (processInfo == null) {
            return null;
        }
        return processInfo.getName();
    }

    public static boolean killPid(int pid) throws IOException {
        JProcessesResponse jProcessesResponse = JProcesses.killProcess(pid);
        if (!jProcessesResponse.isSuccess()) {
            log.error("killPid failed : {}", jProcessesResponse.getMessage());
        }
        return jProcessesResponse.isSuccess();
    }
    //    public static void killPid(int pid) throws IOException {
    //        String sPid = String.valueOf(pid);
    //
    //        if (SystemUtils.IS_OS_WINDOWS) {
    //            Runtime.getRuntime().exec("taskkill /F /PID " + sPid);
    //        } else {
    //            Runtime.getRuntime().exec("kill -9 " + sPid);
    //        }
    //    }
}
