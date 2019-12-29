package com.whoisspy;

import com.whoisspy.client.game.PlayerData;
import com.whoisspy.client.game.PlayerProfile;

public class PlayerGameInfo {

    private PlayerData playerData;
    private String type;
    private String word;

    public PlayerGameInfo(PlayerData playerData, String type, String word) {
        this.playerData = playerData;
        this.type = type;
        this.word = word;
    }

    public PlayerGameInfo(PlayerData playerData) {
        this.playerData = playerData;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
