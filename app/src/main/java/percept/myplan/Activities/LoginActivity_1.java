package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.customviews.PinEntryEditText;

public class LoginActivity_1 extends AppCompatActivity {

    private TextView TV_FORGETPWD;
    private Utils UTILS;
    private EditText EDT_EMAIL;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;
    private PinEntryEditText pinEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.app_name));

        UTILS = new Utils(LoginActivity_1.this);
        TV_FORGETPWD = (TextView) findViewById(R.id.tvForgotPwd);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);
        pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        PB = (ProgressBar) findViewById(R.id.progressBar);

        if (!UTILS.getPreference(Constant.PREF_EMAIL).equals("")) {
            EDT_EMAIL.setText(UTILS.getPreference(Constant.PREF_EMAIL));
            pinEntry.requestFocus();
        }

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);


        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
//                    if (str.toString().equals("1234")) {
//                    Toast.makeText(LoginActivity_1.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity_1.this, HomeActivity.class));
//                        LoginActivity_1.this.finish();

                    try {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        LoginCall(str.toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                        PB.setVisibility(View.GONE);
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

    private void LoginCall(final String str) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constant.USER_NAME, EDT_EMAIL.getText().toString().trim());
        params.put(Constant.PASSWORD, str.toString().trim());
        PB.setVisibility(View.VISIBLE);
        try {
            new General().getJSONContentFromInternetService(LoginActivity_1.this, General.PHPServices.LOGIN, params, true, false, true, new VolleyResponseListener() {

                @Override
                public void onError(VolleyError message) {
                    Log.d(":::::::::: ", message.toString());
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(":::::::::: ", response.toString());
                    PB.setVisibility(View.GONE);
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
                                UTILS.setPreference(Constant.PREF_LOGGEDIN, "true");
                                UTILS.setPreference(Constant.PREF_SID, Constant.SID);
                                UTILS.setPreference(Constant.PREF_SNAME, Constant.SNAME);
                                UTILS.setPreference(Constant.PREF_PROFILE_IMG_LINK, Constant.PROFILE_IMG_LINK);
                                UTILS.setPreference(Constant.PREF_PROFILE_USER_NAME, Constant.PROFILE_USER_NAME);
                                UTILS.setPreference(Constant.PREF_PROFILE_EMAIL, Constant.PROFILE_EMAIL);
                                String names[] = TextUtils.split(Constant.PROFILE_NAME, " ");
                                UTILS.setPreference(Constant.PREF_PROFILE_FNAME, names[0]);
                                if (names.length > 1)
                                    UTILS.setPreference(Constant.PREF_PROFILE_LNAME, names[1]);
                                UTILS.setPreference(Constant.PASSWORD, str.trim());
                            } else {
                                pinEntry.setText("", null);
                                Toast.makeText(LoginActivity_1.this, "Login Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        PB.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PB.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LoginCall(str);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity_1.this, LoginActivity.class));
    }
}
