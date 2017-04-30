/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author natarajan
 */
//@XmlRootElement
@Entity
public class UserApp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String githubAccount;
    
    @Column(unique = true)
    private String linkedinAccount;
    
    @JsonIgnore(true)
    private String followersGithubURL;
    
    @JsonIgnore(true)
    private String orgsGithubURL;
    
    @JsonIgnore(true)
    private String reposGithubURL;
    
    private double rank;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "userapp_id")
    private List<GithubRepository> repositories;
    
    public UserApp() {
        this.repositories = new ArrayList<>();
    }

    public UserApp(String githubAccount, String linkedinAccount) {
        this.githubAccount = githubAccount;
        this.linkedinAccount = linkedinAccount;
        this.repositories = new ArrayList<>();
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

    public String getFollowersGithubURL() {
        return followersGithubURL;
    }

    public void setFollowersGithubURL(String followersGithubURL) {
        this.followersGithubURL = followersGithubURL;
    }

    public String getOrgsGithubURL() {
        return orgsGithubURL;
    }

    public void setOrgsGithubURL(String orgsGithubURL) {
        this.orgsGithubURL = orgsGithubURL;
    }

    public String getReposGithubURL() {
        return reposGithubURL;
    }

    public void setReposGithubURL(String reposGithubURL) {
        this.reposGithubURL = reposGithubURL;
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

    public void setRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }
    
    public void addRepository(GithubRepository githubRepository) {
        this.repositories.add(githubRepository);
    }
        
    public void removeRepository(GithubRepository githubRepository) {
        this.repositories.remove(githubRepository);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserApp other = (UserApp) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserApp{" + "id=" + id + ", githubAccount=" + githubAccount + ", linkedinAccount=" + linkedinAccount + ", followersGithubURL=" + followersGithubURL + ", orgsGithubURL=" + orgsGithubURL + ", reposGithubURL=" + reposGithubURL + ", rank=" + rank + ", repositories=" + repositories + '}';
    }

    
}
