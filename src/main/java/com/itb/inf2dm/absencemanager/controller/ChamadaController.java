package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Chamada;
import com.itb.inf2dm.absencemanager.model.services.ChamadaService;
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
@RequestMapping("/api/v1/chamada")
public class ChamadaController {

    @Autowired
    private ChamadaService chamadaService;

    @GetMapping
    public ResponseEntity <List<Chamada>> listarTodosChamada() {

        return ResponseEntity.ok(chamadaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Chamada> salvarChamada(@RequestBody Chamada chamada) {
        Chamada novoChamada = chamadaService.save(chamada);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoChamada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarChamadaPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(chamadaService.findById(Long.parseLong(id)));
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
                            "message", "Chamada não encontrado com o id: " + id
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarChamada(@PathVariable String id, @RequestBody Chamada chamada) {
        try {
            return ResponseEntity.ok(chamadaService.update(Long.parseLong(id), chamada));
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
                            "message", "Chamada não encontrado com o id: " + id
                    )
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarChamadaPorId(@PathVariable String id) {
        try {
            chamadaService.delete(Long.parseLong(id));
            return ResponseEntity.ok().body(
                    Map.of(
                            "status", 200,
                            "message", "Chamada excluído com sucesso!"
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
                            "message", "Chamada não encontrado com o id: " + id
                    )
            );
        }
    }

}
