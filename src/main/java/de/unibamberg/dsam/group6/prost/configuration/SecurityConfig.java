package de.unibamberg.dsam.group6.prost.configuration;

import de.unibamberg.dsam.group6.prost.service.UserDetailSecurityService;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
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

    private final UserErrorManager errors;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> {
                    req.antMatchers("POST").authenticated();
                    req.antMatchers("/cart/**").authenticated();
                    req.antMatchers("/orders/**").authenticated();
                    req.antMatchers("/user/**").authenticated();

                    if (this.activeProfiles.contains("dev")) {
                        req.antMatchers("/h2-console/**").permitAll();
                    } else {
                        req.antMatchers("/admin/**").hasRole("ADMIN");
                    }
                    req.anyRequest().permitAll();
                })
                .formLogin(form -> {
                    form.loginPage("/login").permitAll();
                    form.failureHandler((req, res, e) -> {
                        this.errors.addToast(Toast.error(e.getMessage()));
                        res.sendRedirect("/login");
                    });
                })
                .logout(t -> t.logoutUrl("/logout").permitAll());

        if (this.activeProfiles.contains("dev")) {
            http.csrf().ignoringAntMatchers("/h2-console/**");
            http.headers(h -> {
                h.frameOptions().disable().httpStrictTransportSecurity().disable();
            });
        } else if (this.activeProfiles.contains("prod")) {
            http.csrf();
            http.headers(h -> {
                h.httpStrictTransportSecurity();
                h.frameOptions().sameOrigin();
            });
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
