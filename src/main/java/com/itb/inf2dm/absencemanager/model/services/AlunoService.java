package com.itb.inf2dm.absencemanager.model.services;


import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired: Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;


    // Listar todos os produtos
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    // Salvar Produto
    public Aluno save(Aluno aluno) {
        aluno.setStatusAluno("Ativo");
        return alunoRepository.save(aluno);
    }

    // Listar Produto por Id
    public Produto findById(Long id) {
        return produtoRepository.findById()
                .orElseThrow(()-> new RuntimeException("Produto não encontrado com o id:" + id));
    }

    // Atualizar Produto
    public Produto update(Long id, Produto produto) {
        Produto produtoExistente = findById(id);
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setTipo(produto.getTipo());
        produtoExistente.setValorVenda(produto.getValorVenda());
        produtoExistente.setValorCompra(produto.getValorCompra());
        produtoExistente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        return produtoRepository.save(produtoExistente);
    }


    // Excluir Produto
    public void delete(Long id) {
        Produto produtoExistente = findById(id);
        produtoRepository.delete(produtoExistente);
    }
}

