package server_config;

import config.JpaConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Import(JpaConfig.class)
@PropertySources(
        @PropertySource("classpath:application.properties")
)
@ComponentScan(basePackages = {"services", "domain", "repositories", "bootstrap"})
public class AppLocalConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
