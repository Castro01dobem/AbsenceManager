package com.itb.inf2dm.absencemanager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // ================= CSRF / CORS =================
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())

            // ================= AUTORIZAÇÃO =================
            .authorizeHttpRequests(auth -> auth
                // Público
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/mensagens/enviar").permitAll()
                .requestMatchers("/notificacoes/**").permitAll()
                // Qualquer outra rota
                // (dev) permitir acesso sem autenticação para ligar frontend↔backend
                .anyRequest().permitAll()
            )

            // ================= LOGIN =================
            .formLogin(form -> form
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                          "message": "Login realizado com sucesso"
                        }
                    """);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                          "error": "Usuário ou senha inválidos"
                        }
                    """);
                })
            )

            // ================= LOGOUT =================
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                          "message": "Logout realizado com sucesso"
                        }
                    """);
                })
            );

        return http.build();
    }

    // ================= PASSWORD ENCODER =================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}