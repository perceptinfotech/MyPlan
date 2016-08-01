package percept.myplan.POJO;

/**
 * Created by percept on 1/8/16.
 */

public class Alarm {

    private String AlarmName;
    private String AlarmTime;
    private boolean Status;
    private String AlarmTune;
    private String AlarmRepeat;
    private String Snooze;

    public Alarm(String alarmName, String alarmTime, boolean status, String alarmTune, String alarmRepeat, String snooze) {
        AlarmName = alarmName;
        AlarmTime = alarmTime;
        Status = status;
        AlarmTune = alarmTune;
        AlarmRepeat = alarmRepeat;
        Snooze = snooze;
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

}
