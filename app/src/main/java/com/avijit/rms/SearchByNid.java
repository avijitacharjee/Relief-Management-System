package com.avijit.rms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.adapters.SearchByNidRecyclerViewAdapter;
import com.avijit.rms.utils.AppUtils;
import com.avijit.rms.viewmodels.ReliefVM;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchByNid extends AppCompatActivity {
    TableLayout tableLayout;
    Button searchButton;
    EditText searchEditText;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigation;
    AppBarConfiguration appBarConfiguration;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    SearchByNidRecyclerViewAdapter searchByNidRecyclerViewAdapter;
    SearchView searchView;
    List<String> names= new ArrayList<>();
    List<String> nids= new ArrayList<>();
    List<String> contacts= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_nid);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search by Nid/contact No");
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        appBarConfiguration = new AppBarConfiguration.Builder().setDrawerLayout(drawer).build();
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(Gravity.RIGHT))
                {
                    drawer.closeDrawer(Gravity.RIGHT);
                }
                else
                {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
        Menu menu = navigation.getMenu();

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        navigation.setNavigationItemSelectedListener(new AppUtils(this).navigationItemSelectedListener);
        saveUserInfo();
        fetchData("1");
        searchByNidRecyclerViewAdapter = new SearchByNidRecyclerViewAdapter(names,nids,contacts);
        recyclerView.setAdapter(searchByNidRecyclerViewAdapter);
        /*searchButton=findViewById(R.id.search_btn);
        searchEditText = findViewById(R.id.search_edit_text);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchRecentRecords.class));

            }
        });
        searchButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getApplicationContext(),PendingRequest.class));
                return true;
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(dpToPx((int)dpWidth-50),dpToPx(50));
        searchEditText.setLayoutParams(layoutparams);

        tableLayout = findViewById(R.id.table_layout);
        tableLayout.setStretchAllColumns(true);
        loadData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Recent Records");
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();*/
    }
    public void saveUserInfo(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/user";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getSharedPreferences("RMS",MODE_PRIVATE).edit().putString("user",response).apply();}
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchByNid.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+getSharedPreferences("RMS",MODE_PRIVATE).getString("token",""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
    }


    private void fetchData(String param) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://aniksen.me/covidbd/api/relief/search/"+param;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(SearchByNid.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray reliefs= jsonObject.getJSONArray("reliefs");
                    String[] address = new String[reliefs.length()];
                    List<String> s = new ArrayList<>();
                    List<String> s1 = new ArrayList<>();
                    List<String> s2 = new ArrayList<>();
                    names.clear();contacts.clear();nids.clear();
                    //names.add("Name");contacts.add("Contact");nids.add("NID");
                    for(int i=0;i<reliefs.length();i++)
                    {
                        names.add(reliefs.getJSONObject(i).getString("given_to"));
                        contacts.add(reliefs.getJSONObject(i).getString("contact_no"));
                        nids.add(reliefs.getJSONObject(i).getString("nid"));
                    }

                    searchByNidRecyclerViewAdapter.notifyDataSetChanged();
                }catch (Exception e)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchByNid.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-type","Application/json");
                headers.put("Content-type","Application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer: "+getSharedPreferences("RMS",MODE_PRIVATE).getString("token",""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(AppUtils.STRING_REQUEST_RETRY_POLICY);
        queue.add(stringRequest);
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //searchByNidRecyclerViewAdapter.getFilter().filter(query);
                fetchData(query);
                //searchByNidRecyclerViewAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //mAdapter.getFilter().filter(query);
                fetchData(query);
                //searchByNidRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        if(!getSharedPreferences("RMS",MODE_PRIVATE).getString("token","").equals(""))
        {
            Intent intent = new Intent(SearchByNid.this,
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else
        {
            super.onBackPressed();
        }

    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
    public void loadData() {
        int leftRowMargin =0;
        int topRowMargin =0;
        int rightRowMargin =0;
        int bottomRowMargin =0;
        int textSize =0, smallTextSize =0 , mediumTextSize =0;
        textSize =(int) getResources().getDimension(R.dimen.font_size);
        smallTextSize =(int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        ReliefVM row = null;
        final List<ReliefVM> reliefs = ReliefVM.getReliefs();
        int rows = reliefs.size();
        getSupportActionBar().setTitle("Reliefs");
        TextView textSpacer = null;
        tableLayout.removeAllViews();
        //-1 means heading row
        for(int i=-1;i<rows;i++)
        {
            if(i>=0)
            {
                row = reliefs.get(i);
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }
            //data columns
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding(5,15,0,15);
            if(i==-1)
            {
                tv.setText("Inv.#");
            }
            else
            {
                tv.setText(row.getId());
            }
            final TextView tv2 = new TextView(this);
            if(i==-1)
            {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setText("Name");
            }
            else
            {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT));
                tv2.setText(row.getName());
            }
            tv2.setGravity(Gravity.LEFT);
            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setText("Contact");
            }
            else {
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3.setText(row.getContact());
            }
            tv3.setGravity(Gravity.TOP);
            final TableRow tr = new TableRow(this);
            tr.setBackground(getResources().getDrawable(R.drawable.border3));
            tr.addView(tv);
            tr.addView(tv2);
            tr.addView(tv3);
            tableLayout.addView(tr);
            final ReliefVM finalRow= row;
            if(i>-1)
            {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(SearchByNid.this, finalRow.getName(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchByNid.this);

                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        View v1 = getLayoutInflater().inflate(R.layout.relief_details_fragment_dialog, null, false);
                        builder.setView(v1);
                        builder.create();
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }
    }
}
