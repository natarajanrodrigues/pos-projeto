/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author natarajan
 */
@Entity
public class RankedUser implements Serializable{
    private static final long serialVersionUID = 1989L;
    
    @Id
    private Long idUser;
    private double ranking;
    private int rankingPosition;

    public RankedUser() {
    }

    public RankedUser(Long idUser, double ranking, int rankingPosition) {
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

    public int getRankingPosition() {
        return rankingPosition;
    }

    public void setRankingPosition(int rankingPosition) {
        this.rankingPosition = rankingPosition;
    }
    
    
}
