package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.services.RelatorioService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/health")
    public String health() {
        return "Relatorios OK";
    }

    @GetMapping("/geral")
    public ResponseEntity<Map<String, Object>> geral() {
        return ResponseEntity.ok(relatorioService.geral());
    }

    @GetMapping("/turmas")
    public ResponseEntity<List<Map<String, Object>>> listarTurmas() {
        return ResponseEntity.ok(relatorioService.listarTurmas());
    }

    @GetMapping("/turmas/{id}")
    public ResponseEntity<Map<String, Object>> turma(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.turma(id));
    }

    @GetMapping("/professores")
    public ResponseEntity<List<Map<String, Object>>> listarProfessores() {
        return ResponseEntity.ok(relatorioService.listarProfessores());
    }

    @GetMapping("/alunos-criticos")
    public ResponseEntity<List<Map<String, Object>>> alunosCriticos(
            @RequestParam(required = false) Double limite) {
        return ResponseEntity.ok(relatorioService.alunosCriticos(limite));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", e.getMessage()));
    }
}
