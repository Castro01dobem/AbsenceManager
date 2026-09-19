package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.services.ValidacaoService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validacao")
public class ValidacaoController {

    private final ValidacaoService validacaoService;

    public ValidacaoController(ValidacaoService validacaoService) {
        this.validacaoService = validacaoService;
    }

    @GetMapping("/email")
    public ResponseEntity<Map<String, Object>> validarEmail(@RequestParam String email) {
        boolean valido = validacaoService.validarFormatoEmail(email);
        return ResponseEntity.ok(Map.of(
                "email", email,
                "valido", valido,
                "mensagem", valido ? "E-mail em formato valido." : "Informe um e-mail em um formato valido."
        ));
    }

    @GetMapping("/cpf")
    public ResponseEntity<Map<String, Object>> validarCpf(@RequestParam String cpf) {
        boolean valido = validacaoService.validarCpf(cpf);
        return ResponseEntity.ok(Map.of(
                "cpf", cpf,
                "valido", valido,
                "mensagem", valido ? "CPF valido." : "CPF invalido."
        ));
    }
}
