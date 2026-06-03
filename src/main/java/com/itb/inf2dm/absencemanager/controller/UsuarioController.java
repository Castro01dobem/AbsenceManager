package com.itb.inf2dm.absencemanager.controller;

import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.services.UsuarioService;
import com.itb.inf2dm.absencemanager.dto.UsuarioDTO;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioService.create(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Usuario> editar(
            @PathVariable Long id,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart Usuario usuario) {

        Usuario usuarioAtualizado = usuarioService.editar(file, id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Usuario>  alterarSenha(@PathVariable Long id,
            @RequestParam String newPassword) {
        Usuario usuario = usuarioService.alterarSenha(id, newPassword);
        return ResponseEntity.ok(usuario);
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
    public UsuarioDTO me(Authentication authentication) {
        UsuarioDTO usuario = usuarioService
                .findByUsername(authentication);
        return usuario;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

}
