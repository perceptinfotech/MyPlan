package percept.myplan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import percept.myplan.R;

public class AddStrategyActivity extends AppCompatActivity {

    private EditText EDT_TITLE, EDT_TEXT;
    private TextView TV_ALARM, TV_IMAGES, TV_LINKS, TV_NETWORK, TV_MUSIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addastrategy));

        EDT_TITLE = (EditText) findViewById(R.id.edtTitle);
        EDT_TEXT = (EditText) findViewById(R.id.edtText);

        TV_ALARM = (TextView) findViewById(R.id.tvAlarm);
        TV_IMAGES = (TextView) findViewById(R.id.tvImages);
        TV_LINKS = (TextView) findViewById(R.id.tvLinks);
        TV_NETWORK = (TextView) findViewById(R.id.tvNetwork);
        TV_MUSIC = (TextView) findViewById(R.id.tvMusic);


    }
}
