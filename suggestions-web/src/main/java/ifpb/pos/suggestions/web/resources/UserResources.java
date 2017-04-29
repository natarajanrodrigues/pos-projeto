/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.web.resources;

import ifpb.pos.suggestions.models.UserApp;
import ifpb.pos.suggestions.persistence.UserRepository;
import javax.ejb.EJB;
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
    UserRepository userRepository;
    
    @GET
    @Path("{idUser}")
    public Response getUser(@PathParam("idUser") String idUser){
        return Response.ok().entity("getUser aqui: " + idUser).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(
            @QueryParam("github_account") String githubAccount, 
            @QueryParam("linkedin_account") String linkedinAccount){
                
        if (githubAccount == null || githubAccount.trim().equals("") 
                || linkedinAccount == null || linkedinAccount.trim().equals("") )
            return Response.noContent().status(Status.BAD_REQUEST).build();
              
        UserApp user = new UserApp(githubAccount, linkedinAccount);
        userRepository.save(user);
        return Response.ok().entity(user).build();
    }
    
    @PUT
    public Response updateUser(){
        return Response.ok().entity("update user").build();
    }
    
    @GET
    @Path("{idUser}/project")
    public Response getProjects(
            @PathParam("idUser") String idUser, 
            @Context UriInfo info) {

        return Response.ok().entity("retornando: " + idUser).build();
    }
    
    @GET
    @Path("{idUser}/project/{language}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProjectsWithLanguage(
            @PathParam("idUser") String idUser, 
            @PathParam("language") String language, 
            @Context UriInfo info) {

        return Response.ok().entity("retornando projetos de  " + idUser + " com a linguagem: " + language).build();
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
