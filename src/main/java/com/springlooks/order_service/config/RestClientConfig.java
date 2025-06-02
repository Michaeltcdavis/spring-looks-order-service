package com.springlooks.order_service.config;

import com.springlooks.order_service.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${client.inventory.url}")
    private String inventoryClientUrl;

    @Bean
    public InventoryClient inventoryClient() {
        RestClient inventoryClient = RestClient.builder()
                .baseUrl(inventoryClientUrl)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(inventoryClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(InventoryClient.class);
    }
}
