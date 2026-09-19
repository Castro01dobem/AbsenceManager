package com.itb.inf2dm.absencemanager.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Canal alternativo de identificacao de sessao para quando o cookie de
 * sessao nao sobrevive entre dominios diferentes (frontend e backend em
 * hosts separados). Nao substitui a sessao HTTP existente: apenas guarda,
 * em memoria, qual usuario corresponde a qual token opaco, para que o
 * frontend possa reenviar essa identidade via header Authorization quando
 * o cookie nao for aceito pelo navegador.
 */
@Service
public class TokenAuthService {

    private final Map<String, String> tokensPorUsuario = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public String criarToken(String username) {
        String token;
        do {
            byte[] bytes = new byte[32];
            secureRandom.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } while (tokensPorUsuario.containsKey(token));

        tokensPorUsuario.put(token, username);
        return token;
    }

    public String resolverUsername(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return tokensPorUsuario.get(token);
    }

    public void invalidar(String token) {
        if (token != null) {
            tokensPorUsuario.remove(token);
        }
    }
}
