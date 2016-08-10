package percept.myplan.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import percept.myplan.Dialogs.dialogAddStrategy;
import percept.myplan.Dialogs.dialogSelectPic;
import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

import static percept.myplan.Activities.AddStrategyToSymptomActivity.GET_STRATEGIES;
import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

public class SignUpActivity extends AppCompatActivity {
    private ImageView IMG_USER;
    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_EMAIL, EDT_PHONENO, EDT_BIRTHDAY, EDT_PASSWORD;
    private Button BTN_ENTER;
    private int SDAY = 0, SMONTH = 0, SYEAR = 0;
    private TextView TV_CAPTUREIMG;
    private String FILE_PATH = "";
    private Utils UTILS;
    private ProgressBar PB;
    private static Uri IMG_URI;
    private static final int REQ_TAKE_PICTURE = 33;

    private final static int MY_PERMISSIONS_REQUEST = 14;

    public static boolean PIC_FROM_GALLERY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.myplan));

        UTILS = new Utils(SignUpActivity.this);
        PB = (ProgressBar) findViewById(R.id.progressBar);
        TV_CAPTUREIMG = (TextView) findViewById(R.id.tvCaptureImg);
        IMG_USER = (ImageView) findViewById(R.id.imgUserImage);
        EDT_FIRSTNAME = (EditText) findViewById(R.id.edtFirstName);
        EDT_LASTNAME = (EditText) findViewById(R.id.edtLastName);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);
        EDT_PHONENO = (EditText) findViewById(R.id.edtPhoneNo);
        EDT_BIRTHDAY = (EditText) findViewById(R.id.edtBirthDay);
        EDT_PASSWORD = (EditText) findViewById(R.id.edtPassword);
        BTN_ENTER = (Button) findViewById(R.id.btnEnter);

        IMG_USER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogSelectPic _dialogDate = new dialogSelectPic(SignUpActivity.this);
                _dialogDate.setCanceledOnTouchOutside(false);
                _dialogDate.show();
                _dialogDate.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                                        Manifest.permission.CAMERA)) {
                                    // Show an expanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
                                } else {
                                    // No explanation needed, we can request the permission.
                                    ActivityCompat.requestPermissions(SignUpActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                            MY_PERMISSIONS_REQUEST);
                                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }
                            } else {
                                if (PIC_FROM_GALLERY) {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);
                                } else {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    IMG_URI = Uri.fromFile(Constant.getOutputMediaFile());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, IMG_URI);
                                    // start the image capture Intent
                                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                                }

                            }
                        } else {
                            if (PIC_FROM_GALLERY) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                IMG_URI = Uri.fromFile(Constant.getOutputMediaFile());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, IMG_URI);
                                // start the image capture Intent
                                startActivityForResult(intent, REQ_TAKE_PICTURE);
                            }
                        }
                    }
                });


            }
        });

        Calendar cal = Calendar.getInstance();
        SYEAR = cal.get(Calendar.YEAR);
        SMONTH = cal.get(Calendar.MONTH);
        SDAY = cal.get(Calendar.DAY_OF_MONTH);
        showDate(SYEAR, SMONTH, SDAY);

        EDT_BIRTHDAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this, myDateListener, SYEAR, SMONTH - 1, SDAY).show();
            }
        });

        BTN_ENTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
