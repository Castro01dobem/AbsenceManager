package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.dto.ChamadaDetalhesResponseDTO;
import com.itb.inf2dm.absencemanager.dto.ConfirmarPresencaRequestDTO;
import com.itb.inf2dm.absencemanager.dto.CriarChamadaResponseDTO;
import com.itb.inf2dm.absencemanager.dto.SolicitarCodigoPresencaRequestDTO;
import com.itb.inf2dm.absencemanager.services.ChamadaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChamadaController {

    private static final Logger log = LoggerFactory.getLogger(ChamadaController.class);

    private final ChamadaService chamadaService;

    public ChamadaController(ChamadaService chamadaService) {
        this.chamadaService = chamadaService;
    }

    @PostMapping({"/api/turmas/{turmaId}/chamadas", "/turmas/{turmaId}/chamadas"})
    public ResponseEntity<CriarChamadaResponseDTO> criarChamada(@PathVariable Long turmaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chamadaService.criarChamada(turmaId));
    }

    @GetMapping({"/api/turmas/{turmaId}/chamadas/{chamadaId}", "/turmas/{turmaId}/chamadas/{chamadaId}"})
    public ResponseEntity<ChamadaDetalhesResponseDTO> buscarChamada(@PathVariable Long turmaId,
            @PathVariable Long chamadaId) {
        return ResponseEntity.ok(chamadaService.buscarDetalhes(turmaId, chamadaId));
    }

    @GetMapping({"/api/turmas/{turmaId}/chamadas", "/turmas/{turmaId}/chamadas"})
    public ResponseEntity<List<ChamadaDetalhesResponseDTO>> listarChamadasRecentes(@PathVariable Long turmaId) {
        return ResponseEntity.ok(chamadaService.listarRecentes(turmaId));
    }

    @PostMapping({"/api/turmas/{turmaId}/chamadas/{chamadaId}/confirmar",
            "/turmas/{turmaId}/chamadas/{chamadaId}/confirmar"})
    public ResponseEntity<ChamadaDetalhesResponseDTO> confirmarChamada(@PathVariable Long turmaId,
            @PathVariable Long chamadaId) {
        return ResponseEntity.ok(chamadaService.confirmarChamada(turmaId, chamadaId));
    }

    @PostMapping({"/api/chamadas/solicitar-codigo", "/chamadas/solicitar-codigo"})
    public ResponseEntity<Map<String, Object>> solicitarCodigoPresenca(
            @Valid @RequestBody SolicitarCodigoPresencaRequestDTO request) {
        try {
            chamadaService.solicitarCodigoPresenca(request);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Codigo enviado para o e-mail informado."));
        } catch (IllegalStateException e) {
            log.error("Falha ao enviar codigo de confirmacao de presenca", e);
            String causa = causaRaiz(e).getMessage();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "status", 503,
                            "message", "Nao foi possivel enviar o e-mail agora. Tente novamente em instantes.",
                            "detalhe", causa == null ? e.getClass().getSimpleName() : causa
                    ));
        }
    }

    @PostMapping({"/api/chamadas/confirmar-presenca", "/chamadas/confirmar-presenca"})
    public ResponseEntity<ChamadaDetalhesResponseDTO> confirmarPresenca(
            @Valid @RequestBody ConfirmarPresencaRequestDTO request) {
        return ResponseEntity.ok(chamadaService.confirmarPresenca(request));
    }

    private Throwable causaRaiz(Throwable e) {
        Throwable atual = e;
        while (atual.getCause() != null && atual.getCause() != atual) {
            atual = atual.getCause();
        }
        return atual;
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception e) {
        String message = e instanceof MethodArgumentNotValidException
                ? "Token, email e codigo sao obrigatorios."
                : e.getMessage();
        return ResponseEntity.badRequest().body(Map.of("status", 400, "message", message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", e.getMessage()));
    }
}
