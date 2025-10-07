package com.itb.inf2dm.absencemanager.repository;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
}
