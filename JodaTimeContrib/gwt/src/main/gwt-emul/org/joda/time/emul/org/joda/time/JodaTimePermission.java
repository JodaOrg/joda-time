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

import java.security.BasicPermission;

/**
 * JodaTimePermission is used for securing global method calls in the Joda-Time
 * library. Since this class extends BasicPermission, asterisks may be used to
 * denote wildcard permissions. The following permissions are supported:
 *
 * <pre>
 * DateTimeZone
 *   .setDefault                 Allows a default DateTimeZone to be set
 *   .setProvider                Allows the DateTimeZone instance provider to be set
 *   .setNameProvider            Allows the DateTimeZone name provider to be set
 *
 * ConverterManager
 *   .alterInstantConverters     Allows an instant converter to be added or removed
 *   .alterPartialConverters     Allows a partial converter to be added or removed
 *   .alterDurationConverters    Allows a duration converter to be added or removed
 *   .alterPeriodConverters      Allows a period converter to be added or removed
 *   .alterIntervalConverters    Allows an interval converter to be added or removed
 *
 * CurrentTime.setProvider       Allows the current time provider to be set
 * </pre>
 * <p>
 * JodaTimePermission is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class JodaTimePermission extends BasicPermission {
    
    /** Serialization version */
    private static final long serialVersionUID = 1408944367355875472L;

    /**
     * Constructs a new permission object.
     * 
     * @param name  the permission name
     */
    public JodaTimePermission(String name) {
        super(name);
    }

}
