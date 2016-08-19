package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import percept.myplan.Global.Utils;
import percept.myplan.R;

public class EmergencyContactActivity extends AppCompatActivity {


    private TextView TV_ADDEMERGENCYCONTACT, TV_EMERGENCYCONTACT;
    private Utils UTILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_emergency_contact));

        UTILS = new Utils(EmergencyContactActivity.this);
        TV_ADDEMERGENCYCONTACT = (TextView) findViewById(R.id.tvAddNewContactEmergency);
        TV_EMERGENCYCONTACT = (TextView) findViewById(R.id.tvEmergencyContact);
        TV_ADDEMERGENCYCONTACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(EmergencyContactActivity.this, AddContactFromPhoneActivity.class);
                _intent.putExtra("FROM", "emergency");
                startActivity(_intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UTILS.getPreference("EMERGENCY_CONTACT_NAME").equals("")) {
            TV_EMERGENCYCONTACT.setText(UTILS.getPreference("EMERGENCY_CONTACT_NAME"));
        } else {
            TV_EMERGENCYCONTACT.setText("112");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            EmergencyContactActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_addContact) {
            Intent _intent = new Intent(EmergencyContactActivity.this, AddContactFromPhoneActivity.class);
            _intent.putExtra("FROM", "emergency");
            startActivity(_intent);
            return true;
        }

        return false;
    }

}
