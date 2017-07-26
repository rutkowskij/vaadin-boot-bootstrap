package io.thingcare.bootstrap.fe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

@Configuration
public class MessageBundleConfiguration {

    @Bean
    MessageProvider messageProvider() {
        return new ResourceBundleMessageProvider("io.thingcare.bootstrap.i18n.messages");
    }
}
