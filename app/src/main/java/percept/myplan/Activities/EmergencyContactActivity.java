package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import percept.myplan.R;

public class EmergencyContactActivity extends AppCompatActivity {


    private TextView TV_ADDEMERGENCYCONTACT;

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

        TV_ADDEMERGENCYCONTACT = (TextView) findViewById(R.id.tvAddNewContactEmergency);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            EmergencyContactActivity.this.finish();
            return true;
        }
        return false;
    }

}
