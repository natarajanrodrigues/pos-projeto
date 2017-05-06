/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author natarajan
 */
//@Entity
//@Table(name = "ranking")
//@NamedQuery(name = "Ranking.all", query = "FROM Ranking")
//@NamedQueries({
//    @NamedQuery(name = "Ranking.all", query = "FROM ranking")
//        , 
//    @NamedQuery(name = "Ranking.user", query = "FROM ranking WHERE iduser = :userapp")
//})
public class RankedUser implements Serializable{
    private static final long serialVersionUID = 1989L;
    
//    @Id
    private Long idUser;
    private double ranking;
    private Long rankingPosition;

    public RankedUser() {
    }

    public RankedUser(Long idUser, double ranking, Long rankingPosition) {
        this.idUser = idUser;
        this.ranking = ranking;
        this.rankingPosition = rankingPosition;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public double getRanking() {
        return ranking;
    }

    public void setRanking(double ranking) {
        this.ranking = ranking;
    }

    public Long getRankingPosition() {
        return rankingPosition;
    }

    public void setRankingPosition(Long rankingPosition) {
        this.rankingPosition = rankingPosition;
    }
    
    
}
