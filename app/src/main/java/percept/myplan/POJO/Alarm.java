package percept.myplan.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by percept on 1/8/16.
 */

public class Alarm {

    @SerializedName("alarmname")
    private String AlarmName;
    @SerializedName("alarmtime")
    private String AlarmTime;
    @SerializedName("status")
    private boolean Status;
    @SerializedName("alarmtune")
    private String AlarmTune;
    @SerializedName("alarmtunename")
    private String AlarmTuneName;
    @SerializedName("alarmrepeat")
    private String AlarmRepeat;
    @SerializedName("snooze")
    private String Snooze;

    public Alarm(String alarmName, String alarmTime, boolean status, String alarmTune, String alarmRepeat, String snooze,
                 String alarmTuneName) {
        AlarmName = alarmName;
        AlarmTime = alarmTime;
        Status = status;
        AlarmTune = alarmTune;
        AlarmRepeat = alarmRepeat;
        Snooze = snooze;
        AlarmTuneName = alarmTuneName;
    }

    public String getSnooze() {
        return Snooze;
    }

    public void setSnooze(String snooze) {
        Snooze = snooze;
    }

    public String getAlarmTune() {
        return AlarmTune;
    }

    public void setAlarmTune(String alarmTune) {
        AlarmTune = alarmTune;
    }

    public String getAlarmRepeat() {
        return AlarmRepeat;
    }

    public void setAlarmRepeat(String alarmRepeat) {
        AlarmRepeat = alarmRepeat;
    }


    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getAlarmTime() {
        return AlarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        AlarmTime = alarmTime;
    }

    public String getAlarmName() {
        return AlarmName;
    }

    public void setAlarmName(String alarmName) {
        AlarmName = alarmName;
    }


    public String getAlarmTuneName() {
        return AlarmTuneName;
    }

    public void setAlarmTuneName(String alarmTuneName) {
        AlarmTuneName = alarmTuneName;
    }
}
