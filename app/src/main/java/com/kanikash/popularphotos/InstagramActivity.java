package com.kanikash.popularphotos;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InstagramActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "e3d22439c075426bb5e3a2c4691efba0";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>();
        //Create the adapter and bind it to the array list
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //Populate the view created by the adapter in the listView
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        //Set the adapter to the listView
        lvPhotos.setAdapter(aPhotos);
        //https://api.instagram.com/v1/media/popular?client_id=<client_id>
        //Setup popular URL endpoint
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create the network request
        AsyncHttpClient client = new AsyncHttpClient();
        //Send the network request
        client.get(popularUrl, new JsonHttpResponseHandler(){
            //define success and failure callbacks
            //Handle the success response

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //fired when the success response comes back
                //super.onSuccess(statusCode, headers, response);
                //url, height, username, caption
                JSONArray photosJson = null;
                photos.clear();
                try{
                    photosJson = response.getJSONArray("data");
                    for(int i = 0; i < photosJson.length(); i++) {
                        JSONObject photoJSON = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if(!photoJSON.getJSONObject("user").isNull("profile_picture")) {
                            photo.userImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        }
                        if(!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.url = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.height = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.width = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likes_count = photoJSON.getJSONObject("likes").getInt("count");
                        photo.photo_time = photoJSON.getInt("created_time");
                        //Get the top two comments on the photo
                        if(!photoJSON.getJSONObject("comments").isNull("count")) {
                            int totalComments = photoJSON.getJSONObject("comments").getInt("count");
                            if(totalComments > 0) {
                                JSONArray photoCommentsJson = photoJSON.getJSONObject("comments").getJSONArray("data");
                                if (totalComments >= 2) {
                                    photo.comment1 = photoCommentsJson.getJSONObject(0).getString("text");
                                    photo.comment1User = photoCommentsJson.getJSONObject(0).getJSONObject("from").getString("username");
                                    photo.comment2 = photoCommentsJson.getJSONObject(1).getString("text");
                                    photo.comment2User = photoCommentsJson.getJSONObject(0).getJSONObject("from").getString("username");
                                }
                                else if(totalComments >= 1) {
                                    photo.comment1 = photoCommentsJson.getJSONObject(0).getString("text");
                                    photo.comment1User = photoCommentsJson.getJSONObject(0).getJSONObject("from").getString("username");
                                }
                            }

                        }
                        photos.add(photo);
                    }
                    //Notified the adapter that it should populate new changes in the list view
                    aPhotos.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch(JSONException e) {
                    // Fire if json parsing fails
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
