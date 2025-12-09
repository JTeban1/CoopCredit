package com.riwi.coopcredit.infrastructure.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public Counter authenticationFailureCounter(MeterRegistry registry) {
        return Counter.builder("authentication.failures")
            .description("Number of failed authentication attempts")
            .register(registry);
    }

    @Bean
    public Counter creditApplicationCounter(MeterRegistry registry) {
        return Counter.builder("credit.applications.evaluated")
            .description("Number of credit applications evaluated")
            .register(registry);
    }
}
