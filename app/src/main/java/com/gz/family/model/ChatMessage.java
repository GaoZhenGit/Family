package com.gz.family.model;

import com.gz.family.dataBase.UserStore;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by host on 2016/2/6.
 */
@DatabaseTable(tableName = "chat_message")
public class ChatMessage implements Serializable{
    public static final int ME = 0;
    public static final int OTHER = 1;
    public static final int SYSTEM = 2;

    @DatabaseField(columnName = "id", id = true)
    private String id;
    @DatabaseField(columnName = "user_id", foreign = true)
    private User owner;
    @DatabaseField(columnName = "to_id", foreign = true)
    private User toWhom;
    @DatabaseField(columnName = "text")
    private String text;
    @DatabaseField(columnName = "time", dataType = DataType.SERIALIZABLE)
    private Calendar time;
    @DatabaseField(columnName = "type")
    private int type = -1;

    public boolean isMe(User me) {
        return owner.equals(me);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        ChatMessage message = (ChatMessage) o;
        if (this.id.equals(message.getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public User getToWhom() {
        return toWhom;
    }

    public void setToWhom(User toWhom) {
        this.toWhom = toWhom;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("owner", owner.getId());
            jsonObject.put("toWhom", toWhom.getId());
            jsonObject.put("text", text);
            jsonObject.put("type", type);
            jsonObject.put("time", time.getTimeInMillis());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stringSet(String s) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            id = jsonObject.optString("id");
            owner = UserStore.getUser(jsonObject.optString("owner"));
            toWhom = UserStore.getUser(jsonObject.optString("toWhom"));
            text = jsonObject.optString("text", text);
            type = jsonObject.optInt("type");
            time = Calendar.getInstance();
            time.setTimeInMillis(jsonObject.optLong("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
