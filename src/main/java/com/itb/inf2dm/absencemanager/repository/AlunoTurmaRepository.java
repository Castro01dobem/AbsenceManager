package com.itb.inf2dm.absencemanager.repository;

import com.itb.inf2dm.absencemanager.model.entity.AlunoTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, Integer> {
}
