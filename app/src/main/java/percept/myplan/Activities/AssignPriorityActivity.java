package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import percept.myplan.R;

/**
 * Created by percept on 22/8/16.
 */
public class AssignPriorityActivity extends AppCompatActivity {
    TextView tvHelp, tvEmergency;
    private int con_priority = 0; // 0: Default 1:Help  2:Emergency

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_priority);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.assign_priority));

        tvEmergency = (TextView) findViewById(R.id.tvEmergency);
        tvHelp = (TextView) findViewById(R.id.tvHelp);

        if (getIntent().getStringExtra("ADD_TO_HELP").equals("1")) {
            tvHelp.setBackgroundColor(getResources().getColor(R.color.sidemenu_seperator));
            tvEmergency.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        tvEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEmergency.setBackgroundColor(getResources().getColor(R.color.sidemenu_seperator));
                tvHelp.setBackgroundColor(getResources().getColor(android.R.color.white));
                con_priority = 2;
            }
        });

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHelp.setBackgroundColor(getResources().getColor(R.color.sidemenu_seperator));
                tvEmergency.setBackgroundColor(getResources().getColor(android.R.color.white));
                con_priority = 1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_saveNote) {
            Intent intent = new Intent();
            intent.putExtra("FROM_PRIORITY", con_priority);
            setResult(Activity.RESULT_OK, intent);
            AssignPriorityActivity.this.finish();
            return true;
        }
        return false;
    }
}
