package com.starter.SpringStarter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.starter.SpringStarter.util.constants.Privileges;

import static org.springframework.security.config.Customizer.withDefaults;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    // Paths that are accessible without authentication
    private static final String[] WHITELIST = {
            "/",
            "/login",
            "/register",
            "/forgot-password",
            "/change-password",
            "/reset-password",
            "/db-console/**", // H2 console
            "/css/**",
            "/fonts/**",
            "/images/**",
            "/js/**"
    };

    // Define the PasswordEncoder bean
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure the security filter chain
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> requests
                        .requestMatchers(WHITELIST).permitAll() // Allow whitelisted paths
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
                        .requestMatchers("/admin/**").hasAuthority(Privileges.ACCESS_ADMIN_PANEL.getPrivilege())
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/login") // Form action URL
                        .usernameParameter("email") // Username field
                        .passwordParameter("password") // Password field
                        .defaultSuccessUrl("/", true) // Redirect to homepage on success
                        .failureUrl("/login?error") // Redirect to login page on failure
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // Custom logout URL
                        .logoutSuccessUrl("/") // Redirect on logout success
                        .permitAll())
                .rememberMe(me -> me.rememberMeParameter("remember-me"))
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity, enable in production
                .headers(headers -> headers.frameOptions().disable()); // Disable frame options for H2 console access

        return http.build();
    }
}
