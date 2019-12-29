package com.whoisspy.client.game;

public class PlayerData implements Comparable<PlayerData> {

    private PlayerProfile playerProfile;
    private String playerStatus;
    private String playerText;
    private int playerVotes;

    public PlayerData(PlayerProfile playerProfile, String playerStatus, String playerText, int playerVotes) {
        this.playerProfile = playerProfile;
        this.playerStatus = playerStatus;
        this.playerText = playerText;
        this.playerVotes = playerVotes;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public String getPlayerStatus() {
        return playerStatus;
    }

    public String getPlayerText() {
        return playerText;
    }

    public int getPlayerVotes() {
        return playerVotes;
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void setPlayerStatus(String playerStatus) {
        this.playerStatus = playerStatus;
    }

    public void setPlayerText(String playerText) {
        this.playerText = playerText;
    }

    public void setPlayerVotes(int playerVotes) {
        this.playerVotes = playerVotes;
    }

    public void addVote() {
        this.playerVotes += 1;
    }


    @Override
    public int compareTo(PlayerData o) {
        return this.playerVotes - o.getPlayerVotes();
    }

}
