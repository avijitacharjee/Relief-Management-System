package com.avijit.rms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.avijit.rms.viewmodels.ReliefVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SearchRecentRecords extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TableLayout tableLayout;
    Spinner divisionSpinner,districtSpinner;

    String[] divisions = {"--Select division--"};
    String[] districts = {"--Select district--"};
    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recent_records);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });




        tableLayout = findViewById(R.id.table_layout);
        divisionSpinner = findViewById(R.id.division_spinner);
        districtSpinner = findViewById(R.id.district_spinner);
        toolbar = findViewById(R.id.toolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,divisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(adapter);

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,districts);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter1);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //district = districtSpinner.getSelectedItem().toString();
                //setDistricts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loadData();


    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        Toast.makeText(this, ""+item, Toast.LENGTH_SHORT).show();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pending_request, menu);
        return true;
    }



    public void loadData()
    {
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
        TextView textSpacer = null;
        tableLayout.removeAllViews();
        //-1 means heading row
        for(int i=-1;i<rows;i++)
        {
            if(i==-1)
            {
                row=null;
            }
            else
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
                        Toast.makeText(SearchRecentRecords.this, finalRow.getName().toString(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchRecentRecords.this);

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Toast.makeText(this, ""+menuItem, Toast.LENGTH_SHORT).show();
        return true;
    }
}
