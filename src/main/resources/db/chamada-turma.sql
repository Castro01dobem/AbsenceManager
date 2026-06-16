IF COL_LENGTH('Aluno', 'email') IS NULL
BEGIN
    ALTER TABLE Aluno ADD email VARCHAR(255) NULL;
END;

UPDATE Aluno
SET email = CONCAT('aluno', rm, '@local.invalid')
WHERE email IS NULL;

ALTER TABLE Aluno ALTER COLUMN email VARCHAR(255) NOT NULL;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'uk_aluno_email'
      AND object_id = OBJECT_ID('Aluno')
)
BEGIN
    CREATE UNIQUE INDEX uk_aluno_email ON Aluno(email);
END;

IF OBJECT_ID('Chamada', 'U') IS NULL
BEGIN
    CREATE TABLE Chamada (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        turma_id BIGINT NOT NULL,
        token VARCHAR(100) NOT NULL,
        data_geracao DATETIME2 NOT NULL,
        data_expiracao DATETIME2 NOT NULL,
        status VARCHAR(20) NOT NULL,
        CONSTRAINT uk_chamada_token UNIQUE (token),
        CONSTRAINT fk_chamada_turma FOREIGN KEY (turma_id) REFERENCES Turma(id)
    );
END;

IF OBJECT_ID('ChamadaAluno', 'U') IS NULL
BEGIN
    CREATE TABLE ChamadaAluno (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        chamada_id BIGINT NOT NULL,
        aluno_rm INT NOT NULL,
        status VARCHAR(20) NOT NULL,
        data_confirmacao DATETIME2 NULL,
        CONSTRAINT fk_chamada_aluno_chamada FOREIGN KEY (chamada_id) REFERENCES Chamada(id),
        CONSTRAINT fk_chamada_aluno_aluno FOREIGN KEY (aluno_rm) REFERENCES Aluno(rm),
        CONSTRAINT uk_chamada_aluno UNIQUE (chamada_id, aluno_rm)
    );
END;
