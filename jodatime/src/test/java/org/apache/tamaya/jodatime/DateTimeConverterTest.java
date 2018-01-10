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
package org.apache.tamaya.jodatime;

import org.apache.tamaya.base.convert.ConversionContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doCallRealMethod;

public class DateTimeConverterTest {
    /*
     * I am aware of the 'Parameterized tests' feature of JUnit but
     * decided not to use it. Oliver B. Fischer, 3th April 2015
     */
    private static DateTimeConverter converter = new DateTimeConverter();

    private static DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Test
    public void canConvertISO8601DateTimeSpecWithTimezoneOffset() {
        Object[][] inputResultPairs = {
             {"2007-08-31T16:47:01.123+00:00", FORMATTER.parseDateTime("2007-08-31T16:47:01.123+00:00")},
             {"2007-08-31T16:47:01.123+00", FORMATTER.parseDateTime("2007-08-31T16:47:01.123+00:00")},
             {"2007-08-31T16:47:01.123 UTC", FORMATTER.parseDateTime("2007-08-31T16:47:01.123+00:00")},
             {"2007-08-31T16:47:01.123UTC", FORMATTER.parseDateTime("2007-08-31T16:47:01.123+00:00")},

             {"2007-08-31T16:47:01+00:00", FORMATTER.parseDateTime("2007-08-31T16:47:01.0+00:00")},
             {"2007-08-31T16:47:01UTC", FORMATTER.parseDateTime("2007-08-31T16:47:01.0+00:00")},
             {"2007-08-31T16:47:01 UTC", FORMATTER.parseDateTime("2007-08-31T16:47:01.0+00:00")},

             {"2007-08-31T16:47+00:00", FORMATTER.parseDateTime("2007-08-31T16:47:00.0+00:00")},
             {"2007-08-31T16:47UTC", FORMATTER.parseDateTime("2007-08-31T16:47:00.0+00:00")},
             {"2007-08-31T16:47 UTC", FORMATTER.parseDateTime("2007-08-31T16:47:00.0+00:00")},

             {"2007-08-31T16+00:00", FORMATTER.parseDateTime("2007-08-31T16:00:00.0+00:00")},
             {"2007-08-31T16UTC", FORMATTER.parseDateTime("2007-08-31T16:00:00.0+00:00")},
             {"2007-08-31T16 UTC", FORMATTER.parseDateTime("2007-08-31T16:00:00.0+00:00")},

             // For testing the trimming of the overhanded input values
             {" 2007-08-31T16:47:01.123+00:00", FORMATTER.parseDateTime("2007-08-31T16:47:01.123+00:00")},
             {"2007-08-31T16:47:01+00:00 ", FORMATTER.parseDateTime("2007-08-31T16:47:01.0+00:00")},
        };

        for (Object[] pair : inputResultPairs) {
            DateTime date = converter.convert((String)pair[0]);

            assertThat("Converter failed to convert input value " + pair[0], date, notNullValue());
            assertThat(date.isEqual((DateTime)pair[1]), is(true));
        }
    }

    @Test
    public void invalidInputValuesResultInReturningNull() {
        String[] inputValues = {
             "00:00", "a", "-", "+ :00", "+00:"
        };

        for (String input : inputValues) {
            DateTime date = converter.convert(input);

            assertThat(date, nullValue());
        }
    }

    @Test
    public void allSupportedFormatsAreAddedToTheConversionContext() {
        ConversionContext context = new ConversionContext.Builder(null, DateTime.class).build();
        ConversionContext.setContext(context);

        converter.convert("2007-08-31T16+00:00");
        ConversionContext.reset();
        assertThat(context.getSupportedFormats(), hasSize(DateTimeConverter.PARSER_FORMATS.length));

        for (String format : DateTimeConverter.PARSER_FORMATS) {
            String expected = format + " (" + DateTimeConverter.class.getSimpleName() + ")";
            assertThat(context.getSupportedFormats(), hasItem(expected));
        }

    }
}
