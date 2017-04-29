/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author natarajan
 */
@XmlRootElement
@Entity
public class UserApp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String githubAccount;
    private String linkedinAccount;
    
    @OneToOne(mappedBy = "user")
    private GithubUser githubUser;

    public UserApp() {
        
    }

    public UserApp(String githubAccount, String linkedinAccount) {
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
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.githubAccount);
        hash = 29 * hash + Objects.hashCode(this.linkedinAccount);
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
        if (!Objects.equals(this.githubAccount, other.githubAccount)) {
            return false;
        }
        if (!Objects.equals(this.linkedinAccount, other.linkedinAccount)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserApp{" + "id=" + id + ", githubAccount=" + githubAccount + ", linkedinAccount=" + linkedinAccount + '}';
    }
    
    
}
