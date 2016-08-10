package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import percept.myplan.R;

public class SettingMoodRatingsActivity extends AppCompatActivity {

    private Switch SWITCH_MOOD, SWITCH_SIDAS;
    private LinearLayout LAY_SIDAS;
    private CheckBox CHK_EVERYWEEK, CHK_EVERYTWOWEEK, CHK_EVERYTHREEWEEK, CHK_ONCEAMONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_ratings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_moodratings));

        SWITCH_MOOD = (Switch) findViewById(R.id.switchMood);
        SWITCH_SIDAS = (Switch) findViewById(R.id.switchSidas);

        LAY_SIDAS = (LinearLayout) findViewById(R.id.laySidas);

        CHK_EVERYWEEK = (CheckBox) findViewById(R.id.chkEveryWeek);
        CHK_EVERYTWOWEEK = (CheckBox) findViewById(R.id.chkEveryTwoWeek);
        CHK_EVERYTHREEWEEK = (CheckBox) findViewById(R.id.chkEveryThreeWeek);
        CHK_ONCEAMONTH = (CheckBox) findViewById(R.id.chkOnceAMonth);

        SWITCH_SIDAS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    LAY_SIDAS.setVisibility(View.VISIBLE);
                else
                    LAY_SIDAS.setVisibility(View.GONE);
            }
        });

        SWITCH_MOOD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        CHK_EVERYWEEK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    CHK_EVERYWEEK.setTextColor(getResources().getColor(android.R.color.black));
                else
                    CHK_EVERYWEEK.setTextColor(getResources().getColor(R.color.toobarbelow));
            }
        });

        CHK_EVERYTWOWEEK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    CHK_EVERYTWOWEEK.setTextColor(getResources().getColor(android.R.color.black));
                else
                    CHK_EVERYTWOWEEK.setTextColor(getResources().getColor(R.color.toobarbelow));
            }
        });

        CHK_EVERYTHREEWEEK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    CHK_EVERYTHREEWEEK.setTextColor(getResources().getColor(android.R.color.black));
                else
                    CHK_EVERYTHREEWEEK.setTextColor(getResources().getColor(R.color.toobarbelow));
            }
        });
        CHK_ONCEAMONTH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    CHK_ONCEAMONTH.setTextColor(getResources().getColor(android.R.color.black));
                else
                    CHK_ONCEAMONTH.setTextColor(getResources().getColor(R.color.toobarbelow));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SettingMoodRatingsActivity.this.finish();
            return true;
        }
        return false;
    }

}
