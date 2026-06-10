package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.services.TurmaService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<List<Turma>> listar() {
        return ResponseEntity.ok(turmaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody Turma turma) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaService.create(turma));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(turmaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody Turma turma) {
        try {
            return ResponseEntity.ok(turmaService.update(id, turma));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/professor/{professorId}")
    public ResponseEntity<Object> designarProfessor(@PathVariable Long id, @PathVariable Long professorId) {
        try {
            return ResponseEntity.ok(turmaService.designarProfessor(id, professorId));
        } catch (IllegalStateException | IllegalArgumentException e) {
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
            turmaService.delete(id);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Turma excluida com sucesso!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
