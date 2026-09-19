package com.itb.inf2dm.absencemanager.services;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Envia o codigo de troca de senha via API HTTPS do Brevo
 * (https://brevo.com), em vez de SMTP direto. Hospedagens free (como o
 * Render) costumam bloquear as portas SMTP tradicionais (587/465/25) para
 * evitar abuso de spam; uma chamada HTTPS comum nao sofre esse bloqueio.
 * Diferente do Resend, o Brevo permite enviar para qualquer destinatario
 * assim que UM unico e-mail remetente e' verificado (nao precisa de dominio
 * proprio) - configurado em brevo.from.
 */
@Service
public class EmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestClient restClient = RestClient.create();

    @Value("${brevo.api-key:}")
    private String apiKey;

    @Value("${brevo.from:}")
    private String remetente;

    public void enviarCodigoTrocaSenha(String destinatario, String nomeUsuario, String codigo) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("BREVO_API_KEY nao configurada.");
        }
        if (remetente == null || remetente.isBlank()) {
            throw new IllegalStateException("BREVO_FROM nao configurado (precisa ser um remetente verificado no Brevo).");
        }

        Map<String, Object> corpo = Map.of(
                "sender", Map.of("name", "Absence Manager", "email", remetente),
                "to", List.of(Map.of("email", destinatario)),
                "subject", "Seu código para trocar a senha",
                "htmlContent", montarHtml(nomeUsuario, codigo)
        );

        try {
            restClient.post()
                    .uri(BREVO_URL)
                    .header("api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException e) {
            throw new IllegalStateException("Falha ao enviar e-mail via Brevo: " + e.getMessage(), e);
        }
    }

    private String montarHtml(String nomeUsuario, String codigo) {
        String primeiroNome = (nomeUsuario == null || nomeUsuario.isBlank())
                ? "" : nomeUsuario.trim().split("\\s+")[0];
        String saudacao = primeiroNome.isBlank() ? "Olá," : "Olá, " + primeiroNome + ",";

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <body style="margin:0;padding:0;background-color:#f2f4f7;font-family:'Segoe UI',Arial,sans-serif;">
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f2f4f7;padding:32px 16px;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="max-width:480px;width:100%%;background:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(16,24,40,0.08);">
                      <tr>
                        <td style="background:linear-gradient(90deg,#3B429F,#4CC9F0);padding:28px 32px;">
                          <span style="color:#ffffff;font-size:18px;font-weight:800;letter-spacing:-0.3px;">Absence Manager</span>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:36px 32px 8px;">
                          <p style="margin:0 0 4px;color:#101828;font-size:16px;font-weight:700;">%s</p>
                          <p style="margin:0 0 24px;color:#475467;font-size:14px;line-height:1.6;">
                            Recebemos um pedido para trocar a senha da sua conta. Use o código abaixo para autorizar essa troca:
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:0 32px 28px;" align="center">
                          <div style="background:#f2f8fd;border:1px solid #cdeaf9;border-radius:12px;padding:20px 24px;display:inline-block;">
                            <span style="font-size:34px;font-weight:800;letter-spacing:10px;color:#0f6fa8;font-family:'Courier New',monospace;">%s</span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:0 32px 32px;">
                          <p style="margin:0 0 8px;color:#475467;font-size:13px;line-height:1.6;">
                            Esse código vale por <strong>10 minutos</strong>. Não compartilhe esse código com ninguém.
                          </p>
                          <p style="margin:0;color:#98a2b3;font-size:12px;line-height:1.6;">
                            Se você não pediu essa troca de senha, pode ignorar este e-mail com segurança.
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:18px 32px;background:#f9fafb;border-top:1px solid #eaecf0;">
                          <p style="margin:0;color:#98a2b3;font-size:11px;">Absence Manager · e-mail automático, não responda.</p>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(saudacao, codigo);
    }
}
