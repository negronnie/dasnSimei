# DASN - SIMEI | Declaração de Faturamento do MEI (Simples Nacional) #

Projeto de Estudos onde vou documentando os avanços dos meus estudos em um projeto real.
A proposta é importar arquivos exportados pelos bancos (primeiramente csv, e posteriormente pdf/xls), considerar todas as transações positivas e somá-las para obter o valor final necessário para a declaração.
Como bonus, o software vai gerar totalizadores mensais e trimestrais.

O desenvolvimento está dividido da seguinte forma:

### Fase 1 - Base
As funcionalidades foram implementadas usando java puro, a sua operação é através de linha de comando.
Nesta fase, garanto que aprendi a implementação das lógicas de programação e também das bibliotecas básicas do java, fixando os conceitos de herança, polimorfismo, encapsulamento, operações com listas, laços de repetição e condicionais.

### Fase 2 - Persistência
As funcionalidades serão implementadas usando java puro, a operação ainda é através de linha de comando mas agora temos persistência em JPA.
Nesta fase, experimento as implementações de persistência, fixando os fundamentos de bancos de dados, conhecimentos em SQL, JDBC e JPA. 

### Fase 3 - API
As funcionalidades serão implementadas usando spring, e a operação deve acontecer através de uma API.
Nesta fase, devo dominar a implementação de uma plataforma de comunicação através de requests HTTP/S.

### Fase 4 - Padrões de Projeto
Nesta etapa o sistema está totalmente implementado, e vamos fazer a adequação das implementações aos padrões de projeto.
Padrões: SOLID, Clean Code, Arquitetura etc.



### Fase 5 - Testes Unitários e Integrados
As funcionalidades já estão implementadas utilizando spring, e a operação acontece através de uma API.
Nesta fase, a intenção é conhecer e aplicar as técnicas básicas de teste de software, a fim de atingir os padrões usados no mercado.

### Fase 6 - FrontEnd
As funcionalidades já estão implementadas utilizando spring, mas a partir deste momento a operação deve acontecer através de uma página web.
Tecnologias: React, Tailwind. 

### Fase 7 - Deploy
Com o sistema totalmente implementado, vamos fazer o deploy em server local utilizando um platform-as-a-service self hosted, com banco de dados, e plataforma de gestão de projeto.

### Fase 8 - CI/CD
Actions/Pipeline de entrega contínua com base no push/merge.

# Status

- [x] Fase 1 - [Status](https://git.negronnie.com.br/negronnie/dasnSimei/src/tag/0.1) | [Versão](https://git.negronnie.com.br/negronnie/dasnSimei/releases/tag/0.1)
- [x] Fase 2 - [Status](https://git.negronnie.com.br/negronnie/dasnSimei/src/tag/0.2) | [Versão](https://git.negronnie.com.br/negronnie/dasnSimei/releases/tag/0.2)
- [x] Fase 3 - [Status](https://git.negronnie.com.br/negronnie/dasnSimei/src/tag/0.3) | [Versão](https://git.negronnie.com.br/negronnie/dasnSimei/releases/tag/0.3)
- [x] Fase 4.1 - [Status](https://git.negronnie.com.br/negronnie/dasnSimei/src/tag/0.4.1) | [Versão](https://git.negronnie.com.br/negronnie/dasnSimei/releases/tag/0.4.1)
- [x] Fase 4.2 - [Status](https://git.negronnie.com.br/negronnie/dasnSimei/src/tag/0.4.2) | [Versão](https://git.negronnie.com.br/negronnie/dasnSimei/releases/tag/0.4.2) 
- [ ] Fase 4.3 -
- [ ] Fase 5
---
- [ ] Revisão A
- [ ] Revisão B
---

- [ ] Fase 6
- [ ] Fase 7
- [ ] Fase 8



--------

## Revisão
Na revisão do projeto, vou apresentar o código nas suas três primeiras versões para desenvolvedores de confiança para que vejam a evolução do código e da lógica de implementação, e para que apontem pontos de melhoria.
Após a avaliação farei a correção e apresentarei novamente, como se fosse uma forma de Merge Request.


--------

## Finalização (Fase 9)

Assim que finalizado, disponibilizar o código para uso open source, através do github (público).
Assim que possível, implementar um sistema de autenticação utilizando o spring, e disponibilizar a aplicação para usuários através do link mei.negronnie.com.br

- [ ] Fase 9
