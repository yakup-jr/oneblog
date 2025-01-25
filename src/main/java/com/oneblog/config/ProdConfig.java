package com.oneblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@Import({OneblogConfig.class})
public class ProdConfig {}
