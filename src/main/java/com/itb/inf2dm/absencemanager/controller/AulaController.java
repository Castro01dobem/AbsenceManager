package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.model.services.AulaService;
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
@RequestMapping("/api/v1/aula")
public class AulaController {

    @Autowired
    private AulaService aulaService;

    @GetMapping
    public ResponseEntity <List<Aula>> listarTodosAula() {

        return ResponseEntity.ok(aulaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Aula> salvarAula(@RequestBody Aula aula) {
        Aula novoProduto = aulaService.save(aula);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarAulaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(aulaService.findById(Long.parseLong(id)));
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
                            "message", "Aula não encontrado com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarAula(@PathVariable String id, @RequestBody Aula aula) {
        try {
            return ResponseEntity.ok(aulaService.update(Long.parseLong(id), aula));
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
                            "message", "Aula não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAulaPorId(@PathVariable String id) {
        try {
            aulaService.delete(Long.parseLong(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "Aula excluído com sucesso!"
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
                            "message", "Aula não encontrado com o id: " + id
                    )
            );
        }
    }

}