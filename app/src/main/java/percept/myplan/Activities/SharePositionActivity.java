package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import percept.myplan.R;

import static percept.myplan.Activities.AddContactFromPhoneActivity.SENT;

public class SharePositionActivity extends AppCompatActivity {

    private Button BTN_SEND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_position);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.sharepos));

        BTN_SEND = (Button) findViewById(R.id.btnSend);

        BTN_SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SENT = true;
                SharePositionActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SharePositionActivity.this.finish();
            return true;
        }
        return false;
    }
}
