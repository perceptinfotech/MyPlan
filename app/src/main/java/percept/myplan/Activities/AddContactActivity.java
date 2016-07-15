package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import percept.myplan.R;

public class AddContactActivity extends AppCompatActivity {

    TextView TV_PHONELIST, TV_NEWCONTACT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TV_NEWCONTACT = (TextView) findViewById(R.id.tvNewContact);
        TV_PHONELIST = (TextView) findViewById(R.id.tvPhoneList);

        TV_NEWCONTACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TV_PHONELIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddContactActivity.this,AddContactFromPhoneActivity.class));
                AddContactActivity.this.finish();
            }
        });
    }

}
