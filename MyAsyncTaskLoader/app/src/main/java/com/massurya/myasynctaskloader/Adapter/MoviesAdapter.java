package com.massurya.myasynctaskloader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.massurya.myasynctaskloader.Entity.MoviesItem;
import com.massurya.myasynctaskloader.MovieDetail;
import com.massurya.myasynctaskloader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.massurya.myasynctaskloader.BuildConfig.IMAGE_URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CardViewViewHolder> {

    private ArrayList<MoviesItem> movieItemNowPlayings;
    private Context context;

    private ArrayList<MoviesItem> getMovieItemNowPlayings() {
        return movieItemNowPlayings;
    }

    public void setMovieItemNowPlayings(ArrayList<MoviesItem> items) {
        this.movieItemNowPlayings = items;
    }

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CardViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent, false);
        return new CardViewViewHolder(view);
    }

    public void clearMovie() {
        movieItemNowPlayings.clear();
    }

    @Override
    public void onBindViewHolder(CardViewViewHolder holder, int position) {
        holder.textViewTitle.setText(getMovieItemNowPlayings().get(position).getName());
        holder.textViewOverview.setText(getMovieItemNowPlayings().get(position).getDesc());
        holder.textViewRelease.setText(getMovieItemNowPlayings().get(position).getDate());
        Picasso.with(context).load(IMAGE_URL + movieItemNowPlayings.get(position).getImage()).placeholder(R.drawable.ic_image_black_24dp).into(holder.imgPoster);

        holder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent i = new Intent(context, MovieDetail.class);

                i.putExtra("id", getMovieItemNowPlayings().get(position).getId());
                i.putExtra("name", getMovieItemNowPlayings().get(position).getName());
                i.putExtra("desc", getMovieItemNowPlayings().get(position).getDesc());
                i.putExtra("image", getMovieItemNowPlayings().get(position).getImage());
                i.putExtra("date", getMovieItemNowPlayings().get(position).getDate());
                i.putExtra("rating", getMovieItemNowPlayings().get(position).getRating());

                context.startActivity(i);
            }
        }));

        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getMovieItemNowPlayings().get(position).getName() + " \n" + getMovieItemNowPlayings().get(position).getDesc());
                intent.setType("text/plain");
                context.startActivity(intent);
                Toast.makeText(context, "Share " + getMovieItemNowPlayings().get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public int getItemCount() {
        Log.d("getCount", String.valueOf(movieItemNowPlayings.size()));
        if (movieItemNowPlayings == null) {
            return 0;
        }
        return movieItemNowPlayings.size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewOverview, textViewRelease;
        ImageView imgPoster;
        Button btnDetail, btnShare;

        CardViewViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tvName);
            textViewOverview = itemView.findViewById(R.id.tvDesc);
            textViewRelease = itemView.findViewById(R.id.tvDate);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }
}
