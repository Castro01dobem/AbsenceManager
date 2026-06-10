# TODO - Inativar aluno (substituir excluir)

- [ ] Backend: adicionar endpoint `PUT /api/v1/aluno/{rm}/inativar`
- [ ] Backend: implementar `AlunoService.inativar(rm)` para setar `statusAluno=INATIVO` e `usuario.statusUsuario=INATIVO`
- [ ] Backend: (manter) endpoint DELETE como está, mas não usado pela UI
- [ ] Frontend: atualizar `AlunoServices.js` com `inativarAluno(rm)`
- [ ] Frontend: em `AlunosListar.jsx`, trocar botão + handler de Excluir -> Inativar
- [ ] Validar funcionamento: listar alunos, inativar, recarregar tabela e confirmar bloqueio de login

