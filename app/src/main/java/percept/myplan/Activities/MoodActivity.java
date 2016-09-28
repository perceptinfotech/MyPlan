package percept.myplan.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.SquareCellView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.CustomEvent;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Mood;
import percept.myplan.R;

public class MoodActivity extends AppCompatActivity implements FlexibleCalendarView.OnMonthChangeListener,
        FlexibleCalendarView.OnDateClickListener {

    Map<String, String> params;
    private FlexibleCalendarView calendarView;
    private TextView TV_CALENDERMONTH, TV_CALENDERYEAR;
    private Map<Integer, List<CustomEvent>> eventMap;
    private ImageView IMG_VSAD, IMG_SAD, IMG_OK, IMG_HAPPY, IMG_VHAPPY;
    private String STR_NOTE = "";
    private List<Mood> LIST_MOOD, LIST_MOOD_TMP;
    private Button BTN_SEEALLNOTE;
    private ProgressBar PB;

    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.mood);

//        initializeEvents();
        eventMap = new HashMap<>();
        calendarView = (FlexibleCalendarView) findViewById(R.id.calendar_view);
        TV_CALENDERMONTH = (TextView) findViewById(R.id.tvCalenderMonth);
        TV_CALENDERYEAR = (TextView) findViewById(R.id.tvCalenderYear);

        IMG_VSAD = (ImageView) findViewById(R.id.imgVerySad);
        IMG_SAD = (ImageView) findViewById(R.id.imgSad);
        IMG_OK = (ImageView) findViewById(R.id.imgOk);
        IMG_HAPPY = (ImageView) findViewById(R.id.imgHappy);
        IMG_VHAPPY = (ImageView) findViewById(R.id.imgVeryHappy);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        BTN_SEEALLNOTE = (Button) findViewById(R.id.btnSeeAllNote);
        PB = (ProgressBar) findViewById(R.id.pbMood);

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(MoodActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar1_date_cell_view, null);
                }
                if (cellType == BaseCellView.OUTSIDE_MONTH) {
                    cellView.setTextColor(getResources().getColor(R.color.date_outside_month_text_color_activity_1));
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(MoodActivity.this);
                    cellView = (SquareCellView) inflater.inflate(R.layout.calendar1_week_cell_view, null);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return String.valueOf(defaultValue.charAt(0));
            }
        });
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnDateClickListener(this);

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<CustomEvent> getEventsForTheDay(int year, int month, int day) {
                return getEvents(year, month, day);
            }
        });

        updateTitle(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth());

        calendarView.setDisableAutoDateSelection(true);


        IMG_HAPPY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.goToCurrentDay();
                List<CustomEvent> _Happy = new ArrayList<>();
                _Happy.add(new CustomEvent(R.color.happy));
                eventMap.put(calendarView.getSelectedDateItem().getDay(), _Happy);
                calendarView.refresh();
                MoodRatingAddNoteDialog("2");
            }
        });

        IMG_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.goToCurrentDay();
                List<CustomEvent> _Ok = new ArrayList<>();
                _Ok.add(new CustomEvent(R.color.ok));
                eventMap.put(calendarView.getSelectedDateItem().getDay(), _Ok);
                calendarView.refresh();
                MoodRatingAddNoteDialog("3");
            }
        });

        IMG_VHAPPY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.goToCurrentDay();
                List<CustomEvent> _Vhappy = new ArrayList<>();
                _Vhappy.add(new CustomEvent(R.color.veryhappy));
                eventMap.put(calendarView.getSelectedDateItem().getDay(), _Vhappy);
                calendarView.refresh();
                MoodRatingAddNoteDialog("1");
            }
        });

        IMG_SAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.goToCurrentDay();
                List<CustomEvent> _Sad = new ArrayList<>();
                _Sad.add(new CustomEvent(R.color.sad));
                eventMap.put(calendarView.getSelectedDateItem().getDay(), _Sad);
                calendarView.refresh();
                MoodRatingAddNoteDialog("4");
            }
        });

        IMG_VSAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.goToCurrentDay();
                List<CustomEvent> _vSad = new ArrayList<>();
                _vSad.add(new CustomEvent(R.color.verysad));
                eventMap.put(calendarView.getSelectedDateItem().getDay(), _vSad);
                calendarView.refresh();
                MoodRatingAddNoteDialog("5");
            }
        });

        BTN_SEEALLNOTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(MoodActivity.this, MoodSummaryActivity.class);
                Log.d("::: ", String.valueOf(calendarView.getCurrentMonth()));
                _intent.putExtra("MONTH", calendarView.getCurrentMonth());
                _intent.putExtra("YEAR", calendarView.getCurrentYear());
                startActivity(_intent);
            }
        });
        //Uncomment for enable current date selection
        calendarView.selectDate(Calendar.getInstance().getTime());

        GetMoodCalender();
    }

    private void GetMoodCalender() {
        PB.setVisibility(View.VISIBLE);
        eventMap.clear();
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("year", String.valueOf(calendarView.getCurrentYear()));
        params.put("month", String.valueOf(calendarView.getCurrentMonth() + 1));
        try {
            new General().getJSONContentFromInternetService(MoodActivity.this, General.PHPServices.GET_MOODCALENDER, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        if (response.get(Constant.DATA) instanceof JSONObject ||
                                response.get(Constant.DATA) instanceof String) {
//                            showMessageOK(response.getString(Constant.DATA));
                            return;
                        }
                        LIST_MOOD = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Mood>>() {
                        }.getType());

//                        Collections.sort(LIST_MOOD, new Comparator<Mood>() {
//                            @Override
//                            public int compare(Mood o1, Mood o2) {
//                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                try {
//                                    Date date = format.parse(o1.getMOOD_DATE());
//                                    Date date2 = format.parse(o2.getMOOD_DATE());
//                                    return date.compareTo(date2);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                return 0;
//
//                            }
//                        });
//                        LIST_MOOD = new ArrayList<Mood>();
//                        for (int i = 0; i < LIST_MOOD_TMP.size(); i++) {
//                            Mood _obj = LIST_MOOD_TMP.get(i);
//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            Date date = format.parse(_obj.getMOOD_DATE());
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(date);
//                            String strDate = cal.get(Calendar.YEAR) + "-" +
//                                    cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
//
//                            for (int j = i + 1; j < LIST_MOOD_TMP.size(); j++) {
//
//                                Date date1 = format.parse(LIST_MOOD_TMP.get(j).getMOOD_DATE());
//                                Calendar cal1 = Calendar.getInstance();
//                                cal.setTime(date1);
//                                String strDate1 = cal1.get(Calendar.YEAR) + "-" +
//                                        cal1.get(Calendar.MONTH) + "-" + cal1.get(Calendar.DAY_OF_MONTH);
//
//                                if (strDate.contains(strDate1)) {
//                                    _obj.setMEASUREMENT(_obj.getMEASUREMENT() + ","
//                                            + LIST_MOOD_TMP.get(j).getMEASUREMENT());
//                                    _obj.setNOTE(_obj.getNOTE() + ","
//                                            + LIST_MOOD_TMP.get(j).getNOTE());
//                                    LIST_MOOD_TMP.remove(j);
//                                    break;
//                                }
//                            }
//                            LIST_MOOD.add(_obj);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
                    }
                    Log.i("Mood::::", new Gson().toJson(LIST_MOOD));
                    for (Mood _obj : LIST_MOOD) {

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = format.parse(_obj.getMOOD_DATE());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int _day = cal.get(Calendar.DAY_OF_MONTH);
                            int _Month = cal.get(Calendar.MONTH);
                            List<CustomEvent> colorLst = new ArrayList<>();
                            if (_Month == calendarView.getCurrentMonth()) {
                                switch (_obj.getMEASUREMENT()) {
                                    case "1":
                                        colorLst.add(new CustomEvent(R.color.veryhappy));
                                        break;
                                    case "2":
                                        colorLst.add(new CustomEvent(R.color.happy));
                                        break;
                                    case "3":
                                        colorLst.add(new CustomEvent(R.color.ok));
                                        break;
                                    case "4":
                                        colorLst.add(new CustomEvent(R.color.sad));
                                        break;
                                    case "5":
                                        colorLst.add(new CustomEvent(R.color.verysad));
                                        break;
                                }
                                eventMap.put(_day, colorLst);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    PB.setVisibility(View.GONE);
                    calendarView.refresh();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PB.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            GetMoodCalender();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MoodActivity.this.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
//        Toast.makeText(MoodActivity.this,cal.getTime().toString()+ " Clicked",Toast.LENGTH_SHORT).show();
        String NOTE = "";
        for (Mood _obj : LIST_MOOD) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(_obj.getMOOD_DATE());
                Calendar _cal = Calendar.getInstance();
                _cal.setTime(date);
                int _day = _cal.get(Calendar.DAY_OF_MONTH);
                int _Month = _cal.get(Calendar.MONTH);
                if (_day == day && _Month == month) {
                    NOTE = _obj.getNOTE();
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(NOTE)) {
            NoteCalender _dialog = new NoteCalender(MoodActivity.this, NOTE);
            _dialog.setCanceledOnTouchOutside(true);
            _dialog.show();
        }
    }

    @Override
    public void onMonthChange(int year, int month, int direction) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        updateTitle(year, month);


        GetMoodCalender();
        Log.d(":::::::::: ", String.valueOf(month));
//        eventMap.clear();
//        for (Mood _obj : LIST_MOOD) {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                Date date = format.parse(_obj.getMOOD_DATE());
//                Calendar _cal = Calendar.getInstance();
//                _cal.setTime(date);
//                int _day = _cal.get(Calendar.DAY_OF_MONTH);
//                int _Month = _cal.get(Calendar.MONTH);
//                List<CustomEvent> colorLst = new ArrayList<>();
//                if (_Month == month) {
//                    switch (_obj.getMEASUREMENT()) {
//                        case "1":
//                            colorLst.add(new CustomEvent(R.color.veryhappy));
//                            eventMap.put(_day, colorLst);
//                            break;
//                        case "2":
//                            colorLst.add(new CustomEvent(R.color.happy));
//                            eventMap.put(_day, colorLst);
//                            break;
//                        case "3":
//                            colorLst.add(new CustomEvent(R.color.ok));
//                            eventMap.put(_day, colorLst);
//                            break;
//                        case "4":
//                            colorLst.add(new CustomEvent(R.color.sad));
//                            eventMap.put(_day, colorLst);
//                            break;
//                        case "5":
//                            colorLst.add(new CustomEvent(R.color.verysad));
//                            eventMap.put(_day, colorLst);
//                            break;
//                    }
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        calendarView.refresh();
    }

    private void MoodRatingAddNoteDialog(final String mood) {
        fragmentAddNoteCalender _dialog = new fragmentAddNoteCalender(MoodActivity.this, mood);
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.show();
        _dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SubmitTodayMood(mood);
            }
        });
    }

    private void SubmitTodayMood(final String mood) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("note", STR_NOTE);
        params.put("measurement", mood);
        try {
            new General().getJSONContentFromInternetService(MoodActivity.this, General.PHPServices.ADD_MOOD, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PB.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SubmitTodayMood(mood);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

//    private void initializeEvents() {
//        eventMap = new HashMap<>();
//        List<CustomEvent> colorLst = new ArrayList<>();
//        colorLst.add(new CustomEvent(R.color.sad));
//        eventMap.put(25, colorLst);
//
//        List<CustomEvent> colorLst1 = new ArrayList<>();
//        colorLst1.add(new CustomEvent(R.color.veryhappy));
//        eventMap.put(22, colorLst1);
//
//        List<CustomEvent> colorLst2 = new ArrayList<>();
//        colorLst2.add(new CustomEvent(R.color.ok));
//        eventMap.put(28, colorLst2);
//
//        List<CustomEvent> colorLst3 = new ArrayList<>();
//        colorLst3.add(new CustomEvent(R.color.verysad));
//        eventMap.put(29, colorLst3);
//    }

    public List<CustomEvent> getEvents(int year, int month, int day) {
        return eventMap.get(day);
    }


    private void updateTitle(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        TV_CALENDERMONTH.setText("Calender - " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                this.getResources().getConfiguration().locale));

        TV_CALENDERYEAR.setText(String.valueOf(year));

//        someTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
//                this.getResources().getConfiguration().locale) + " " + year);
    }

    private void showMessageOK(String message) {
        new AlertDialog.Builder(MoodActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MoodActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public class fragmentAddNoteCalender extends Dialog {

        private EditText EDT_NOTE;
        private Button BTN_SAVENOTE;
        private String mood;
        private Context CONTEXT;

        public fragmentAddNoteCalender(Context context, String mood) {
            super(context, R.style.DialogTheme);
            CONTEXT = context;
            this.mood = mood;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.lay_moodratings_savenote);

            EDT_NOTE = (EditText) findViewById(R.id.edtNote);
            BTN_SAVENOTE = (Button) findViewById(R.id.btnSaveNote);


            BTN_SAVENOTE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(":::: ", EDT_NOTE.getText().toString());
                    fragmentAddNoteCalender.this.dismiss();
                    STR_NOTE = EDT_NOTE.getText().toString().trim();
                }
            });
            findViewById(R.id.imgCloseMoodRating).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentAddNoteCalender.this.dismiss();
                }
            });
        }
    }

    public class NoteCalender extends Dialog {

        private TextView TV_NOTE;
        private String NOTE;
        private Context CONTEXT;

        public NoteCalender(Context context, String note) {
            super(context, R.style.DialogTheme);
            CONTEXT = context;
            NOTE = note;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.lay_note);

            TV_NOTE = (TextView) findViewById(R.id.tvNote);
            TV_NOTE.setText(NOTE);
        }
    }
}
