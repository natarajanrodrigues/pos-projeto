/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.mdb;

import ifpb.pos.suggestions.services.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author natarajan
 */

@MessageDriven(
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
            @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/suggQueue")
        }
)
public class CadastroListener implements MessageListener {
    
    @EJB
    private UserService userService;

    @Override
    public void onMessage(Message message) {
        try {
            String githubUserLogin = message.getBody(String.class);
            
            userService.atualizarDadosUser(githubUserLogin);
            
        } catch (JMSException ex) {
            Logger.getLogger(CadastroListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
