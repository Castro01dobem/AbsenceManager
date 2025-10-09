package com.itb.inf2dm.absencemanager.model.repository;

import com.itb.inf2dm.absencemanager.model.entity.Chamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada,Integer> {
}
