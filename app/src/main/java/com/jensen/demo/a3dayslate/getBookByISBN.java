package com.jensen.demo.a3dayslate;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class getBookByISBN extends AppCompatActivity {

    private TextView bookText;
    String bodyText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getbookbyisbnlayout);
        bookText = findViewById(R.id.bookInfo);
        // Do all the stuff here for now
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:9780140328721";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    bodyText = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(bodyText);
                        JSONArray itemsArray = jsonObject.getJSONArray("items");
                        JSONObject items = itemsArray.getJSONObject(0);
                        JSONObject volInfo = items.getJSONObject("volumeInfo");
                        String titleText = volInfo.getString("title");
                        JSONArray authors = volInfo.getJSONArray("authors");
                        String author1 = authors.getString(0);
                        Log.d("TAG", "TESTING JENSEN " + titleText);

                       bookText.setText(author1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }
}
