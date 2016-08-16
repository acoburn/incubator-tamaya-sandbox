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
package org.apache.tamaya.ui.views;

import com.vaadin.ui.Tree;
import org.apache.tamaya.Configuration;
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.spi.PropertyValueCombinationPolicy;

import javax.annotation.Priority;

/**
 * Created by atsticks on 29.06.16.
 */
@Priority(0)
public class TamayaGeneralSystemInfoProvider implements SystemInfoProvider{
    @Override
    public void provideSystemInfo(Tree tree) {
        Configuration config = ConfigurationProvider.getConfiguration();
        String currentParent = "General";
        tree.addItem(currentParent);
        tree.addItem("Configuration.class");
        tree.setItemCaption("Configuration.class", "Configuration class = " + config.getClass().getName());
        tree.setParent("Configuration.class", currentParent);
        tree.setChildrenAllowed("Configuration.class", false);

        tree.addItem("ConfigurationContext.class");
        tree.setItemCaption("ConfigurationContext.class", "ConfigurationContext class = " +
                config.getContext().getClass().getName());
        tree.setParent("ConfigurationContext.class", currentParent);
        tree.setChildrenAllowed("ConfigurationContext.class", false);

        tree.addItem("PropertyValueCombinationPolicy.class");
        tree.setItemCaption("PropertyValueCombinationPolicy.class",
                PropertyValueCombinationPolicy.class.getSimpleName() + " class = " +
                        config.getContext().getPropertyValueCombinationPolicy().getClass().getName());
        tree.setParent("PropertyValueCombinationPolicy.class", currentParent);
        tree.setChildrenAllowed("PropertyValueCombinationPolicy.class", false);
    }
}
