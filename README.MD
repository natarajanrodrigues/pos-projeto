### Passo a passo para rodar o Suggestions API

Esta versão do Suggestions roda sobre o Docker. Abaixo seguem os passos para inicializar e utiliza-lo.


1. Certifique-se que seu serviço Docker esteja rodando.

2. Inicialize o `docker-compose` incluso no projeto. Para isso, já provemos um script sh que pode ser executado da seguinte forma:

  a. **unix/Linux/macOs**

    Numa janela de terminal, navegue até o caminho da pasta raiz deste projeto.
    ```
      sh run.sh
    ```
  b. **Windows (versões que contém powershel - Windows 8 ou +):**

    Dentro da pasta raiz deste projeto, clique no arquivo `run.sh`; ou então, através de um terminal de comando, navegue até a pasta raiz deste projeto e execute:
    ```
      sh run.sh
    ```

3. Depois de alguns instantes, tente abrir o seguinte caminho no navegador: [http://localhost:8090/suggestions-web/](http://localhost:8090/suggestions-web/). Depois de inicializado, o serviço deve mostrar um mensagem de boas vidas.


Para utilizar o Suggestions, comece visitando a [documentação da API](https://natarajanrodrigues.gitbooks.io/suggestions-api/content/) para ver exemplos de uso e outras descrições. 
