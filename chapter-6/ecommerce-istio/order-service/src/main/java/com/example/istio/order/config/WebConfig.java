package com.example.istio.order.config;  // or .inventory.config

import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {

    @Bean
    public TraceContextFilter traceContextFilter(OpenTelemetry openTelemetry) {
        return new TraceContextFilter(openTelemetry);
    }

    @Bean
    public FilterRegistrationBean<TraceContextFilter> traceFilterRegistration(TraceContextFilter filter) {
        FilterRegistrationBean<TraceContextFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(filter);
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        // Create new instance of our filter
        registrationBean.setFilter(new RequestLoggingFilter());

        // Set filter order to ensure it runs first
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        // Apply filter to all URLs
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}