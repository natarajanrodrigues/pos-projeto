/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.GithubUser;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natarajan
 */
public class GithubClient {

    private final Client cliente = ClientBuilder.newClient();
    private final WebTarget gitApiTarget = cliente.target("https://api.github.com")
            .queryParam("access_token", "884d4581f490e9832cd001e5dbbe0be6f495197a");

    public GithubClient() {
    }
     
    public GithubUser getGithubUser (String githubUserLogin){
        //https://api.github.com/search/users?q=natarajanrodrigues+in:login        
        //String query = String.format("%s+in:login", githubUser);
        String query = String.format("%s", githubUserLogin);
        Response response = gitApiTarget
                .path("search/users")
                .queryParam("q", query)
                .request()
                .get();
        
//        return extractUserJson(response, githubUserLogin);// devolve String
        
        return extractUserValues(response, githubUserLogin);
    }

    public List<GithubRepository> getAllUserRepos(String githubUserLogin) {        
        Response response = gitApiTarget
                .path("users/{user}/repos")
                .resolveTemplate("user", githubUserLogin)
                .request().get();
        
        String resultJson = response.readEntity(String.class);
        JsonArray array = JsonUtils.getJsonArrayFromString(resultJson);
//        JsonArray array = response.readEntity(JsonArray.class);
        return extractReposWithGSON(array);        
    }
    
    public List<GithubRepository> getAllUserReposByLanguage(String githubUserLogin, String language) {        
         
//        String query = String.format("user:%s language:%s&topic:%s", githubUser, language, language);
        
        String query = String.format("user:%s language:%s", githubUserLogin, language);
        
        Response response = gitApiTarget
                .path("search/repositories")
                .queryParam("q", query)
                .request().get();
        String resultJson = response.readEntity(String.class);
        JsonArray array = JsonUtils.getJsonElementFromString(resultJson, "items").getAsJsonArray();
        return extractReposWithGSON(array);
    }
    
    public List<GithubRepository> extractReposWithGSON(JsonArray jsonArray){
        List<GithubRepository> result = new ArrayList<>();
        
//        JsonArray jsonArray = JsonUtils.getJsonArrayFromString(stringJsonArray);
        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            
            JsonObject asJsonObject = iterator.next().getAsJsonObject();
            
            GithubRepository repo = new GithubRepository();
            repo.setId(asJsonObject.get("id").getAsLong());
            repo.setName(asJsonObject.get("name").getAsString());
            try {
                // a isJsonNull NÃO FUNCIONA!!!
                repo.setLanguage(asJsonObject.get("language").getAsString());
            } catch(Exception e) {}
            repo.setLanguages_url(asJsonObject.get("languages_url").getAsString());
            repo.setLanguages(extractLanguagesRepo(repo.getLanguages_url()));
            result.add(repo);
        }
        
