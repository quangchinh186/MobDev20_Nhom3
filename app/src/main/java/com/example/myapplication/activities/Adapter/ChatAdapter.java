package com.example.myapplication.activities.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.system.BatoSystem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatModelViewHolder> {
  Context context;
  List<ChatMessage> messages;

  public ChatAdapter(Context context, List<ChatMessage> messages) {
    this.context = context;
    this.messages = messages;
  }

  public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler,parent,false);
    return new ChatModelViewHolder(view);
  }

  private String standardizeTime(String time){
    String[] timeSplit = time.split(" ");
    StringBuilder standardTime = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      standardTime.append(timeSplit[i]).append(" ");
    }
    return standardTime.toString();
  }

  @Override
  public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position) {
    Log.d("ChatAdapter", "onBindViewHolder: "+messages.size());

      //Log.d("ChatAdapter", "onBindViewHolder: "+message.getMessage());
      if(messages.get(position).getFrom().equals(ApplicationActivity.user.getId())){
        holder.leftChatLayout.setVisibility(View.GONE);
        holder.rightChatLayout.setVisibility(View.VISIBLE);
        holder.rightChatTextview.setText(messages.get(position).getMessage());
        holder.sendTime.setText(standardizeTime(messages.get(position).getDateTime().toString()));
      } else {
        holder.rightChatLayout.setVisibility(View.GONE);
        holder.leftChatLayout.setVisibility(View.VISIBLE);
        holder.leftChatTextview.setText(messages.get(position).getMessage());
        holder.receiveTime.setText(standardizeTime(messages.get(position).getDateTime().toString()));
        Picasso.get()
                .load(ApplicationActivity.queryHelper.getProfilePicture(messages.get(position).getFrom()))
                .into(holder.senderChatImage);
      }

  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  class ChatModelViewHolder extends RecyclerView.ViewHolder{

    LinearLayout leftChatLayout,rightChatLayout;
    TextView leftChatTextview,rightChatTextview;
    ImageView senderChatImage;
    TextView sendTime, receiveTime;

    public ChatModelViewHolder(@NonNull View itemView) {
      super(itemView);
      leftChatLayout = itemView.findViewById(R.id.received_message_container);
      rightChatLayout = itemView.findViewById(R.id.sent_message_container);
      leftChatTextview = itemView.findViewById(R.id.received_message_textview);
      rightChatTextview = itemView.findViewById(R.id.sent_message_textview);
      senderChatImage = itemView.findViewById(R.id.chat_sender_image);
      sendTime = itemView.findViewById(R.id.sent_message_time_textview);
      receiveTime = itemView.findViewById(R.id.received_message_time_textview);
    }
  }
}