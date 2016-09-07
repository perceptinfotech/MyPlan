package percept.myplan.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import percept.myplan.services.MyIntentService;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(":::::::::::::::: Alarm ", "BroadCast received.");

        Intent _service = new Intent(context, MyIntentService.class);
        _service.putExtra("ALARM_SOUND", intent.getExtras().getString("ALARM_SOUND"));
        context.startService(_service);
    }
}