        return result;
    } 
    
    private String extractUserJson(Response response, String githubUserLogin) {
        String resultResponse = response.readEntity(String.class);
        System.out.println(resultResponse);
        int totalCount = JsonUtils.getJsonElementFromString(resultResponse, "total_count").getAsInt();
        int userIndex;
        
        
        if (totalCount == 0) 
            return null;
        else {
            JsonArray array = JsonUtils.getJsonElementFromString(resultResponse, "items").getAsJsonArray();      
            userIndex = 0;
            if (totalCount > 1) {
                for (int i = 0; i < totalCount; i++) {
                    JsonObject userJsonObject = array.get(i).getAsJsonObject();
                    String loginUser = userJsonObject.get("login").getAsString();
                    if (loginUser.equals(githubUserLogin))
                        return userJsonObject.toString();
                }   
            } else {
                JsonObject userJsonObject = array.get(0).getAsJsonObject();
                return userJsonObject.toString();
            }
        }
        
//        JsonObject userJsonObject = array.get(0).getAsJsonObject();        
//        return userJsonObject.toString();
        return null;
    }
    
    private GithubUser extractUserValues(Response response, String githubUserLogin) {

        String userJson = extractUserJson(response, githubUserLogin);
        
        if (userJson != null) {
            String login = JsonUtils.getJsonElementFromString(userJson, "login").getAsString();
            String urlFollowers = JsonUtils.getJsonElementFromString(userJson, "followers_url").getAsString();
            String urlOrgs = JsonUtils.getJsonElementFromString(userJson, "organizations_url").getAsString();
            String urlRepos = JsonUtils.getJsonElementFromString(userJson, "repos_url").getAsString();
            return new GithubUser(login, urlFollowers, urlOrgs, urlRepos);
        }
        
        return null;
    }
    
    
    public List<String> extractLanguagesRepo(String urlRepoLanguages) {        
        
        List<String> result = new ArrayList<>();
        Client localCliente = ClientBuilder.newClient();
        WebTarget target = localCliente.target(urlRepoLanguages);
        Response response = target
                .request().get();
        String resultResponse = response.readEntity(String.class);
        JsonObject jsonObjectFromString = JsonUtils.getJsonObjectFromString(resultResponse);

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectFromString.entrySet();
        entrySet.forEach(e->{
            result.add(e.getKey()); 
        });
        
        return result;
    }

    private int calculateSizeList(String url) {
        Client localCliente = ClientBuilder.newClient();
        WebTarget target = localCliente.target(url);
        Response response = target
                .request().get();
        JsonArray jsonArray = JsonUtils.getJsonArrayFromString(response.readEntity(String.class));
        return jsonArray.size();
    }

    private int numCommittesCurrentMonth(String githubUserLogin){
        //https://api.github.com/search/commits?q=user:vmvini+committer-date:>2017-01-31
        //                .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        String stringDate = firstOfMonth.format(DateTimeFormatter.ISO_DATE);
        
        String query = String.format("user:%s author-date:>%s", githubUserLogin, stringDate);
        
        Response response = gitApiTarget
                .path("search/commits")
                .queryParam("q", query)
                .request()

                .accept("application/vnd.github.cloak-preview")
                .get();
        
        String resultResponse = response.readEntity(String.class);
        
        return JsonUtils.getJsonElementFromString(resultResponse, "total_count").getAsInt();
        
    }

    private double rank(GithubUser githubUser) {
        
        int numContribution = numCommittesCurrentMonth(githubUser.getLogin());
        int numFollowers    = calculateSizeList(githubUser.getFollowersURL());
//        int numRepos        = calculateSizeList(githubUser.getReposURL());
        int numRepos        = githubUser.getRepositories().size();
        int numOrgs         = calculateSizeList(githubUser.getOrganizationsURL());
        
        System.out.println("contr: "+ numContribution);
        System.out.println("numFoll: "+ numFollowers);
        System.out.println("Repos: "+ numRepos);
        System.out.println("orgs: "+ numOrgs);
        
        long soma = numContribution + numFollowers + numRepos + numOrgs;
        double rankNormal = (double) 1 / ( 1 + soma);
        
        return 1 - rankNormal;
    }
    
    public GithubUser updateGithubUserInfos(GithubUser githubUser) {
        if (validGithubUser(githubUser)) {
            List<GithubRepository> newRepos = getAllUserRepos(githubUser.getLogin());
            
            //update the repositories
            Iterator<GithubRepository> iteratorAncienteRepo = githubUser.getRepositories().iterator();
            while (iteratorAncienteRepo.hasNext()) {
                GithubRepository ancientUserRepo = iteratorAncienteRepo.next();
                if (!newRepos.contains(ancientUserRepo)) {
                    githubUser.removeRepository(ancientUserRepo);
                } else {
                    newRepos.remove(ancientUserRepo);
                }
            }
            newRepos.forEach(r->{
                githubUser.addRepository(r);
            });
            
            //do ranking magic
            githubUser.setRank(rank(githubUser));
        }
        return githubUser;
    }
    
    public boolean validGithubUser(GithubUser githubUser){
        if (!isValid(githubUser.getLogin()))
            return false;
        if (!isValid(githubUser.getFollowersURL()))
            return false;
        if (!isValid(githubUser.getOrganizationsURL()))
            return false;
        if (!isValid(githubUser.getReposURL()))
            return false;
        
        return true;
    }
    
    
    public boolean isValid(String s){
        return (s != null && !s.trim().equals(""));
    }

}
