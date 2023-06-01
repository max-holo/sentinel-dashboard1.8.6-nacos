package com.alibaba.csp.sentinel.dashboard.rule.nacos.system;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * system provider
 *
 * @author max
 * @date 2023/06/01 11:21
 **/
@Component("systemRuleNacosProvider")
public class SystemRuleNacosProvider implements DynamicRuleProvider<List<SystemRuleEntity>> {

    @Autowired
    private ConfigService configService;

    @Override
    public List<SystemRuleEntity> getRules(String appName) throws Exception {
        return NacosConfigUtil.getRuleEntities4Nacos(
                configService,
                appName,
                NacosConfigUtil.SYSTEM_FULE_DATA_ID_POSTFIX,
                SystemRuleEntity.class
        );
    }
}
