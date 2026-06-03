package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.dto.PresencaRequest;
import com.itb.inf2dm.absencemanager.model.entity.Presenca;
import com.itb.inf2dm.absencemanager.services.PresencaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/presenca")
@Tag(name = "Presença", description = "Registro de presença dos alunos nas aulas")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    @PostMapping("/registrar")
    public String registrar(@RequestBody PresencaRequest request) {

        presencaService.registrarPresenca(
                request.getAlunoRm(),
                request.getAulaId(),
                request.getToken()
        );

        return "Presença registrada com sucesso";
    }

    @GetMapping
    @Operation(summary = "Listar todas as presenças")
    public ResponseEntity<List<Presenca>> listar() {
        return ResponseEntity.ok(presencaService.findAll());
    }

    @PostMapping
    @Operation(summary = "Registrar presença")
    public ResponseEntity<Presenca> cadastrar(@RequestBody Presenca presenca) {
        return ResponseEntity.status(HttpStatus.CREATED).body(presencaService.save(presenca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar presença por ID")
    public ResponseEntity<Object> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(presencaService.findById(Integer.parseInt(id)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar presença")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Presenca presenca) {
        try {
            return ResponseEntity.ok(presencaService.update(Integer.parseInt(id), presenca));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir presença")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            presencaService.delete(Integer.parseInt(id));
            return ResponseEntity.ok(Map.of("status", 200, "message", "Presença excluída com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
