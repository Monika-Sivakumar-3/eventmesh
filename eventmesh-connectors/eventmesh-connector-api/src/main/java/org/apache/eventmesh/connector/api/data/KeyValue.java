/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.eventmesh.connector.api.data;

import java.util.Set;

/**
 * used for connector-record extension
 */
public interface KeyValue {

    KeyValue put(String key, boolean value);

    KeyValue put(String key, short value);

    KeyValue put(String key, int value);

    KeyValue put(String key, long value);

    KeyValue put(String key, double value);

    KeyValue put(String key, String value);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    short getShort(String key);

    short getShort(String key, short defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    String getString(String key);

    String getString(String key, String defaultValue);

    Set<String> keySet();

    boolean containsKey(String key);
}