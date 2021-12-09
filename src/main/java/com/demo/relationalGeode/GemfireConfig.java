package com.demo.relationalGeode;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.geode.config.annotation.EnableClusterAware;
import org.springframework.geode.config.annotation.UseMemberName;

@Configuration
@EnableCachingDefinedRegions
@EnableEntityDefinedRegions(basePackageClasses = {SomeObject.class})
@EnableGemfireRepositories(basePackageClasses = {SomeObjectRepository.class})
@UseMemberName("RelationalGeode")
@SuppressWarnings("unused")
public class GemfireConfig {

    @Configuration
    @EnableClusterAware
    @Profile("client-cache")
    static class ClientCacheGemfireConfig { }

    @Configuration
    @PeerCacheApplication
    @Profile("peer-cache")
    static class PeerCacheGemfireConfig { }

    @Bean
    @ConditionalOnProperty(prefix = "geode", name = "host")
    public ClientCacheConfigurer clientCacheConfigurer(@Value("${geode.host}") String host, @Value("${geode.port}") Integer port) {
        return ((beanName, bean) -> bean.setServers(Collections.singletonList(new ConnectionEndpoint(host, port))));
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> initializeCache(SomeObjectRepository repository) {
        return event -> repository.save(SomeObject.builder()
            .someId("id1")
            .build());
    }
}
