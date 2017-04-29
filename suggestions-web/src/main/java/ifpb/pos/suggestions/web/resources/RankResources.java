/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.web.resources;

import ifpb.pos.suggestions.models.HankedUser;
import ifpb.pos.suggestions.services.UserService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author natarajan
 */
@Path("rank")
@Stateless
public class RankResources {
    
    @EJB
    private UserService userService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRank(){
        List<HankedUser> allHankedUsers = userService.getAllHank();
        GenericEntity<List<HankedUser>> entity = new GenericEntity<List<HankedUser>>(allHankedUsers) {
        };
        return Response.ok().entity(entity).build();
    }
    
    @GET
    @Path("{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersRank(@PathParam("idUser") String idUser){
        HankedUser hankedUser = userService.getHankedUser(idUser);
        return Response.ok().entity(hankedUser).build();
    }
    
}
