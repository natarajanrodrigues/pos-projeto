/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.services;

import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.GithubUser;
import ifpb.pos.suggestions.models.RankedUser;
import ifpb.pos.suggestions.models.UserApp;
import ifpb.pos.suggestions.persistence.UserRepository;
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
    
    private GithubClient githubClient;
    
    public UserService() {   
    }

    public void createUser(UserApp userApp){
        if (isValid(userApp.getGithubAccount()) && isValid(userApp.getLinkedinAccount())) {
            GithubUser githubUser = githubClient.getGithubUser(userApp.getGithubAccount());
            // IMPLEMENTAR AINDA
            // de preferência chamar assincronamente
            // githubClient.updateGithubUserInfos(githubUser);
            // para atualizar os repositório e pegar o rank
        }
        userRepository.save(userApp);
    }
    
    public boolean isValid(String s){
        return (s != null && !s.trim().equals(""));
    }
    
    public List<GithubRepository> getAllRepositorys(String idUser) {
        UserApp user = userRepository.get(new Long(idUser));
        return githubClient.getAllUserRepos(user.getGithubAccount());
    }
    
    public List<GithubRepository> getAllRepositorysByLanguage(String idUser, String language) {
        UserApp user = userRepository.get(new Long(idUser));
        return githubClient.getAllUserReposByLanguage(user.getGithubAccount(), language);
    }
    
    public List<RankedUser> getAllHank(){
        //IMPLEMENTAR AINDA
        return null;
    }
    
    public RankedUser getHankedUser(String userId){
        Long longId = new Long(userId);
        UserApp get = userRepository.get(longId);
        GithubUser githubUser = githubClient.getGithubUser(get.getGithubAccount());
        return new RankedUser(longId, githubUser.getRank(), 1);
    } 
    
}
