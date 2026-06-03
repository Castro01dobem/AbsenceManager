package com.itb.inf2dm.absencemanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itb.inf2dm.absencemanager.model.entity.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

}