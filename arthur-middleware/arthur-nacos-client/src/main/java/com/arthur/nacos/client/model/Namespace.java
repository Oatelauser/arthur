/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthur.nacos.client.model;

import com.arthur.nacos.client.constant.ChangeType;

import java.util.Objects;

/**
 * <ul>拓展{@code com.alibaba.nacos.console.model.Namespace}
 * <li>新增hash equal</li>
 * <li>新增命名空间变动状态枚举{@link #changeType}</li>
 * </ul>
 */
public class Namespace {

    private String namespace;

    private String namespaceShowName;

    private String namespaceDesc;

    private int quota;

    private int configCount;

    /**
     * 如果为null，则说明是正常状态
     *
     * @see ChangeType
     */
    private ChangeType changeType;

    /**
     * @see com.arthur.nacos.client.constant.NamespaceTypeEnum
     */
    private int type;

    public String getNamespaceShowName() {
        return namespaceShowName;
    }

    public void setNamespaceShowName(String namespaceShowName) {
        this.namespaceShowName = namespaceShowName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Namespace() {
    }

    public Namespace(String namespace, String namespaceShowName) {
        this.namespace = namespace;
        this.namespaceShowName = namespaceShowName;
    }

    public Namespace(String namespace, String namespaceShowName, int quota, int configCount, int type) {
        this.namespace = namespace;
        this.namespaceShowName = namespaceShowName;
        this.quota = quota;
        this.configCount = configCount;
        this.type = type;
    }

    public Namespace(String namespace, String namespaceShowName, String namespaceDesc, int quota, int configCount, int type) {
        this.namespace = namespace;
        this.namespaceShowName = namespaceShowName;
        this.quota = quota;
        this.configCount = configCount;
        this.type = type;
        this.namespaceDesc = namespaceDesc;
    }

    public String getNamespaceDesc() {
        return namespaceDesc;
    }

    public void setNamespaceDesc(String namespaceDesc) {
        this.namespaceDesc = namespaceDesc;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getConfigCount() {
        return configCount;
    }

    public void setConfigCount(int configCount) {
        this.configCount = configCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace namespace1 = (Namespace) o;
        return Objects.equals(namespace, namespace1.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace);
    }

}
