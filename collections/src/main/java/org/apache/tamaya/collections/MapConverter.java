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
package org.apache.tamaya.collections;

import org.apache.tamaya.meta.MetaProperties;

import javax.config.spi.Converter;
import java.util.Collections;
import java.util.Map;

/**
 *  PropertyConverter for gnerating HashMap representation of a values.
 */
public class MapConverter implements Converter<Map> {

    @Override
    public Map convert(String value) {
        String collectionType = MetaProperties.getOptionalMetaEntry(
                ItemTokenizer.config(),
                ItemTokenizer.key(),
                "collection-type").orElse("Map");
        if(collectionType.startsWith("java.util.")){
            collectionType = collectionType.substring("java.util.".length());
        }
        Map result = null;
        switch(collectionType){
            case "TreeMap":
                result = TreeMapConverter.getInstance().convert(value);
                break;
            case "ConcurrentHashMap":
                result = ConcurrentHashMapConverter.getInstance().convert(value);
                break;
            case "Map":
            case "HashMap":
            default:
                result = HashMapConverter.getInstance().convert(value);
                break;
        }
        if(MetaProperties.getOptionalMetaEntry(
                ItemTokenizer.config(),
                ItemTokenizer.key(),
                "read-only", boolean.class).orElse(true)){
            return Collections.unmodifiableMap(result);
        }
        return result;
    }
}
