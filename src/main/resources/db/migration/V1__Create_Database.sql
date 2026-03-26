CREATE TABLE IF NOT EXISTS `movimento_financeiro` (
                                                      `tipo_movimento` varchar(31) NOT NULL,
                                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                                      `data` date DEFAULT NULL,
                                                      `descricao` varchar(255) DEFAULT NULL,
                                                      `identificador_transacao` varchar(255) DEFAULT NULL,
                                                      `valor` decimal(15,2) NOT NULL,
                                                      PRIMARY KEY (`id`),
                                                      CONSTRAINT `movimento_financeiro_chk_1` CHECK ((`tipo_movimento` in (_utf8mb4'MV',_utf8mb4'PR',_utf8mb4'VE')))
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;