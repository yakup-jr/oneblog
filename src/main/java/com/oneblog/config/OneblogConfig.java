package com.oneblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
@Configuration
public class OneblogConfig {}
