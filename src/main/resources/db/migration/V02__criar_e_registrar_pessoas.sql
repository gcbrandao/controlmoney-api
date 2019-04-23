CREATE TABLE controlmoneyapi.pessoa (
	codigo 		BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome 		VARCHAR(50) NOT NULL,
	ativo 		boolean NOT NULL,
	logradouro	VARCHAR(50),
	numero		VARCHAR(10),
	complemento	VARCHAR(50),
	bairro		VARCHAR(50),
	cep			VARCHAR(10),
	cidade		VARCHAR(50),
	estado		VARCHAR(50)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Gilberto', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('pedro', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('maria', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('paula', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('jade', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('joao', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('rui', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('marcos', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('pafuncio', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('pafuncio silva', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
INSERT INTO controlmoneyapi.pessoa (nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('chico bento', true, 'rua 1', '239', '', 'moema', '04677-002', 'sao Paulo', 'SP');
