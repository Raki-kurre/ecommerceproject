package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                /* PUBLIC */
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/shop/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/videos/**",
                    "/productImages/**",
                    "/profile-images/**",
                    "/uploads/**"
                ).permitAll()

                /* ADMIN ONLY */
                .requestMatchers("/admin/**").hasRole("ADMIN")

                /* USER ONLY */
                .requestMatchers(
                    "/profile/**",
                    "/cart/**",
                    "/checkout/**"
                ).authenticated()

                /* EVERYTHING ELSE */
                .anyRequest().permitAll()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}