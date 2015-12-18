package com.example.adityasrivastava.cardview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.adityasrivastava.cardview.adapter.CardViewAdapter;
import com.example.adityasrivastava.cardview.listener.CardSelectionListener;
import com.example.adityasrivastava.cardview.modal.PostDetails;
import com.example.adityasrivastava.cardview.network.NetworkConnection;
import com.facebook.appevents.AppEventsLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{

    private static final String ENDPOINT = "http://javatechig.com";
    private static final String TAG = "Main Activity";
    private static final List<PostDetails> mModalList = new ArrayList<PostDetails>();
    private static boolean sort_flag = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;
    ;
    private CardViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        hideStatusBar();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        fetchData();

        mAdapter = new CardViewAdapter(MainActivity.this, mModalList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new CardSelectionListener(this, new CardSelectionListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                        PostDetails postDetails = mModalList.get(position);

                        intent.putExtra("post", postDetails);
                        startActivity(intent);

                    }
                })
        );


    }

    private void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    private void fetchData(){

        if(isNetworkAvailable()){


            try {
                StringBuilder in = new NetworkConnection().execute("Hello").get();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(in.toString());


                Iterator<JsonNode> iNode = node.path("posts").elements();

                while(iNode.hasNext()){
                    JsonNode jn = iNode.next();

                    PostDetails pd = new PostDetails();
                    pd.setId(jn.path("id").asInt());
                    pd.setDate(jn.path("date").asText());
                    pd.setContent(jn.path("content").asText());
                    pd.setThumbnail(jn.path("thumbnail").asText());
                    pd.setTitle(jn.path("title").asText());
                    pd.setUrl(jn.path("url").asText());
                    pd.setExcerpt(jn.path("excerpt").asText());
                    mModalList.add(pd);

                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final MenuItem sortMenuItem = menu.findItem(R.id.sort_by_date);
        final SimpleDateFormat v_objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");

        sortMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(sort_flag == true){
                    Collections.shuffle(mModalList);
                    sort_flag = false;
                    mAdapter.animateTo(mModalList);
                    mRecyclerView.scrollToPosition(0);

                    Toast.makeText(getApplicationContext(), "List UnSorted",
                            Toast.LENGTH_SHORT).show();

                    return false;
                }


                Collections.sort(mModalList, new Comparator<PostDetails>() {
                    @Override
                    public int compare(PostDetails lhs, PostDetails rhs) {
                        Date lhs_date = null;
                        Date rhs_date = null;

                        try {
                            lhs_date = v_objSimpleDateFormat.parse(lhs.getDate());
                            rhs_date = v_objSimpleDateFormat.parse(rhs.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        if (lhs_date.compareTo(rhs_date) > 0) {
                            sort_flag = true;
                            return 1;
                        }

                        if (lhs_date.compareTo(rhs_date) < 0) {
                            sort_flag = true;
                            return -1;
                        }

                        if (lhs_date.compareTo(rhs_date) == 0) {
                            sort_flag = true;
                            return 0;
                        }

                        return 0;

                    }
                });

                mAdapter.animateTo(mModalList);
                mRecyclerView.scrollToPosition(0);
                Toast.makeText(getApplicationContext(), "List Sorted",
                        Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("----------------->");

        final List<PostDetails> list = filter(mModalList, newText);

        mAdapter.animateTo(list);

        mRecyclerView.scrollToPosition(0);

        return true;
    }


    private List<PostDetails> filter(List<PostDetails> modals, String query) {

        query = query.toLowerCase();

        final List<PostDetails> filteredModelList = new ArrayList<PostDetails>();

        for (PostDetails modal : modals) {

            final String text = modal.getTitle().toLowerCase();

            if (text.contains(query)) {
                filteredModelList.add(modal);
            }

        }


        return filteredModelList;
    }

}
