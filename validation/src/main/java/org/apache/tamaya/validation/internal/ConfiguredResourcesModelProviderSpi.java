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
package org.apache.tamaya.validation.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tamaya.format.ConfigurationData;
import org.apache.tamaya.format.ConfigurationFormats;
import org.apache.tamaya.validation.ConfigValidation;
import org.apache.tamaya.validation.spi.ConfigValidationReader;
import org.apache.tamaya.validation.spi.ConfigValidationProviderSpi;
import org.apache.tamaya.resource.ConfigResources;

import javax.config.ConfigProvider;

/**
 * ConfigModel provider that reads model metadata from property files from
 * {@code classpath*:META-INF/configmodel.json} in the following format:
 * <pre>
 *  Example of a configuration metamodel expressed via YAML.
 *  Structure is shown through indentation (one or more spaces).
 *  Sequence items are denoted by a dash,
 *  key value pairs within a map are separated by a colon.
 * </pre>
 */
public class ConfiguredResourcesModelProviderSpi implements ConfigValidationProviderSpi {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(ConfiguredResourcesModelProviderSpi.class.getName());
    /**
     * The parameter that can be used to configure the location of the configuration model resources.
     */
    private static final String MODEL_RESOURCE_PARAM = "org.apache.tamaya.validation.resources";
    /**
     * The resource class to checked for testing the availability of the resources extension module.
     */
    private static final String CONFIG_RESOURCE_CLASS = "org.apache.tamaya.resource.ConfigResource";
    /**
     * The resource class to checked for testing the availability of the formats extension module.
     */
    private static final String CONFIGURATION_FORMATS_CLASS = "org.apache.tamaya.format.ConfigurationFormats";
    /**
     * Initializes the flag showing if the formats module is present (required).
     */
    private static final boolean AVAILABLE = checkAvailabilityFormats();
    /**
     * Initializes the flag showing if the resources module is present (optional).
     */
    private static final boolean RESOURCES_EXTENSION_AVAILABLE = checkAvailabilityResources();

    /**
     * The configModels read.
     */
    private List<ConfigValidation> configModels = new ArrayList<>();

    /**
     * Initializes the flag showing if the formats module is present (required).
     */
    private static boolean checkAvailabilityFormats() {
        try {
            Class.forName(CONFIGURATION_FORMATS_CLASS);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Initializes the flag showing if the resources module is present (optional).
     */
    private static boolean checkAvailabilityResources() {
        try {
            Class.forName(CONFIG_RESOURCE_CLASS);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Constructor, mostly called from {@link java.util.ServiceLoader}
     */
    public ConfiguredResourcesModelProviderSpi() {
        if (!AVAILABLE) {
            LOG.info("tamaya-format extension is required to read model configuration, No extended model support AVAILABLE.");
        } else {
            final String resources = ConfigProvider.getConfig().getValue(MODEL_RESOURCE_PARAM, String.class);
            if (resources == null || resources.trim().isEmpty()) {
                LOG.info("Mo model resources location configured in " + MODEL_RESOURCE_PARAM + ".");
                return;
            }
            Collection<URL> urls;
            if (RESOURCES_EXTENSION_AVAILABLE) {
                LOG.info("Using tamaya-resources extension to read model configuration from " + resources);
                urls = ConfigResources.getResourceResolver().getResources(resources.split(","));
            } else {
                LOG.info("Using default classloader resource location to read model configuration from " + resources);
                urls = new ArrayList<>();
                for (final String resource : resources.split(",")) {
                    if (!resource.trim().isEmpty()) {
                        Enumeration<URL> configs;
                        try {
                            configs = getClass().getClassLoader().getResources(resource);
                            while (configs.hasMoreElements()) {
                                urls.add(configs.nextElement());
                            }
                        } catch (final IOException e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                                    "Error evaluating config model locations from " + resource, e);
                        }
                    }
                }
            }
            // Reading configs
            for (final URL config : urls) {
                try (InputStream is = config.openStream()) {
                    final ConfigurationData data = ConfigurationFormats.readConfigurationData(config);
                    Map<String,String> props = data.getCombinedProperties();
                    String owner = props.get("_model.provider");
                    if(owner==null){
                        owner = config.toString();
                    }
                    configModels.addAll(ConfigValidationReader.loadValidations(owner, props));
                } catch (final Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                            "Error loading config model data from " + config, e);
                }
            }
        }
        configModels = Collections.unmodifiableList(configModels);
    }


    @Override
    public Collection<ConfigValidation> getConfigValidations() {
        return configModels;
    }
}
