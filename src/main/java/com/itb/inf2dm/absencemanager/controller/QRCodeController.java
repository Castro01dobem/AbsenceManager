package com.itb.inf2dm.absencemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.itb.inf2dm.absencemanager.model.entity.QRCode;
import com.itb.inf2dm.absencemanager.services.QRCodeService;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping(value = "/gerar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> gerarQRCode(@RequestParam Long aulaId) {

        QRCode qr = qrCodeService.gerarESalvar(aulaId);

        String conteudo = "{ \"aulaId\": " + aulaId + ", \"token\": \"" + qr.getToken() + "\" }";

        byte[] imagem = qrCodeService.gerarQRCode(conteudo, 300, 300);

        return ResponseEntity.ok(imagem);
    }
}