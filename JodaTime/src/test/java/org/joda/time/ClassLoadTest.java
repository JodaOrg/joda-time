/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time;

import org.joda.time.base.AbstractDateTime;
import org.joda.time.base.AbstractInstant;

/**
 * This class displays what the ClassLoader is up to.
 * Run using JVM -verbose:class.
 *
 * @author Stephen Colebourne
 */
public class ClassLoadTest {

    // run using JVM -verbose:class
    public static void main(String[] args) {
        System.out.println("-----------------------------------------------");
        System.out.println("-----------AbstractInstant---------------------");
        Class cls = AbstractInstant.class;
        System.out.println("-----------ReadableDateTime--------------------");
        cls = ReadableDateTime.class;
        System.out.println("-----------AbstractDateTime--------------------");
        cls = AbstractDateTime.class;
        System.out.println("-----------DateTime----------------------------");
        cls = DateTime.class;
        System.out.println("-----------DateTimeZone------------------------");
        cls = DateTimeZone.class;
        System.out.println("-----------new DateTime()----------------------");
        DateTime dt = new DateTime();
        System.out.println("-----------new DateTime(ReadableInstant)-------");
        dt = new DateTime(dt);
        System.out.println("-----------new DateTime(Long)------------------");
        dt = new DateTime(new Long(0));
        System.out.println("-----------------------------------------------");
    }
    
}
