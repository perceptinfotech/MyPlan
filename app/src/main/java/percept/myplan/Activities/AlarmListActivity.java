package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import percept.myplan.POJO.Alarm;
import percept.myplan.R;
import percept.myplan.adapters.AlarmAdapter;


public class AlarmListActivity extends AppCompatActivity {

    private RecyclerView LST_STRATEGYALARM;
    private AlarmAdapter ADAPTER;

    private static final int ADDALARM = 9;
    private String STR_ALARM;
    private boolean FROM_EDIT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Alarms");

        if (getIntent().hasExtra("FROM_EDIT")) {
            FROM_EDIT = true;
        }

        LST_STRATEGYALARM = (RecyclerView) findViewById(R.id.lstStrategyAlarm);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AlarmListActivity.this);
        LST_STRATEGYALARM.setLayoutManager(mLayoutManager);
        LST_STRATEGYALARM.setItemAnimator(new DefaultItemAnimator());
        if (FROM_EDIT) {
            ADAPTER = new AlarmAdapter(AlarmListActivity.this, StrategyEditActivity.LIST_ALARM);
            LST_STRATEGYALARM.setAdapter(ADAPTER);
        } else {
            ADAPTER = new AlarmAdapter(AlarmListActivity.this, AddStrategyActivity.LIST_ALARM);
            LST_STRATEGYALARM.setAdapter(ADAPTER);
        }
//        ADAPTER = new AlarmAdapter(AlarmListActivity.this,)

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_alarm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ALARMS", String.valueOf(STR_ALARM));
            setResult(Activity.RESULT_OK, returnIntent);
            AlarmListActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_AddAlarm) {
            startActivityForResult(new Intent(AlarmListActivity.this, AddAlarmActivity.class), ADDALARM);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADDALARM) {
            if (resultCode == Activity.RESULT_OK) {
//                STR_STRATEGYID = data.getStringExtra("result");
                data.getStringExtra("ALARM_URI");
                data.getStringExtra("ALARM_SOUND_NAME");
                data.getStringExtra("ALARM_NAME");
                data.getStringExtra("ALARM_REPEAT");
                data.getStringExtra("ALARM_TIME");

                Alarm _obj = new Alarm(data.getStringExtra("ALARM_NAME"), data.getStringExtra("ALARM_TIME"),
                        true, data.getStringExtra("ALARM_URI"), data.getStringExtra("ALARM_REPEAT"), "Snooze",
                        data.getStringExtra("ALARM_SOUND_NAME"));

                if (FROM_EDIT) {
                    StrategyEditActivity.LIST_ALARM.add(_obj);
                    ADAPTER = new AlarmAdapter(AlarmListActivity.this, StrategyEditActivity.LIST_ALARM);
                } else {
                    AddStrategyActivity.LIST_ALARM.add(_obj);
                    ADAPTER = new AlarmAdapter(AlarmListActivity.this, AddStrategyActivity.LIST_ALARM);
                }
                LST_STRATEGYALARM.setAdapter(ADAPTER);


//                Gson gson = new Gson();
//                STR_ALARM = gson.toJson(AddStrategyActivity.LIST_ALARM);
//               UTILS.setPreference("LOCAL_DATA", _str);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
