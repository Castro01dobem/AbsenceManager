package com.itb.inf2dm.absencemanager.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;

@Repository
public interface TurmaAlunoRepository extends JpaRepository<TurmaAluno, Long> {

    List<TurmaAluno> findByTurmaId(Long turmaId);

    List<TurmaAluno> findByAlunoRm(Integer alunoRm);

    boolean existsByTurmaIdAndAlunoRm(Long turmaId, Integer alunoRm);
}
