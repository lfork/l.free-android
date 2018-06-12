package com.lfork.a98620.lfree.chatwindow;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.imservice.message.Message;

import java.util.List;

///**
// * Created by 98620 on 2017/7/25.
// */


class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    private static final String TAG="MessageListAdapter";
    private List<Message> mMessageList;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView msgView_sent, msgView_rec;
        LinearLayout msgView_sent_parent, msgView_rec_parent;

        ViewHolder(View view) {
            super(view);
            msgView_rec = view.findViewById(R.id.message_rec);
            msgView_rec_parent = view.findViewById(R.id.message_rec_parent);
            msgView_sent = view.findViewById(R.id.message_sent);
            msgView_sent_parent = view.findViewById(R.id.message_sent_parent);
        }
    }

    MessageListAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    //对于listView的点击事件， 只能设置item的点击事件， 不能设置item的子项的点击事件

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_window_msg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        if (message.getChatType() == Message.ReceiveType) {

            //奇怪的操作  如果这里不设置隐藏属性的话  下面有些地方就会自动的跳出某些内容~~~~  可是这些隐藏的属性我是默认就设置好了的呀
            holder.msgView_rec_parent.setVisibility(View.VISIBLE);
            holder.msgView_sent_parent.setVisibility(View.GONE);
            holder.msgView_rec.setText(message.getContent());
        } else {
            holder.msgView_rec_parent.setVisibility(View.GONE);
            holder.msgView_sent_parent.setVisibility(View.VISIBLE);
            holder.msgView_sent.setText(message.getContent());

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
