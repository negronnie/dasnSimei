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
Nesta fase, devo dominar a implementação de uma plataforma de comunicação através de requests HTTP/S, fixando os conhecimentos em ambientes e 

### Fase 4 - Testes Unitários e Integrados
As funcionalidades já estão implementadas utilizando spring, e a operação acontece através de uma API.
Nesta fase, a intenção é conhecer e aplicar as técnicas básicas de teste de software, a fim de atingir os padrões usados no mercado.

### Fase 5 - FrontEnd
As funcionalidades já estão implementadas utilizando spring, mas a partir deste momento a operação deve acontecer através de uma página web.
Tecnologias: React, Tailwind. 

### Fase 6 - Deploy
Com o sistema totalmente implementado, vamos fazer o deploy em server local utilizando Apache e Coolify, com banco de dados, e plataforma de gestão de projeto.

### Fase 7 - CI/CD
Actions/Pipeline de entrega contínua com base no push/merge.

# Status

- [x] Fase 1
- [ ] Fase 2
- [ ] Fase 3
- [ ] Fase 4
- [ ] Fase 5
- [ ] Fase 6
- [ ] Fase 7


--------

## Revisão
Na revisão do projeto, vou apresentar o código nas suas três primeiras versões para desenvolvedores de confiança para que vejam a evolução do código e da lógica de implementação, e para que apontem pontos de melhoria.
Após a avaliação farei a correção e apresentarei novamente, como se fosse uma forma de Merge Request.


--------

## Finalização (Fase 7)

Assim que finalizado, disponibilizar o código para uso open source, através do github (público).
Assim que possível, implementar um sistema de autenticação utilizando o spring, e disponibilizar a aplicação para usuários através do link mei.negronnie.com.br
