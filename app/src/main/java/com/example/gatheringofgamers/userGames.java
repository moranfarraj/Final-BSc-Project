package com.example.gatheringofgamers;

public class userGames {
    public String userId;
    public String gameId;
    public String communicationLevel;
    public String competitiveLevel;
    public String skillLevel;

    public userGames(String userId, String gameId, String communicationLevel, String competitiveLevel, String skillLevel) {
        this.userId = userId;
        this.gameId = gameId;
        this.communicationLevel = communicationLevel;
        this.competitiveLevel = competitiveLevel;
        this.skillLevel = skillLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "userGames{" +
                "userId='" + userId + '\'' +
                ", gameId='" + gameId + '\'' +
                ", communicationLevel='" + communicationLevel + '\'' +
                ", competitiveLevel='" + competitiveLevel + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                '}';
    }

    public String getCommunicationLevel() {
        return communicationLevel;
    }

    public void setCommunicationLevel(String communicationLevel) {
        this.communicationLevel = communicationLevel;
    }

    public String getCompetitiveLevel() {
        return competitiveLevel;
    }

    public void setCompetitiveLevel(String competitiveLevel) {
        this.competitiveLevel = competitiveLevel;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}
