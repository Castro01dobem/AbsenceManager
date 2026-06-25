package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.services.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/aluno")
@Tag(name = "Aluno", description = "Gerenciamento de alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @Operation(summary = "Listar todos os alunos")
    public ResponseEntity<List<Aluno>> listar() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo aluno")
    public ResponseEntity<Aluno> cadastrar(@RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.save(aluno));
    }

    @GetMapping("/{rm}")
    @Operation(summary = "Buscar aluno por RM")
    public ResponseEntity<Object> buscarPorRm(@PathVariable String rm) {
        try {
            return ResponseEntity.ok(alunoService.findById(Integer.parseInt(rm)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "RM invalido: " + rm));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @GetMapping("/{rm}/frequencia")
    @Operation(summary = "Buscar frequencia do aluno por RM")
    public ResponseEntity<Object> buscarFrequencia(@PathVariable String rm) {
        try {
            return ResponseEntity.ok(alunoService.frequencia(Integer.parseInt(rm)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "RM invalido: " + rm));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{rm}")
    @Operation(summary = "Atualizar aluno")
    public ResponseEntity<Object> atualizar(@PathVariable String rm, @RequestBody Aluno aluno) {
        try {
            return ResponseEntity.ok(alunoService.update(Integer.parseInt(rm), aluno));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "RM invalido: " + rm));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{rm}")
    @Operation(summary = "Excluir aluno")
    public ResponseEntity<Object> deletar(@PathVariable String rm) {
        try {
            alunoService.delete(Integer.parseInt(rm));
            return ResponseEntity.ok(Map.of("status", 200, "message", "Aluno excluido com sucesso!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "RM invalido: " + rm));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }

    @PutMapping("/{rm}/inativar")
    @Operation(summary = "Inativar aluno")
    public ResponseEntity<Object> inativar(@PathVariable String rm) {
        try {
            Aluno aluno = alunoService.inativar(Integer.parseInt(rm));
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Aluno inativado com sucesso!",
                    "aluno", aluno
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "RM invalido: " + rm));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "message", e.getMessage()));
        }
    }
}
