/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.GithubUser;
import ifpb.pos.suggestions.models.UserApp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author natarajan
 */
public class GithubClient {

    private static String token = "3744c2789ffa71c3ed4d76b4f6d4fe002fe041df";            
    private final Client cliente = ClientBuilder.newClient();
    private final WebTarget gitApiTarget = cliente
            .target("https://api.github.com");
//            .queryParam("access_token", "3744c2789ffa71c3ed4d76b4f6d4fe002fe041df"); //outra forma
    
    private String encodedToken;
    private String baseAuth;
    
    public GithubClient() {
        this.encodedToken = Base64.getEncoder().encodeToString(token.getBytes());
        this.baseAuth = "Basic " + encodedToken;
    }
     
    public GithubUser getGithubUser (String githubUserLogin){
        //https://api.github.com/search/users?q=natarajanrodrigues+in:login        
        //String query = String.format("%s+in:login", githubUser);
        String query = String.format("%s", githubUserLogin);
        Response response = gitApiTarget
                .path("search/users")
                .queryParam("q", query)
                .request()
                .header("Authorization", baseAuth)
                .get();
        
        return extractUserValues(response, githubUserLogin);
    }

    public List<GithubRepository> getAllUserRepos(String githubUserLogin) {        
        Response response = gitApiTarget
                .path("users/{user}/repos")
                .resolveTemplate("user", githubUserLogin)
                .request()
                .header("Authorization", baseAuth)
                .get();
        
        String resultJson = response.readEntity(String.class);
        JsonArray array = JsonUtils.getJsonArrayFromString(resultJson);

        return extractReposWithGSON(array);        
    }
    
    public List<GithubRepository> getAllUserReposByLanguage(String githubUserLogin, String language) {        
         
        //String query = String.format("user:%s language:%s&topic:%s", githubUser, language, language);        
        String query = String.format("user:%s language:%s", githubUserLogin, language);
        
        Response response = gitApiTarget
                .path("search/repositories")
                .queryParam("q", query)
                .request()
                .header("Authorization", baseAuth)
                .get();
        String resultJson = response.readEntity(String.class);
        JsonArray array = JsonUtils.getJsonElementFromString(resultJson, "items").getAsJsonArray();
        return extractReposWithGSON(array);
    }
    
    public List<GithubRepository> extractReposWithGSON(JsonArray jsonArray){
        List<GithubRepository> result = new ArrayList<>();
        
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
                .request()
                .header("Authorization", baseAuth)
                .get();
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
                .queryParam("access_token", token)
                .request()
                .get();
        String readEntity = response.readEntity(String.class);
        
        JsonArray jsonArray = JsonUtils.getJsonArrayFromString(readEntity);
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
                .header("Authorization", baseAuth)
                .accept("application/vnd.github.cloak-preview")
                .get();
        
        String resultResponse = response.readEntity(String.class);
        
        return JsonUtils.getJsonElementFromString(resultResponse, "total_count").getAsInt();
        
    }


    private double rankByValues(
            String githubLong,
            String followersUrl, 
            String orgsUrl, 
            int reposSize) {
        int numContribution = numCommittesCurrentMonth(githubLong);
        int numFollowers    = calculateSizeList(followersUrl);
        int numOrgs         = calculateSizeList(orgsUrl);
        int numRepos        = reposSize;
        
        long soma = numContribution + numFollowers + numRepos + numOrgs;
        double rankNormal = (double) 1 / ( 1 + soma);
        
        return 1 - rankNormal;
    }
    
    private double rankFromGithubUser(GithubUser githubUser) {
        
        return rankByValues(
                githubUser.getLogin(), githubUser.getFollowersURL(), 
                githubUser.getOrganizationsURL(), githubUser.getRepositories().size());
    }
    
    public double rankFromUserapp(UserApp userApp) {
        
        return rankByValues(
                userApp.getGithubAccount(), userApp.getFollowersGithubURL(), 
                userApp.getOrgsGithubURL(), userApp.getRepositories().size());
    }

}
