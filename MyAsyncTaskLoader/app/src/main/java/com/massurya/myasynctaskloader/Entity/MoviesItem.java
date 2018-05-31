package com.massurya.myasynctaskloader.Entity;

import org.json.JSONObject;

public class MoviesItem {
    private String name;
    private String desc;
    private String date;
    private String image;
    private String rating;
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getRating() {
        return rating;
    }
    public MoviesItem(JSONObject object) {

        try {

            String name = object.getString("title");
            String image = object.getString("poster_path");
            String desc = object.getString("overview");
            String date = object.getString("release_date");
            String rating = object.getString("vote_average");
            int id = object.getInt("id");

            this.name = name;
            this.image = image;
            this.desc = desc;
            this.date = date;
            this.rating = rating;
            this.id = id;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

