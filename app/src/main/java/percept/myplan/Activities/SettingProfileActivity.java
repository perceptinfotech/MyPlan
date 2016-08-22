package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

public class SettingProfileActivity extends AppCompatActivity {

    private EditText EDT_FIRSTNAME, EDT_LASTNAME,  EDT_EMAIL;
    Map<String, String> params;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;
    private LinearLayout LL_PASSWORD;
    private int CHANGE_PASSWORD_REQUEST_CODE = 1;
    private TextView TV_PASSWORD;

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

        EDT_FIRSTNAME = (EditText) findViewById(R.id.edtFirstName);
        EDT_LASTNAME = (EditText) findViewById(R.id.edtLastName);
        TV_PASSWORD = (TextView) findViewById(R.id.tvPassword);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);

        LL_PASSWORD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SettingProfileActivity.this,
                        ChangePasswordActivity.class), CHANGE_PASSWORD_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SettingProfileActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_SaveProfile) {
            SettingProfileActivity.this.finish();
//            Toast.makeText(SettingProfileActivity.this, "Profile saved called", Toast.LENGTH_SHORT).show();

            SaveProfile();

            return true;
        }
        return false;
    }

    private void SaveProfile() {
        try {
            new General().getJSONContentFromInternetService(SettingProfileActivity.this, General.PHPServices.SAVE_PROFILE, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {

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
                            SaveProfile();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHANGE_PASSWORD_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            if (data!=null && data.hasExtra("change_password"))
            {
                TV_PASSWORD.setText(data.getStringExtra("change_password"));
            }
        }
    }
}
