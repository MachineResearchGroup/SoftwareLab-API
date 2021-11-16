<h1 align="center">SoftwareLab API</h1>

<!-- 
<p align="center">
 <img src="softwarelab.png" width="520" alt="SoftwareLab Logo" /> -->


<h2 align="center">💻Tecnologias Utilizadas </h2>
<p align="center">
<a href="https://docs.spring.io/spring-boot/docs/2.5.3.RELEASE/reference/html/"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.5.3-brightgreen.svg"/></a>
<!-- <a href="https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MachineResearchGroup/SoftwareLab-API&amp;utm_campaign=Badge_Grade"><img src="https://app.codacy.com/project/badge/Grade/cf253d76b9fa4d4887191a74c6bc30a9"/></a> -->
<a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img alt="JDK" src="https://img.shields.io/badge/JDK-16-orange.svg"/></a>
<a href="https://maven.apache.org/"><img alt="Maven" src="https://img.shields.io/badge/Maven-4.0.0-yellowgreen.svg"/></a>
<a href="https://maven.apache.org/"><img alt="Maven" src="https://img.shields.io/badge/PostgreSQL-blue.svg"/></a>
</p>

### 📁 Como baixar / configurar o projeto

``` 
# Clone o repositório do projeto
$ git clone https://github.com/MachineResearchGroup/SoftwareLab-API.git

Apos clonar altere as seguintes informações no application-homol.properties
spring.datasource.url (nome do banco de dados e porta utilizada)
spring.datasource.username (usuário do banco de dados)
spring.datasource.password (senha do banco de dados)

# Instalar as dependências
$ maven install 
```

### 🐋 Como criar a imagem do Docker

1. Criando o container
   
   a. Abrir no terminal a pasta em que se encontra o docker-compose:
    ```sh
    cd /SoftwareLab-API/docker
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

OBS.: O host do banco de dados precisa estar igual ao nome do serviço definido no docker-compose.yaml


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. 
There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[DBeaver]: <https://dbeaver.io/download/>
