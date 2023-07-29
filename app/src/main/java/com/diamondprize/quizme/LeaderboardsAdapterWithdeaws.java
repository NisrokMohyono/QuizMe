package com.diamondprize.quizme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diamondprize.quizme.databinding.RowLeaderboardsBinding;

import java.util.ArrayList;


public class LeaderboardsAdapterWithdeaws extends RecyclerView.Adapter<LeaderboardsAdapterWithdeaws.LeaderboardViewHolder> {

    Context context;
    ArrayList<User> users;

    public LeaderboardsAdapterWithdeaws(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_listcoins, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));
        holder.binding.index.setText(String.format("%d", position+1));

        Glide.with(context)
                .load(user.getProfile())
                ;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        RowLeaderboardsBinding binding;
        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }


}