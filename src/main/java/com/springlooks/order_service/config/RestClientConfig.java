package com.springlooks.order_service.config;

import com.springlooks.order_service.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${client.inventory.url}")
    private String inventoryClientUrl;

    @Bean
    public InventoryClient inventoryClient() {
        RestClient inventoryClient = RestClient.builder()
                .baseUrl(inventoryClientUrl)
                .requestFactory(clientRequestFactory())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(inventoryClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(InventoryClient.class);
    }

    private ClientHttpRequestFactory clientRequestFactory() {
        return ClientHttpRequestFactoryBuilder.simple()
                .withCustomizer(factory -> factory.setConnectTimeout(Duration.ofSeconds(3)))
                .withCustomizer(factory -> factory.setReadTimeout(Duration.ofSeconds(3)))
                .build();
    }
}
