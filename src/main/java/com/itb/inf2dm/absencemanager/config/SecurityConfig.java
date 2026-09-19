package com.itb.inf2dm.absencemanager.config;


import com.itb.inf2dm.absencemanager.services.TokenAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource,
            TokenAuthService tokenAuthService, UserDetailsService userDetailsService)
            throws Exception {

        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenAuthService,
                userDetailsService);

        http
            // ================= CSRF / CORS =================
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // ================= AUTORIZAÇÃO =================
            .authorizeHttpRequests(auth -> auth
                // Público
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/mensagens/enviar").permitAll()
                .requestMatchers("/notificacoes/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/v1/aluno/*").hasRole("ADMIN")
                // Qualquer outra rota
                // (dev) permitir acesso sem autenticação para ligar frontend↔backend
                .anyRequest().permitAll()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":401,\"message\":\"Sessao invalida ou expirada. Faca login novamente.\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":403,\"message\":\"Usuario sem permissao para esta operacao.\"}");
                })
            )

            // ================= LOGIN =================
            .formLogin(form -> form
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    String token = tokenAuthService.criarToken(authentication.getName());
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                          "message": "Login realizado com sucesso",
                          "token": "%s"
                        }
                    """.formatted(token));
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
                    String header = request.getHeader("Authorization");
                    if (header != null && header.startsWith("Bearer ")) {
                        tokenAuthService.invalidar(header.substring("Bearer ".length()));
                    }
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
