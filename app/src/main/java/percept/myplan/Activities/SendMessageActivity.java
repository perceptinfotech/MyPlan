package percept.myplan.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import percept.myplan.R;

public class SendMessageActivity extends AppCompatActivity {

    private Button BTN_SEND;
    private TextView TV_MSG;
    private String NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra("NUMBER"))
            NUMBER = getIntent().getStringExtra("NUMBER");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        BTN_SEND = (Button) findViewById(R.id.btnSendMessage);
        TV_MSG = (TextView) findViewById(R.id.tvMsg);


        BTN_SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SendMessageActivity.this.finish();
            return true;
        }
        return false;
    }
}
