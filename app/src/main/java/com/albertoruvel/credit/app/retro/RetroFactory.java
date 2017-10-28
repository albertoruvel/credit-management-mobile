package com.albertoruvel.credit.app.retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jose.rubalcaba on 10/25/2017.
 */

public class RetroFactory {

    private static final String URL = "http://mycmanagement-albertoruvel.appspot.com/rest/";
    private static Retrofit INSTANCE;

    public static <R> R getInstance(Class rType){
        if(INSTANCE == null){
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return (R)INSTANCE.create(rType);
    }
}
