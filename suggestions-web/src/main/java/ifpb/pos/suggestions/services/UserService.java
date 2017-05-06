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
import ifpb.pos.suggestions.models.SimpleUser;
import ifpb.pos.suggestions.models.UserApp;
import ifpb.pos.suggestions.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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

    public Long createUser(UserApp userApp){
        
        validatePreExistingUserToCreate(userApp);
        UserApp validate = validate(userApp);
        Long idSaved = userRepository.save(userApp);
        queueProducer.sendMessage(userApp.getGithubAccount());
        return idSaved;
        
    }
    
    private UserApp validate(UserApp userApp) {
        if (!isValid(userApp.getGithubAccount()) || !isValid(userApp.getLinkedinAccount())) {
            throw new EJBException("Dados inválidos. Verifique se os parâmetros "
                    + "do usuário informados não estão nulos ou vazios.");
        } else {
            GithubUser githubUser = githubClient.getGithubUser(userApp.getGithubAccount());
            if (githubUser == null) {
                throw new EJBException("Usuário de Github inválido.");
            } else {
                userApp.setFollowersGithubURL(githubUser.getFollowersURL());
                userApp.setOrgsGithubURL(githubUser.getOrganizationsURL());
                userApp.setReposGithubURL(githubUser.getReposURL());   
                return userApp;
            }
        }
    }
    
    private void validatePreExistingUserToCreate(UserApp userApp) {
        
        List<UserApp> result = userRepository.getByLinkedinOrGithubAccount(
                userApp.getGithubAccount(), 
                userApp.getLinkedinAccount());
        if (result.size() > 0 )
            throw new EJBException("Já existe cliente com os dados informados.");
    }
    
    public UserApp updateUser(Long userId, SimpleUser simpleUser) {
        //validar
        
        UserApp get = userRepository.get(userId);
        simpleUser.setId(userId);
        validateToUpdate(simpleUser);
        if (get != null) {
            UserApp newUser = validate(simpleUser.toUserApp());
            newUser.setId(userId);
            userRepository.update(newUser);
            queueProducer.sendMessage(newUser.getGithubAccount());
            return get;
        }
        return null;
    }
    
    private void validateToUpdate(SimpleUser simpleUser) {
        System.out.println("USER to update validate: " + simpleUser);
        UserApp byGithubAccount = userRepository.getByGithubAccount(simpleUser.getGithubAccount());
        UserApp byLinkedinAccount = userRepository.getByLinkedinAccount(simpleUser.getLinkedinAccount());
        if (byGithubAccount != null && !byGithubAccount.getId().equals(simpleUser.getId()) )
            throw new EJBException("Já existe cliente com esta conta de github.");
        if (byLinkedinAccount != null && !byLinkedinAccount.getId().equals(simpleUser.getId()))
            throw new EJBException("Já existe cliente com esta conta de linkedin.");
        
    }
    
    public UserApp getUser(Long userId) {
        
        return userRepository.get(userId);
    }
    
    public List<SimpleUser> getByLinkedinAndGihub(SimpleUser simpleUser) {
        
        List<UserApp> resultSearch 
                = userRepository.getByLinkedinAndGithubAccount(simpleUser.getGithubAccount(), simpleUser.getLinkedinAccount());
        List<SimpleUser> resultList = new ArrayList<>();
        
        resultSearch.forEach((r) -> {
            SimpleUser u = new SimpleUser(r);
            resultList.add(u);
        });
                
        return resultList;        
    }
    
    public List<SimpleUser> getByLinkedinOrGihub(SimpleUser simpleUser) {
        
        List<UserApp> resultSearch 
                = userRepository.getByLinkedinOrGithubAccount(simpleUser.getGithubAccount(), simpleUser.getLinkedinAccount());
        List<SimpleUser> resultList = new ArrayList<>();
        
        resultSearch.forEach((r) -> {
            SimpleUser u = new SimpleUser(r);
            resultList.add(u);
        });
                
        return resultList;        
    }

    public UserApp getUser(String userId) {
        Long longId = new Long(userId);
        return getUser(longId);
    }
    
    
    public boolean isValid(String s){
        return (s != null && !s.trim().equals(""));
    }
    
    public List<GithubRepository> getAllRepositorys(UserApp user) {
        //aqui pode alterar para ir buscar atualizar os repositórios, ou ir buscá-los novamente.
        if (user != null) {
            return user.getRepositories();
        }
        return null;
    }
    
    public List<GithubRepository> getAllRepositorysByLanguage(UserApp user, String language) {
        
        List<GithubRepository> selectedRepos = new ArrayList<>();
        for (GithubRepository r : user.getRepositories()) {
            if (r.getLanguages().contains(language))
                selectedRepos.add(r);
        }
        return selectedRepos;
    }
    
    public List<RankedUser> getAllHank(){
        return userRepository.getTotalRank();
    }
    
    public RankedUser getHankedUser(String userId){
        
        ///MUDAR AQUI!!!! 
        
        Long longId = new Long(userId);
        UserApp get = userRepository.get(longId);
        GithubUser githubUser = githubClient.getGithubUser(get.getGithubAccount());
        return new RankedUser(longId, githubUser.getRank(), 1L);
    } 
    
    public RankedUser getHankedUserBetter(String userId){
        
        return userRepository.getOneRank(new Long(userId));
    } 
    
    
    public void atualizarDadosUser(String githubUserLogin) {
        System.out.println("[atualizando dados de: ]" + githubUserLogin);
                
        UserApp userApp = userRepository.getByGithubAccount(githubUserLogin);

        if (userApp != null) {
            List<GithubRepository> newRepos = githubClient.getAllUserRepos(githubUserLogin);

            //update the repositories
            Iterator<GithubRepository> iteratorAncienteRepo = userApp.getRepositories().iterator();
            while (iteratorAncienteRepo.hasNext()) {
                GithubRepository ancientUserRepo = iteratorAncienteRepo.next();
                if (!newRepos.contains(ancientUserRepo)) {
                    iteratorAncienteRepo.remove();
                } else {
                    newRepos.remove(ancientUserRepo);
                }
            }

            for (GithubRepository r : newRepos) {
                userApp.addRepository(r);
            }

            userApp.setRank(githubClient.rankFromUserapp(userApp));

            userRepository.update(userApp);
        }
        
        
    }
    
    

    
}
