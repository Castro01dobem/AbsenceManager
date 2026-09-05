package com.itb.inf2dm.absencemanager.dto;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;

/**
 * Dados alteráveis de um aluno. turmaId representa somente a matrícula atual
 * armazenada em TurmaAluno; não altera os registros históricos vinculados.
 */
public class AlunoUpdateDTO {

    private Aluno aluno;
    private Long turmaId;

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }
}
