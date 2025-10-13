package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.AlunoTurma;
import com.itb.inf2dm.absencemanager.model.services.AlunoTurmaService;
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
@RequestMapping("/api/v1/AlunoTurma")
public class AlunoTurmaController {

    @Autowired
    private AlunoTurmaService alunoTurmaService;

    @GetMapping
    public ResponseEntity <List<AlunoTurma>> listarTodosAlunoTurma() {

        return ResponseEntity.ok(alunoTurmaService.findAll());
    }

    @PostMapping
    public ResponseEntity<AlunoTurma> salvarAlunoTurma(@RequestBody AlunoTurma alunoTurma) {
        AlunoTurma novoAlunoTurma = alunoTurmaService.save(alunoTurma);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAlunoTurma);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarAlunoTurmaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(alunoTurmaService.findById(Long.parseLong(id)));
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
                            "message", "AlunoTurma não encontrado com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarAlunoTurma(@PathVariable String id, @RequestBody AlunoTurma alunoTurma) {
        try {
            return ResponseEntity.ok(alunoTurmaService.update(Long.parseLong(id), alunoTurma));
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
                            "message", "AlunoTurma não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAlunoTurmaPorId(@PathVariable String id) {
        try {
            alunoTurmaService.delete(Long.parseLong(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "AlunoTurma excluído com sucesso!"
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
                            "message", "AlunoTurma não encontrado com o id: " + id
                    )
            );
        }
    }

}
