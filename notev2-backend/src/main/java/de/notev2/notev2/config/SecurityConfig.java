package de.notev2.notev2.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    
   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
            ).permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter,
             org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsfilter() {

        //corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);

    }
}
//test2:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MiIsImlhdCI6MTc3NzI5NTMwMCwiZXhwIjoxNzc3MzgxNzAwfQ.x5p9B1cD20M8FiW81c8XglaazxUbsUvdyiPB4Ygw3Ic
//test:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzc3Mjk0NzAzLCJleHAiOjE3NzczODExMDN9.AfwhPdsALtz64-xkend3fiRgiNUTUdm86C4YC0TrqNs
