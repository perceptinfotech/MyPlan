package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

import percept.myplan.R;

public class MoodRatingsActivity extends AppCompatActivity {

    private Switch SWITCH_MOOD, SWITCH_SIDAS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_ratings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        SWITCH_MOOD = (Switch) findViewById(R.id.switchMood);
        SWITCH_SIDAS = (Switch) findViewById(R.id.switchSidas);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MoodRatingsActivity.this.finish();
            return true;
        }
        return false;
    }

}
