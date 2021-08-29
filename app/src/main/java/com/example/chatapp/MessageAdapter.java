package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageArrayList;


    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.senserchatlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else {

            View view = LayoutInflater.from(context).inflate(R.layout.recieverchatlayout,parent,false);
               return new ReceiverViewHolder(view);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       Message message = messageArrayList.get(position);

       if(holder.getClass()==SenderViewHolder.class)
       {
           SenderViewHolder viewHolder = (SenderViewHolder)holder;
           viewHolder.textViewmessage.setText(message.getMessage());
           viewHolder.timeofmessage.setText(message.getCurrenttiime());

       }

       else {
           ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
           viewHolder.textViewrmessage.setText(message.getMessage());
           viewHolder.timeOfrmessage.setText(message.getCurrenttiime());
       }

    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId()))
        {
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }




    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.senderMessage);
            timeofmessage = itemView.findViewById(R.id.stimeofmessage);
        }
    }





    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView textViewrmessage;
        TextView timeOfrmessage;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

          textViewrmessage = itemView.findViewById(R.id.receivermessage);
          timeOfrmessage = itemView.findViewById(R.id.rtimeofmessage);

        }
    }

}
