package com.wigell.secureblogproject.security;

import com.wigell.secureblogproject.converters.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(JwtAuthConverter jwtAuthConverter,  CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v2/count").hasRole("blogclient_ADMIN")
                                .requestMatchers("/api/v2/newpost").hasRole("blogclient_USER")
                                .requestMatchers("/api/v2/updatepost").hasRole("blogclient_USER")
                                .requestMatchers("/api/v2/deletepost/**").hasAnyRole("blogclient_USER", "blogclient_ADMIN")
                                .requestMatchers("/api/v2/posts/**", "/api/v2/post/**").authenticated()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().denyAll()
                )
                .exceptionHandling(eh -> eh.accessDeniedHandler(customAccessDeniedHandler))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );
        return http.build();
    }
}
