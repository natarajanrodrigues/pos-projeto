/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.web.resources;

import ifpb.pos.suggestions.models.ErrorMessage;
import ifpb.pos.suggestions.models.GithubRepository;
import ifpb.pos.suggestions.models.SimpleUser;
import ifpb.pos.suggestions.models.UserApp;
import ifpb.pos.suggestions.services.UserService;
import java.net.URI;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author natarajan
 */
@Path("user")
public class UserResources {
    
    @Context
    private ResourceContext resourceContext;
    
    @EJB
    UserService userService;
    
    @GET
    @Path("{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("idUser") String idUser){
        UserApp user = userService.getUser(idUser);
        if (user != null) {
            return Response.ok().entity(new SimpleUser(user)).build();
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }    
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(SimpleUser simpleUser, @Context UriInfo uriInfo){
            
        System.out.println(simpleUser);
        String githubAccount = simpleUser.getGithubAccount();
        String linkedinAccount = simpleUser.getLinkedinAccount();

        UserApp user = new UserApp(githubAccount, linkedinAccount);
        
        try {
            userService.createUser(user);
            simpleUser.setId(user.getId());
            URI uriUser = uriInfo.getBaseUriBuilder()
                .path(UserResources.class) 
                .path(user.getId().toString())
                .build();
            return Response.created(uriUser).entity(simpleUser).build();
            
        } catch(EJBException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }
            
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, SimpleUser simpleUser){

        try {
            UserApp updateUser = userService.updateUser(id, simpleUser);
            if (updateUser != null) {
                return Response.ok().entity(simpleUser).build();
            } else {
                return Response.status(Status.NO_CONTENT).build();
            }
            
        } catch(EJBException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }
        
    }
    
    @GET
    @Path("{idUser}/project")
    public Response getProjects(
            @PathParam("idUser") String idUser) {
        
        UserApp user = userService.getUser(idUser);
        if (user != null) {
            List<GithubRepository> repos = userService.getAllRepositorys(user);
            GenericEntity<List<GithubRepository>> entity = new GenericEntity<List<GithubRepository>>(repos) {};
            return Response.ok().entity(entity).build();
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }
    }
    
    @GET
    @Path("{idUser}/project/{language}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProjectsWithLanguage(
            @PathParam("idUser") String idUser, 
            @PathParam("language") String language, 
            @Context UriInfo info) {
        
        UserApp user = userService.getUser(idUser);
        if (user != null) {
            List<GithubRepository> repos = userService.getAllRepositorysByLanguage(user, language);
            GenericEntity<List<GithubRepository>> entity = new GenericEntity<List<GithubRepository>>(repos) {};
            return Response.ok().entity(entity).build();
        } else {
            return Response.status(Status.NO_CONTENT).build();
        }
        
        
    }
    
    @GET
    @Path("{idUser}/job")
    public Response getJobs(@PathParam("idUser") String idUser){
        return Response.ok().entity("retorna coleção de jobs do usuario: " + idUser).build();
    }
    
    @GET
    @Path("{idUser}/skill")
    public Response getSkills(@PathParam("idUser") String idUser){
        return Response.ok().entity("retorna skills do usuario: " + idUser).build();
    }

    @GET
    @Path("{idUser}/skill/{language}")    
    public Response getSkillsLanguage(
            @PathParam("idUser") String idUser, 
            @PathParam("language") String language
            ){
        return Response.ok().entity("retorna skills do usuario: " + idUser + " com a language: " + language).build();
    }
    
    @GET
    @Path("{idUser}/recomendation")
    public Response getUsersRecomendations(
            @PathParam("idUser") String idUser
            ){
        return Response.ok().entity("retorna recomendation do usuario: " + idUser).build();
    }
    
}
