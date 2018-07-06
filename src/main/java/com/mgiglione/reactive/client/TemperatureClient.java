package com.mgiglione.reactive.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication(scanBasePackages = { "com.mgiglione" })
public class TemperatureClient {

    Logger logger = LoggerFactory.getLogger(TemperatureClient.class);

    @Bean
    WebClient getWebClient() {
        return WebClient.create("http://localhost:8081");
    }

    @Bean
    CommandLineRunner demo(WebClient client) {
        return args -> {
            client.get()
                .uri("/temperatures")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(Integer.class)
                .map(s -> String.valueOf(s))
                .subscribe(msg -> {
                    logger.info(msg);
                });
        };
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TemperatureClient.class).properties(java.util.Collections.singletonMap("server.port", "8081"))
            .run(args);
    }

}
