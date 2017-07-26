package io.thingcare.bootstrap.be.config;

import io.thingcare.bootstrap.BootstrapApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EntityScan(basePackageClasses = {BootstrapApplication.class, Jsr310JpaConverters.class})
@EnableJpaAuditing
public class PersistanceConfiguration {
}
