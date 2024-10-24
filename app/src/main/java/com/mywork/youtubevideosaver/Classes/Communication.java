package com.mywork.youtubevideosaver.Classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Communication {
    private final String data_url;
    private List<JsonObject> formatsList;
    private DData dData;
    public Communication(String data_url){
        this.data_url = data_url;

        try{
            String url = "https://submagic-free-tools.fly.dev/api/youtube-info";
            Document document = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .data("url",this.data_url)
                    .post();
            String jsonData = document.body().text();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
            Gson gson1 = new Gson();
            Type formatListType = new TypeToken<List<JsonObject>>(){}.getType();
            formatsList = gson1.fromJson(jsonObject.get("formats").toString(),formatListType);
            Gson gson2 = new Gson();
            dData = gson2.fromJson(jsonObject.toString(),DData.class);
        }catch (Exception e){
            formatsList = new ArrayList<>();
            dData = new DData();
        }
    }
    public List<JsonObject> getFormats(){
        return formatsList;
    }
    public DData getDData(){
        return dData;
    }
}