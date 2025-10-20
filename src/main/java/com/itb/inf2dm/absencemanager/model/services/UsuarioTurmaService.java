package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.UsuarioTurma;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioTurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired: Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class UsuarioTurmaService {

    @Autowired
    private UsuarioTurmaRepository usuarioTurmaRepository;


    // Listar todos os produtos
    public List<UsuarioTurma> findAll() {
        return usuarioTurmaRepository.findAll();
    }

    // Salvar Produto
    public UsuarioTurma save(UsuarioTurma usuarioTurma) {
        usuarioTurma.setStatusUsuarioTurma("Ativo");
        return usuarioTurmaRepository.save(usuarioTurma);
    }

    // Listar Produto por Id
    public UsuarioTurma findById(int id) {
        return usuarioTurmaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Turma do usuario não encontrado com o id:" + id));
    }

    // Atualizar Produto
    public UsuarioTurma update(int id, UsuarioTurma usuarioTurma) {

        UsuarioTurma usuarioTurmaExistente = findById(id);
        usuarioTurmaExistente.setId(usuarioTurma.getId());
        usuarioTurmaExistente.setStatusUsuarioTurma(usuarioTurma.getStatusUsuarioTurma());
        usuarioTurmaExistente.setDataCadastro(usuarioTurma.getDataCadastro());
        return usuarioTurmaRepository.save(usuarioTurmaExistente);
    }


    // Excluir Aluno
    public void delete(int id) {
        UsuarioTurma usuarioTurmaExistente = findById(id);
        usuarioTurmaRepository.delete(usuarioTurmaExistente);
    }
}
