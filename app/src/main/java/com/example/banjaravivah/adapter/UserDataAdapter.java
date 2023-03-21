package com.example.banjaravivah.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.banjaravivah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import xyz.hanks.library.bang.SmallBangView;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.MyViewHolder> {
    String[] names;
    Context context;
    int lastPosition = -1;

    public UserDataAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        SmallBangView imageview;
       // ImageView imageView;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            imageview =itemView.findViewById(R.id.imageViewAnimation);

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
        holder.name.setText(names[position]);
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
        return names.length;
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