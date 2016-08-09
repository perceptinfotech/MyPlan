package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

public class SettingProfileActivity extends AppCompatActivity {

    private EditText EDT_FIRSTNAME, EDT_LASTNAME, EDT_PASSWORD, EDT_EMAIL;
    Map<String, String> params;

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

        EDT_FIRSTNAME = (EditText) findViewById(R.id.edtFirstName);
        EDT_LASTNAME = (EditText) findViewById(R.id.edtLastName);
        EDT_PASSWORD = (EditText) findViewById(R.id.edtPassword);
        EDT_EMAIL = (EditText) findViewById(R.id.edtEmail);
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
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
            Toast.makeText(SettingProfileActivity.this, "Profile saved called", Toast.LENGTH_SHORT).show();

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
            }
            return true;
        }
        return false;
    }
}