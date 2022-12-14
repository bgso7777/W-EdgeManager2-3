/**
 * @copyright
 * ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one
 *    or more contributor license agreements.  See the NOTICE file
 *    distributed with this work for additional information
 *    regarding copyright ownership.  The ASF licenses this file
 *    to you under the Apache License, Version 2.0 (the
 *    "License"); you may not use this file except in compliance
 *    with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 * ====================================================================
 * @endcopyright
 */

package org.apache.subversion.javahl;

import org.apache.subversion.javahl.callback.ReposFreezeAction;
import org.apache.subversion.javahl.callback.ReposNotifyCallback;
import org.apache.subversion.javahl.callback.ReposVerifyCallback;
import org.apache.subversion.javahl.types.Depth;
import org.apache.subversion.javahl.types.Lock;
import org.apache.subversion.javahl.types.Revision;
import org.apache.subversion.javahl.types.Version;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public interface ISVNRepos {

    /**
     * interface to receive the messages
     */
    public static interface MessageReceiver
    {
        /**
         * receive one message line
         * @param message   one line of message
         */
        public void receiveMessageLine(String message);
    }

    /**
     * release the native peer (should not depend on finalize)
     */
    public abstract void dispose();

    /**
     * Filesystem in a Berkeley DB
     */
    public static final String BDB = "bdb";
    /**
     * Filesystem in the filesystem
     */
    public static final String FSFS = "fsfs";

    /**
     * @return Version information about the underlying native libraries.
     */
    public abstract Version getVersion();

    /**
     * create a subversion repository.
     * @param path                  the path where the repository will been
     *                              created.
     * @param disableFsyncCommit    disable to fsync at the commit (BDB).
     * @param keepLog               keep the log files (BDB).
     * @param configPath            optional path for user configuration files.
     * @param fstype                the type of the filesystem (BDB or FSFS)
     * @throws ClientException  throw in case of problem
     */
    public abstract void create(File path, boolean disableFsyncCommit,
            boolean keepLog, File configPath, String fstype)
            throws ClientException;

    /**
     * deltify the revisions in the repository
     * @param path              the path to the repository
     * @param start             start revision
     * @param end               end revision
     * @throws ClientException  throw in case of problem
     */
    public abstract void deltify(File path, Revision start, Revision end)
            throws ClientException;

    /**
     * dump the data in a repository
     * @param path              the path to the repository
     * @param dataOut           the data will be outputed here
     * @param start             the first revision to be dumped
     * @param end               the last revision to be dumped
     * @param incremental       the dump will be incremantal
     * @param useDeltas         the dump will contain deltas between nodes
         * @param callback          the callback to receive notifications
     * @throws ClientException  throw in case of problem
     */
    public abstract void dump(File path, OutputStream dataOut,
                Revision start, Revision end, boolean incremental,
                boolean useDeltas, ReposNotifyCallback callback)
            throws ClientException;

    /**
     * make a hot copy of the repository
     * @param path              the path to the source repository
     * @param targetPath        the path to the target repository
     * @param cleanLogs         clean the unused log files in the source
     *                          repository
         * @param callback          the callback to receive notifications
     * @throws ClientException  throw in case of problem
         * @since 1.9
     */
    public abstract void hotcopy(File path, File targetPath,
                        boolean cleanLogs, boolean incremental,
                        ReposNotifyCallback callback)
                        throws ClientException;

    public abstract void hotcopy(File path, File targetPath,
            boolean cleanLogs, boolean incremental)
                        throws ClientException;

    public abstract void hotcopy(File path, File targetPath,
            boolean cleanLogs) throws ClientException;

    /**
     * list all logfiles (BDB) in use or not)
     * @param path              the path to the repository
     * @param receiver          interface to receive the logfile names
     * @throws ClientException  throw in case of problem
     */
    public abstract void listDBLogs(File path, MessageReceiver receiver)
            throws ClientException;

    /**
     * list unused logfiles
     * @param path              the path to the repository
     * @param receiver          interface to receive the logfile names
     * @throws ClientException  throw in case of problem
     */
    public abstract void listUnusedDBLogs(File path, MessageReceiver receiver)
            throws ClientException;


    /**
     * load the data of a dump into a repository
     * @param path              the path to the repository
     * @param dataInput         the data input source
     * @param start             the first revision to load
     * @param end               the last revision to load
     * @param ignoreUUID        ignore any UUID found in the input stream
     * @param forceUUID         set the repository UUID to any found in the
     *                          stream
     * @param usePreCommitHook  use the pre-commit hook when processing commits
     * @param usePostCommitHook use the post-commit hook when processing commits
     * @param validateProps     validate "svn:" revision and node properties
     * @param ignoreDates       ignore revision datestamps in the dump stream
     * @param normalizeProps    attempt to normalize invalid Subversion
     *                          revision and node properties
     * @param relativePath      the directory in the repository, where the data
     *                          in put optional.
     * @param callback          the target for processing messages
     * @throws ClientException  throw in case of problem
     * @since 1.10
     */
    public abstract void load(File path, InputStream dataInput,
                              Revision start, Revision end,
                              boolean ignoreUUID, boolean forceUUID,
                              boolean usePreCommitHook,
                              boolean usePostCommitHook,
                              boolean validateProps,
                              boolean ignoreDates,
                              boolean normalizeProps,
                              String relativePath,
                              ReposNotifyCallback callback)
        throws ClientException;

    /**
     * load the data of a dump into a repository
     * @param path              the path to the repository
     * @param dataInput         the data input source
         * @param start             the first revision to load
         * @param end               the last revision to load
     * @param ignoreUUID        ignore any UUID found in the input stream
     * @param forceUUID         set the repository UUID to any found in the
     *                          stream
     * @param usePreCommitHook  use the pre-commit hook when processing commits
     * @param usePostCommitHook use the post-commit hook when processing commits
         * @param validateProps     validate "svn:" revision and node properties
         * @param ignoreDates       ignore revision datestamps in the dump stream
     * @param relativePath      the directory in the repository, where the data
     *                          in put optional.
     * @param callback          the target for processing messages
     * @throws ClientException  throw in case of problem
         * @since 1.9
     */
    public abstract void load(File path, InputStream dataInput,
                                  Revision start, Revision end,
                                  boolean ignoreUUID, boolean forceUUID,
                                  boolean usePreCommitHook,
                                  boolean usePostCommitHook,
                                  boolean validateProps,
                                  boolean ignoreDates,
                                  String relativePath,
                                  ReposNotifyCallback callback)
        throws ClientException;

    /**
     * Load the data of a dump into a repository.  Sets
         * <code>validateProps</code> and <code>ignoreDates</code> to
         * <code>false</code>.
         *
     * @param path              the path to the repository
     * @param dataInput         the data input source
         * @param start             the first revision to load
         * @param end               the last revision to load
     * @param ignoreUUID        ignore any UUID found in the input stream
     * @param forceUUID         set the repository UUID to any found in the
     *                          stream
     * @param usePreCommitHook  use the pre-commit hook when processing commits
     * @param usePostCommitHook use the post-commit hook when processing commits
     * @param relativePath      the directory in the repository, where the data
     *                          in put optional.
     * @param callback          the target for processing messages
     * @throws ClientException  throw in case of problem
         * @since 1.8
     */
    public abstract void load(File path, InputStream dataInput,
                                  Revision start, Revision end,
                                  boolean ignoreUUID, boolean forceUUID,
                                  boolean usePreCommitHook,
                                  boolean usePostCommitHook,
                                  String relativePath,
                                  ReposNotifyCallback callback)
        throws ClientException;

    /**
     * load the data of a dump into a repository
     * @param path              the path to the repository
     * @param dataInput         the data input source
     * @param ignoreUUID        ignore any UUID found in the input stream
     * @param forceUUID         set the repository UUID to any found in the
     *                          stream
     * @param usePreCommitHook  use the pre-commit hook when processing commits
     * @param usePostCommitHook use the post-commit hook when processing commits
     * @param relativePath      the directory in the repository, where the data
     *                          in put optional.
     * @param callback          the target for processing messages
     * @throws ClientException  throw in case of problem
         * @note behaves like the 1.8 vesion with the revision
         *       parameters set to Revision.START and Revision.HEAD.
     */
    public abstract void load(File path, InputStream dataInput,
            boolean ignoreUUID, boolean forceUUID, boolean usePreCommitHook,
            boolean usePostCommitHook, String relativePath,
            ReposNotifyCallback callback)
        throws ClientException;

    /**
     * list all open transactions in a repository
     * @param path              the path to the repository
     * @param receiver          receives one transaction name per call
     * @throws ClientException  throw in case of problem
     */
    public abstract void lstxns(File path, MessageReceiver receiver)
            throws ClientException;

    /**
     * recover the filesystem backend of a repository
     * @param path              the path to the repository
         * @return youngest revision
     * @throws ClientException  throw in case of problem
     */
    public abstract long recover(File path, ReposNotifyCallback callback)
            throws ClientException;

    /**
     * Take an exclusive lock on each of the listed repositories
     * to prevent commits; then, while holding all the locks, call
     * the action.invoke().
     *
     * The repositories may or may not be readable by Subversion
     * while frozen, depending on implementation details of the
     * repository's filesystem backend.
     *
     * Repositories are locked in the listed order.
     * @param action     describes the action to perform
     * @param paths	     the set of repository paths
     * @throws ClientException
         * @since 1.8
     */
    public abstract void freeze(ReposFreezeAction action,
                    File... paths)
        throws ClientException;

    /**
     * remove open transaction in a repository
     * @param path              the path to the repository
     * @param transactions      the transactions to be removed
     * @throws ClientException  throw in case of problem
     */
    public abstract void rmtxns(File path, String[] transactions)
            throws ClientException;

    /**
     * Change the value of the revision property <code>propName</code>
     * to <code>propValue</code>.  By default, does not run
     * pre-/post-revprop-change hook scripts.
     *
     * @param path The path to the repository.
     * @param rev The revision for which to change a property value.
     * @param propName The name of the property to change.
     * @param propValue The new value to set for the property.
     * @param usePreRevPropChangeHook Whether to run the
     * <i>pre-revprop-change</i> hook script.
     * @param usePostRevPropChangeHook Whether to run the
     * <i>post-revprop-change</i> hook script.
     * @throws SubversionException If a problem occurs.
     */
    public abstract void setRevProp(File path, Revision rev, String propName,
            String propValue, boolean usePreRevPropChangeHook,
            boolean usePostRevPropChangeHook) throws SubversionException;

    /**
     * Verify the repository at <code>path</code> between revisions
     * <code>start</code> and <code>end</code>.
     *<p>
     * If <code>verifyCallback</code> is <code>null</code>, verification
     * will stop at the first encountered error. Otherwise, the verification
     * process may continue, depending on the value returned from the
     * invocation of <code>verifyCallback</code>.
     *
     * @param path              the path to the repository
     * @param start             the first revision
     * @param end               the last revision
     * @param checkNormalization report directory entry and mergeinfo name collisions
     *                           caused by denormalized Unicode representations
     * @param metadataOnly      check only metadata, not file contents
     * @param notifyCallback    the callback to receive notifications
     * @param verifyCallback    the callback to receive verification status
     * @throws ClientException If an error occurred.
     * @since 1.9
     */
    public abstract void verify(File path, Revision start, Revision end,
                boolean checkNormalization,
                boolean metadataOnly,
                ReposNotifyCallback notifyCallback,
                ReposVerifyCallback verifyCallback)
            throws ClientException;

    /**
     * Verify the repository at <code>path</code> between revisions
     * <code>start</code> and <code>end</code>.
     *<p>
     *<b>Note:</b> Behaves like the 1.9 version with
     *             <code>checkNormailzation</code> and
     *             <code>metadataOnly</code> set to <code>false</code>
     *             and <code>verifyCallback</code> set to
     *             <code>null</code>.
     *
     * @param path              the path to the repository
     * @param start             the first revision
     * @param end               the last revision
     * @param callback          the callback to receive notifications
     * @throws ClientException If an error occurred.
     */
    public abstract void verify(File path, Revision start, Revision end,
                ReposNotifyCallback callback)
            throws ClientException;

    /**
     * list all locks in the repository
     * @param path              the path to the repository
     * @param depth             the depth to recurse
     * @throws ClientException  throw in case of problem
     */
    public abstract Set<Lock> lslocks(File path, Depth depth)
            throws ClientException;

    /**
     * remove multiple locks from the repository
     * @param path              the path to the repository
     * @param locks             the name of the locked items
     * @throws ClientException  throw in case of problem
     */
    public abstract void rmlocks(File path, String[] locks)
            throws ClientException;

    /**
     * upgrade the repository format
     * @param path              the path to the repository
     * @param callback          for notification
     * @throws ClientException  throw in case of problem
     */
    public abstract void upgrade(File path, ReposNotifyCallback callback)
            throws ClientException;

    /**
     * pack the repository
     * @param path              the path to the repository
     * @param callback          for notification
     * @throws ClientException  throw in case of problem
     */
    public abstract void pack(File path, ReposNotifyCallback callback)
            throws ClientException;

    /**
     * cancel the active operation
     * @throws ClientException
     */
    void cancelOperation() throws ClientException;
}
