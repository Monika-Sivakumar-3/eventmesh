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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.eventmesh.registry.etcd.service;

import org.apache.eventmesh.api.exception.RegistryException;
import org.apache.eventmesh.api.registry.bo.EventMeshAppSubTopicInfo;
import org.apache.eventmesh.api.registry.bo.EventMeshServicePubTopicInfo;
import org.apache.eventmesh.common.Constants;
import org.apache.eventmesh.common.utils.JsonUtils;
import org.apache.eventmesh.registry.etcd.constant.EtcdConstant;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;

public class EtcdCustomService extends EtcdRegistryService {

    private static final String KEY_PREFIX = "eventMesh" + EtcdConstant.KEY_SEPARATOR;
    private static final String KEY_APP = "app";
    private static final String KEY_SERVICE = "service";
    private static final Logger logger = LoggerFactory.getLogger(EtcdCustomService.class);

    @Nullable
    public List<EventMeshServicePubTopicInfo> findEventMeshServicePubTopicInfos() throws RegistryException {

        Client client = getEtcdClient();
        String keyPrefix = KEY_PREFIX + KEY_SERVICE + EtcdConstant.KEY_SEPARATOR;
        List<KeyValue> keyValues = null;
        try {
            List<EventMeshServicePubTopicInfo> eventMeshServicePubTopicInfoList = new ArrayList<>();
            ByteSequence keyByteSequence = ByteSequence.from(keyPrefix.getBytes(Constants.DEFAULT_CHARSET));
            GetOption getOption = GetOption.newBuilder().withPrefix(keyByteSequence).build();
            keyValues = client.getKVClient().get(keyByteSequence, getOption).get().getKvs();


            if (CollectionUtils.isNotEmpty(keyValues)) {
                for (KeyValue kv : keyValues) {
                    EventMeshServicePubTopicInfo eventMeshServicePubTopicInfo =
                        JsonUtils.parseObject(new String(kv.getValue().getBytes(), Constants.DEFAULT_CHARSET), EventMeshServicePubTopicInfo.class);
                    eventMeshServicePubTopicInfoList.add(eventMeshServicePubTopicInfo);
                }
                return eventMeshServicePubTopicInfoList;
            }
        } catch (Exception e) {
            logger.error("[EtcdRegistryService][findEventMeshServicePubTopicInfos] error", e);
            throw new RegistryException(e.getMessage());
        }

        return null;
    }

    @Nullable
    public EventMeshAppSubTopicInfo findEventMeshAppSubTopicInfoByGroup(String group) throws RegistryException {
        Client client = getEtcdClient();
        String keyPrefix = KEY_PREFIX + KEY_APP + EtcdConstant.KEY_SEPARATOR + group;
        List<KeyValue> keyValues = null;
        try {
            ByteSequence keyByteSequence = ByteSequence.from(keyPrefix.getBytes(Constants.DEFAULT_CHARSET));
            GetOption getOption = GetOption.newBuilder().withPrefix(keyByteSequence).build();
            keyValues = client.getKVClient().get(keyByteSequence, getOption).get().getKvs();
            if (CollectionUtils.isNotEmpty(keyValues)) {
                EventMeshAppSubTopicInfo eventMeshAppSubTopicInfo =
                    JsonUtils.parseObject(
                        new String(keyValues.get(0).getValue().getBytes(), Constants.DEFAULT_CHARSET),
                        EventMeshAppSubTopicInfo.class
                    );
                return eventMeshAppSubTopicInfo;
            }
        } catch (Exception e) {
            logger.error("[EtcdRegistryService][findEventMeshAppSubTopicInfoByGroup] error, group: {}", group, e);
            throw new RegistryException(e.getMessage());
        }
        return null;
    }
}
