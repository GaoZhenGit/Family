package com.gz.family.dataBase;

import android.text.TextUtils;

import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * the chat message operaion of local database
 * Created by host on 2016/2/10.
 */
public class ChatMessageStore {
    public static void add(ChatMessage chatMessage) {
        if (chatMessage == null
                || TextUtils.isEmpty(chatMessage.getId())
                || chatMessage.getOwner() == null) {
            return;
        }
        //check the message is in
//        User messageOwner = chatMessage.getOwner();
//        List<User> userList = DBhelper.getInstance().getAll(User.class);
//        if (!userList.contains(messageOwner)) {
//            return;
//        }
        DBhelper.getInstance().add(chatMessage);
    }

    public static List<ChatMessage> get(User user, boolean lazy) {
        if (user == null || TextUtils.isEmpty(user.getId())) {
            return null;
        }
        String selectId = user.getId();
        List<ChatMessage> noSelectList =
                DBhelper.getInstance().getAll(ChatMessage.class);
        List<ChatMessage> selectedList = new ArrayList<>();
        for (ChatMessage m : noSelectList) {
            /**
             * condition:
             * owner == selected user
             * or
             * owner == me
             *      and towhom == selected user
             */
            if (m.getOwner().getId().equals(selectId)
                    || (m.getType() == ChatMessage.ME
                    && m.getToWhom().getId().equals(selectId))) {
                selectedList.add(m);
            }
        }
        if (!lazy) {
            User userFromDB = DBhelper.getInstance().getById(User.class,selectId);
            User me = UserStore.getLocalUser(true);
            for (ChatMessage m : selectedList) {
                if (m.getType() == ChatMessage.ME) {
                    m.setOwner(UserStore.getLocalUser(true));
                } else {
                    m.setOwner(userFromDB);
                }
            }
        }
        return selectedList;
    }
}
