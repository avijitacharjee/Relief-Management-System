package com.avijit.rms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avijit.rms.location.AppLocationService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyRegistration extends AppCompatActivity {
    TextView chooseImageButton,addButton,addAndNextButton,addressTextView;
    EditText fullName,nid, contactNo, members, earningMembers;

    ImageView imageView;

    private Bitmap bitmap;
    int PICK_IMAGE_REQUEST=111;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ProgressDialog progressDialog;
    private double latitude =0;
    private double longitude=0;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_registration);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RMS"); */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chooseImageButton = findViewById(R.id.img);
        imageView=findViewById(R.id.imageinsert);
        fullName = findViewById(R.id.full_name_edit_text);
        nid = findViewById(R.id.nid_edit_text);
        //TODO change nid to numbertype
        contactNo = findViewById(R.id.contact_no_edit_text);
        members = findViewById(R.id.members_edit_text);
        earningMembers = findViewById(R.id.earning_members_edit_text);
        addButton = findViewById(R.id.add_button);
        addAndNextButton = findViewById(R.id.add_and_next_button);
        addressTextView = findViewById(R.id.address_text_view);
        addressTextView.setText("Address: "+getIntent().getExtras().getString("district")+","+getIntent().getExtras().getString("area")+","+getIntent().getExtras().getString("address"));

        latitude = new AppLocationService(FamilyRegistration.this).getLocation().getLatitude();
        longitude = new AppLocationService(FamilyRegistration.this).getLocation().getLongitude();
        //TODO change buttton color
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final String name = fullName.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://aniksen.me/covidbd/api/relief/store";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(FamilyRegistration.this, response, Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(FamilyRegistration.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Map<String,String> params = new HashMap<>();
                        params.put("division_id",getIntent().getExtras().getString("divisionId"));
                        params.put("district_id",getIntent().getExtras().getString("districtId"));
                        params.put("area_id",getIntent().getExtras().getString("areaId"));
                        params.put("address",getIntent().getExtras().getString("address"));
                        params.put("nid",nid.getText().toString());
                        params.put("members_in_family",members.getText().toString());
                        params.put("earning_members",earningMembers.getText().toString());
                      //  params.put("Earnings_member","2");
                        params.put("lat",latitude+"");
                        params.put("long",longitude+"");
                        params.put("image",imageString);
                        System.out.println(imageString);
                        params.put("contact_no",contactNo.getText().toString());
                        params.put("date_given",""+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        //TODO fix given by id
                        params.put("given_by","1");
                        params.put("given_to","1");
                        return params;
                    }

                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        headers.put("Authorization","Bearer: "+"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyIiwianRpIjoiY2M5MDgzMmYxM2VmNDhkODBmOTM3MDYxMjU4ZjVlODMwODg5ZDE0OWRhZDMwZGZkMzVmN2JmYjZjNTNhNTI4NDExMzhmMmNjM2ExNTk4MjQiLCJpYXQiOjE1ODgxNTQwOTMsIm5iZiI6MTU4ODE1NDA5MywiZXhwIjoxNjE5NjkwMDkzLCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.U1dixRGgiyVopgyBdBRtqS5W6xZXXJMTm5pTDoPCeLKc70p3-zifScXo0AqeSKXYgEOnZPl8sp9nUtRILPuDn3ftDQ3vAsnHqgjhhztDb8LAYtIWLC-pcd48gCCBkLrqfr21oieuZ29-pYrSTwPmM1riMNf_Yy3pooGgLDUddWPJpew5srA_vK_3rlKvGMFg5mMUASVEJIsle6kz2yI71iv8yrQZvLwmKJ6rgVLg_8Rv80Mkyyzht0ZZecFrvISyI8Qpv2fpZE79L8tOWYn2EuORHFdlFTsB4B5PPzPiYVkHhaAnmyFWJQC06PsQOBTIOSisjUcXY9BBby3jFFUmbA-aFg78YEzmw9lvLIb_eQyjGr4DsGzdFsZv3d-FMrtYONSQf5FNoMCh3c-3eIrBVlUhTcHj9qB-7O2wtDe2web9Xoi6rwfO8cZdfdOqr_8T6kpVgz-YepsweeglOcKQtM_QTmP6yVYyajIWO9XnfFWj_xxoxxkWnCpO-wCZihAm_LbijXRLZVxVQp6AqhIWk-ozCeC0ccj4yb2w4xcXCWp1EGc7G2i-Z-u49nNz73nIoF3JIF_qdNzekzJH7MG_9EXuefld_PiSlyZ_8kolOR6Fg-viFhzL5JNFj2LzPI58VuIRGWCYloVkkT_iZDuR13PNcYNPzEOB16cAi3MGQ08");
                        return headers;
                    }
                };

                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                requestQueue.add(stringRequest);
                progressDialog = new ProgressDialog(FamilyRegistration.this);
                progressDialog.setMessage("Adding. Please wait..");
                progressDialog.show();

                /*if (isFormValid())
                {
                    Toast.makeText(FamilyRegistration.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }*/
            }
        });


        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(v);
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private String birthimagetostring(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }
    public void chooseImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageBitmap(bm);
    }
    private boolean isFormValid() {
        boolean valid = true;
        if(fullName.getText().toString().length()==0 || (!fullName.getText().toString().contains(" ")))
        {
            valid = false;
            fullName.setError("Full Name is required");
        }
        if(nid.getText().toString().length()==0)
        {
            valid=false;
            nid.setError("NID is required");
        }
        else if((!nid.getText().toString().matches("\\d+")) || (nid.getText().toString().length()!=16))
        {
            valid = false;
            nid.setError("Invalid NID");
        }
        if(contactNo.getText().toString().length()==0)
        {
            valid= false;
            contactNo.setError("phone no is required");
        }
        else if((!contactNo.getText().toString().matches("\\d+")) || (nid.getText().toString().length()!=11) || (nid.getText().toString().length()!=14) )
        {
            valid = false;
            contactNo.setError("Invalid phone no");
        }
        if((!members.getText().toString().matches("\\d+")))
        {
            valid = false;
            members.setError("Please check");
        }
        else if(Integer.parseInt(members.getText().toString())>20)
        {
            valid=false;
            members.setError("Please check");
        }
        if((!earningMembers.getText().toString().matches("\\d+")))
        {
            valid = false;
            earningMembers.setError("Please check");
        }
        else if(Integer.parseInt(earningMembers.getText().toString())>20)
        {
            valid=false;
            earningMembers.setError("Please check");
        }
        return valid;
    }
    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }
}
