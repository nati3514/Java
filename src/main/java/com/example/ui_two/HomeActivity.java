package com.example.ui_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.CaseMap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    public static final String URL_SAVE_NAME = "http://192.168.43.160/SqliteSync/saveName.php";

    //database helper object
    private DatabaseHelper db;

    // Variable declarations
    private String userEmail;
    private TextView textView;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter mAdapter;
    private List<Product> products;
    private ProgressBar progressBar;
    private static  final String BASE_URL = "http://192.168.43.160/Android/getProducts.php";
    private List<Title> products_list_item_layout;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    private TitleAdapter titleAdapter;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        if (item.getItemId() == R.id.action_settings){

            intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(HomeActivity.this,"Settings clicked!",Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.action_notifications){

            intent = new Intent(HomeActivity.this,NotificationsActivity.class);
            startActivity(intent);
            Toast.makeText(HomeActivity.this,"Notifications clicked!",Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_menu,menu);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelper(this);
        products_list_item_layout = new ArrayList<>();

        mToolbar = findViewById(R.id.dashboard_toolbar);
        progressBar = findViewById(R.id.progressbar);
        //setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        recyclerView = findViewById(R.id.products_recyclerView);
        manager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        products = new ArrayList<>();
        //readFromLocalStorage();

        getProducts();


        //calling the method to load all the stored names
        loadTitles();

        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                loadTitles();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

    }
//    public void submitTitle(View view)
//    {
//
//
//    }
//    private void readFromLocalStorage ()
//    {
//        products.clear();
//        DbHelper dbHelper = new DbHelper(this);
//        SQLiteDatabase database = dbHelper.getReadableDatabase();
//
//        Cursor cursor = dbHelper.readFromLocalDatabase(database);
//        while (cursor.moveToNext())
//        {
//            String title = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.TITLE));
//            String image = cursor.getString(cursor.getColumnIndexOrThrow(DbContract.IMAGE));
////            double price = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf(DbContract.PRICE))));
////            float rating = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(String.valueOf(DbContract.RATING))));
//            //products.add(new Product(title, image));
//        }
//        mAdapter.notifyDataSetChanged();
//        cursor.close();
//        dbHelper.close();
//    }
//    private void saveToLocalStorage(String title, String image)
//    {
//        DbHelper dbHelper = new DbHelper(this);
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//
//        if(checkNetworkConnection())
//        {
//
//        }
//        else
//        {
//            dbHelper.saveToLocalDatabase(title, image);
//        }
//        readFromLocalStorage();
//        dbHelper.close();
//
//    }
//    public boolean checkNetworkConnection()
//    {
//        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        return   (networkInfo!=null && networkInfo.isConnected());
//    }

    private void getProducts (){
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                String title = object.getString("title");
                                double price = object.getDouble("price");
                                double rating = object.getDouble("rating");
                                String image = object.getString("image");

                                String rate = String.valueOf(rating);
                                float newRate = Float.valueOf(rate);

                                Product product = new Product(title,price, newRate,image);
                                products.add(product);
                            }

                        }catch (Exception e){

                        }

                        mAdapter = new RecyclerAdapter(HomeActivity.this,products);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(HomeActivity.this).add(stringRequest);

    }


    private void loadTitles() {
        products_list_item_layout.clear();
        Cursor cursor = db.getTitles();
        if (cursor.moveToFirst()) {
            do {
                Title name = new Title(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS))
                );
                products_list_item_layout.add(name);
            } while (cursor.moveToNext());
        }

        titleAdapter = new TitleAdapter(this, R.layout.products_list_item_layout, products_list_item_layout);
        recyclerView.setAdapter(mAdapter);
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        titleAdapter.notifyDataSetChanged();
    }

    /*
     * this method is saving the name to ther server
     * */
    private void saveNameToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Name...");
        progressDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveTitleToLocalStorage(String.valueOf(titleAdapter), NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveTitleToLocalStorage(String.valueOf(titleAdapter), NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveTitleToLocalStorage(String.valueOf(titleAdapter), NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", String.valueOf(titleAdapter));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //saving the name to local storage
    private void saveTitleToLocalStorage(String title, int status) {

        db.addTitle(title, status);
        Title n = new Title(title, status);
        products_list_item_layout.add(n);
        refreshList();
    }

}

