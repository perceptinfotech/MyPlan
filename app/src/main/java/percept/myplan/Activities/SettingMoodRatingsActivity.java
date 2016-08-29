package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import percept.myplan.POJO.SidaSchedule;
import percept.myplan.R;
import percept.myplan.adapters.SidaScheduleAdapter;

public class SettingMoodRatingsActivity extends AppCompatActivity {

    private SwitchCompat SWITCH_SIDAS, SWITCH_MOOD;
    private LinearLayout LAY_SIDAS;
    private RecyclerView rcvSidas;
    private ArrayList<SidaSchedule> listSidasSchedule = new ArrayList<>();

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

        SWITCH_MOOD = (SwitchCompat) findViewById(R.id.switchMood);
        SWITCH_SIDAS = (SwitchCompat) findViewById(R.id.switchSidas);

        LAY_SIDAS = (LinearLayout) findViewById(R.id.laySidas);
        rcvSidas = (RecyclerView) findViewById(R.id.rcvSidas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SettingMoodRatingsActivity.this);
        rcvSidas.setLayoutManager(mLayoutManager);
        rcvSidas.setItemAnimator(new DefaultItemAnimator());
        listSidasSchedule.add(new SidaSchedule(getString(R.string.everyweek), true));
        listSidasSchedule.add(new SidaSchedule(getString(R.string.everytwoweek), false));
        listSidasSchedule.add(new SidaSchedule(getString(R.string.everythreeweek), false));
        listSidasSchedule.add(new SidaSchedule(getString(R.string.onceamonth), false));

        rcvSidas.setAdapter(new SidaScheduleAdapter(SettingMoodRatingsActivity.this, listSidasSchedule));

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


    }

    private void deselectAll() {

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
