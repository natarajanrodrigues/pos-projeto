/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.services;

import ifpb.pos.suggestions.mdb.CadastroProducerQueue;
import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.GithubUser;
import ifpb.pos.suggestions.models.RankedUser;
import ifpb.pos.suggestions.models.UserApp;
import ifpb.pos.suggestions.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author natarajan
 */
@Stateless
public class UserService {

    
    @EJB
    private UserRepository userRepository;
    
    @EJB
    private CadastroProducerQueue queueProducer;
    
    private GithubClient githubClient = new GithubClient();
    
    public UserService() {   
    }

    public void createUser(UserApp userApp){
        if (isValid(userApp.getGithubAccount()) && isValid(userApp.getLinkedinAccount())) {

            GithubUser githubUser = githubClient.getGithubUser(userApp.getGithubAccount());
            if (githubUser != null) {
                
                userApp.setFollowersGithubURL(githubUser.getFollowersURL());
                userApp.setOrgsGithubURL(githubUser.getOrganizationsURL());
                userApp.setReposGithubURL(githubUser.getReposURL());
                
                userRepository.save(userApp);
                
                queueProducer.sendMessage(userApp.getGithubAccount());
            }
        }
    }
    
    
    
    public boolean isValid(String s){
        return (s != null && !s.trim().equals(""));
    }
    
    public List<GithubRepository> getAllRepositorys(UserApp user) {
//        UserApp user = userRepository.get(new Long(idUser));
//        return githubClient.getAllUserRepos(user.getGithubAccount());
        
        //aqui pode alterar para ir buscar atualizar os repositórios, ou ir buscá-los novamente.
        if (user != null) {
            return user.getRepositories();
        }
        return null;
    }
    
    public List<GithubRepository> getAllRepositorysByLanguage(UserApp user, String language) {
        //UserApp user = userRepository.get(new Long(idUser));
        //return githubClient.getAllUserReposByLanguage(user.getGithubAccount(), language);
        List<GithubRepository> selectedRepos = new ArrayList<>();
        for (GithubRepository r : user.getRepositories()) {
            if (r.getLanguages().contains(language))
                selectedRepos.add(r);
        }
        return selectedRepos;
    }
    
    public List<RankedUser> getAllHank(){
//        List<UserApp> usersOrderedByRank = userRepository.getAllOrderByRank();
//        
//        List<RankedUser> allRanking = new ArrayList<>();
//        
//        for (int i = 0; i < usersOrderedByRank.size(); i++) {
//            UserApp user = usersOrderedByRank.get(i);
//            allRanking.add(new RankedUser(user.getId(), user.getRank(), i + 1));
//        }
//        return allRanking;

        return userRepository.getTotalRank();
    }
    
    public RankedUser getHankedUser(String userId){
        
        ///MUDAR AQUI!!!! 
        
        Long longId = new Long(userId);
        UserApp get = userRepository.get(longId);
        GithubUser githubUser = githubClient.getGithubUser(get.getGithubAccount());
        return new RankedUser(longId, githubUser.getRank(), 1);
    } 
    
    public RankedUser getHankedUserBetter(String userId){
        
        return userRepository.getOneRank(new Long(userId));
    } 
    
    public UserApp getUser(String userId) {
        Long longId = new Long(userId);
        return userRepository.get(longId);
    }

    
    public void atualizarDadosUser(String githubUserLogin) {
        
        UserApp userApp = userRepository.getByGithubAccount(githubUserLogin);

        if (userApp != null) {
            List<GithubRepository> newRepos = githubClient.getAllUserRepos(githubUserLogin);

            //update the repositories
            Iterator<GithubRepository> iteratorAncienteRepo = userApp.getRepositories().iterator();
            while (iteratorAncienteRepo.hasNext()) {
                GithubRepository ancientUserRepo = iteratorAncienteRepo.next();
                if (!newRepos.contains(ancientUserRepo)) {
                    userApp.removeRepository(ancientUserRepo);
                } else {
                    newRepos.remove(ancientUserRepo);
                }
            }

            for (GithubRepository r : newRepos) {
                userApp.addRepository(r);
            }

            userApp.setRank(githubClient.rank(userApp));

            userRepository.update(userApp);
        }
        
        
    }
    
    

    
}
