package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.repository")
@EntityScan(basePackages = "com.epam.esm.model")
public class RepositoryConfig {

}
