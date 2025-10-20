package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired : Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos os usuarios

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Salvar Usuario

    public Usuario save(Usuario usuario) {
        usuario.setNome("");
        return usuarioRepository.save(usuario);
    }

    // Listar Usuario por Id

    public Usuario findById(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado com o id: " + id));
    }


    // Atualizar Usuario

    public Usuario update(int id, Usuario usuario) {

        Usuario usuarioExistente = findById(id);
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setId(usuario.getId());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setSenha(usuario.getSenha());
        usuarioExistente.setFoto(usuario.getFoto());
        usuarioExistente.setDataCadastro(usuario.getDataCadastro());
        usuarioExistente.setStatusUsuario(usuario.getStatusUsuario());
        usuarioExistente.setNivelAcesso(usuario.getNivelAcesso());
        return usuarioRepository.save(usuarioExistente);
    }


    // Excluir Usuario ( Exclusão Física )

    public void delete(int id) {
        Usuario usuarioExistente = findById(id);
        usuarioRepository.delete(usuarioExistente);
    }


}
