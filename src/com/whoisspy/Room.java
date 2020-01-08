package com.whoisspy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.whoisspy.client.game.PlayerData;
import com.whoisspy.client.game.PlayerProfile;
import com.whoisspy.server.UserConnection;
import org.bson.Document;

import java.util.*;

public class Room {

    private Map<String, UserConnection> clients = new HashMap<>();
    private Map<String, PlayerData> playerDataMap = new HashMap<>();
    private Map<String, PlayerGameInfo> playerGameInfoMap = new HashMap<>();

    private UserConnection roomOwner;
    private RoomInformation roomInformation;

    private Thread gameThread;

    private Gson gson = new Gson();

    private Logger logger;

    public Room(UserConnection roomOwner,
                Integer id,
                String roomName,
                int roomAmount,
                String roomDescription,
                boolean roomPrivate,
                String roomPassword) {

        this.roomOwner = roomOwner;
        roomInformation = new RoomInformation(id, roomName, roomAmount, 0, roomDescription, roomPrivate, roomPassword);
        logger = new Logger("ServerRoom", String.valueOf(getRoomId()));

        //clients.put(roomOwner.getUser().getAccount(), roomOwner);
        gameThread = new Thread(gameThreadRunnable);
        gameThread.start();
    }

    public Runnable gameThreadRunnable = new Runnable() {

        boolean isWin = false;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    if ((getRoomAmount() - getRoomCount()) != 0) {
                        notifyAllPlayerMessage(new Message(Message.OP.playerWait,
                                Message.Status.process,
                                "等待人數到齊後自動開始，目前還缺 " + (getRoomAmount() - getRoomCount()) + " 位玩家",
                                "{}"));
                    } else {
                        notifyAllPlayerMessage(new Message(Message.OP.playerWait,
                                Message.Status.process,
                                "人數到齊囉！遊戲準備開始",
                                "{}"));
                    }
                    for (PlayerData playerData : playerDataMap.values()) {
                        playerData.setPlayerStatus("等待中");
                        playerData.setPlayerText("null");
                        playerData.setPlayerVotes(0);
                    }
                    notifyAllPlayerDataChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getRoomCount() == getRoomAmount()) {
                    // 遊戲開始
                    Map<String, PlayerGameInfo> gameInfoMap = new HashMap<>();

                    try {
                        logger.log("遊戲開始");
                        isWin = false;
                        Message message = new Message(Message.OP.gameStart, Message.Status.process, "gameStart", "{}");
                        notifyAllPlayerMessage(message);
                        for (PlayerData playerData : playerDataMap.values()) {
                            playerData.setPlayerStatus("存活");
                            playerData.setPlayerText("null");
                            playerData.setPlayerVotes(0);
                        }
                        notifyAllPlayerDataChanged();
                        Thread.sleep(5000);

                        // 隨機出一個單字
                        Document wordDocument = roomOwner.getRandomWord();
                        String humanWord = wordDocument.getString("humanWord");
                        String spyWord = wordDocument.getString("spyWord");
                        logger.log(String.format("這場遊戲中平民單字是 '%s' , 臥底單字是 '%s'", humanWord, spyWord));
                        List<String> clientsList = new ArrayList<>(clients.keySet());
                        String spyAccount = clientsList.get(new Random().nextInt(clients.size()));
                        logger.log(spyAccount + " 在這場遊戲中是臥底");

                        //通知所有玩家各自單字並 refresh data
                        for (UserConnection player : clients.values()) {
                            PlayerData playerData = playerDataMap.get(player.getUser().getAccount());
                            playerData.setPlayerStatus("存活");
                            PlayerGameInfo playerGameInfo = new PlayerGameInfo(playerData);
                            if (player.getUser().getAccount().equals(spyAccount)) {
                                playerGameInfo.setType("臥底");
                                playerGameInfo.setWord(spyWord);
                            } else {
                                playerGameInfo.setType("平民");
                                playerGameInfo.setWord(humanWord);
                            }
                            player.send(new Message(Message.OP.gameWord, Message.Status.process, "gameWord", gson.toJson(playerGameInfo)));
                            playerGameInfoMap.put(playerData.getPlayerProfile().getAccount(), playerGameInfo);
                        }

                        logger.log("通知所有玩家單字");
                        notifyAllPlayerDataChanged();

                        Thread.sleep(5000);
                        for (int round = 1; round <= getRoomAmount(); round++) {
                            for (PlayerData playerData : playerDataMap.values()) {
                                playerData.setPlayerText("null");
                                playerData.setPlayerVotes(0);
                            }
                            logger.log("第 " + round + " 回合開始");

                            //通知玩家開始描述 refresh data
                            logger.log("開始作答");
                            message.setOp(Message.OP.gameRoundStart);
                            message.setMsg("開始作答囉～");
                            notifyAlivePlayerMessage(message);

                            Thread.sleep(25000);

                            message.setOp(Message.OP.gameRoundWait);
                            message.setMsg("5 秒後結束描述...");
                            notifyAllPlayerMessage(message);

                            Thread.sleep(5000);

                            //接收所有描述並等待3秒後 refresh data
                            logger.log("接收所有玩家的描述");
                            message.setOp(Message.OP.gameRoundWait);
                            message.setMsg("正在處理大家的描述...");
                            notifyAllPlayerMessage(message);

                            Thread.sleep(3000);

                            logger.log("通知所有玩家其他玩家的描述");
                            notifyAllPlayerDataChanged();

                            logger.log("10 秒後進行誰是臥底投票");
                            message.setOp(Message.OP.gameRoundWait);
                            message.setMsg("10 秒後進行誰是臥底投票～");
                            notifyAllPlayerMessage(message);

                            Thread.sleep(10000);

                            while (true) {
                                //告知玩家開始 refresh data
                                logger.log("開始投票");
                                message.setOp(Message.OP.gameVote);
                                message.setMsg("開始投票！您覺得誰是臥底呢？");
                                JsonObject returnData = new JsonObject();
                                returnData.addProperty("votedPlayer", gson.toJson(getAlivePlayerList()));
                                message.setData(returnData.toString());
                                notifyAlivePlayerMessage(message);

                                Thread.sleep(15000);

                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("5 秒後結束投票...");
                                notifyAllPlayerMessage(message);

                                Thread.sleep(5000);

                                //接收所有票數並等待3秒後 refresh data
                                logger.log("接收所有玩家的票數");
                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("正在處理大家的票數...");
                                notifyAllPlayerMessage(message);

                                Thread.sleep(4000);

                                logger.log("通知所有玩家各自的票數");
                                notifyAllPlayerDataChanged();

                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("票數出爐囉！");
                                notifyAllPlayerMessage(message);

                                logger.log("5 秒後告知票數最高的身分");
                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("5 秒後告知投票結果！！");
                                notifyAllPlayerMessage(message);

                                Thread.sleep(5000);

                                ArrayList<String> votedPlayerList = getVotedPlayerList();
                                if (votedPlayerList.size() > 1) {
                                    //最高票數一位以上 重新投票 並等待3秒後 refresh data
                                    logger.log("最高票數有 " + votedPlayerList.size() + " 位");
                                    message.setOp(Message.OP.gameRoundWait);
                                    message.setMsg("最高票數有 " + votedPlayerList.size() + " 位, 將於 5 秒後重新投票誰是臥底。");
                                    notifyAllPlayerMessage(message);

                                    for (PlayerData playerData : playerDataMap.values()) {
                                        playerData.setPlayerVotes(0);
                                    }
                                    Thread.sleep(5000);
                                } else {
                                    //最高票一位 判斷是否為臥底 並等待3秒後 refresh data
                                    String guessedPlayer = votedPlayerList.get(0);
                                    if (playerGameInfoMap.get(guessedPlayer).getType().equals("臥底")) {
                                        //猜對臥底 遊戲準備結束
                                        logger.log("臥底被猜出來了！！");
                                        message.setOp(Message.OP.gameGuessSuccess);
                                        message.setMsg("感謝各位平民的合作，臥底被殺掉了！！遊戲結束！！");
                                        notifyAllPlayerMessage(message);
                                        isWin = true;
                                    } else if (playerGameInfoMap.get(guessedPlayer).getType().equals("平民")) {
                                        //猜錯臥底 遊戲尚未結束
                                        logger.log("平民被殺掉了！！");
                                        message.setOp(Message.OP.gameGuessError);
                                        message.setMsg("喔不！有一位平民白白被犧牲了！");
                                        notifyAllPlayerMessage(message);
                                    }

                                    //更新死亡玩家狀態 狀態->死亡 票數歸零
                                    playerDataMap.get(guessedPlayer).setPlayerStatus("死亡");
                                    playerDataMap.get(guessedPlayer).setPlayerText("");
                                    playerDataMap.get(guessedPlayer).setPlayerVotes(0);
                                    notifyAllPlayerDataChanged();
                                    Thread.sleep(5000);
                                    break;
                                }
                            } //投票 loop

                            if (isWin) {
                                logger.log("第 " + round + " 回合結束");
                                message.setOp(Message.OP.gameRoundOver);
                                message.setMsg("第 " + round + " 回合結束");
                                notifyAllPlayerMessage(message);
                                Thread.sleep(5000);
                                break;
                            } else if (getCount("平民", "存活") == 1) {
                                logger.log("只剩一位平民，遊戲結束");
                                message.setOp(Message.OP.gameRoundOver);
                                message.setMsg("僅存一位平民，世界幾乎被臥底稱霸了...");
                                notifyAllPlayerMessage(message);
                                Thread.sleep(5000);
                                break;
                            } else if (getCount("臥底", "存活") == 0) {
                                logger.log("平民勝利");
                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("平民大獲全勝！臥底要在好好學習！");
                                notifyAllPlayerMessage(message);
                                Thread.sleep(5000);
                                break;
                            } else if (getCount("平民", "存活") > 1 && round < getRoomAmount()) {
                                logger.log("五秒後開始下一回合");
                                message.setOp(Message.OP.gameRoundWait);
                                message.setMsg("臥底很會藏！將於 5 秒後開始下一回合");
                                notifyAllPlayerMessage(message);
                                Thread.sleep(5000);
                            }
//                            else if (getCount("平民", "存活") == 0) {
////                                logger.log("平民被殺光了");
////                                message.setOp(Message.OP.gameRoundOver);
////                                message.setMsg("平民都死光了，臥底徹底稱霸世界了！");
////                                notifyAllPlayerMessage(message);
////                                Thread.sleep(5000);
////                                break;
////                            }

                        } //回合 loop

                        //遊戲結束
                        logger.log("遊戲結束");
                        message.setOp(Message.OP.gameOver);
                        message.setMsg("遊戲結束！謝謝各位的參與！十秒後自動重新啟動房間");
                        notifyAllPlayerMessage(message);

                        for (PlayerData playerData : playerDataMap.values()) {
                            playerData.setPlayerStatus("等待中");
                            playerData.setPlayerText("null");
                            playerData.setPlayerVotes(0);
                        }
                        notifyAllPlayerDataChanged();

                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } // 遊戲 loop
        }
    };

    public Map<String, UserConnection> getClients() {
        return clients;
    }

    public boolean addPlayer(UserConnection userConnection) {
        User user = userConnection.getUser();
        if (!clients.containsKey(user.getAccount()) && (roomInformation.getRoomCount() + 1) <= roomInformation.getRoomAmount()) {

            //建立好profile及data 就通知房間所有人
            PlayerProfile playerProfile = new PlayerProfile(
                    user.getAccount(),
                    user.getPhotoBase64());
            PlayerData playerData = new PlayerData(playerProfile, "等待中", "null", 0);
            Message message = new Message(Message.OP.playerJoin,
                    Message.Status.process,
                    user.getAccount() + " 加入房間",
                    gson.toJson(playerData));

            notifyAllPlayerMessage(message);

            clients.put(user.getAccount(), userConnection);
            playerDataMap.put(user.getAccount(), playerData);
            roomInformation.setRoomCount(roomInformation.getRoomCount() + 1);

            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(UserConnection userConnection) {
        User user = userConnection.getUser();
        if (clients.containsKey(userConnection.getUser().getAccount())) {

            //先取得data，將本玩家移除後，就通知房間所有人
            PlayerData playerData = playerDataMap.get(user.getAccount());
            Message message = new Message(Message.OP.playerLeave,
                    Message.Status.process,
                    user.getAccount() + " 離開房間",
                    gson.toJson(playerData));

            clients.remove(userConnection.getUser().getAccount());
            playerDataMap.remove(userConnection.getUser().getAccount());
            roomInformation.setRoomCount(roomInformation.getRoomCount() - 1);

            notifyAllPlayerMessage(message);

            return true;
        } else {
            return false;
        }
    }

    public boolean changeOwner(UserConnection newOwner) {
        this.roomOwner = newOwner;
        return true;
    }

    public RoomInformation getRoomInformation() {
        return roomInformation;
    }

    public boolean isOwner(UserConnection userConn) {
        return roomOwner == userConn;
    }

    public boolean isRoomPrivate() {
        return roomInformation.isRoomPrivate();
    }

    public String getRoomName() {
        return roomInformation.getRoomName();
    }

    public int getRoomAmount() {
        return roomInformation.getRoomAmount();
    }

    public String getRoomDescription() {
        return roomInformation.getRoomDescription();
    }

    public String getRoomPassword() {
        return roomInformation.getRoomPassword();
    }

    public int getRoomId() {
        return roomInformation.getRoomId();
    }

    public int getRoomCount() {
        return clients.size();
    }

    public void start() {

    }

    public void notifyAllPlayerMessage(Message message) {

        for (UserConnection player : clients.values()) {
            player.send(message);
        }

    }

    public void notifyAlivePlayerMessage(Message message) {

        for (PlayerData playerData : playerDataMap.values()) {
            if (playerData.getPlayerStatus().equals("存活")) {
                clients.get(playerData.getPlayerProfile().getAccount()).send(message);
            }
        }

    }

    public void notifyAllPlayerDataChanged() {
        JsonObject data = new JsonObject();
        data.addProperty("players", getAllPlayerDataWithoutPhoto());

        notifyAllPlayerMessage(new Message(Message.OP.gameRefresh, Message.Status.process, "gameRefresh", data.toString()));
    }

    public void onChat(Message message) {
        notifyAllPlayerMessage(message);
    }

    public void onPlayerWordDes(Message message) {
        JsonObject data = gson.fromJson(message.getData(), JsonObject.class);
        logger.log(String.format("接收到 '%s' 的描述: '%s'", data.get("account").getAsString(), data.get("des").getAsString()));
        playerDataMap.get(data.get("account").getAsString()).setPlayerText(data.get("des").getAsString());
    }

    public void onVote(Message message) {
        JsonObject data = gson.fromJson(message.getData(), JsonObject.class);
        logger.log(String.format("接收到 '%s' 的投票: '%s'", data.get("account").getAsString(), data.get("guess").getAsString()));
        playerDataMap.get(data.get("guess").getAsString()).addVote();
    }

    private ArrayList<Map.Entry<String, PlayerData>> sortedPlayerVotesList;

    public void doSortPlayerVotes() {
        sortedPlayerVotesList = new ArrayList<>(playerDataMap.entrySet());
        sortedPlayerVotesList.sort(Map.Entry.comparingByValue());
    }

    public ArrayList<String> getVotedPlayerList() {
        doSortPlayerVotes();
        ArrayList<String> votesPlayerList = new ArrayList<>();
        for (int index = sortedPlayerVotesList.size() - 1; index > 0; index--) {
            if (sortedPlayerVotesList.get(index).getValue().getPlayerVotes() == sortedPlayerVotesList.get(index - 1).getValue().getPlayerVotes()) {
                //如果最高票有兩位以上 就都加入並 繼續往下尋找
                if (!votesPlayerList.contains(sortedPlayerVotesList.get(index).getKey())) {
                    votesPlayerList.add(sortedPlayerVotesList.get(index).getKey());
                }
                votesPlayerList.add(sortedPlayerVotesList.get(index - 1).getKey());
            } else {
                //如果最高票只有一位 就直接結束
                votesPlayerList.add(sortedPlayerVotesList.get(index).getKey());
                break;
            }
        }
        return votesPlayerList;
    }

    public ArrayList<String> getAlivePlayerList() {
        ArrayList<String> alivePlayerList = new ArrayList<>();
        for (PlayerData playerData : playerDataMap.values()) {
            if (!playerData.getPlayerStatus().equals("死亡")) {
                alivePlayerList.add(playerData.getPlayerProfile().getAccount());
            }
        }
        return alivePlayerList;
    }

    public int getCount(String type, String status) {
        int count = 0;
        for (PlayerData playerData : playerDataMap.values()) {
            if (playerData.getPlayerStatus().equals(status) &&
                    playerGameInfoMap.get(playerData.getPlayerProfile().getAccount()).getType().equals(type)) {
                count++;
            }
        }
        return count;
    }

    public String getAllPlayerData() {
        List<PlayerData> playerDataArrayList;
        if (playerDataMap.size() != 0) {
            playerDataArrayList = new ArrayList<>(playerDataMap.values());
            String result = gson.toJson(playerDataArrayList, new TypeToken<List<PlayerData>>() {
            }.getType());
            return result;
        } else {
            return "";
        }
    }

    public String getAllPlayerDataWithoutPhoto() {
        List<PlayerData> playerDataArrayList;
        if (playerDataMap.size() != 0) {
            playerDataArrayList = new ArrayList<>(playerDataMap.values());
            for (PlayerData playerData : playerDataArrayList) {
                playerData.getPlayerProfile().setPhotoBase64("");
            }
            String result = gson.toJson(playerDataArrayList, new TypeToken<List<PlayerData>>() {
            }.getType());
            return result;
        } else {
            return "";
        }
    }

}
