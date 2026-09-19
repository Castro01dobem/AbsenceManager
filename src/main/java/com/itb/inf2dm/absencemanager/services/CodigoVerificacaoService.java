package com.itb.inf2dm.absencemanager.services;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Guarda em memoria (por chave) o codigo de verificacao enviado por e-mail
 * para autorizar uma acao (troca de senha, confirmacao de presenca, etc).
 * Nao precisa de tabela nova: o codigo e' efemero (expira em poucos minutos)
 * e nao faz sentido persistir no banco. Se a instancia reiniciar, quem
 * pediu o codigo so precisa solicitar um novo - mesma degradacao aceitavel
 * ja usada pelo token de sessao.
 *
 * A chave e' uma String para permitir varios usos independentes (ex.:
 * "senha:42" para troca de senha do usuario 42, "presenca:7:1234" para a
 * chamada 7 confirmada pelo aluno de RM 1234) sem que um codigo sobrescreva
 * o outro.
 */
@Service
public class CodigoVerificacaoService {

    private static final Duration VALIDADE = Duration.ofMinutes(10);

    private record CodigoInfo(String codigo, Instant expiraEm) {
    }

    private final Map<String, CodigoInfo> codigosPorChave = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public String gerarCodigo(String chave) {
        String codigo = String.format("%06d", secureRandom.nextInt(1_000_000));
        codigosPorChave.put(chave, new CodigoInfo(codigo, Instant.now().plus(VALIDADE)));
        return codigo;
    }

    public boolean validarCodigo(String chave, String codigoInformado) {
        CodigoInfo info = codigosPorChave.get(chave);
        if (info == null || codigoInformado == null) {
            return false;
        }
        if (Instant.now().isAfter(info.expiraEm())) {
            codigosPorChave.remove(chave);
            return false;
        }
        return info.codigo().equals(codigoInformado.trim());
    }

    public void invalidarCodigo(String chave) {
        codigosPorChave.remove(chave);
    }

    /* ===== Atalhos mantidos para o fluxo de troca de senha (chave = usuarioId) ===== */

    public String gerarCodigo(Long usuarioId) {
        return gerarCodigo(chaveUsuario(usuarioId));
    }

    public boolean validarCodigo(Long usuarioId, String codigoInformado) {
        return validarCodigo(chaveUsuario(usuarioId), codigoInformado);
    }

    public void invalidarCodigo(Long usuarioId) {
        invalidarCodigo(chaveUsuario(usuarioId));
    }

    private String chaveUsuario(Long usuarioId) {
        return "senha:" + usuarioId;
    }
}
