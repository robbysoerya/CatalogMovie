package com.massurya.myasynctaskloader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns;
import com.massurya.myasynctaskloader.Databases.FavoriteHelper;
import com.massurya.myasynctaskloader.Entity.MovieDetailItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import cz.msebera.android.httpclient.util.EntityUtils;

import static com.massurya.myasynctaskloader.BuildConfig.API_KEY;
import static com.massurya.myasynctaskloader.BuildConfig.BASE_URL;
import static com.massurya.myasynctaskloader.BuildConfig.IMAGE_URL;
import static com.massurya.myasynctaskloader.Constant.Constant.API_QUERY;
import static com.massurya.myasynctaskloader.ContentProvider.DatabaseContract.CONTENT_URI;

public class MovieDetail extends AppCompatActivity {
    private TextView name, date, desc, runtime, txtRating, homepage;
    private RatingBar rating;
    private Integer id;
    private LinearLayout reviews;
    private String Title, Date, Desc, Img;
    private ImageView iv_fav;
    private ProgressDialog mDialog;
    private Boolean isFavorite = false;
    private FavoriteHelper favoriteHelper;
    ArrayList<String[]> authorArray = new ArrayList<>();
    ArrayList<String[]> reviewArray = new ArrayList<>();
    ArrayList<MovieDetailItem> durationArray = new ArrayList<>();
    float hasil, nilai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        name = findViewById(R.id.movie_title);
        date = findViewById(R.id.movie_date);
        desc = findViewById(R.id.movie_overview);
        rating = findViewById(R.id.ratingbar);
        txtRating = findViewById(R.id.movie_rating);
        ImageView imageView = findViewById(R.id.img_poster);
        iv_fav = findViewById(R.id.iv_fav);
        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) FavoriteRemove();
                else FavoriteSave();

                isFavorite = !isFavorite;
                favoriteSet();
            }
        });

        reviews = findViewById(R.id.view_reviews);
        runtime = findViewById(R.id.movie_duration);
        homepage = findViewById(R.id.movie_homepage);
        mDialog = new ProgressDialog(this);

        id = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
        Title = getIntent().getExtras().getString("name");
        Date = getIntent().getExtras().getString("date");
        Desc = getIntent().getExtras().getString("desc");
        Img = getIntent().getExtras().getString("image");
        nilai = Float.parseFloat(getIntent().getExtras().getString("rating"));
        hasil = nilai / 2;
        loadDataSQLite();
        Picasso.with(this)
                .load(IMAGE_URL + Img)
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(imageView);

        TheTask task = new TheTask();
        task.execute();

    }

    private void favoriteSet() {

        if (isFavorite) iv_fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        else iv_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    private void FavoriteSave() {
        //Log.d("TAG", "FavoriteSave: " + item.getId());
        ContentValues cv = new ContentValues();
        cv.put(FavoriteColumns.COLUMN_ID, id);
        cv.put(FavoriteColumns.COLUMN_TITLE, Title);
        cv.put(FavoriteColumns.COLUMN_POSTER, Img);
        cv.put(FavoriteColumns.COLUMN_RELEASE_DATE, Date);
        cv.put(FavoriteColumns.COLUMN_VOTE, Objects.requireNonNull(getIntent().getExtras()).getString("rating"));
        cv.put(FavoriteColumns.COLUMN_OVERVIEW, Desc);

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(this, "Movie has been add to favorite", Toast.LENGTH_SHORT).show();
    }

    private void loadDataSQLite() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + id),
                null,
                null,
                null,
                null

        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }
        favoriteSet();
    }

    private void FavoriteRemove() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + id),
                null,
                null
        );
        Toast.makeText(this, "Movie has been remove from favorite", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favoriteHelper != null) favoriteHelper.close();
    }

    @SuppressLint("StaticFieldLeak")
    class TheTask extends AsyncTask<String, String, String> {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            mDialog.dismiss();

            for (int i = 0; i < authorArray.size(); i++) {

                TextView author = new TextView(MovieDetail.this);
                TextView review = new TextView(MovieDetail.this);

                View view = new View(MovieDetail.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , 3));
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                author.setText("\n" + authorArray.get(i)[0] + "\n");
                review.setText("\n" + reviewArray.get(i)[0] + "\n");
                reviews.addView(author);
                reviews.addView(review);
                reviews.addView(view);
            }

            String duration, page;

            duration = durationArray.get(0).getRuntime();
            page = durationArray.get(0).getHomepage();

            name.setText(Title);
            date.setText(Date);
            runtime.setText(duration + " " + getString(R.string.minute));
            desc.setText(Desc);
            homepage.setText(page);
            rating.setRating(hasil);
            txtRating.setText(Objects.requireNonNull(getIntent().getExtras()).getString("rating"));
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mDialog.setMessage(getString(R.string.loading));
            mDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            getReview();
            getDetail();
            return null;

        }

        private void getDetail() {

            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpGet requestdetail = new HttpGet(BASE_URL + "movie/" + id + "?" + API_QUERY +
                    API_KEY);

            try {

                HttpResponse response1 = httpclient.execute(requestdetail);

                HttpEntity resEntity1 = response1.getEntity();

                String _response1 = EntityUtils.toString(resEntity1);

                JSONObject responseObject1 = new JSONObject(_response1);
                MovieDetailItem movieDetailItem = new MovieDetailItem(responseObject1);
                durationArray.add(movieDetailItem);

            } catch (Exception e) {
                e.printStackTrace();

            }

            httpclient.getConnectionManager().shutdown();
        }
    }

    private void getReview() {

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpGet request = new HttpGet(BASE_URL + "movie/" + id + "/reviews?" + API_QUERY +
                API_KEY);

        try {
            HttpResponse response = httpclient.execute(request);

            HttpEntity resEntity = response.getEntity();

            String _response = EntityUtils.toString(resEntity);
            JSONObject responseObject = new JSONObject(_response);

            JSONArray list = responseObject.getJSONArray("results");

            for (int i = 0; i < list.length(); i++) {
                JSONObject j = list.getJSONObject(i);
                String[] s = {j.getString("author")};
                String[] t = {j.getString("content")};
                int total = responseObject.getInt("total_pages");

                if (total != 0) {

                    authorArray.add(s);
                    reviewArray.add(t);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        httpclient.getConnectionManager().shutdown();
    }
}
