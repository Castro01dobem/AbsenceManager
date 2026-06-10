package com.itb.inf2dm.absencemanager.model.repository;

import com.itb.inf2dm.absencemanager.model.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    boolean existsByTurmaAlunoAlunoRmAndAulaId(Integer alunoRm, Long aulaId);

    boolean existsByTurmaAlunoAlunoRm(Integer alunoRm);
}



