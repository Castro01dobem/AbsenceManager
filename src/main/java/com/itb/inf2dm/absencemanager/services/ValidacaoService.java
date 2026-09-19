package com.itb.inf2dm.absencemanager.services;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 * Validações de formato reutilizáveis (e-mail e CPF). Não dependem de
 * nenhuma tabela nova: usam apenas os campos que já existem em Usuario e
 * Aluno (username/email e cpf).
 */
@Service
public class ValidacaoService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public boolean validarFormatoEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Algoritmo oficial de validação de CPF (dígitos verificadores),
     * rejeitando também sequências com todos os dígitos iguais (ex.: 111.111.111-11),
     * que passam por qualquer checagem de tamanho mas não são CPFs válidos.
     */
    public boolean validarCpf(String cpfBruto) {
        if (cpfBruto == null) {
            return false;
        }

        String cpf = cpfBruto.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }

        int[] digitos = cpf.chars().map(c -> c - '0').toArray();

        int soma1 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += digitos[i] * (10 - i);
        }
        int resto1 = soma1 % 11;
        int dv1 = resto1 < 2 ? 0 : 11 - resto1;
        if (dv1 != digitos[9]) {
            return false;
        }

        int soma2 = 0;
        for (int i = 0; i < 10; i++) {
            soma2 += digitos[i] * (11 - i);
        }
        int resto2 = soma2 % 11;
        int dv2 = resto2 < 2 ? 0 : 11 - resto2;
        return dv2 == digitos[10];
    }
}
