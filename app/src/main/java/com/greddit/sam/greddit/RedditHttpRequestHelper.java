package com.greddit.sam.greddit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.*;
/**
 * Created by Sam on 5/16/2016.
 */
public class RedditHttpRequestHelper extends AsyncTask<Void,Void,String>{

    private final String mSubredditString;
    private final Integer mEntries;
    private static final String ENDPOINT_FORMAT = "https://www.reddit.com/r/%s/hot.json?limit=%s&sort=top";
    private final MainActivity mDelegate;

    //constructor sets string to subreddit string, creates settting for hash
    public RedditHttpRequestHelper(String subredditString, Integer entries, MainActivity delegate){
        mSubredditString = subredditString;
        mEntries = entries;
        mDelegate = delegate;
    }

    protected String doInBackground(Void... params) {
        try {
            return httpSendGet();
        }
        catch(Exception except){
            return null;
        }
    }

    protected void onPostExecute(String result){

        System.out.println(result);
        ArrayList<HashMap<String,String>> dataHashList = null;

        if(result!=null) {
            try {
                dataHashList = parseRequestJSON(result);
            } catch (JSONException e) {
                dataHashList = null;
                e.printStackTrace();
            }
            mDelegate.renderListWithData(dataHashList);
        }
        else
            mDelegate.renderListWithData(null);

    }

    private String httpSendGet() throws Exception {

        System.out.println(generateRedditURI());
        URL subredditApiPath = new URL(generateRedditURI());
        HttpURLConnection connection = (HttpURLConnection) subredditApiPath.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer responseText = new StringBuffer();

        while((inputLine = reader.readLine()) != null) {
            responseText.append(inputLine);
        }

        reader.close();
        return responseText.toString();
    }

    public String generateRedditURI(){
        return String.format(ENDPOINT_FORMAT, mSubredditString,mEntries.toString());
    }

    private ArrayList<HashMap<String,String>> parseRequestJSON(String requestJSON) throws JSONException {
        JSONObject object = new JSONObject(requestJSON);
        JSONArray childrenArr = object.getJSONObject("data").getJSONArray("children");
        ArrayList<HashMap<String,String>> returnList = new ArrayList<>();

        for(int i = 1; i <= mEntries; i++) {
            HashMap<String,String> tempHash = new HashMap<>();
            JSONObject post = (JSONObject) childrenArr.get(i);
            tempHash.put("title",post.getJSONObject("data").getString("title"));
            tempHash.put("url",post.getJSONObject("data").getString("url"));
            tempHash.put("num_comments",post.getJSONObject("data").getString("num_comments"));
            returnList.add(tempHash);
        }

        return returnList;
    }
}
