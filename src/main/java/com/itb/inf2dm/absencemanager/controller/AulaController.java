package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.services.AulaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/aula")
@Tag(name = "Aula", description = "Gerenciamento de aulas")
public class AulaController {

    @Autowired
    private AulaService aulaService;

    @GetMapping
    @Operation(summary = "Listar todas as aulas")
    public ResponseEntity<List<Aula>> listar() {
        return ResponseEntity.ok(aulaService.findAll());
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova aula")
    public ResponseEntity<Aula> cadastrar(@RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaService.save(aula));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aula por ID")
    public ResponseEntity<Object> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(aulaService.findById(Integer.parseInt(id)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aula")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Aula aula) {
        try {
            return ResponseEntity.ok(aulaService.update(Integer.parseInt(id), aula));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir aula")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            aulaService.delete(Integer.parseInt(id));
            return ResponseEntity.ok(Map.of("status", 200, "message", "Aula excluída com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