//                SignUpActivity.this.finish();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                if (UTILS.isNetConnected()) {
                    boolean error = false;
                    if (EDT_FIRSTNAME.getText().toString().trim().equals("")) {

                        error = true;
                    } else if (EDT_LASTNAME.getText().toString().trim().equals("")) {
                        error = true;
                    } else if (EDT_PASSWORD.getText().toString().trim().equals("")) {
                        error = true;
                    } else if (EDT_PHONENO.getText().toString().trim().equals("")) {
                        error = true;
                    } else if (EDT_BIRTHDAY.getText().toString().trim().equals("")) {
                        error = true;
                    }

                    if (error) {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.fillupfield),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (UTILS.isEmailValid(EDT_EMAIL.getText().toString())) {
                        PB.setVisibility(View.VISIBLE);
                        new UploadFileToServer().execute();
                    } else {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.writevalidemail), Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.internetconn), Toast.LENGTH_SHORT).show();
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(Constant.FIRST_NAME, EDT_FIRSTNAME.getText().toString().trim());
//                params.put(Constant.LAST_NAME, EDT_LASTNAME.getText().toString().trim());
//                params.put(Constant.EMAIL, EDT_EMAIL.getText().toString().trim());
//                params.put(Constant.PHONE, EDT_PHONENO.getText().toString().trim());
//                params.put(Constant.DOB, EDT_BIRTHDAY.getText().toString().trim());
//                params.put(Constant.PASSWORD, EDT_PASSWORD.getText().toString().trim());
//                params.put(Constant.PROFILE_IMAGE, "admin");
//                try {
//                    new General().getJSONContentFromInternetService(SignUpActivity.this, General.PHPServices.REGISTER, params, false, false, new VolleyResponseListener() {
//                        @Override
//                        public void onError(VolleyError message) {
//                            Log.d("::::::::::", message.toString());
//                        }
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d("::::::::::", response.toString());
//                            startActivity(new Intent(SignUpActivity.this, LoginActivity_1.class));
//                            SignUpActivity.this.finish();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
        setViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.readstoragedenied, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".register");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (!FILE_PATH.equals("")) {
                    File sourceFile = new File(FILE_PATH);

                    // Adding file data to http body
                    entity.addPart("profile_image", new FileBody(sourceFile));
                }
                // Extra parameters if you want to pass to server
                try {
                    entity.addPart(Constant.FIRST_NAME, new StringBody(EDT_FIRSTNAME.getText().toString().trim()));
                    entity.addPart(Constant.LAST_NAME, new StringBody(EDT_LASTNAME.getText().toString().trim()));
                    entity.addPart(Constant.EMAIL, new StringBody(EDT_EMAIL.getText().toString().trim()));
                    entity.addPart(Constant.PHONE, new StringBody(EDT_PHONENO.getText().toString().trim()));
                    entity.addPart(Constant.DOB, new StringBody(EDT_BIRTHDAY.getText().toString().trim()));
                    entity.addPart(Constant.PASSWORD, new StringBody(EDT_PASSWORD.getText().toString().trim()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            PB.setVisibility(View.GONE);
            try {
                JSONObject _object = new JSONObject(result);
                JSONObject _ObjData = _object.getJSONObject(Constant.DATA);
                if (_ObjData.getString(Constant.STATUS).equals("Success")) {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity_1.class));
                    SignUpActivity.this.finish();
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.signuperror), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void setViews() {
        IMG_USER = (ImageView) findViewById(R.id.imgUserImage);

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        EDT_BIRTHDAY.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year));
        SYEAR = year;
        SMONTH = month;
        SDAY = day;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                FILE_PATH = c.getString(columnIndex);
                c.close();
//                IMG_USER.setImageURI(selectedImage);
                Picasso.with(SignUpActivity.this).load(selectedImage).into(IMG_USER);
                TV_CAPTUREIMG.setVisibility(View.INVISIBLE);
            }
            if (requestCode == REQ_TAKE_PICTURE) {

                try {

                    Calendar cal = Calendar.getInstance();
                    int seconds = cal.get(Calendar.SECOND);
                    int hour = cal.get(Calendar.HOUR);
                    int min = cal.get(Calendar.MINUTE);
                    String currentDateTimeString = new SimpleDateFormat("ddMMMyyyy").format(new Date());

                    String name = "IMG_" + currentDateTimeString + seconds + hour + min + ".jpeg";

                    String _imgPath = IMG_URI.getPath();

                    File mediaStorageDir = new File(Constant.APP_MEDIA_PATH + File.separator + "IMAGES");

                    // Create the storage directory if it does not exist
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {

                        }
                    }

                    Constant.copyFile(_imgPath, Constant.APP_MEDIA_PATH + File.separator + "IMAGES", name);

                    File file = new File(_imgPath);
                    if (file.exists()) {
//                        file.delete();
                    }
                    FILE_PATH = Constant.APP_MEDIA_PATH + File.separator + "IMAGES" + File.separator + name;

                    Picasso.with(SignUpActivity.this).load(IMG_URI).into(IMG_USER);
                    TV_CAPTUREIMG.setVisibility(View.INVISIBLE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        SignUpActivity.this.finish();
    }
}
