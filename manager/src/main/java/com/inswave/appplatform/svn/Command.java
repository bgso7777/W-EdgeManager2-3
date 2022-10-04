package com.inswave.appplatform.svn;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class Command {
    private Process process = null;

    private String   command;
    private String[] parameters = new String[] {};

    private OutputStream out = System.out;
    private OutputStream err = System.err;

    public Command(String command) {
        this.command = command;
    }

    /**
     * @param err The err to set.
     */
    public void setErr(OutputStream err) {
        this.err = err;
    }

    /**
     * @param out The out to set.
     */
    public void setOut(OutputStream out) {
        this.out = out;
    }

    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @return Returns the process.
     */
    public Process getProcess() {
        return process;
    }

    public void kill() {
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

    public void exec() throws IOException {
        String[] cmdArray = new String[parameters.length + 1];
        cmdArray[0] = command;
        System.arraycopy(parameters, 0, cmdArray, 1, parameters.length);
        ProcessBuilder builder = new ProcessBuilder(cmdArray);
        builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        process = builder.start();

//        Runtime.getRuntime().addShutdownHook(new Thread(this::kill));
    }

    /**
     * causes the current thread to wait, if necessary, until the process
     * represented by this <code>Command</code> object has terminated
     *
     * @return the exit value of the process. By convention, <code>0</code>
     * indicates normal termination.
     * @throws InterruptedException
     */
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }

}