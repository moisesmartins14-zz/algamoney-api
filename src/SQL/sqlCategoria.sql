CREATE TABLE categoria (
    codigo SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);
INSERT INTO categoria (nome) VALUES  ('Salário');
INSERT INTO categoria (nome) VALUES ('Lazer');
INSERT INTO categoria (nome) VALUES ('Alimentação');
INSERT INTO categoria (nome) VALUES ('Supermercado');
INSERT INTO categoria (nome) VALUES ('Farmácia');
INSERT INTO categoria (nome) VALUES ('Informática');
INSERT INTO categoria (nome) VALUES ('Multas');
INSERT INTO categoria (nome) VALUES ('Impostos');
INSERT INTO categoria (nome) VALUES ('Combustível');
INSERT INTO categoria (nome) VALUES ('Outros');