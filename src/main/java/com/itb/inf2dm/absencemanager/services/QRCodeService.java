package com.itb.inf2dm.absencemanager.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itb.inf2dm.absencemanager.model.entity.QRCode;
import com.itb.inf2dm.absencemanager.model.repository.QRCodeRepository;
import com.google.zxing.common.BitMatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.UUID;

@Service
public class QRCodeService {

    @Autowired
    private QRCodeRepository repository;

    public byte[] gerarQRCode(String texto, int largura, int altura) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

            BufferedImage image = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < largura; x++) {
                for (int y = 0; y < altura; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }



            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code", e);
        }
    }

    public QRCode gerarESalvar(Long aulaId) {

        String token = UUID.randomUUID().toString();

        QRCode qr = new QRCode();
        qr.setAulaId(aulaId);
        qr.setToken(token);
        // Evita discrepância de horário (banco/JSON podem aplicar timezone diferente).
        // Armazena o horário convertido para o fuso do Brasil (America/Sao_Paulo).
        ZoneId saoPaulo = ZoneId.of("America/Sao_Paulo");
        LocalDateTime agoraSaoPaulo = Instant.now().atZone(saoPaulo).toLocalDateTime();

        qr.setDataGeracao(agoraSaoPaulo);
        qr.setDataExpiracao(agoraSaoPaulo.plusMinutes(50)); // expira em 50 min

        qr.setAtivo(true);

        return repository.save(qr);
    }
}