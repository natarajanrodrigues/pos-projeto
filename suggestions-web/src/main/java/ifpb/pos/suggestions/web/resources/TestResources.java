/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.web.resources;

import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.GithubUser;
import ifpb.pos.suggestions.models.GithubWrapperUser;
import ifpb.pos.suggestions.models.ImmutableGithubWrapperUser;
import ifpb.pos.suggestions.services.GithubClient;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;

/**
 *
 * @author natarajan
 */
@Path("test")
@Stateless
public class TestResources {
    
    GithubClient service = new GithubClient();
    
    @GET
    @Path("{githubUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("githubUser") String gitUser){
        List<GithubRepository> repos = service.getAllUserRepos(gitUser);
        
        GenericEntity<List<GithubRepository>> entity = new GenericEntity<List<GithubRepository>>(repos) {};
        return Response.ok().entity(entity).build();
    }
    
    @GET
    @Path("{githubUser}/language/{language}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(
            @PathParam("githubUser") String gitUser, 
            @PathParam("language") String language){
        List<GithubRepository> repos = service.getAllUserReposByLanguage(gitUser, language);
        
        GenericEntity<List<GithubRepository>> entity = new GenericEntity<List<GithubRepository>>(repos) {};
        return Response.ok().entity(entity).build();
    }
    
    @GET
    @Path("user/{githubUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@PathParam("githubUser") String gitUser){
        
        GithubUser resultUser = service.getGithubUser(gitUser);
        return Response.ok().entity(resultUser).build();
    }
    
    
}
