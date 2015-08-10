/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya.model;

import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.model.spi.ValidationProviderSpi;
import org.apache.tamaya.spi.ServiceContextManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validator accessor to validate the current configuration.
 */
public final class ConfigValidator {

    /**
     * Singleton constructor.
     */
    private ConfigValidator() {
    }

    /**
     * Get the sections defined.
     *
     * @return the sections defined, never null.
     */
    public static Collection<Validation> getValidations() {
        List<Validation> result = new ArrayList<>();
        for (ValidationProviderSpi model : ServiceContextManager.getServiceContext().getServices(ValidationProviderSpi.class)) {
            result.addAll(model.getValidations());
        }
        return result;
    }

    /**
     * Validates the current configuration.
     *
     * @return the validation results, never null.
     */
    public static Collection<ValidationResult> validate() {
        return validate(false);
    }

    /**
     * Validates the current configuration.
     * @param showUndefined show any unknown parameters.
     * @return the validation results, never null.
     */
    public static Collection<ValidationResult> validate(boolean showUndefined) {
        return validate(ConfigurationProvider.getConfiguration(), showUndefined);
    }

    /**
     * Validates the given configuration.
     *
     * @param config the configuration to be validated against, not null.
     * @return the validation results, never null.
     */
    public static Collection<ValidationResult> validate(Configuration config) {
        return validate(config, false);
    }

    /**
     * Validates the given configuration.
     *
     * @param config the configuration to be validated against, not null.
     * @return the validation results, never null.
     */
    public static Collection<ValidationResult> validate(Configuration config, boolean showUndefined) {
        List<ValidationResult> result = new ArrayList<>();
        for (Validation defConf : getValidations()) {
            result.addAll(defConf.validate(config));
        }
        if(showUndefined){
            Map<String,String> map = new HashMap<>(config.getProperties());
            for (Validation defConf : getValidations()) {
                if("Parameter".equals(defConf.getType())){
                    map.remove(defConf.getName());
                }
            }
            for(Map.Entry<String,String> entry:map.entrySet()){
                result.add(ValidationResult.ofUndefined(entry.getKey()));
            }
        }
        return result;
    }

}
