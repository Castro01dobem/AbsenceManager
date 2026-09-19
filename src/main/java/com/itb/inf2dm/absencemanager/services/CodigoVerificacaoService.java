package com.itb.inf2dm.absencemanager.services;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Guarda em memoria (por usuario) o codigo de verificacao enviado por
 * e-mail para autorizar a troca de senha. Nao precisa de tabela nova: o
 * codigo e' efemero (expira em poucos minutos) e nao faz sentido persistir
 * no banco. Se a instancia reiniciar, o usuario so precisa pedir um novo
 * codigo - mesma degradacao aceitavel ja usada pelo token de sessao.
 */
@Service
public class CodigoVerificacaoService {

    private static final Duration VALIDADE = Duration.ofMinutes(10);

    private record CodigoInfo(String codigo, Instant expiraEm) {
    }

    private final Map<Long, CodigoInfo> codigosPorUsuario = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public String gerarCodigo(Long usuarioId) {
        String codigo = String.format("%06d", secureRandom.nextInt(1_000_000));
        codigosPorUsuario.put(usuarioId, new CodigoInfo(codigo, Instant.now().plus(VALIDADE)));
        return codigo;
    }

    public boolean validarCodigo(Long usuarioId, String codigoInformado) {
        CodigoInfo info = codigosPorUsuario.get(usuarioId);
        if (info == null || codigoInformado == null) {
            return false;
        }
        if (Instant.now().isAfter(info.expiraEm())) {
            codigosPorUsuario.remove(usuarioId);
            return false;
        }
        return info.codigo().equals(codigoInformado.trim());
    }

    public void invalidarCodigo(Long usuarioId) {
        codigosPorUsuario.remove(usuarioId);
    }
}
