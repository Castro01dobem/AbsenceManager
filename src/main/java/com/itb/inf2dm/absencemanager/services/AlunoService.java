package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.PresencaRepository;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Aluno save(Aluno aluno) {
        // Validações para evitar 500 opaco; ajuda a identificar payload inválido.
        if (aluno == null) {
            throw new IllegalArgumentException("payload do aluno nao informado");
        }
        if (aluno.getNome() == null) {
            throw new IllegalArgumentException("nome nao informado para cadastrar aluno");
        }
        if (aluno.getEmail() == null) {
            throw new IllegalArgumentException("email nao informado para cadastrar aluno");
        }
        if (aluno.getDataNascimento() == null) {
            throw new IllegalArgumentException("dataNascimento nao informada para cadastrar aluno");
        }
        if (aluno.getCpf() == null) {
            throw new IllegalArgumentException("cpf nao informado para cadastrar aluno");
        }
        if (aluno.getTelefone() == null) {
            throw new IllegalArgumentException("telefone nao informado para cadastrar aluno");
        }
        if (aluno.getSexo() == null) {
            throw new IllegalArgumentException("sexo nao informado para cadastrar aluno");
        }

        // Garantir vínculo correto com Usuario (evita 500 por referência parcial).
        if (aluno.getUsuario() == null || aluno.getUsuario().getId() == null) {
            throw new IllegalArgumentException("usuario_id nao informado para cadastrar aluno");
        }

        Usuario usuario = usuarioRepository.findById(aluno.getUsuario().getId())
                .orElseThrow(() -> new IllegalStateException("Usuario nao encontrado para id=" + aluno.getUsuario().getId()));

        aluno.setRm(null);
        aluno.setStatusAluno("ATIVO");
        aluno.setUsuario(usuario);

        return alunoRepository.save(aluno);
    }



    public Aluno findById(int rm) {
        return alunoRepository.findById(rm)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o rm: " + rm));
    }

    public Aluno update(int rm, Aluno aluno) {
        Aluno existente = findById(rm);
        existente.setNome(aluno.getNome());
        existente.setEmail(aluno.getEmail());
        existente.setDataNascimento(aluno.getDataNascimento());
        existente.setSexo(aluno.getSexo());
        existente.setCpf(aluno.getCpf());
        existente.setTelefone(aluno.getTelefone());
        existente.setUsuario(aluno.getUsuario());
        existente.setStatusAluno(aluno.getStatusAluno());
        return alunoRepository.save(existente);
    }

    public void delete(int rm) {
        Aluno aluno = findById(rm);

        if (presencaRepository.existsByTurmaAlunoAlunoRm(rm)) {
            throw new IllegalStateException("Nao e possivel excluir este aluno porque ele possui registros de presenca.");
        }

        try {
            alunoRepository.delete(aluno);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Nao e possivel excluir este aluno porque ele possui registros relacionados.");
        }
    }

    public Aluno inativar(int rm) {
        Aluno aluno = findById(rm);

        if (aluno.getUsuario() != null) {
            aluno.getUsuario().setStatusUsuario("INATIVO");
        }
        aluno.setStatusAluno("INATIVO");

        return alunoRepository.save(aluno);
    }
}

