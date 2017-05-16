### Passo a passo para rodar o Suggestions API

Esta versão do Suggestions roda sobre o Docker. Abaixo seguem os passos para inicializa-lo.


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


<hr></hr>

### Atualização

O recurso JMS do tipo `Queue` agora é criado através de um `Singleton` (que é a classe `ifpb.pos.suggestion.mdb.StartMDB`) que faz uso da anotação `@JMSDestinationDefinition`.

Esta solução é baseada em apontamento do professor Ricardo Job (@ricardojob) e [desta postagem no weblog de Adam Bien](http://www.adam-bien.com/roller/abien/entry/auto_creating_jms_destinations_with).

Em resumo:
- a classe StartMDB cria o recurso Queue (assim que inicializa a aplicação, já que está anotada com `@Startup` - e apenas uma vez, já que também é um singleton ) e o disponibiliza para injeção através da anotação `@Produces` no método `exposeQueue`;
- para utilizar a `Queue` recém produzida, a classe `ifpb.pos.suggestion.mdb.CadastroProducerQueue` apenas declara o objeto Queue com `@Inject`;
- Ao realizar o `@Produces` do recurso também adicionamos um qualificador; o uso do qualificador aqui é desnecessário, mas deve ser uma boa prática se houvesse mais de um recursos do mesmo tipo que quiséssemos injetar em outros beans;
- ao implementar essa abordagem também preferimos usar o Connection Factory padrão do Payara;

Assim, a nova configuração pode ser visualizada nos seguintes código:
```Java
package ifpb.pos.suggestions.mdb;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;


/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 08/05/2017, 13:49:06
 */

@JMSDestinationDefinitions({
    @JMSDestinationDefinition(
            name = "java:global/jms/suggQueue",
            resourceAdapter = "jmsra",
            interfaceName = "javax.jms.Queue",
            destinationName = "suggQueue",
            description = "My Sync Queue")
})
@Startup
@Singleton
public class StartMDB {    

    @Resource(lookup = "java:global/jms/suggQueue")
    private Queue queue;

    public StartMDB() {
        System.out.println("[Starting MDBs --->]");
    }

    @Produces @MyQueue
    public Queue exposeQueue() {
        return this.queue;
    }

}
```
...

```Java
package ifpb.pos.suggestions.mdb;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author natarajan
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface MyQueue {
}

```

...

```Java
package ifpb.pos.suggestions.mdb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

/**
 *
 * @author natarajan
 */

@Stateless
public class CadastroProducerQueue {
        
    @Inject
    private JMSContext context;

    @Inject @MyQueue
    private Queue queue;

    public void sendMessage(String userIdString){
        JMSProducer producer = context.createProducer();
        TextMessage message = context.createTextMessage(userIdString);
        producer.send((Destination) queue, message);
    }

}

```
