package de.unibamberg.dsam.group6.prost.configuration;

import de.unibamberg.dsam.group6.prost.service.UserDetailSecurityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailSecurityService detailsService;

    @Value("${spring.profiles.active}")
    private final List<String> activeProfiles;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> {
                    req.antMatchers("/cart").authenticated();
                    if (!this.activeProfiles.contains("dev")) {
                        req.antMatchers("/admin").hasRole("ADMIN");
                    }
                    req.antMatchers("/**").permitAll();
                })
                .formLogin(form -> {
                    form.loginPage("/login").permitAll();
                })
                .logout(Customizer.withDefaults());

        // TODO: CSRF
        if (this.activeProfiles.contains("dev")) {
            http.headers(h -> {
                h.frameOptions().disable().httpStrictTransportSecurity().disable();
            });
            http.csrf().disable();
        }
        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.passwordEncoder());
        provider.setUserDetailsService(this.detailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
