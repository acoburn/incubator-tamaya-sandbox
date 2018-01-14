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
package org.apache.tamaya.validation.spi;

import org.apache.tamaya.validation.ValidationTarget;

/**
 * JMX Management bean for accessing current configuration information
 */
public interface ConfigValidationMBean {
    /**
     * Validates the configuration for the given context.
     *
     * @param showUndefined allows filtering for undefined configuration elements.
     * @return the validation results, never null.
     */
    String validate(boolean showUndefined);

    String getConfigurationModel();

    String getConfigurationModel(ValidationTarget type);

    /**
     * Find the validations by checking the validation's name using the given regular expression.
     * @param namePattern the regular expression to use, not null.
     * @return the sections defined, never null.
     */
    String findConfigurationModels(String namePattern);

    /**
     * Find the validations by checking the validation's name using the given regular expression.
     * @param type the target ModelTypes (optional), not null.
     * @param namePattern the regular expression to use, not null.
     * @return the sections defined, never null.
     */
    String findValidationModels(String namePattern, ValidationTarget... type);
}
