package percept.myplan.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.General;
import percept.myplan.Global.VolleyResponseListener;
import percept.myplan.R;

public class SignUpActivity extends AppCompatActivity {
    private ImageView IMG_USER;
    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_EMAIL, EDT_PHONENO, EDT_BIRTHDAY, EDT_PASSWORD;
    private Button BTN_ENTER;
    private int SDAY = 0, SMONTH = 0, SYEAR = 0;
    private TextView TV_CAPTUREIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", EDT_FIRSTNAME.getText().toString().trim());
                params.put("last_name", EDT_LASTNAME.getText().toString().trim());
                params.put("email", EDT_EMAIL.getText().toString().trim());
                params.put("phone", EDT_PHONENO.getText().toString().trim());
                params.put("dob", EDT_BIRTHDAY.getText().toString().trim());
                params.put("password", EDT_PASSWORD.getText().toString().trim());
                params.put("profile_image", "admin");
                try {
                    new General().getJSONContentFromInternetService(SignUpActivity.this, General.PHPServices.REGISTER, params, false, false, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            Log.d("::::::::::", message.toString());
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("::::::::::", response.toString());
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            SignUpActivity.this.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        setViews();
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
