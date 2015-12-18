package com.example.adityasrivastava.cardview.network;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by adi on 7/12/15.
 */
public class NetworkConnection extends AsyncTask<String, Integer, StringBuilder> {

    private URL url;
    private HttpURLConnection conn;
    InputStream in;
    StringBuilder stringBuilder;
    BufferedReader reader;
    ObjectMapper objectMapper;
    JsonNode jsonNode;

    @Override
    protected StringBuilder doInBackground(String... params) {

        try {
            url = new URL("http://javatechig.com/api/get_category_posts/?dev=1&slug=android");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();

            conn.connect();
            in = conn.getInputStream();

            stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            conn.disconnect();
        }

        return stringBuilder;
    }

    @Override
    protected void onPostExecute(StringBuilder o) {
        super.onPostExecute(o);
    }

}
