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