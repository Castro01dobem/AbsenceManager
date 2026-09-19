package com.itb.inf2dm.absencemanager.config;

import com.itb.inf2dm.absencemanager.services.TokenAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Reconhece o header "Authorization: Bearer {token}" emitido no login e
 * restaura a autenticacao a partir dele, sem depender do cookie de sessao.
 * So atua quando a requisicao ainda nao chega autenticada (ex.: cookie
 * bloqueado pelo navegador por ser de terceiro); quando o cookie funciona
 * normalmente, o fluxo de sessao do Spring Security segue como sempre.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIXO = "Bearer ";

    private final TokenAuthService tokenAuthService;
    private final UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(TokenAuthService tokenAuthService, UserDetailsService userDetailsService) {
        this.tokenAuthService = tokenAuthService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication existente = SecurityContextHolder.getContext().getAuthentication();
        boolean jaAutenticado = existente != null && existente.isAuthenticated()
                && !(existente instanceof AnonymousAuthenticationToken);

        if (!jaAutenticado) {
            String token = extrairToken(request);
            String username = tokenAuthService.resolverUsername(token);

            if (username != null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacao);
                } catch (RuntimeException ignored) {
                    // Token nao corresponde mais a um usuario valido; segue como anonimo.
                }
            }
        }

        chain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIXO)) {
            return header.substring(PREFIXO.length());
        }
        return null;
    }
}
