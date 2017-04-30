/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.persistence;

import ifpb.pos.suggestions.models.RankedUser;
import ifpb.pos.suggestions.models.UserApp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
    
    public UserApp getByGithubAccount(String gitAccount) {
        TypedQuery<UserApp> query = em
                .createQuery("SELECT u FROM UserApp u"
                + " WHERE u.githubAccount = :account", UserApp.class)
                .setParameter("account", gitAccount);
        
        return query.getSingleResult();        
    }
    
    public UserApp getByLinkedinAccount(String linkedinAccount) {
        TypedQuery<UserApp> query = em
                .createQuery("SELECT u FROM UserApp u"
                + " WHERE u.linkedinactcoun = :account", UserApp.class)
                .setParameter("account", linkedinAccount);
        
        return query.getSingleResult();        
    }
    
    public List<UserApp> getAllOrderByRank() {
        return em.createQuery("FROM UserApp u ORDER BY u.rank DESC", UserApp.class).getResultList();
    }
    
    
    public List<RankedUser> getTotalRank() {
        Query query = em.createNativeQuery("SELECT ROW_NUMBER() OVER(ORDER BY rank desc) as "
                + "rankingposition, id as iduser, rank as ranking FROM userapp;", RankedUser.class);
        
        return query.getResultList();        

    }
    
    public RankedUser getOneRank(Long anId) {
        
        Query query = em.createNativeQuery("SELECT ROW_NUMBER() OVER(ORDER BY rank desc) as "
                + "rankingposition, id as iduser, rank as ranking FROM userapp WHERE id = ?1", RankedUser.class)
                .setParameter(1, anId);
        
        return (RankedUser) query.getSingleResult();        

    }
    
}
