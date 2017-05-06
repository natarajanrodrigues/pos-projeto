/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import java.io.Serializable;


/**
 *
 * @author natarajan
 */
public class SimpleUser implements Serializable {

    private Long id;
    private String githubAccount;
    private String linkedinAccount;
    
    public SimpleUser() {
    }
    
    public SimpleUser(UserApp userApp) {
        this.id = userApp.getId();
        this.githubAccount = userApp.getGithubAccount();
        this.linkedinAccount = userApp.getLinkedinAccount();
    }

    public SimpleUser(String githubAccount, String linkedinAccount) {
        this.githubAccount = githubAccount;
        this.linkedinAccount = linkedinAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGithubAccount() {
        return githubAccount;
    }

    public void setGithubAccount(String githubAccount) {
        this.githubAccount = githubAccount;
    }

    public String getLinkedinAccount() {
        return linkedinAccount;
    }

    public void setLinkedinAccount(String linkedinAccount) {
        this.linkedinAccount = linkedinAccount;
    }

    @Override
    public String toString() {
        return "SimpleUser{" + "id=" + id + ", githubAccount=" + githubAccount + ", linkedinAccount=" + linkedinAccount + '}';
    }
    
    public UserApp toUserApp() {
        UserApp user = new UserApp();
        user.setId(this.id);
        user.setGithubAccount(this.githubAccount);
        user.setLinkedinAccount(this.linkedinAccount);
        return user;
    }
    
}
