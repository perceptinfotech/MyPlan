package percept.myplan.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import percept.myplan.R;

public class AddAlarmActivity extends AppCompatActivity {

    private final int REQ_CODE_ALARM_REPEAT = 320;
    private TextView TV_ALARMREPEAT, TV_ALARMSOUND, tvAlarmTime;
    private Switch SWITCH_ALARMSNOOZE;
    private EditText EDT_ALARMLABLE;
    private AlarmManager ALARM_MANAGER;
    private String ALARM_SOUND, ALARM_SOUND_NAME, ALARM_NAME, ALARM_REPEAT, ALARM_TIME;
    //    private DatePicker DATE_PICKER;
    private TimePicker TIME_PICKER;
    private String repeatIds = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.add_alarm));


        TV_ALARMREPEAT = (TextView) findViewById(R.id.tvAlarmRepeat);
        tvAlarmTime = (TextView) findViewById(R.id.tvAlarmTime);
        TV_ALARMSOUND = (TextView) findViewById(R.id.tvAlarmSound);
        SWITCH_ALARMSNOOZE = (Switch) findViewById(R.id.switchAlarmSnooze);
        EDT_ALARMLABLE = (EditText) findViewById(R.id.edtAlarmLable);
//        DATE_PICKER = (DatePicker) findViewById(R.id.datePicker);
        TIME_PICKER = (TimePicker) findViewById(R.id.timePicker);

        if (getIntent().hasExtra("EDIT_ALARM")) {
            EDT_ALARMLABLE.setText(getIntent().getExtras().getString("ALARM_NAME"));
            repeatIds = getIntent().getExtras().getString("ALARM_REPEAT");

            TV_ALARMSOUND.setText(getIntent().getExtras().getString("ALARM_TUNE"));
            ALARM_SOUND_NAME = getIntent().getExtras().getString("ALARM_NAME");
            ALARM_SOUND = getIntent().getExtras().getString("ALARM_URI");

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(Long.valueOf(getIntent().getExtras().getString("ALARM_TIME")));
            TIME_PICKER.setCurrentHour(calendar2.get(Calendar.HOUR_OF_DAY)); // or Calendar.HOUR for 12 hour format
            TIME_PICKER.setCurrentMinute(calendar2.get(Calendar.MINUTE));
        } else {
            Ringtone ringtone = RingtoneManager.getRingtone(AddAlarmActivity.this,
                    Settings.System.DEFAULT_NOTIFICATION_URI);
            TV_ALARMSOUND.setText(ringtone.getTitle(AddAlarmActivity.this));
        }
        tvAlarmTime.setText(TIME_PICKER.getCurrentHour() + " : " + TIME_PICKER.getCurrentMinute());
        String[] repeatIdArr = TextUtils.split(repeatIds, ",");
        if (repeatIdArr.length == 1) {
            TV_ALARMREPEAT.setText(getResources().getStringArray(R.array.alarm_repeat)[Integer.parseInt(repeatIdArr[0])]);

        } else
            TV_ALARMREPEAT.setText(getString(R.string.multiple_days));

        TV_ALARMSOUND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(ringtone, 12);
            }
        });
        TV_ALARMREPEAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAlarmActivity.this, AlarmRepeatListActivity.class);
                intent.putExtra("ALARM_REPEAT", repeatIds);
                startActivityForResult(intent, REQ_CODE_ALARM_REPEAT);
            }
        });

        TIME_PICKER.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                tvAlarmTime.setText(hour + " : " + minute);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddAlarmActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_SaveProfile) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, TIME_PICKER.getCurrentHour());
            cal.set(Calendar.MINUTE, TIME_PICKER.getCurrentMinute());

            Long _Alarmtime = cal.getTimeInMillis();

            Intent returnIntent = new Intent();
            String _str = "";
            returnIntent.putExtra("ALARM_URI", ALARM_SOUND);
            returnIntent.putExtra("ALARM_SOUND_NAME", ALARM_SOUND_NAME);
            returnIntent.putExtra("ALARM_NAME", EDT_ALARMLABLE.getText().toString());
            returnIntent.putExtra("ALARM_REPEAT", repeatIds);
            returnIntent.putExtra("ALARM_TIME", String.valueOf(_Alarmtime));
            if (getIntent().hasExtra("EDIT_ALARM")) {
                returnIntent.putExtra("EDIT_ALARM", "EDIT_ALARM");
            }
            setResult(Activity.RESULT_OK, returnIntent);
            AddAlarmActivity.this.finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 12:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Ringtone ringtone;
                    String Str;
                    if (uri != null) {
                        ringtone = RingtoneManager.getRingtone(this, uri);
                        // Get your title here `ringtone.getTitle(this)
                        Str = ringtone.getTitle(this);
                    } else Str = "Slient";


                    TV_ALARMSOUND.setText(Str);
                    Log.d("::::: ", Str);
                    ALARM_SOUND = String.valueOf(uri);
                    ALARM_SOUND_NAME = Str;
                }
                break;
            case REQ_CODE_ALARM_REPEAT:
                if (data != null) {
                    repeatIds = data.getStringExtra("ALARM_REPEAT");
                    String[] repeatIdArr = TextUtils.split(repeatIds, ",");
                    if (repeatIdArr.length == 1) {
                        TV_ALARMREPEAT.setText(getResources().getStringArray(R.array.alarm_repeat)[Integer.parseInt(repeatIdArr[0])]);

                    } else
                        TV_ALARMREPEAT.setText(getString(R.string.multiple_days));
                } else
                    TV_ALARMREPEAT.setText(getString(R.string.repeat));
                break;

        }
    }

}



