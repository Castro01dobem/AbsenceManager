package com.itb.inf2dm.absencemanager.model.repository;

import com.itb.inf2dm.absencemanager.model.entity.ChamadaAluno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChamadaAlunoRepository extends JpaRepository<ChamadaAluno, Long> {

    List<ChamadaAluno> findByChamadaId(Long chamadaId);

    Optional<ChamadaAluno> findByChamadaIdAndAlunoRm(Long chamadaId, Integer alunoRm);
}
