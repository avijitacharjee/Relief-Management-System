package com.avijit.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.avijit.rms.viewmodels.ReliefVM;

import java.util.List;

public class SearchByNid extends AppCompatActivity {
    TableLayout tableLayout;
    Button searchButton;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_nid);

        searchButton=findViewById(R.id.search_btn);
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
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
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
