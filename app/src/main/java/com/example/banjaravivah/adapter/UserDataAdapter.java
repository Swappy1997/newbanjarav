package com.example.banjaravivah.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.banjaravivah.R;
import com.example.banjaravivah.activity.DashboardMainActivity;
import com.example.banjaravivah.helper.Allusers;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.MyViewHolder> {
    String[] names;
    Context context;
    ArrayList<Allusers> userDetails;
    int lastPosition = -1;

    public UserDataAdapter(Context context, ArrayList<Allusers> userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        SmallBangView imageview;
        CircleImageView pp;
        // ImageView imageView;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            imageview = itemView.findViewById(R.id.imageViewAnimation);
            pp = itemView.findViewById(R.id.pp);

        }
    }


    @NonNull
    @Override
    public UserDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_user_data_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAdapter.MyViewHolder holder, int position) {
        Allusers allusers = userDetails.get(position);
        holder.name.setText(allusers.getFirst_name());
        Glide.with(context).load("https://banjaravivah.online/images/"+allusers.getImage())
                .into(holder.pp);

        setRecyclerViewAnimation(holder.itemView, position);
        //holder.imageView.setBackgroundResource(img[position]);
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.imageview.isSelected()) {
                    holder.imageview.setSelected(false);
                } else {
                    // if not selected only
                    // then show animation.
                    holder.imageview.setSelected(true);
                    holder.imageview.likeAnimation();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    private void setRecyclerViewAnimation(View view, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setLocationBtn() {
    }
}