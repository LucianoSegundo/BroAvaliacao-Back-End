# BroAvaliacao-Back-End
Back-End para o sistema de avaliação de broas que será apresentado no final da disciplina de desenvolvimento web 2 do curso de Tecnologia em Análise e Desenvolvimento de Sistemas.
```mermaid 
classDiagram

    %% Classe Usuario
    class Usuario {
        String usuario
        String senha
        String email
        List~Resenha~ resenhas
        Configuracao configuracoes
        List~Notificacao~ notificacoes
        List~Comentario~ comentarios
    }

    %% Classe Resenha
    class Resenha {
        String titulo
        String conteudo
        Usuario autor
        Restaurante restaurante
        Item item
        List~Comentario~ comentarios
    }

    %% Classe Item
    class Item {
        String nome
        String descricao
        List~Resenha~ resenhas
        Restaurante restaurante
    }

    %% Classe Restaurante
    class Restaurante {
        String nome
        Usuario proprietario
        Endereco endereco
        List~Item~ cardapio
        List~Resenha~ resenhas
    }

    %% Classe Endereco
    class Endereco {
        String estado
        String cidade
        String bairro
        String rua
        Restaurante restaurante
    }

    %% Classe Configuracao
    class Configuracao {
        boolean notificar
        boolean notificarPorEmail
    }

    %% Classe Comentario
    class Comentario {
        String mensagem
        long numeroComentarios
        List~Comentario~ comentarios
    }

    %% Classe Notificacao
    class Notificacao {
        Usuario destinatario
        timestamp dataCriacao
        String mensagem
        String local
    }
    
           %% ENUM TipoAtualizacao
    class TipoAtualizacao {
        int categoria
        String descricao
    }

    %% Classe LogAtualizacao
    class LogAtualizacao {
        Long id
        String nomeItem
        int categoriaAtualizacao
        String justificativaAlualizacao
        LocalDateTime dataCriacao
    }

 %% Classe RepresentacaoModificacao
    class RepresentacaoModificacao {
        Long id
        String valorOriginal
        String nomeAtributo
        String novoValor
    }

    %% Relacionamentos
    Usuario "1" --> "many" Resenha : escreve
    Usuario "1" --> "1" Configuracao : possui
    Usuario "1" --> "many" Notificacao : recebe
    Usuario "1" --> "many" Comentario : escreve
    Usuario "1" --> "many" LogAtualizacao : responsavel pela geração de
    
    Resenha "1" --> "1" Usuario : autor
    Resenha "1" --> "1" Restaurante : refere-se
    Resenha "1" --> "1" Item : refere-se
    Resenha "1" --> "many" Comentario : possui
    
    Item "1" --> "many" Resenha : é avaliado por
    Item "1" --> "1" Restaurante : pertence a
    Item "1" --> "many" LogAtualizacao : É referenciado por
    
    Restaurante "1" --> "many" Item : possui
    Restaurante "1" --> "many" Resenha : é avaliado por
    Restaurante "1" --> "1" Endereco : localizado em
    Restaurante "1" --> "1" Usuario : proprietario
    Restaurante "1" --> "many" LogAtualizacao : É referenciado por
    
    Endereco "1" --> "1" Restaurante : pertence a
    Comentario "1" --> "many" Comentario : respostas
    Notificacao "1" --> "1" Usuario : destinatário
    
    LogAtualizacao "1" --> "1" Usuario : Foi gerado por
    LogAtualizacao "1" --> "0..1" Restaurante : Faz referencia a
    LogAtualizacao "1" --> "0..1" Item :  Faz referencia a
    LogAtualizacao "1" --> "many" RepresentacaoModificacao : Contem uma lista de
    
    RepresentacaoModificacao "1" --> "1" LogAtualizacao : armazena as alterações de

```
