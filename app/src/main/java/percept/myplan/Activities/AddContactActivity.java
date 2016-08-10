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

public class AddContactActivity extends AppCompatActivity {

    TextView TV_PHONELIST, TV_NEWCONTACT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_add_contact));

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
                Intent _intent = new Intent(AddContactActivity.this, AddContactFromPhoneActivity.class);
                if (getIntent().hasExtra("ADD_TO_HELP")) {
                    _intent.putExtra("ADD_TO_HELP", "true");
                }
                if(getIntent().hasExtra("FROM_QUICKMSG"))
                    _intent.putExtra("FROM_QUICKMSG",getIntent().getExtras().getString("FROM_QUICKMSG"));
                startActivity(_intent);
                AddContactActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddContactActivity.this.finish();
            return true;
        }
        return false;
    }
}
