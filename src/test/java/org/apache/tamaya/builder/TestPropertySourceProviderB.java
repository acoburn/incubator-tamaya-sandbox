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
package org.apache.tamaya.builder;

import org.apache.tamaya.core.propertysource.BasePropertySource;
import org.apache.tamaya.spi.PropertySource;
import org.apache.tamaya.spi.PropertySourceProvider;
import org.apache.tamaya.spi.PropertyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TestPropertySourceProviderB
    implements PropertySourceProvider
{
    @Override
    public Collection<PropertySource> getPropertySources() {
        ArrayList<PropertySource> sources = new ArrayList<>(2);

        sources.add(new AProvidingPropertySource());
        sources.add(new BProvidingPropertySource());

        return sources;
    }

    private class BProvidingPropertySource extends BasePropertySource {
        private Map<String, String> props = Collections.singletonMap("tpsp_b", "B");

        @Override
        public int getOrdinal() {
            return 100;
        }

        @Override
        public String getName() {
            return "BProvidingPropertySource";
        }

        @Override
        public PropertyValue get(String key) {
            return PropertyValue.of(key,getProperties().get(key), getName());
        }

        @Override
        public Map<String, String> getProperties() {
            return props;
        }
    }

    private class AProvidingPropertySource extends BasePropertySource {
        private Map<String, String> props = Collections.singletonMap("tpsp_a", "A");

        @Override
        public Map<String, String> getProperties() {
            return props;
        }

        @Override
        public PropertyValue get(String key) {
            return PropertyValue.of(key, getProperties().get(key), getName());
        }

        @Override
        public int getOrdinal() {
            return 100;
        }

        @Override
        public String getName() {
            return "AProvidingPropertySource";
        }
    }
}
