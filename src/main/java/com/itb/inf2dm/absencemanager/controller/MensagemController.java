package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Mensagem;
import com.itb.inf2dm.absencemanager.model.services.MensagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mensagem")
@Tag(name = "Mensagem", description = "Gerenciamento de mensagens de contato")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @GetMapping
    @Operation(summary = "Listar todas as mensagens")
    public ResponseEntity<List<Mensagem>> listar() {
        return ResponseEntity.ok(mensagemService.findAll());
    }

    @PostMapping
    @Operation(summary = "Enviar nova mensagem")
    public ResponseEntity<Mensagem> cadastrar(@RequestBody Mensagem mensagem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemService.save(mensagem));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID")
    public ResponseEntity<Object> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(mensagemService.findById(Integer.parseInt(id)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mensagem")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody Mensagem mensagem) {
        try {
            return ResponseEntity.ok(mensagemService.update(Integer.parseInt(id), mensagem));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir mensagem")
    public ResponseEntity<Object> deletar(@PathVariable String id) {
        try {
            mensagemService.delete(Integer.parseInt(id));
            return ResponseEntity.ok(Map.of("status", 200, "message", "Mensagem excluída com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "ID inválido: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
