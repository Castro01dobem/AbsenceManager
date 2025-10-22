package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.UsuarioTurma;
import com.itb.inf2dm.absencemanager.model.services.UsuarioTurmaService;
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
@RequestMapping("/api/v1/UsuarioTurma")
public class UsuarioTurmaController {

    @Autowired
    private UsuarioTurmaService usuarioTurmaService;

    @GetMapping
    public ResponseEntity <List<UsuarioTurma>> listarTodosUsuarioTurma() {

        return ResponseEntity.ok(usuarioTurmaService.findAll());
    }

    @PostMapping
    public ResponseEntity<UsuarioTurma> salvarUsuarioTurma(@RequestBody UsuarioTurma usuarioTurma) {
        UsuarioTurma novoUsuarioTurma = usuarioTurmaService.save(usuarioTurma);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioTurma);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarUsuarioTurmaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioTurmaService.findById(Integer.parseInt(id)));
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
                            "message", "UsuarioTurma não encontrado com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarUsuarioTurma(@PathVariable String id, @RequestBody UsuarioTurma usuarioTurma) {
        try {
            return ResponseEntity.ok(usuarioTurmaService.update(Integer.parseInt(id), usuarioTurma));
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
                            "message", "UsuarioTurma não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarUsuarioTurmaPorId(@PathVariable String id) {
        try {
            usuarioTurmaService.delete(Integer.parseInt(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "UsuarioTurma excluído com sucesso!"
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
                            "message", "UsuarioTurma não encontrado com o id: " + id
                    )
            );
        }
    }

}
