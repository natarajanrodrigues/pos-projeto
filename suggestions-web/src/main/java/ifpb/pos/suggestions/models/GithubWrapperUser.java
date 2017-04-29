/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 *
 * @author natarajan
 */
@Value.Immutable
@JsonSerialize(as=ImmutableGithubWrapperUser.class)
@JsonDeserialize(as=ImmutableGithubWrapperUser.class)
public interface GithubWrapperUser {
    
    String login();
    String followers_url();
    String organizations_url();
    String repos_url();
    
}
