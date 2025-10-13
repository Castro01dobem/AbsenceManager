package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.services.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
ResponseEntity : Toda resposta HTTP (status, cabeçalhos e corpo ), aqui teremos mais controle sobre o que é devolvido ao cliente
1. Status HTTP: (200 ok, 201 CREATED, 404 NOT FOUND etc ...)
2. Headers: (cabeçalhos extras, como Location, Authorization etc...)
3. Body:    ( O objeto que será convertido em JSON/XML para o cliente )

@RequestBody: Corpo da requisição ( Recebendo um objeto JSON)

*/

@RestController
@RequestMapping("/api/v1/turma")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity <List<Turma>> listarTodosTurma() {

        return ResponseEntity.ok(turmaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Turma> salvarTurma(@RequestBody Turma turma) {
        Turma novoTurma = turmaService.save(turma);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTurma);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarTurmaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(turmaService.findById(Integer.parseInt(id)));
        }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Turma não encontrada com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarTurma(@PathVariable String id, @RequestBody Turma turma) {
        try {
            return ResponseEntity.ok(turmaService.update(Integer.parseInt(id), turma));
        }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Turma não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTurmaPorId(@PathVariable String id) {
        try {
            turmaService.delete(Integer.parseInt(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "Turma excluído com sucesso!"
                    ));
        }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "O id informado não é válido: " + id
                    )
            );
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "status", 404,
                            "error", "Not Found",
                            "message", "Turma não encontrado com o id: " + id
                    )
            );
        }
    }

}
