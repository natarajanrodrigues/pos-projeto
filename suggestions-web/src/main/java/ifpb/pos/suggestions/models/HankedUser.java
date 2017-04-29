/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pos.suggestions.models;

/**
 *
 * @author natarajan
 */
public class HankedUser {
    
    private Long idUser;
    private double ranking;
    private int rankingPosition;

    public HankedUser() {
    }

    public HankedUser(Long idUser, double ranking, int rankingPosition) {
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
