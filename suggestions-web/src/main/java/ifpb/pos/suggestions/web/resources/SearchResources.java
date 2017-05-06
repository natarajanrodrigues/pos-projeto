/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.web.resources;

import ifpb.pos.suggestions.models.SimpleUser;
import ifpb.pos.suggestions.services.UserService;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author natarajan
 */
@Path("search")
public class SearchResources {
    
    @Context
    private ResourceContext resourceContext;
    
    @EJB
    UserService userService;
    
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByLinkedinAndGithub(
            @QueryParam("github_account") String github, 
            @QueryParam("linkedin_account") String linkedin ){
        
        SimpleUser simpleUser = new SimpleUser(github, linkedin);
        
        List<SimpleUser> byLinkedinAndGihub = userService.getByLinkedinAndGihub(simpleUser);
        GenericEntity<List<SimpleUser>> entity = new GenericEntity<List<SimpleUser>>(byLinkedinAndGihub) {};
            return Response.ok().entity(entity).build();
        
    }
    
    
    
}
