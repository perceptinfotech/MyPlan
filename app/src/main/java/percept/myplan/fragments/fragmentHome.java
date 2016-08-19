package percept.myplan.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Calendar;

import percept.myplan.Activities.HelpListActivity;
import percept.myplan.Activities.HelpListEditActivity;
import percept.myplan.AppController;
import percept.myplan.Dialogs.fragmentAddNote;
import percept.myplan.Dialogs.fragmentMoodSummary;
import percept.myplan.Dialogs.fragmentSidasRating;
import percept.myplan.Global.Constant;
import percept.myplan.R;
import percept.myplan.receivers.AlarmReceiver;

import static android.content.Context.ALARM_SERVICE;


public class fragmentHome extends Fragment {


    public static final int INDEX = 0;

    private ImageView IMG_USER,IMG_MOODRATING_CLOSE;
    private LinearLayout LAY_HELP, LAY_EMERGENCY, LAY_MOODRATING;

    private ImageView IMG_USERPROFILE;

    private AlarmManager ALARM_MANAGER;
    public static final int DIALOG_ADDNOTE = 1;

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
        LAY_MOODRATING = (LinearLayout) _View.findViewById(R.id.layMoodRatings);

        IMG_USERPROFILE = (ImageView) _View.findViewById(R.id.imgUserImage);

        IMG_MOODRATING_CLOSE= (ImageView) _View.findViewById(R.id.imgCloseMoodRating);
//        Picasso.with(getActivity()).load(Constant.PROFILE_IMG_LINK).into(IMG_USERPROFILE);


        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(Constant.PROFILE_IMG_LINK, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    IMG_USERPROFILE.setImageBitmap(response.getBitmap());
                }
            }
        });
        IMG_MOODRATING_CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LAY_MOODRATING.setVisibility(View.GONE);
            }
        });


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
//                Toast.makeText(getActivity(), "Home Called", Toast.LENGTH_SHORT).show();
                Intent _intent = new Intent(getActivity(), HelpListActivity.class);
                startActivity(_intent);
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home Fragments");
//        setAlarmForMood();

//        MoodRatingAddNoteConfirmDialog();

//        SidasDialog();
        return _View;
    }

    private void SidasDialog() {

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_add_note");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        fragmentSidasRating fraSidasDialog = new fragmentSidasRating();
//        fraSidasDialog.setTargetFragment(this, DIALOG_ADDNOTE);

        fraSidasDialog.show(getFragmentManager().beginTransaction(), "fragment_add_note");
    }

    private void MoodRatingAddNoteConfirmDialog() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Include dialog.xml file
        dialog.setContentView(R.layout.lay_moodratings_addnote);
        // Set dialog TV_TITLE
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        TextView _tvNo = (TextView) dialog.findViewById(R.id.tvNo);
        TextView _tvYes = (TextView) dialog.findViewById(R.id.tvYes);

        dialog.show();
        _tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        _tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                MoodRatingAddNoteDialog();
            }
        });
    }

    private void MoodRatingAddNoteDialog() {

        // close existing dialog fragments
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_add_note");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        fragmentAddNote fraAddNoteDialog = new fragmentAddNote();
        fraAddNoteDialog.setTargetFragment(this, DIALOG_ADDNOTE);

        fraAddNoteDialog.show(getFragmentManager().beginTransaction(), "fragment_add_note");
    }

    private void MoodRatingSummaryDialog(String note) {

        // close existing dialog fragments
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_add_note");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        DialogFragment fraMoodSummartDialog = fragmentMoodSummary.newInstance(note);
        fraMoodSummartDialog.setTargetFragment(this, DIALOG_ADDNOTE);

        fraMoodSummartDialog.show(getFragmentManager().beginTransaction(), "fragment_add_note");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_ADDNOTE:
                if (resultCode == Activity.RESULT_OK) {
                    String str = data.getStringExtra("NOTE");
                    Log.d(":::::::::::::::: ", str);
                    MoodRatingSummaryDialog(str);
                    // After Ok code.
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                    MoodRatingSummaryDialog("");
                }
                break;
        }
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
