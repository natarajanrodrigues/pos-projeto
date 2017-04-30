/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.mdb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
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
    @JMSConnectionFactory("jms/suggCon")
    private JMSContext context;

    @Resource(lookup="jms/suggQueue")
    Queue queue;
    
    public void sendMessage(String userIdString){
        JMSProducer producer = context.createProducer();
        TextMessage message = context.createTextMessage(userIdString);
        producer.send((Destination) queue, message);
    }
    
}
