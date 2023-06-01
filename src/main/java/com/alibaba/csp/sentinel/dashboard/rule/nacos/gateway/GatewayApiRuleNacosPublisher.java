package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * gateway api publisher
 *
 * @author max
 * @date 2023/06/01 11:06
 **/
@Component("gatewayApiRuleNacosPublisher")
public class GatewayApiRuleNacosPublisher implements DynamicRulePublisher<List<ApiDefinitionEntity>> {

    @Autowired
    private ConfigService configService;

    @Override
    public void publish(String app, List<ApiDefinitionEntity> rules) throws Exception {
        NacosConfigUtil.setRuleString2Nacos(
                this.configService,
                app,
                NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX,
                rules
        );
    }
}
