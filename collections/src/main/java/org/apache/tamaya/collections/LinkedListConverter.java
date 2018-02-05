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

import javax.config.spi.Converter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  PropertyConverter for gnerating LinkedList representation of a values.
 */
public class LinkedListConverter implements Converter<LinkedList> {
    private static final Logger LOG = Logger.getLogger(LinkedListConverter.class.getName());

    /** The shared instance, used by other collection converters in this package.*/
    private static final LinkedListConverter INSTANCE = new LinkedListConverter();

    /**
     * Provide a shared instance, used by other collection converters in this package.
     * @return the shared instance, never null.
     */
    static LinkedListConverter getInstance(){
        return INSTANCE;
    }

    @Override
    public LinkedList convert(String value) {
        List<String> rawList = ItemTokenizer.split(value);
        LinkedList<Object> result = new LinkedList<>();
        for(String raw:rawList){
            String[] items = ItemTokenizer.splitMapEntry(raw);
            Object convValue = ItemTokenizer.convertValue(items[1]);
            if(convValue!=null){
                result.add(convValue);
            }else{
                LOG.log(Level.SEVERE, "Failed to convert collection value type for '"+raw+"'.");
            }
        }
        return result;
    }

}
