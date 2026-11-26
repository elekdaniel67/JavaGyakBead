package com.example.beadando.security;

import com.example.beadando.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/user");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/register",
                                "/login",
                                "/adatbazis",
                                "/kapcsolat",
                                "/diagram",
                                "/rest",
                                "/api/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/assets/**",
                                "/webfonts/**"
                        ).permitAll()
                        .requestMatchers("/user", "/uzenetek/**").hasAnyAuthority("ROLE_USER", "USER", "ROLE_ADMIN", "ADMIN")
                        .requestMatchers("/admin", "/crud/**").hasAnyAuthority("ROLE_ADMIN", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> response.sendRedirect("/login"),
                                request -> request.getRequestURI().equals("/user") || request.getRequestURI().startsWith("/uzenetek")
                        )
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> response.sendRedirect("/"),
                                request -> request.getRequestURI().equals("/admin") || request.getRequestURI().startsWith("/crud")
                        )
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> response.sendRedirect("/")
                        )
                );

        return http.build();
    }
}