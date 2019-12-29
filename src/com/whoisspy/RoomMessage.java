package com.whoisspy;

public class RoomMessage {

    public enum OP {
        playerJoin,     //玩家加入房間，更新房間狀態
        playerLeave,    //玩家離開房間，更新房間狀態
        playerWait,     //等待人數到齊
        gameStart,      //人數到齊，通知所有玩家遊戲開始
        gameRoundStart, //通知所有玩家回合開始
        gameWord,       //分配所有玩家的單字，並告訴玩家臥底或平民身分，玩家端顯示大概5秒後顯示輸入框讓玩家描述單字，並等待大家回傳文字描述，估計 30 秒
        gameRoundWait,  //通知大家等待回傳文字描述，收集到所有玩家的描述
        gameWordDes,    //玩家回傳單字描述
        gamePlayersDes, //其他玩家的單字描述，估計顯示 10 秒，並直接顯示輸入框讓大家猜測誰是臥底
        gameVote,      //所有玩家猜測誰是臥底
        gamePlayerVotes, //告知所有玩家的猜測票數
        gameGuessError, //猜錯臥底，告知所有遊戲玩家，該平民狀態設定為死亡
        gameGuessSuccess, //猜對臥底，告知所有遊戲玩家，該臥底設定狀態為死亡，回合結束
        gameRoundOver,  //告知玩家們遊戲結束，並告知平名單字跟臥底單字，等待約 10 秒，進入下一回合
        gameWait,       //回合間等待，估計 5 秒開始下一回合
        gameAnswer,     //如果臥底票數較多，通知所有玩家誰是臥底
    }

    public enum Status {
        process,
        success,
        failure
    }

    public OP op;
    public Status status;
    public String msg;
    public String data;

    public RoomMessage(OP op, Status status, String msg, String data) {
        this.op = op;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public OP getOp() {
        return op;
    }

    public void setOp(OP op) {
        this.op = op;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
