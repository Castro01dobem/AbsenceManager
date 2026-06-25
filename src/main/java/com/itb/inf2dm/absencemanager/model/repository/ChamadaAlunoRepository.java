package com.itb.inf2dm.absencemanager.model.repository;

import com.itb.inf2dm.absencemanager.model.entity.ChamadaAluno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChamadaAlunoRepository extends JpaRepository<ChamadaAluno, Long> {

    List<ChamadaAluno> findByChamadaId(Long chamadaId);

    Optional<ChamadaAluno> findByChamadaIdAndAlunoRm(Long chamadaId, Integer alunoRm);

    @Query("select count(ca) from ChamadaAluno ca where ca.chamada.turma.professor.id = :professorId and ca.status = :status")
    long countByProfessorIdAndStatus(@Param("professorId") Long professorId, @Param("status") String status);

    @Query("select count(ca) from ChamadaAluno ca where ca.chamada.turma.id = :turmaId and ca.status = :status")
    long countByTurmaIdAndStatus(@Param("turmaId") Long turmaId, @Param("status") String status);

    @Query("select count(ca) from ChamadaAluno ca where ca.chamada.turma.id = :turmaId and ca.aluno.rm = :alunoRm and ca.status = :status")
    long countByTurmaIdAndAlunoRmAndStatus(@Param("turmaId") Long turmaId, @Param("alunoRm") Integer alunoRm,
            @Param("status") String status);
}
