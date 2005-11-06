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
package org.joda.time.contrib.holiday.currency;

import java.util.ArrayList;

/**
 * Choice list.
 *
 * @author Scott R. Duchin
 */
public class ChoiceList extends ArrayList<Choice> {

    private final Class cls;

    public static ChoiceList choiceList(Class cls) {
        return new ChoiceList(cls);
    }

    public ChoiceList(Class cls) {
        super();
        this.cls = cls;
    }

    public RawHolidayChoice choice(String holi) {
        return null;
    }

    public Class choiceClass() {
        return cls;
    }

}
