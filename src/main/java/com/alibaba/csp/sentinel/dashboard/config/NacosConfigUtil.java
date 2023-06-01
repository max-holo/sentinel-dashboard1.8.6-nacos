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
package com.alibaba.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.util.JSONUtils;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public final class NacosConfigUtil {

    public static final String GROUP_ID = "SENTINEL_GROUP";

    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";
    public static final String DEGRADE_DATA_ID_POSTFIX = "-degrade-rules";
    public static final String SYSTEM_FULE_DATA_ID_POSTFIX = "-system-rules";
    public static final String AUTHORITY_DATA_ID_POSTFIX = "-authority-rules";
    public static final String GATEWAY_FLOW_DATA_ID_POSTFIX = "-gateway-flow-rules";
    public static final String GATEWAY_API_DATA_ID_POSTFIX = "-gateway-api-rules";
    public static final String DASHBOARD_POSTFIX = "-dashboard";

    public static final String PARAM_FLOW_DATA_ID_POSTFIX = "-param-rules";
    public static final String CLUSTER_MAP_DATA_ID_POSTFIX = "-cluster-map";


    /**
     * cc for `cluster-client`
     */
    public static final String CLIENT_CONFIG_DATA_ID_POSTFIX = "-cc-config";
    /**
     * cs for `cluster-server`
     */
    public static final String SERVER_TRANSPORT_CONFIG_DATA_ID_POSTFIX = "-cs-transport-config";
    public static final String SERVER_FLOW_CONFIG_DATA_ID_POSTFIX = "-cs-flow-config";
    public static final String SERVER_NAMESPACE_SET_DATA_ID_POSTFIX = "-cs-namespace-set";

    private NacosConfigUtil() {}

    /**
     * 方法实现说明:从nacos服务上获取配置
     *
     * @param configService:nacos配置服务
     * @param appName                 应用微服务名称
     * @param postfix                 eg.NacosConfigUtil.FLOW_DATA_ID_POSTFIX
     * @param clazz:反序列化class类型
     */
    public static <T> List<T> getRuleEntities4Nacos(ConfigService configService, String appName, String postfix, Class<T> clazz) throws NacosException {
        //去nacos注册中心获取配置
        String rules = configService.getConfig(genDataId(appName, postfix) + DASHBOARD_POSTFIX, NacosConfigUtil.GROUP_ID, 3000);

        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return JSONUtils.parseObject(clazz, rules);
    }

    /**
     * 方法实现说明:存规则到nacos server上
     * @author:smlz
     * @param configService nacos配置服务
     * @param app:微服务名称
     * @param postfix: eg.NacosConfigUtil.FLOW_DATA_ID_POSTFIX
     * @param rules:规则列表
     */
    public static <T> void setRuleString2Nacos(ConfigService configService, String app, String postfix, List<T> rules) throws NacosException {
        AssertUtil.assertNotBlank(app,"app name not be empty");

        List<Rule> ruleList = new ArrayList<>();

        for(T t:rules) {
            RuleEntity ruleEntity = (RuleEntity) t;
            Rule realRule = ruleEntity.toRule();
            ruleList.add(realRule);
        }

        //生成数据ID
        String dataId = genDataId(app,postfix);

        //发布配置
        configService.publishConfig(
                dataId,
                NacosConfigUtil.GROUP_ID,
                JSONUtils.toJSONString(rules)
        );

        // 存储，给控制台使用
        configService.publishConfig(
                dataId + DASHBOARD_POSTFIX,
                NacosConfigUtil.GROUP_ID,
                JSONUtils.toJSONString(rules)
        );
    }

    /**
     * 方法实现说明:生成数据ID
     *
     * @param appName 微服务名称
     * @param postfix 规则后缀 eg.NacosConfigUtil.FLOW_DATA_ID_POSTFIX
     */
    private static String genDataId(String appName, String postfix) {
        return appName + postfix;
    }
}
