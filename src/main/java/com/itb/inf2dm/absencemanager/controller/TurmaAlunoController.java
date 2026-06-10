package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.services.TurmaAlunoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turma-alunos")
public class TurmaAlunoController {

    @Autowired
    private TurmaAlunoService turmaAlunoService;

    @GetMapping
    public ResponseEntity<List<TurmaAluno>> listar() {
        return ResponseEntity.ok(turmaAlunoService.findAll());
    }

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<TurmaAluno>> listarPorTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok(turmaAlunoService.findByTurmaId(turmaId));
    }

    @GetMapping("/aluno/{alunoRm}")
    public ResponseEntity<List<TurmaAluno>> listarPorAluno(@PathVariable Integer alunoRm) {
        return ResponseEntity.ok(turmaAlunoService.findByAlunoRm(alunoRm));
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody TurmaAluno turmaAluno) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaAlunoService.create(turmaAluno));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        try {
            turmaAlunoService.delete(id);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Vinculo removido com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
