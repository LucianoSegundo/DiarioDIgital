# DiarioDIgital

O projeto **DiárioDigital** surgiu como parte da disciplina de testes de software. O objetivo inicial da disciplina era apenas introduzir os alunos à prática de realizar testes em seu código. No entanto, com o decorrer da disciplina, o escopo do projeto se expandiu, passando de uma simples aplicação **Java** para uma plataforma completa. O **Back-End** foi desenvolvido em **Java**, utilizando o **framework Spring Boot**, enquanto o **Front-End** foi construído com **HTML**, **TypeScript** e **CSS**, contando com o apoio do **framework Angular**.

## Repositórios
- **DiarioDIgital** : [https://github.com/LucianoSegundo/DiarioDIgital](https://github.com/LucianoSegundo/DiarioDIgital)
- **DiarioDIgital** : [https://github.com/LucianoSegundo/DiarioDigital-Front-End](https://github.com/LucianoSegundo/DiarioDigital-Front-End)
- **Hospedado da Plataforma** : A plataforma pode ser testada [clicando aqui](https://diariodigital-front-end.onrender.com). A plataforma podera ser testada durante 30 dias, contanto apartir de **15/03/2025**, esse é o tempo de duração do banco de dados gratuito fornecido pela plataforma de hospedagem;
- **Hospegagem da API** : A documentação da API pode ser acessada [clicando aqui](https://diariodigital-2.onrender.com/swagger-ui/index.html), assim comoa plataforma, só estará disponivel pelos proximos 30 dias, contando apartir do dia ***15/03/2025*. OBS: è politica do fornecedor da hospedagem gratuita desligar o servidor caso ele passe alguns minutos sem receber nenhum acesso, o servidor é restartado quando recebe uma nova requisição, oque leva um tempo para acontecer.

## Diagrama de Classes
```mermaid

classDiagram
    class Usuario {
        +Long id
        +String nome
        +String senha
        +int idade
        +String PalavraSegu
        +Boolean ativo
        +List<Livro> livros
        +Role role
    }

    class Livro {
        +Long id
        +String titulo
        +List<Capitulo> capitulos
    }

    class Capitulo {
        +Long id
        +String titulo
        +int numeroCapitulo
        +String conteudo
    }

    class Role {
        <<enumeration>>
        +int value
        +ADMIN(1)
        +USER(2)
        +getValue()
        +fromValue(int value)
    }

    Usuario "1" -- "0..*" Livro : possui
    Livro "1" -- "0..*" Capitulo : possui
    Livro "1" -- "1" Usuario : pertence a
    Capitulo "1" -- "1" Livro : pertence a
    Usuario "1...*" -- "1...*" Role : possui


 
```

## Imagens da Plataforma

### Homepage Parte 01

![pagina01](imagens/pagina01.png)

### Homepage Parte 02: Cadastro

![pagina02](imagens/pagina02.png)

### Pagina de Login

![pagina03](imagens/pagina03.png)

### Pagina de Login Aguardando resposta do Back-End

![pagina04](imagens/pagina04.png)

### Pagina de Login Negado

![pagina05](imagens/pagina05.png)

### Pagina de Recuperação de Senha

![pagina06](imagens/pagina06.png)

### Pagina Principal Sem Nenhum Livro

![pagina07](imagens/pagina07.png)

### Pagina Principal + Criação de Livro

![pagina08](imagens/pagina08.png)

### Pagina Principal com Livro

![pagina09](imagens/pagina09.png)

### Pagina do Livro Selecionado sem Capitulos

![pagina10](imagens/pagina10.png)

### Pagina de Exclusão de Livros

![pagina11](imagens/pagina11.png)

### Pagina de Criação de Capitulos

![pagina12](imagens/pagina12.png)

### Pagina do Livro Selecionado com Capitulos

![pagina13](imagens/pagina13.png)

### Pagina de Leitura de Capitulo

![pagina14](imagens/pagina14.png)

### Pagina Pagina de Exclusão de Capitulo + Aviso de Fim de Sessão

![pagina15](imagens/pagina15.png)

### Pagina de Exclusão de Conta
![pagina16](imagens/pagina16.png)

## Banco de Dados
Em produção foi usado um banco PostGreSQL, o banco fornecido pelo serviço de hospedagem gratuita.
Contudo em Desenvolviemento foi usado um Banco MySQL, com o qual era inicialmente projetado para trabalhar.

## Ferramentas Utilizadas
- Swegger
- MySql
- PostgresSQL
- Spring Data JPA
- Spring Web
- Spring Security
- JWT
