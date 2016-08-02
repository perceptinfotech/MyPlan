package percept.myplan.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import percept.myplan.POJO.SymptomStrategy;
import percept.myplan.R;
import percept.myplan.receivers.AlarmReceiver;

import static percept.myplan.Activities.AddNewSymptomActivity.LIST_ADDSYMPTOMSTRATEGY;
import static percept.myplan.Activities.SymptomDetailsActivity.LIST_SYMPTOMSTRATEGY;

public class AddAlarmActivity extends AppCompatActivity {

    private TextView TV_ALARMREPEAT, TV_ALARMSOUND;
    private Switch SWITCH_ALARMSNOOZE;
    private EditText EDT_ALARMLABLE;
    private AlarmManager ALARM_MANAGER;
    private String ALARM_SOUND, ALARM_SOUND_NAME, ALARM_NAME, ALARM_REPEAT, ALARM_TIME;
    private DatePicker DATE_PICKER;
    private TimePicker TIME_PICKER;

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
        mTitle.setText("Add Alarm");

        TV_ALARMREPEAT = (TextView) findViewById(R.id.tvAlarmRepeat);
        TV_ALARMSOUND = (TextView) findViewById(R.id.tvAlarmSound);
        SWITCH_ALARMSNOOZE = (Switch) findViewById(R.id.switchAlarmSnooze);
        EDT_ALARMLABLE = (EditText) findViewById(R.id.edtAlarmLable);

        DATE_PICKER = (DatePicker) findViewById(R.id.datePicker);
        TIME_PICKER = (TimePicker) findViewById(R.id.timePicker);

        TV_ALARMSOUND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                startActivityForResult(ringtone, 12);
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
            cal.set(DATE_PICKER.getYear(),
                    DATE_PICKER.getMonth(),
                    DATE_PICKER.getDayOfMonth(),
                    TIME_PICKER.getCurrentHour(),
                    TIME_PICKER.getCurrentMinute(),
                    00);

            Long _Alarmtime = cal.getTimeInMillis();

            Intent returnIntent = new Intent();
            String _str = "";
            returnIntent.putExtra("ALARM_URI", ALARM_SOUND);
            returnIntent.putExtra("ALARM_SOUND_NAME", ALARM_SOUND_NAME);
            returnIntent.putExtra("ALARM_NAME", EDT_ALARMLABLE.getText().toString());
            returnIntent.putExtra("ALARM_REPEAT", "Once");
            returnIntent.putExtra("ALARM_TIME", String.valueOf(_Alarmtime));
            setResult(Activity.RESULT_OK, returnIntent);
            AddAlarmActivity.this.finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12 && resultCode == RESULT_OK) {
            final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            final Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            // Get your title here `ringtone.getTitle(this)
            String Str = ringtone.getTitle(this);

            TV_ALARMSOUND.setText(Str);
            Log.d("::::: ", Str);
            ALARM_SOUND = String.valueOf(uri);
            ALARM_SOUND_NAME = Str;


//            ALARM_MANAGER = (AlarmManager) AddAlarmActivity.this.getSystemService(ALARM_SERVICE);
//            // AlarmReceiver1 = broadcast receiver
////            Intent alarmIntent = new Intent(AddAlarmActivity.this, AlarmReceiver.class);
//
//            Intent alarmIntent = new Intent(AddAlarmActivity.this, AlarmReceiver.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("ALARM_SOUND", String.valueOf(uri));
//            alarmIntent.putExtras(bundle);
//
////            alarmIntent.putExtra("ALARM_SOUND",String.valueOf(uri));
//            PendingIntent _pendingIntent = PendingIntent.getBroadcast(AddAlarmActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager manager = (AlarmManager) AddAlarmActivity.this.getSystemService(Context.ALARM_SERVICE);
//            ALARM_MANAGER.cancel(_pendingIntent);
//            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, _pendingIntent);
//            Toast.makeText(AddAlarmActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }
}
