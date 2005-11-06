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

/**
 * Abstract choice implementation.
 *
 * @author Scott R. Duchin
 */
public abstract class AbstractChoice {

    private final ChoiceList _choices;
    private final String _label;

    public AbstractChoice(ChoiceList list, String label) {
        super();
        _choices = list;
        _label = label;
    }

    public Class choiceClass() {
        return _choices.choiceClass();
    }

    public ChoiceList choiceList() {
        return _choices;
    }

    public String getLabel() {
        return _label;
    }

}
