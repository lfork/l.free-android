package com.lfork.a98620.lfree.chatwindow;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.entity.message.MessageContentType;
import com.lfork.a98620.lfree.data.entity.message.MessageType;
import com.lfork.a98620.lfree.data.source.IMDataRepository;
import com.lfork.a98620.lfree.data.source.MessageDataRepository;
import com.lfork.a98620.lfree.data.source.remote.imservice.MessageListener;

import java.util.List;

/**
 * Created by 98620 on 2018/4/22.
 */
public class ChatWindowViewModel extends BaseViewModel implements MessageListener{

    public final ObservableArrayList<Message> messages = new ObservableArrayList<>();

    public final ObservableField<String> newMessage = new ObservableField<>();

    public final ObservableField<String> username = new ObservableField<>();

    private IMDataRepository repository;

    private MessageDataRepository msgMepository;

    private ChatWindowNavigator navigator;

    private int userId;

    private int thisUserId;

    public ChatWindowViewModel(Context context) {
        super(context);
        repository = IMDataRepository.getInstance();
        msgMepository = MessageDataRepository.getInstance(0);
    }

    public void start(){
        loadMessages();
    }

    public void setNavigator(ChatWindowNavigator navigator) {
        this.navigator = navigator;
    }

    public void setUserInfo(String username, int userId, int thisUserId) {
        this.username.set(username);
        this.userId = userId;
        this.thisUserId = thisUserId;
    }


    private void loadMessages() {
        MessageContentType type = MessageContentType.COMMUNICATION_USER;
        msgMepository.getMessages(userId, type, new DataSource.GeneralCallback<List<Message>>() {
            @Override
            public void succeed(List<Message> data) {
                messages.clear();
                messages.addAll(data);
//                navigator.notifyMessagesChanged();
//                notifyPropertyChanged(BR.viewModel); // It's a @Bindable so update manually
                navigator.showToast("聊天记录加载成功");
            }

            @Override
            public void failed(String log) {
                navigator.showToast("log");
            }
        });
    }

    public void sendMessage() {

        String message_sent = newMessage.get();
        if (!message_sent.equals("")) {

            Message m;
            m = new Message(message_sent, Message.SendType);
            m.setSenderID(thisUserId);
            m.setReceiverID(userId);
            m.setType(MessageType.NORMAL_MESSAGE);
            m.setContentType(MessageContentType.COMMUNICATION_USER);
            m.setMessageID(System.currentTimeMillis());
            messages.add(m);

            navigator.sendMessage(m, new DataSource.GeneralCallback<Message>() {
                @Override
                public void succeed(Message data) {
                    navigator.showToast("发送成功");
                }

                @Override
                public void failed(String log) {
                    navigator.showToast(log);

                }
            });

            notifyChange();
            newMessage.set(""); //清空输入框的内容

        }
    }



    private void refreshMessages(Message message) {

    }


    public void addUser(User user) {
        repository.addChatUser(user, new DataSource.GeneralCallback<String>() {
            @Override
            public void succeed(String data) {

            }

            @Override
            public void failed(String log) {

            }
        });
    }


    @Override
    public void onReceived(Message message) {
        message.setChatType(Message.ReceiveType);
        messages.add(message);
        navigator.notifyMessagesChanged();
    }
}
