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
import com.illinodes.jouchats.MessageActivity;
import com.illinodes.jouchats.Model.User;
import com.illinodes.jouchats.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>
{
    //Declarations
    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;

    public UserAdapter(Context context, List<User> users, boolean isChat)
    {
        mContext = context;
        mUsers = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.user_item, viewGroup, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        final User user = mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else
            {
                Glide.with(mContext)
                        .load(user.getImageURL())
                        .into(viewHolder.profile_image);
            }

        //Shows whether user online or offline
        if (isChat)
        {
            if (user.getStatus().equals("Online"))
            {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            } else
                {
                    viewHolder.img_on.setVisibility(View.GONE);
                    viewHolder.img_off.setVisibility(View.VISIBLE);
                }
        } else
            {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.GONE);
            }

        //When user clicks another user to send a message,
        // carries the clicked user ID to the message activity
        viewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Declarations
        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }
}
