package com.alibaba.csp.sentinel.dashboard.rule.nacos.auth;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth publisher
 *
 * @author max
 * @date 2023/06/01 11:06
 **/
@Component("authorityRuleNacosPublisher")
public class AuthorityRuleNacosPublisher implements DynamicRulePublisher<List<AuthorityRuleEntity>> {

    @Autowired
    private ConfigService configService;

    @Override
    public void publish(String app, List<AuthorityRuleEntity> rules) throws Exception {
        NacosConfigUtil.setRuleString2Nacos(
                this.configService,
                app,
                NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX,
                rules
        );
    }
}
