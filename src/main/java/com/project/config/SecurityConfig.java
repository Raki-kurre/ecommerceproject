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
	                "/profile-images/**"
	            ).permitAll()

	            /* ADMIN ONLY */
	            .requestMatchers("/admin/**")
	            .hasRole("ADMIN")

	            /* AUTHENTICATED USER */
	            .requestMatchers(
	                "/profile/**",
	                "/cart/**",
	                "/checkout/**"
	            ).authenticated()

	            .anyRequest().authenticated()
	        )

	        .formLogin(form -> form
	            .loginPage("/login")
	            .usernameParameter("email")
	            .passwordParameter("password")
	            .defaultSuccessUrl("/", true)
	            .permitAll()
	        )

	        /* 🔴 CUSTOM ACCESS DENIED HANDLER */
	        .exceptionHandling(ex -> ex
	            .accessDeniedPage("/access-denied")
	        )

	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout=true")
	            .permitAll()
	        );

	    return http.build();
	}
}
