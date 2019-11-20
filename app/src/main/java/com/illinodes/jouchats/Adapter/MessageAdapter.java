package com.illinodes.jouchats.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.illinodes.jouchats.MessageActivity;
import com.illinodes.jouchats.Model.Chat;
import com.illinodes.jouchats.Model.User;
import com.illinodes.jouchats.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    //Declarations
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> chat, String imageurl)
    {
        mContext = context;
        mChat = chat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        if (i == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else
            {
                View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.chat_item_left, viewGroup, false);
                return new MessageAdapter.ViewHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i)
    {
        Chat chat = mChat.get(i);

        viewHolder.show_message.setText(chat.getMessage());

        if (imageurl.equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else
            {
                Glide.with(mContext).load(imageurl).into(viewHolder.profile_image);
            }
    }

    @Override
    public int getItemCount()
    {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Declarations
        public TextView show_message;
        public ImageView profile_image;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (mChat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }else
            {
                return MSG_TYPE_LEFT;
            }
    }
}
