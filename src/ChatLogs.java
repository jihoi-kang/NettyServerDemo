package server;

public class ChatLogs {

    private int _id = 0;
    private int target_id = 0;
    private String body = "";

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatLogs{" +
                "_id=" + _id +
                ", target_id=" + target_id +
                ", body='" + body + '\'' +
                '}';
    }
}
