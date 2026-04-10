package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        usuario.setId(null);
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setStatusUsuario("ATIVO");
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o id: " + id));
    }

    public Usuario update(Integer id, Usuario usuario) {
        Usuario existente = findById(id);
        existente.setNome(usuario.getNome());
        existente.setUsername(usuario.getUsername());
        existente.setSenha(usuario.getSenha());
        existente.setNivelAcesso(usuario.getNivelAcesso());
        existente.setFoto(usuario.getFoto());
        existente.setStatusUsuario(usuario.getStatusUsuario());
        existente.setDataAtualizacao(LocalDateTime.now());
        return usuarioRepository.save(existente);
    }

    public void delete(int id) {
        usuarioRepository.delete(findById(id));
    }
}
