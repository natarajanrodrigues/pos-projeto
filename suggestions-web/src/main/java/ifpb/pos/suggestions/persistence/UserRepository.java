/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.persistence;

import ifpb.pos.suggestions.models.UserApp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author natarajan
 */
@Stateless
public class UserRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    public void save(UserApp user) {
        em.persist(user);
    }
    
    public void update(UserApp user){
        em.merge(user);
    }
    
    public void delete(UserApp user) {
        em.remove(user);
    }
    
    public UserApp get(Long idUser){
        return em.find(UserApp.class, idUser);
    }
    
    public List<UserApp> getAll() {
        return em.createNativeQuery("SELECT * FROM UserApp", UserApp.class).getResultList();
    }
    
}
