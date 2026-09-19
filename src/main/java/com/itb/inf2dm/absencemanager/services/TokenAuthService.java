package com.itb.inf2dm.absencemanager.services;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

/**
 * Canal alternativo de identificacao de sessao para quando o cookie de
 * sessao nao sobrevive entre dominios diferentes (frontend e backend em
 * hosts separados). Nao substitui a sessao HTTP existente: gera um token
 * assinado (username + validade + assinatura HMAC) que o proprio backend
 * consegue validar sem guardar nada em memoria por token individual -
 * assim ele sobrevive a reinicios da instancia (comuns em planos free de
 * hospedagem, que derrubam o processo por inatividade). A chave de
 * assinatura e gerada uma vez por processo: se a instancia reiniciar, os
 * tokens antigos deixam de validar e o usuario simplesmente precisa logar
 * de novo, do mesmo jeito que ja acontecia com a sessao por cookie.
 */
@Service
public class TokenAuthService {

    private static final String ALGORITMO = "HmacSHA256";
    private static final Duration VALIDADE = Duration.ofHours(8);
    private static final String SEPARADOR = "|";

    private final SecretKeySpec chave;
    private final Set<String> tokensInvalidados = ConcurrentHashMap.newKeySet();

    public TokenAuthService() {
        byte[] segredo = new byte[32];
        new SecureRandom().nextBytes(segredo);
        this.chave = new SecretKeySpec(segredo, ALGORITMO);
    }

    public String criarToken(String username) {
        long expiraEm = System.currentTimeMillis() + VALIDADE.toMillis();
        String payload = username + SEPARADOR + expiraEm;
        String assinatura = assinar(payload);
        String tokenBruto = payload + SEPARADOR + assinatura;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBruto.getBytes(StandardCharsets.UTF_8));
    }

    public String resolverUsername(String token) {
        if (token == null || token.isBlank() || tokensInvalidados.contains(token)) {
            return null;
        }

        try {
            String decodificado = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] partes = decodificado.split("\\" + SEPARADOR, 3);
            if (partes.length != 3) {
                return null;
            }

            String username = partes[0];
            long expiraEm = Long.parseLong(partes[1]);
            String assinaturaRecebida = partes[2];
            String payload = username + SEPARADOR + expiraEm;

            if (!assinar(payload).equals(assinaturaRecebida)) {
                return null;
            }
            if (System.currentTimeMillis() > expiraEm) {
                return null;
            }

            return username;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public void invalidar(String token) {
        if (token != null) {
            tokensInvalidados.add(token);
        }
    }

    private String assinar(String payload) {
        try {
            Mac mac = Mac.getInstance(ALGORITMO);
            mac.init(chave);
            byte[] resultado = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(resultado);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao assinar token de autenticacao.", e);
        }
    }
}
