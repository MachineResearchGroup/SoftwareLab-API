# SoftwareLab-API

## Tecnologias utilizadas

| Tecnologia | Versão |
| ------ | ------ |
| Java | 11 |
| Spring Boot | 2.5.4 |
| Maven |  |
| PostgreSQL | 13 |
| Poiji | 3.1.1 |

## Instalação

```sh
# Clonar o repositório do project
$ git clone https://gitlab.com/softwarelab1/api-softwarelab.git

# Instalar as dependências
$ maven install
```

## Banco de Dados

1. Criando o container
   
   a. Abrir no terminal a pasta em que se encontra o docker-compose:
    ```sh
    cd /api-softwarelab/docker
    ```
   b. Rodar o comando:
   ```sh
    docker-compose up -d
    ```
2. Crie uma conexão no [DBeaver]
    ```sh
    ##Infos
    host=localhost
    database=swl
    port=5436
    ```


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. 
There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[DBeaver]: <https://dbeaver.io/download/>
