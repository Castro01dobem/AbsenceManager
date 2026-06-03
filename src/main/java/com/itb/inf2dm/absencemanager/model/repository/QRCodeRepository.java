package com.itb.inf2dm.absencemanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itb.inf2dm.absencemanager.model.entity.QRCode;

import java.util.Optional;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {

    Optional<QRCode> findByToken(String token);


}
