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

import java.lang.annotation.Native;

/**
 * The constants in this interface describe the changes to an item to
 * be committed.
 */
public interface CommitItemStateFlags
{
    /**
     * the item has been added
     */
    @Native
    public static final int Add=1;

    /**
     * the item has been deleted
     */
    @Native
    public static final int Delete=2;

    /**
     * the item has text modifications
     */
    @Native
    public static final int TextMods=4;

    /**
     * the item has property modifications
     */
    @Native
    public static final int PropMods=8;

    /**
     * the item has been copied
     */
    @Native
    public static final int IsCopy=16;

    /**
     * the item has a lock token
     */
    @Native
    public static final int LockToken = 32;

    /**
     * the item was moved to this location
     * @since 1.8
     */
    @Native
    public static int MovedHere = 64;
}
