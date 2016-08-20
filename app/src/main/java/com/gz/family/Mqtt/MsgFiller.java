package com.gz.family.Mqtt;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.family.R;
import com.gz.family.activity.ChatActivity;
import com.gz.family.activity.InvitationActivity;
import com.gz.family.activity.MainActivity;
import com.gz.family.dataBase.ChatMessageStore;
import com.gz.family.dataBase.DBhelper;
import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by host on 2016/2/24.
 */
public class MsgFiller {

    public void arrived(String topic, String msg, Context context) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.stringSet(msg);
        switch (chatMessage.getType()) {
            case ChatMessage.ME:
                handleME(chatMessage, context);
                break;
            case ChatMessage.SYSTEM:
                handleSystem(msg, context);
            default:
                break;
        }
    }

    //已有好友信息处理(type==ME)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleME(ChatMessage chatMessage, Context context) {
        if (chatMessage.getOwner() == null) {
            return;
        }
        chatMessage.setType(ChatMessage.OTHER);
        ChatMessageStore.add(chatMessage);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        NotificationManager noteMng =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Bundle bundle = new Bundle();
        bundle.putSerializable("chatMember", chatMessage.getOwner());
        Intent chatIntent = new Intent(context, ChatActivity.class);
        chatIntent.putExtras(bundle);
        Intent mainIntent = new Intent(context, MainActivity.class);
        Intent[] intents = {mainIntent, chatIntent};
        if (currentapiVersion < 16) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, chatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.family_small)
                    .setTicker(chatMessage.getText())
                    .setContentTitle(chatMessage.getOwner().getName())
                    .setContentIntent(pendingIntent)
                    .setContentText(chatMessage.getText())
                    .setWhen(System.currentTimeMillis());
            notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(context)
                    .setTicker(chatMessage.getText())
                    .setContentTitle(chatMessage.getOwner().getName())
                    .setContentText(chatMessage.getText())
                    .setSmallIcon(R.mipmap.family_small)
                    .setContentIntent(pendingIntent)
                    .build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
        clearNotifiy(noteMng, chatMessage.getOwner());
        noteMng.notify(chatMessage.hashCode(), notification);
    }

    //消除同一个用户发过来的Notifcation
    private void clearNotifiy(NotificationManager manager, User last) {
        List<ChatMessage> list = ChatMessageStore.get(last, true);
        try {
            ChatMessage chatMessage = list.get(list.size() - 2);//-2表示倒数第二条（倒数第一条是刚收到的）
            manager.cancel(chatMessage.hashCode());
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    //处理系统信息(type==System)
    private void handleSystem(String msg, Context context) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        ChatMessage chatMessage = gson.fromJson(msg, ChatMessage.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(chatMessage.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null && jsonObject.optString("type").equals("invitation")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("system_message", chatMessage);
            Intent invitationIntent = new Intent(context, InvitationActivity.class);
            invitationIntent.putExtras(bundle);

            NotificationManager noteMng =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification;
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion < 16) {
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, invitationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setTicker(chatMessage.getOwner().getName() + " 邀请好友")
                        .setContentTitle(chatMessage.getOwner().getName())
                        .setContentText("邀请好友")
                        .setSmallIcon(R.mipmap.family_small)
                        .setContentIntent(pendingIntent)
                        .build();
                notification = builder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
            } else {
                Intent mainIntent = new Intent(context, MainActivity.class);
                Intent[] intents = {mainIntent, invitationIntent};

                PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                notification = new Notification.Builder(context)
                        .setTicker(chatMessage.getOwner().getName() + " 邀请好友")
                        .setContentTitle(chatMessage.getOwner().getName())
                        .setContentText("邀请好友")
                        .setSmallIcon(R.mipmap.family_small)
                        .setContentIntent(pendingIntent)
                        .build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
            }

            noteMng.notify(0, notification);
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }
}
