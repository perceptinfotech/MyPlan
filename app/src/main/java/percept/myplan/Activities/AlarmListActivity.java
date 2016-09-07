package percept.myplan.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import percept.myplan.POJO.Alarm;
import percept.myplan.R;
import percept.myplan.adapters.AlarmAdapter;
import percept.myplan.receivers.AlarmReceiver;


interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}

class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

public class AlarmListActivity extends AppCompatActivity {

    private static final int ADDALARM = 9;
    private static final int EDITALARM = 34;
    private RecyclerView LST_STRATEGYALARM;
    private AlarmAdapter ADAPTER;
    private String STR_ALARM;
    private boolean FROM_EDIT = false;
    private int POSITION = 0;

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

    public void editAlarm(int position) {
        Alarm _objAlarm;
        if (FROM_EDIT) {
            _objAlarm = StrategyEditActivity.LIST_ALARM.get(position);
        } else {
            _objAlarm = AddStrategyActivity.LIST_ALARM.get(position);
        }
        Intent _intent = new Intent(AlarmListActivity.this, AddAlarmActivity.class);
        _intent.putExtra("EDIT_ALARM", "EDIT_ALARM");
        _intent.putExtra("ALARM_NAME", _objAlarm.getAlarmName());
        _intent.putExtra("ALARM_REPEAT", _objAlarm.getAlarmRepeat());
        _intent.putExtra("ALARM_TIME", _objAlarm.getAlarmTime());
        _intent.putExtra("ALARM_TUNE", _objAlarm.getAlarmTuneName());
        _intent.putExtra("ALARM_STATUS", String.valueOf(_objAlarm.isStatus()));
        POSITION = position;
        startActivityForResult(_intent, ADDALARM);
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

                if (data.hasExtra("EDIT_ALARM")) {
                    if (FROM_EDIT) {
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setAlarmName(data.getStringExtra("ALARM_NAME"));
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setAlarmTime(data.getStringExtra("ALARM_TIME"));
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setStatus(true);
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setAlarmTune(data.getStringExtra("ALARM_URI"));
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setAlarmRepeat(data.getStringExtra("ALARM_REPEAT"));
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setSnooze("Snooze");
                        StrategyEditActivity.LIST_ALARM.get(POSITION).setAlarmTuneName(data.getStringExtra("ALARM_SOUND_NAME"));
                        ADAPTER = new AlarmAdapter(AlarmListActivity.this, StrategyEditActivity.LIST_ALARM);
                    } else {
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setAlarmName(data.getStringExtra("ALARM_NAME"));
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setAlarmTime(data.getStringExtra("ALARM_TIME"));
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setStatus(true);
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setAlarmTune(data.getStringExtra("ALARM_URI"));
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setAlarmRepeat(data.getStringExtra("ALARM_REPEAT"));
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setSnooze("Snooze");
                        AddStrategyActivity.LIST_ALARM.get(POSITION).setAlarmTuneName(data.getStringExtra("ALARM_SOUND_NAME"));
                        ADAPTER = new AlarmAdapter(AlarmListActivity.this, AddStrategyActivity.LIST_ALARM);
                    }
                } else {
                    if (FROM_EDIT) {
                        StrategyEditActivity.LIST_ALARM.add(_obj);
                        ADAPTER = new AlarmAdapter(AlarmListActivity.this, StrategyEditActivity.LIST_ALARM);
                    } else {
                        AddStrategyActivity.LIST_ALARM.add(_obj);
                        ADAPTER = new AlarmAdapter(AlarmListActivity.this, AddStrategyActivity.LIST_ALARM);
                    }
                }

//                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("ALARM_SOUND", data.getStringExtra("ALARM_SOUND"));
//                intent.putExtras(bundle);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                        getBaseContext(), 11, intent, 0);
//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(data.getStringExtra("ALARM_TIME")), AlarmManager.INTERVAL_DAY,
//                        pendingIntent);

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
