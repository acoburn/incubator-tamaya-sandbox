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
package org.apache.tamaya.integration.cdi.internal;

import org.apache.tamaya.ConfigException;
import org.apache.tamaya.TypeLiteral;
import org.apache.tamaya.spi.ConfigurationContext;
import org.apache.tamaya.spi.ConfigurationContextBuilder;
import org.apache.tamaya.spi.PropertyConverter;
import org.apache.tamaya.spi.PropertyFilter;
import org.apache.tamaya.spi.PropertySource;
import org.apache.tamaya.spi.PropertyValueCombinationPolicy;

import javax.enterprise.inject.Vetoed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation of {@link ConfigurationContextBuilder}.
 */
@Vetoed
public class DefaultConfigurationContextBuilder implements ConfigurationContextBuilder {

    Map<String, PropertySource> propertySources = new HashMap<>();
    List<PropertyFilter> propertyFilters = new ArrayList<>();
    Map<TypeLiteral<?>, List<PropertyConverter<?>>> propertyConverters = new HashMap<>();
    PropertyValueCombinationPolicy combinationPolicy;

    @Override
    public ConfigurationContextBuilder setContext(ConfigurationContext context) {
        this.propertySources.clear();
        for(PropertySource ps:context.getPropertySources()) {
            this.propertySources.put(ps.getName(), ps);
        }
        this.propertyFilters.clear();
        this.propertyFilters.addAll(context.getPropertyFilters());
        this.propertyConverters.clear();
        this.propertyConverters.putAll(context.getPropertyConverters());
        this.combinationPolicy = context.getPropertyValueCombinationPolicy();
        return this;
    }

    @Override
    public ConfigurationContextBuilder addPropertySources(Collection<PropertySource> propertySourcesToAdd) {
        for(PropertySource ps:propertySourcesToAdd){
            if(this.propertySources.containsKey(ps.getName())){
                throw new ConfigException("Duplicate PropertySource: " + ps.getName());
            }
        }
        for(PropertySource ps:propertySourcesToAdd) {
            this.propertySources.put(ps.getName(), ps);
        }
        return this;
    }

    @Override
    public ConfigurationContextBuilder addPropertySources(PropertySource... propertySourcesToAdd) {
        return addPropertySources(Arrays.asList(propertySourcesToAdd));
    }

    @Override
    public ConfigurationContextBuilder removePropertySources(Collection<String> propertySourcesToRemove) {
        for(String key: propertySourcesToRemove){
            this.propertySources.remove(key);
        }
        return this;
    }

    @Override
    public ConfigurationContextBuilder removePropertySources(String... propertySourcesToRemove) {
        return removePropertySources(Arrays.asList(propertySourcesToRemove));
    }

    @Override
    public ConfigurationContextBuilder addPropertyFilters(Collection<PropertyFilter> filters) {
        this.propertyFilters.addAll(filters);
        return this;
    }

    @Override
    public ConfigurationContextBuilder addPropertyFilters(PropertyFilter... filters) {
        return addPropertyFilters(Arrays.asList(filters));
    }

    @Override
    public ConfigurationContextBuilder removePropertyFilters(Collection<PropertyFilter> filters) {
        this.propertyFilters.removeAll(filters);
        return this;
    }

    @Override
    public ConfigurationContextBuilder removePropertyFilters(PropertyFilter... filters) {
        return removePropertyFilters(Arrays.asList(filters));
    }

    @Override
    public <T> ConfigurationContextBuilder addPropertyConverter(TypeLiteral<T> typeToConvert, PropertyConverter<T> propertyConverter) {
        List<PropertyConverter<?>> converters = this.propertyConverters.get(typeToConvert);
        if(converters==null){
            converters =  new ArrayList<>();
            this.propertyConverters.put(typeToConvert, converters);
        }
        return this;
    }

    @Override
    public ConfigurationContextBuilder removePropertyConverters(TypeLiteral<?> typeToConvert, PropertyConverter<?>... converters) {
        return removePropertyConverters(typeToConvert, Arrays.asList(converters));
    }

    @Override
    public ConfigurationContextBuilder removePropertyConverters(TypeLiteral<?> typeToConvert, Collection<PropertyConverter<?>> converters) {
        List<PropertyConverter<?>> existing = this.propertyConverters.get(typeToConvert);
        if(existing!=null) {
            existing.removeAll(converters);
        }
        return this;
    }

    @Override
    public ConfigurationContextBuilder setPropertyValueCombinationPolicy(PropertyValueCombinationPolicy policy) {
        this.combinationPolicy = Objects.requireNonNull(policy);
        return this;
    }

    @Override
    public ConfigurationContext build() {
        return new DefaultConfigurationContext(this);
    }

}
