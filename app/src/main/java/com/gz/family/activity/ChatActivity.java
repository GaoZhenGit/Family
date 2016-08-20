package com.gz.family.activity;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.family.Mqtt.MService;
import com.gz.family.Network.Net;
import com.gz.family.R;
import com.gz.family.adapter.ChatAdapter;
import com.gz.family.application.Constant;
import com.gz.family.dataBase.ChatMessageStore;
import com.gz.family.dataBase.UserStore;
import com.gz.family.model.ChatMessage;
import com.gz.family.model.User;
import com.gz.family.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by host on 2016/2/6.
 */
public class ChatActivity extends BasePageActivity implements MService.MCallback {
    private User chatMember;
    private AQuery aQuery;

    private ListView chatListView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    private MService mService;

    private ServiceConnection serviceConnection;

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            chatMember = (User) getIntent().getExtras().getSerializable("chatMember");
        } else {
            finish();
        }
        Intent intent = new Intent(this, MService.class);
        //bind the service of mqtt
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((MService.ReceiveServiceBinder) service).getService();
                mService.subscribe(UserStore.getLocalUser(true).getId(), 2);
                mService.setCallback(ChatActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void initLayoutView() {
        setContentView(R.layout.activity_chat);
        aQuery = new AQuery(this);
    }

    @Override
    protected void initView() {
        aQuery.id(R.id.title_mid_text).text(chatMember.getName());
        aQuery.id(R.id.title_left_img).visible();
        chatListView = (ListView) findViewById(R.id.chat_listview);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatListView.setAdapter(chatAdapter);
        chatListView.setDivider(null);
        chatListView.setOnItemSelectedListener(null);
        getChatData();
        chatListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatListView.setSelection(chatListView.getBottom());
            }
        }, 200);
    }

    @Override
    protected void setListener() {
        aQuery.id(R.id.title_left_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        aQuery.id(R.id.btn_chat_send).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = aQuery.id(R.id.et_chat_input).getText().toString();
                sendMessage(msg);
                aQuery.id(R.id.et_chat_input).text("");
            }
        });
    }

    private void getChatData() {
        //get chatlist from database and refresh
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.clear();
                List<ChatMessage> list = ChatMessageStore.get(chatMember, false);
                messageList.addAll(list);
                chatAdapter.notifyDataSetChanged();
                chatListView.smoothScrollToPosition(chatListView.getBottom() + 1);

                //取消当前页面用户的notifcation
                NotificationManager noteMng = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (list.size() > 0)
                    noteMng.cancel(list.get(messageList.size() - 1).hashCode());
            }
        });
    }

    private void sendMessage(final String message) {
        final User me = UserStore.getLocalUser(true);

        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setOwner(me);
        chatMessage.setToWhom(chatMember);
        chatMessage.setText(message);
        chatMessage.setType(ChatMessage.ME);
        chatMessage.setTime(Calendar.getInstance());
        Log.i("chatACT", "send->" + chatMessage.toString());

        Map<String, String> param = new HashMap<>();
        param.put("target", chatMember.getId());
        param.put("message", chatMessage.toString());
        param.put("qos", "2");
        Net.post(Constant.URL.MQTT, param, new Net.NetworkListener() {
            @Override
            public void onSuccess(String response) {
                ChatMessageStore.add(chatMessage);
                getChatData();
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    @Override
    public void arrived(String topic, String msg) {
        getChatData();
//        NotificationManager noteMng =
//                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        noteMng.cancel(messageList.get(messageList.size()-1).hashCode());
    }

    @Override
    protected void onDestroy() {
        mService.setCallback(null);
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
