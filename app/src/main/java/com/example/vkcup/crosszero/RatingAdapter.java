package com.example.vkcup.crosszero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vkcup.R;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private ArrayList<Rating> ratings;

    RatingAdapter(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    class RatingViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView wins;

        RatingViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            wins = view.findViewById(R.id.wins);
        }
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_item, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        holder.name.setText(ratings.get(position).getName());
        holder.wins.setText(ratings.get(position).getWins());
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }
}
