/*
 *  Copyright 2001-2015 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.tz;

/**
 * Logger for the compiler.
 */
public class ZoneInfoLogger {

    static ThreadLocal<Boolean> cVerbose = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    /**
     * Gets a flag indicating that verbose logging is required.
     * @return true to log verbosely
     */
    public static boolean verbose() {
        return cVerbose.get();
    }

    public static void set(boolean verbose) {
        cVerbose.set(verbose);
    }

}
