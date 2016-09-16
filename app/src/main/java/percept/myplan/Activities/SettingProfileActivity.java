package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.MultiPartParsing;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

public class SettingProfileActivity extends AppCompatActivity {

    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_EMAIL;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;
    private LinearLayout LL_PASSWORD;
    private int CHANGE_PASSWORD_REQUEST_CODE = 1;
    private TextView TV_PASSWORD;
    private Utils utils;
    private String strOldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_profile));

        PB = (ProgressBar) findViewById(R.id.pbSaveProfile);
        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        LL_PASSWORD = (LinearLayout) findViewById(R.id.llPassword);

        EDT_FIRSTNAME = (EditText) findViewById(R.id.edtProfileFirstName);
        EDT_LASTNAME = (EditText) findViewById(R.id.edtProfileLastName);
        TV_PASSWORD = (TextView) findViewById(R.id.tvPassword);
        EDT_EMAIL = (EditText) findViewById(R.id.edtProfileEmail);


        LL_PASSWORD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SettingProfileActivity.this,
                        ChangePasswordActivity.class), CHANGE_PASSWORD_REQUEST_CODE);
            }
        });
        utils = new Utils(SettingProfileActivity.this);
        getProfile();
        EDT_FIRSTNAME.setText(utils.getPreference(Constant.PREF_PROFILE_FNAME));
        EDT_LASTNAME.setText(utils.getPreference(Constant.PREF_PROFILE_LNAME));
        EDT_EMAIL.setText(utils.getPreference(Constant.PREF_PROFILE_EMAIL));
        TV_PASSWORD.setText(utils.getPreference(Constant.PASSWORD));
        strOldPassword = utils.getPreference(Constant.PASSWORD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SettingProfileActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_SaveProfile) {
            SaveProfile();
            return true;
        }
        return false;
    }

    private void getProfile() {
        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(SettingProfileActivity.this, General.PHPServices.GET_PROFILE, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    if (response.has(Constant.DATA)) {
                        try {
                            Constant.PROFILE_IMG_LINK = response.getJSONObject(Constant.DATA).getString(Constant.PROFILE_IMAGE);
                            Constant.PROFILE_EMAIL = response.getJSONObject(Constant.DATA).getString(Constant.EMAIL);

                            utils.setPreference(Constant.PREF_EMAIL, EDT_EMAIL.getText().toString().trim());
                            utils.setPreference(Constant.PREF_PROFILE_IMG_LINK, Constant.PROFILE_IMG_LINK);
                            utils.setPreference(Constant.PREF_PROFILE_EMAIL, Constant.PROFILE_EMAIL);
                            utils.setPreference(Constant.PREF_PROFILE_FNAME, response.getJSONObject(Constant.DATA).getString(Constant.FIRST_NAME));
                            utils.setPreference(Constant.PREF_PROFILE_LNAME, response.getJSONObject(Constant.DATA).getString(Constant.FIRST_NAME));

                            EDT_FIRSTNAME.setText(utils.getPreference(Constant.PREF_PROFILE_FNAME));
                            EDT_LASTNAME.setText(utils.getPreference(Constant.PREF_PROFILE_LNAME));
                            EDT_EMAIL.setText(utils.getPreference(Constant.PREF_PROFILE_EMAIL));
                            TV_PASSWORD.setText(utils.getPreference(Constant.PASSWORD));
                            strOldPassword = utils.getPreference(Constant.PASSWORD);
                            PB.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SaveProfile() {
        if (!utils.isNetConnected()) {
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SaveProfile();
                        }
                    });
            snackbar.show();
            return;
        }
        PB.setVisibility(View.VISIBLE);


        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("first_name", EDT_FIRSTNAME.getText().toString());
        params.put("last_name", EDT_LASTNAME.getText().toString());
        params.put("email", EDT_EMAIL.getText().toString());
        params.put("password", TV_PASSWORD.getText().toString());
        params.put("oldpass", strOldPassword);
        params.put("phone", "");
        params.put("dob", "");


        new MultiPartParsing(SettingProfileActivity.this, params, General.PHPServices.PROFILE, new AsyncTaskCompletedListener() {
            @Override
            public void onTaskCompleted(String response) {
                Log.d(":::Profile Edit", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getJSONObject("data").getString("status").equalsIgnoreCase("Success")) {
                        utils.setPreference(Constant.PREF_PROFILE_FNAME, EDT_FIRSTNAME.getText().toString());
                        utils.setPreference(Constant.PREF_PROFILE_LNAME, EDT_LASTNAME.getText().toString());
                        utils.setPreference(Constant.PASSWORD, TV_PASSWORD.getText().toString().trim());
                        Toast.makeText(SettingProfileActivity.this, "Profile saved Successfully", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SettingProfileActivity.this, "Profile saved Failed", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PB.setVisibility(View.GONE);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_PASSWORD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("change_password")) {
                strOldPassword = data.getStringExtra("old_password");
                TV_PASSWORD.setText(data.getStringExtra("change_password"));
            }
        }
    }
}
