package com.massurya.myasynctaskloader.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.massurya.myasynctaskloader.Entity.FavoriteItem;
import com.massurya.myasynctaskloader.MovieDetail;
import com.massurya.myasynctaskloader.R;
import com.squareup.picasso.Picasso;

import static com.massurya.myasynctaskloader.BuildConfig.IMAGE_URL;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CardViewViewHolder> {
    private TextView textViewTitle, textViewOverview, textViewRelease;
    private ImageView imgPoster;
    private Button btnDetail, btnShare;
    private Cursor list;
    private int position;

    public FavoriteAdapter(Cursor items) {
        replaceAll(items);

    }

    public void replaceAll(Cursor items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public CardViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.movie_items, parent, false
                )
        );
    }

    private FavoriteItem getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }
        return new FavoriteItem(list);
    }

    @Override
    public void onBindViewHolder(CardViewViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(getItem(position));
        this.position = position;
    }


    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.getCount();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {

        CardViewViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tvName);
            textViewOverview = itemView.findViewById(R.id.tvDesc);
            textViewRelease = itemView.findViewById(R.id.tvDate);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnShare = itemView.findViewById(R.id.btn_share);

        }

        void bind(final FavoriteItem item) {
            textViewTitle.setText(item.getName());
            textViewOverview.setText(item.getDesc());
            textViewRelease.setText(item.getDate());
            Picasso.with(itemView.getContext()).load(IMAGE_URL + item.getImage()).placeholder(R.drawable.ic_image_black_24dp).into(imgPoster);

            btnDetail.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    Intent i = new Intent(itemView.getContext(), MovieDetail.class);

                    i.putExtra("id", item.getId());
                    i.putExtra("name", item.getName());
                    i.putExtra("desc", item.getDesc());
                    i.putExtra("image", item.getImage());
                    i.putExtra("date", item.getDate());
                    i.putExtra("rating", item.getRating());

                    itemView.getContext().startActivity(i);
                }
            }));

            btnShare.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, item.getName() + " \n" + item.getDesc());
                    intent.setType("text/plain");
                    itemView.getContext().startActivity(intent);
                    Toast.makeText(itemView.getContext(), "Share " + item.getName(), Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }
}

