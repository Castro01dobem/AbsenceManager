package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.dto.ChamadaDetalhesResponseDTO;
import com.itb.inf2dm.absencemanager.dto.CriarChamadaResponseDTO;
import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.services.ProfessorService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/turmas")
    public ResponseEntity<List<Turma>> listarTurmas(Authentication authentication) {
        return ResponseEntity.ok(professorService.listarTurmas(authentication));
    }

    @GetMapping("/turmas/{id}")
    public ResponseEntity<Turma> buscarTurma(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarTurma(authentication, id));
    }

    @GetMapping("/turmas/{id}/alunos")
    public ResponseEntity<List<TurmaAluno>> listarAlunos(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(professorService.listarAlunos(authentication, id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(Authentication authentication) {
        return ResponseEntity.ok(professorService.dashboard(authentication));
    }

    @GetMapping("/relatorios")
    public ResponseEntity<Map<String, Object>> relatorios(Authentication authentication) {
        return ResponseEntity.ok(professorService.relatorios(authentication));
    }

    @GetMapping("/turmas/{id}/relatorio")
    public ResponseEntity<Map<String, Object>> relatorioTurma(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(professorService.relatorioTurma(authentication, id));
    }

    @PostMapping("/turmas/{id}/chamadas")
    public ResponseEntity<CriarChamadaResponseDTO> criarChamada(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.criarChamada(authentication, id));
    }

    @GetMapping("/turmas/{id}/chamadas")
    public ResponseEntity<List<ChamadaDetalhesResponseDTO>> listarChamadas(Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(professorService.listarChamadas(authentication, id));
    }

    @GetMapping("/chamadas/{id}")
    public ResponseEntity<ChamadaDetalhesResponseDTO> buscarChamada(Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(professorService.buscarChamada(authentication, id));
    }

    @PutMapping("/chamadas/{id}/encerrar")
    public ResponseEntity<ChamadaDetalhesResponseDTO> encerrarChamada(Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(professorService.encerrarChamada(authentication, id));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("status", 403, "message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("status", 400, "message", e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", e.getMessage()));
    }
}
