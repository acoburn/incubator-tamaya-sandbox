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
package org.apache.tamaya.hjson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link PathBasedHJSONPropertySourceProvider}.
 */
public class PathBasedHJSONPropertySourceProviderTest {

    @Test
    public void getPropertySources() {
        PathBasedHJSONPropertySourceProvider provider = new PathBasedHJSONPropertySourceProvider(
                "configs/valid/*.hjson"
        );
        assertNotNull(provider.getPropertySources());
        assertEquals(7, provider.getPropertySources().size());
    }

    @Test
    public void getPropertySources_one() {
        PathBasedHJSONPropertySourceProvider provider = new PathBasedHJSONPropertySourceProvider(
                "configs/valid/cyril*.hjson"
        );
        assertNotNull(provider.getPropertySources());
        assertEquals(1, provider.getPropertySources().size());
    }

    @Test
    public void getPropertySources_two() {
        PathBasedHJSONPropertySourceProvider provider = new PathBasedHJSONPropertySourceProvider(
                "configs/valid/simple-*.hjson"
        );
        assertNotNull(provider.getPropertySources());
        assertEquals(3, provider.getPropertySources().size());
    }

    @Test
    public void getPropertySources_none() {
        PathBasedHJSONPropertySourceProvider provider = new PathBasedHJSONPropertySourceProvider(
                "configs/valid/foo*.hjson", "configs/valid/*.HJSON"
        );
        assertNotNull(provider.getPropertySources());
        assertEquals(0, provider.getPropertySources().size());
    }
}