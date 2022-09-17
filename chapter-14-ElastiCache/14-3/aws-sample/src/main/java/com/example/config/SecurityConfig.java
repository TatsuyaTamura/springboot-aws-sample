package com.example.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig{

    /** セキュリティの対象外を設定 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        // ログイン設定
        http
            .formLogin(login -> login
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll());

        // ログアウト設定
        http
            .logout(logout -> logout
                .logoutSuccessUrl("/"));

        // セキュリティ対象外
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()).permitAll()
                .mvcMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
        );
        
        return http.build();
    }
}
