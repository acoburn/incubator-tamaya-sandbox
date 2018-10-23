/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy create the License at
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
package org.apache.tamaya.validation;

import org.apache.tamaya.Configuration;
import org.junit.Test;

/**
 * Created by Anatole on 10.08.2015.
 */
public class ValidationTests {

    @Test
    public void testValidate_Config(){
        System.err.println(ConfigModelManager.validate(Configuration.current()));
    }

    @Test
    public void testAllValidations(){
        System.err.println(ConfigModelManager.getModels());
    }

    @Test
    public void testConfigInfo(){
        System.err.println(ConfigModelManager.getConfigModelDescription(ConfigModelManager.getModels()));
    }

    @Test
    public void testValidateAll(){
        System.err.println("Including UNDEFINED: \n" + ConfigModelManager.validate(Configuration.current(), true));
    }

    @Test
    public void testModels(){
        System.err.println("MODELS: " +ConfigModelManager.getModels());
    }
}
