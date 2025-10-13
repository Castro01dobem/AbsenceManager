package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Disciplina;
import com.itb.inf2dm.absencemanager.model.services.DisciplinaService;
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
@RequestMapping("/api/v1/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping
    public ResponseEntity <List<Disciplina>> listarTodosDisciplina() {

        return ResponseEntity.ok(disciplinaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Disciplina> salvarDisciplina(@RequestBody Disciplina disciplina) {
        Disciplina novoDisciplina = disciplinaService.save(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDisciplina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarDisciplinaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(disciplinaService.findById(Integer.parseInt(id)));
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
                            "message", "Disciplina não encontrado com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarDisciplina(@PathVariable String id, @RequestBody Disciplina disciplina) {
        try {
            return ResponseEntity.ok(disciplinaService.update(Integer.parseInt(id), disciplina));
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
                            "message", "Disciplina não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarDisciplinaPorId(@PathVariable String id) {
        try {
            disciplinaService.delete(Integer.parseInt(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "Disciplina excluído com sucesso!"
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
                            "message", "Disciplina não encontrado com o id: " + id
                    )
            );
        }
    }

}
