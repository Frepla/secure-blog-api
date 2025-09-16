package com.wigell.secureblogproject.security;

import com.wigell.secureblogproject.converters.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v2/count").hasAuthority("ROLE_blogclient_ADMIN")
                                .requestMatchers("/api/v2/newpost").hasAuthority("ROLE_blogclient_USER")
                                .requestMatchers("/api/v2/updatepost").hasAuthority("ROLE_blogclient_USER")
                                .requestMatchers("/api/v2/deletepost/**").hasAnyAuthority("ROLE_blogclient_USER", "ROLE_blogclient_ADMIN")
                                .requestMatchers("/api/v2/posts/**", "/api/v2/post/**").authenticated()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2
                                .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );
        return http.build();
    }
}