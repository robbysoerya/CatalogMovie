package com.massurya.myasynctaskloader.Entity;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailItem {
    private String runtime;
    private String homepage;

    public String getRuntime() {
        return runtime;
    }

    public String getHomepage() {
        return homepage;
    }

    public MovieDetailItem(JSONObject object) {
        try{
            String runtime = object.getString("runtime");
            String homepage = object.getString("homepage");
            this.runtime = runtime;
            this.homepage = homepage;

        }catch(JSONException e){

            e.printStackTrace();

        }
    }
}
