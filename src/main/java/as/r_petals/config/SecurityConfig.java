package as.r_petals.config;

import as.r_petals.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF disabled because we are using JWT
                .csrf(csrf -> csrf.disable())

                // No session - JWT based authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth
                        // PUBLIC APIs

                        .requestMatchers(
                                "/",
                                "/api",
                                "/auth/**"
                        ).permitAll()

                        // ADMIN ONLY

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")


                        // USER / SHOPKEEPER / ADMIN

                        .requestMatchers(
                                "/users/**",
                                "/address/**"
                        )
                        .hasAnyRole(
                                "USER",
                                "SHOPKEEPER",
                                "ADMIN"
                        )

                        // SHOP REGISTRATION

                        .requestMatchers("/shop/register")
                        .hasAnyRole(
                                "USER",
                                "SHOPKEEPER"
                        )

                        // EVERYTHING ELSE

                        .anyRequest()
                        .authenticated()
                )

                // Disable form login
                .formLogin(form -> form.disable())

                // Disable HTTP Basic
                .httpBasic(basic -> basic.disable());


        // JWT filter
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}