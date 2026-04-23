package com.jeegit.framework.security.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.jeegit.common.core.result.R;
import com.jeegit.common.core.result.ResultCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/login", "/logout", "/register",
                        "/captcha/**",
                        "/swagger-ui/**", "/v3/api-docs/**", "/doc.html/**", "/webjars/**",
                        "/favicon.ico",
                        "/actuator/**")
                .setAuth(obj -> StpUtil.checkLogin())
                .setError(e -> R.fail(ResultCode.UNAUTHORIZED));
    }
}
