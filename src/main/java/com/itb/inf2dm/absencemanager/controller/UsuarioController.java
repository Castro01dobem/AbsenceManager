package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.services.UsuarioService;
import com.itb.inf2dm.absencemanager.dto.UsuarioDTO;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Usuario usuario) {
        try {
            Usuario createdUsuario = usuarioService.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "message", e.getMessage()));
        }
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Usuario> editar(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "usuario", required = false) Usuario usuario) {

        Usuario usuarioAtualizado = usuarioService.editar(file, id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PostMapping("/{id}/solicitar-codigo-senha")
    public ResponseEntity<Object> solicitarCodigoSenha(@PathVariable Long id) {
        try {
            usuarioService.solicitarCodigoTrocaSenha(id);
            return ResponseEntity.ok(Map.of("status", 200, "message", "Codigo enviado para o e-mail cadastrado."));
        } catch (org.springframework.mail.MailException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", 503, "message", "Nao foi possivel enviar o e-mail agora. Tente novamente em instantes."));
        }
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Object> alterarSenha(@PathVariable Long id,
            @RequestParam String codigo,
            @RequestParam String senhaAtual,
            @RequestParam String newPassword) {
        try {
            Usuario usuario = usuarioService.alterarSenha(id, codigo, senhaAtual, newPassword);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", 400, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/inativar")
    public ResponseEntity<Usuario>  inativar(@PathVariable Long id) {
        Usuario usuario = usuarioService.inativar(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Usuario>  ativar(@PathVariable Long id) {
        Usuario usuario = usuarioService.ativar(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Sessao invalida ou expirada. Faca login novamente."));
        }

        UsuarioDTO usuario = usuarioService.findByUsername(authentication);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "message", e.getMessage()));
    }

}
