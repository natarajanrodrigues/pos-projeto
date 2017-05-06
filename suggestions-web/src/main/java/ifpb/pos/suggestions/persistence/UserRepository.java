/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.persistence;

import ifpb.pos.suggestions.models.RankedUser;
import ifpb.pos.suggestions.models.UserApp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author natarajan
 */
//@DataSourceDefinition(
//        name = "java:app/java:jdbc/suggestions",
//        className = "org.postgresql.Driver",
//        url = "jdbc:postgresql://ec2-23-23-111-171.compute-1.amazonaws.com:5432/dajp4oacqjj587",
////        url = "jdbc:postgresql://localhost:5432/hotel",
//        user = "dsyylkchaffgme",
//        password = "4899304b73897a14faf91b173885b33bf2879e24d6b955dcdfb5159b370c9703"
//)

@Stateless
public class UserRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    public Long save(UserApp user) {
        em.persist(user);
        em.flush();
        return user.getId();
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
        try {
            
            TypedQuery<UserApp> query = em
                .createQuery("SELECT u FROM UserApp u"
                + " WHERE u.githubAccount = :account", UserApp.class)
                .setParameter("account", gitAccount);
        
            return query.getSingleResult();        
        
        } catch(NoResultException e) {
            System.out.println("Não conseguiu pegar o usuário");
            return null;
        }
        
    }
    
    public UserApp getByLinkedinAccount(String linkedinAccount) {
        try {
            TypedQuery<UserApp> query = em
                    .createQuery("SELECT u FROM UserApp u"
                            + " WHERE u.linkedinAccount = :account", UserApp.class)
                    .setParameter("account", linkedinAccount);

            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Não conseguiu pegar o usuário");
            return null;
        }
    }
    
    public List<UserApp> getByLinkedinOrGithubAccount(String gitAccount, String linkedinAccount) {
        try {
            TypedQuery<UserApp> query = em
                    .createQuery("SELECT u FROM UserApp u"
                            + " WHERE u.linkedinAccount = :linkAccount OR u.githubAccount = :gitAccount", UserApp.class)
                    .setParameter("linkAccount", linkedinAccount)
                    .setParameter("gitAccount", gitAccount);

            List<UserApp> resultList = query.getResultList();
            System.out.println(resultList.size());
            return resultList;
                    
        } catch (NoResultException e) {
            System.out.println("Não conseguiu pegar o usuário");
            return null;
        }
    }
    
    public List<UserApp> getByLinkedinAndGithubAccount(String gitAccount, String linkedinAccount) {
        try {
            TypedQuery<UserApp> query = em
                    .createQuery("SELECT u FROM UserApp u"
                            + " WHERE u.linkedinAccount = :linkAccount AND u.githubAccount = :gitAccount", UserApp.class)
                    .setParameter("linkAccount", linkedinAccount)
                    .setParameter("gitAccount", gitAccount);

            List<UserApp> resultList = query.getResultList();
            System.out.println(resultList.size());
            return resultList;
                    
        } catch (NoResultException e) {
            System.out.println("Não conseguiu pegar o usuário");
            return null;
        }
    }
    
    public List<UserApp> getAllOrderByRank() {
        return em.createQuery("FROM UserApp u ORDER BY u.rank DESC", UserApp.class).getResultList();
    }
    
    public List<RankedUser> getTotalRank() {
        
        //        return em.createNamedQuery("Ranking.all").getResultList();
//        return em.createQuery("FROM ranking r").getResultList();
//        
//        Query query = em
//                .createNativeQuery("SELECT * FROM Ranking");
//        return query.getResultList();
        
//        Query query = em.createNativeQuery("SELECT ROW_NUMBER() OVER(ORDER BY rank desc) as "
//                + "rankingposition, id as iduser, rank as ranking FROM userapp;", RankedUser.class);
//        return query.getResultList();

        Query query = em
                .createNativeQuery("SELECT * FROM Ranking");
        List<Object[]> singleResult = (List<Object[]>) query.getResultList();
        
        if (singleResult == null) {
            return null;
        } else {
            List<RankedUser> list = new ArrayList();
            singleResult.forEach((t) -> {
                list.add(extractRakedUser(t));
            });
            return list;
        }

    }
    
    public RankedUser getOneRank(Long userId) {
        
//        Query query = em.createNativeQuery("SELECT ROW_NUMBER() OVER(ORDER BY rank desc) as "
//                + "rankingposition, id as iduser, rank as ranking FROM userapp WHERE id = ?1", RankedUser.class)
//                .setParameter(1, userId);
//        return (RankedUser) query.getSingleResult();        

//        int i = userId.intValue();
        Query query = em
                .createNativeQuery("SELECT * FROM Ranking where iduser = ?1")
                 .setParameter(1, userId);
        Object[] singleResult = (Object[]) query.getSingleResult();
        
        if (singleResult == null) {
            return null;
        } else {
            return extractRakedUser(singleResult);
        }
        
    }
    
    private RankedUser extractRakedUser(Object[] array) {
        Long rankingposition = (Long) array[0];
        int iduser = new Integer((Integer) array[1]).intValue();
        double ranking = new Double((Double) array[2]).doubleValue();

        return new RankedUser(new Long(iduser), ranking, rankingposition);
    }
    

}
