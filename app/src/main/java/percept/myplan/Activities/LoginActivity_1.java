package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.General;
import percept.myplan.Global.Constant;
import percept.myplan.R;
import percept.myplan.Global.Utils;
import percept.myplan.Global.VolleyResponseListener;
import percept.myplan.customviews.PinEntryEditText;

public class LoginActivity_1 extends AppCompatActivity {

    private TextView TV_FORGETPWD;
    private Utils UTILS;
    private EditText EDT_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);
        UTILS = new Utils(LoginActivity_1.this);
        TV_FORGETPWD = (TextView) findViewById(R.id.tvForgotPwd);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);

        if (!UTILS.getPreference(Constant.PREF_EMAIL).equals("")) {
            EDT_EMAIL.setText(UTILS.getPreference(Constant.PREF_EMAIL));
        }
        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
//                    if (str.toString().equals("1234")) {
//                    Toast.makeText(LoginActivity_1.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
//                        LoginActivity_1.this.finish();

                    try {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(Constant.USER_NAME, EDT_EMAIL.getText().toString().trim());
                        params.put(Constant.PASSWORD, str.toString().trim());
                        new General().getJSONContentFromInternetService(LoginActivity_1.this, General.PHPServices.LOGIN, params, false, false, new VolleyResponseListener() {

                            @Override
                            public void onError(VolleyError message) {
                                Log.d(":::::::::: ", message.toString());
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(":::::::::: ", response.toString());
                                try {
                                    if (response.has(Constant.DATA)) {
                                        if (response.getJSONObject(Constant.DATA).getString(Constant.STATUS).equals("Success")) {
                                            Constant.SID = response.getJSONObject(Constant.DATA).getString("sid");
                                            Constant.SNAME = response.getJSONObject(Constant.DATA).getString("sname");
                                            Constant.PROFILE_IMG_LINK = response.getJSONObject(Constant.DATA).getString(Constant.PROFILE_IMAGE);
                                            Constant.PROFILE_EMAIL = response.getJSONObject(Constant.DATA).getJSONObject(Constant.USER).getString(Constant.EMAIL);
                                            Constant.PROFILE_USER_NAME = response.getJSONObject(Constant.DATA).getJSONObject(Constant.USER).getString(Constant.USER_NAME);
                                            Constant.PROFILE_NAME = response.getJSONObject(Constant.DATA).getJSONObject(Constant.USER).getString(Constant.NAME);

                                            UTILS.setPreference(Constant.PREF_EMAIL, EDT_EMAIL.getText().toString().trim());
                                            startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
                                            LoginActivity_1.this.finish();
                                        } else {
                                            Toast.makeText(LoginActivity_1.this, "Login Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    } else {
//                        pinEntry.setError(true);
//                        Toast.makeText(LoginActivity_1.this, "FAIL", Toast.LENGTH_SHORT).show();
//                        pinEntry.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                pinEntry.setText(null);
//                            }
//                        }, 1000);
//                    }
                }
            });
        }

//        startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
//        LoginActivity_1.this.finish();


        TV_FORGETPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity_1.this, ForgetPwdActivity.class));
                LoginActivity_1.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity_1.this, LoginActivity.class));
    }
}
