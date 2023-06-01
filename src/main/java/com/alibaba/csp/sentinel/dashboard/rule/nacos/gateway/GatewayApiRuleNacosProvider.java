package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * gateway api provider
 *
 * @author max
 * @date 2023/06/01 11:05
 **/
@Component("gatewayApiRuleNacosProvider")
public class GatewayApiRuleNacosProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {
    @Autowired
    private ConfigService configService;

    @Override
    public List<ApiDefinitionEntity> getRules(String appName) throws Exception {
        return NacosConfigUtil.getRuleEntities4Nacos(
                this.configService,
                appName,
                NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX,
                ApiDefinitionEntity.class
        );
    }
}
