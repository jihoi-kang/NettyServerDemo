package server;

import io.netty.channel.Channel;

public class MessageHolder {

    private byte sign;
    private byte type;
    private ChatLogs chatLogs;

    public ChatLogs getChatLogs() {
        return chatLogs;
    }

    public void setChatLogs(ChatLogs chatLogs) {
        this.chatLogs = chatLogs;
    }

    public byte getSign() {
        return sign;
    }

    public void setSign(byte sign) {
        this.sign = sign;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageHolder{" +
                "sign=" + sign +
                ", type=" + type +
                ", chatLogs=" + chatLogs +
                '}';
    }
}
