/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.tamaya.metamodel.ext;


import org.apache.tamaya.spi.PropertyValue;
import org.apache.tamaya.spisupport.propertysource.BasePropertySource;

import java.util.Collections;
import java.util.Map;

/**
 * Created by atsticks on 17.04.17.
 */
public class MyPropertySource extends BasePropertySource {

    private String name2;
    private String attrValue;
    private String elemValue;
    private String overrideValue;

    @Override
    public Map<String, PropertyValue> getProperties() {
        return Collections.emptyMap();
    }

    public String getAttrValue() {
        return attrValue;
    }

    public MyPropertySource setAttrValue(String attrValue) {
        this.attrValue = attrValue;
        return this;
    }

    public String getElemValue() {
        return elemValue;
    }

    public MyPropertySource setElemValue(String elemValue) {
        this.elemValue = elemValue;
        return this;
    }

    public String getOverrideValue() {
        return overrideValue;
    }

    public MyPropertySource setOverrideValue(String overrideValue) {
        this.overrideValue = overrideValue;
        return this;
    }

    public String getName2() {
        return name2;
    }
}
