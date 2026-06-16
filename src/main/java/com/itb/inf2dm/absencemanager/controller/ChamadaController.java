package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.dto.ChamadaDetalhesResponseDTO;
import com.itb.inf2dm.absencemanager.dto.ConfirmarPresencaRequestDTO;
import com.itb.inf2dm.absencemanager.dto.CriarChamadaResponseDTO;
import com.itb.inf2dm.absencemanager.services.ChamadaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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

    @PostMapping({"/api/chamadas/confirmar-presenca", "/chamadas/confirmar-presenca"})
    public ResponseEntity<ChamadaDetalhesResponseDTO> confirmarPresenca(
            @Valid @RequestBody ConfirmarPresencaRequestDTO request) {
        return ResponseEntity.ok(chamadaService.confirmarPresenca(request));
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception e) {
        String message = e instanceof MethodArgumentNotValidException
                ? "Token e email sao obrigatorios."
                : e.getMessage();
        return ResponseEntity.badRequest().body(Map.of("status", 400, "message", message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", e.getMessage()));
    }
}
