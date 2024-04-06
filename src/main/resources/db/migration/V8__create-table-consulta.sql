CREATE TABLE consulta (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          medico BIGINT NOT NULL,
                          paciente BIGINT NOT NULL,
                          dataHora DATETIME NOT NULL,
                          FOREIGN KEY (medico) REFERENCES medicos(id),
                          FOREIGN KEY (paciente) REFERENCES pacientes(id)
);

