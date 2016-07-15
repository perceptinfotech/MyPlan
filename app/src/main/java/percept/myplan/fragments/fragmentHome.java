package percept.myplan.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import percept.myplan.R;
import percept.myplan.receivers.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;


public class fragmentHome extends Fragment {

    public static final int INDEX = 0;

    private ImageView IMG_USER;
    private LinearLayout LAY_HELP, LAY_EMERGENCY;

    private AlarmManager ALARM_MANAGER;

    public fragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _View = inflater.inflate(R.layout.fragment_home, container, false);

        LAY_HELP = (LinearLayout) _View.findViewById(R.id.layHelpHome);
        LAY_EMERGENCY = (LinearLayout) _View.findViewById(R.id.layEmergencyHome);
        LAY_EMERGENCY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "07927473473"));
                phoneIntent.setPackage("com.android.phone");
                startActivity(phoneIntent);
            }
        });

        LAY_HELP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Home Called", Toast.LENGTH_SHORT).show();
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home Fragments");
//        setAlarmForMood();
        return _View;
    }

    private void setAlarmForMood() {
        ALARM_MANAGER = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        // AlarmReceiver1 = broadcast receiver
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);

        PendingIntent _pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        ALARM_MANAGER.cancel(_pendingIntent);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000000, _pendingIntent);
        Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
//        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
//        ALARM_MANAGER.cancel(_pendingIntent);
//
//        Calendar alarmStartTime = Calendar.getInstance();
//        Calendar now = Calendar.getInstance();
//        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
//        alarmStartTime.set(Calendar.MINUTE, 00);
//        alarmStartTime.set(Calendar.SECOND, 0);
//        if (now.after(alarmStartTime)) {
//            Log.d("Hey","Added a day");
//            alarmStartTime.add(Calendar.DATE, 1);
//        }
//        ALARM_MANAGER.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, _pendingIntent);
//        Log.d("Alarm","Alarms set for everyday 8 am.");


    }


}
