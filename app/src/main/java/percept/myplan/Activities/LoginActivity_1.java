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
                        params.put("username", EDT_EMAIL.getText().toString().trim());
                        params.put("password", str.toString().trim());
                        new General().getJSONContentFromInternetService(LoginActivity_1.this, General.PHPServices.LOGIN, params, false, false, new VolleyResponseListener() {

                            @Override
                            public void onError(VolleyError message) {
                                Log.d(":::::::::: ", message.toString());
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(":::::::::: ", response.toString());
                                try {
                                    Constant.SID = response.getString("sid");
                                    Constant.SNAME = response.getString("sname");
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
