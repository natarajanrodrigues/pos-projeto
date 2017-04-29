/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author natarajan
 */
@Entity
public class GithubUser {
 
    private static final long serialVersionUID = 123L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String login;
    private String followersURL;
    private String organizationsURL;
    private String reposURL;
    private double rank;
    private List<GithubRepository> repositories;

    public GithubUser() {
        this.repositories = new ArrayList();
    }

    public GithubUser(String login, String followersURL, String organizationsURL, String reposURL) {
        this.login = login;
        this.followersURL = followersURL;
        this.organizationsURL = organizationsURL;
        this.reposURL = reposURL;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFollowersURL() {
        return followersURL;
    }

    public void setFollowersURL(String followersURL) {
        this.followersURL = followersURL;
    }

    public String getOrganizationsURL() {
        return organizationsURL;
    }

    public void setOrganizationsURL(String organizationsURL) {
        this.organizationsURL = organizationsURL;
    }

    public String getReposURL() {
        return reposURL;
    }

    public void setReposURL(String reposURL) {
        this.reposURL = reposURL;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }

    public void addRepository(GithubRepository githubRepository) {
        this.repositories.add(githubRepository);
    }
        
    public void removeRepository(GithubRepository githubRepository) {
        this.repositories.remove(githubRepository);
    }

    @Override
    public String toString() {
        return "GithubUser{" + "login=" + login + ", followersURL=" + followersURL + ", organizationsURL=" + organizationsURL + ", reposURL=" + reposURL + ", rank=" + rank + ", repositories=" + repositories + '}';
    }
    
    
    
}
