package percept.myplan.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.R;

public class SignUpActivity extends AppCompatActivity {
    private ImageView IMG_USER;
    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_EMAIL, EDT_PHONENO, EDT_BIRTHDAY, EDT_PASSWORD;
    private Button BTN_ENTER;
    private int SDAY = 0, SMONTH = 0, SYEAR = 0;
    private TextView TV_CAPTUREIMG;
    private String FILE_PATH;

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
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
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

                new UploadFileToServer().execute();
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

                File sourceFile = new File(FILE_PATH);

                // Adding file data to http body
                entity.addPart("profile_image", new FileBody(sourceFile));

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
