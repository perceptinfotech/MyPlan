package percept.myplan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Mood;
import percept.myplan.R;
import percept.myplan.adapters.MoodSummaryAdapter;

public class MoodSummaryActivity extends AppCompatActivity {

    private RecyclerView LST_MOODSUMMARY;
    private List<Mood> LIST_MOOD;
    private List<Mood> LIST_MOOD_MONTH;
    Map<String, String> params;
    MoodSummaryAdapter ADAPTER;
    private Utils UTILS;
    private int MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_summary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.seenotes);

        MONTH = getIntent().getExtras().getInt("MONTH");
        LIST_MOOD_MONTH = new ArrayList<>();
        UTILS = new Utils(MoodSummaryActivity.this);
        LST_MOODSUMMARY = (RecyclerView) findViewById(R.id.lstMoodSummay);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MoodSummaryActivity.this);
        LST_MOODSUMMARY.setLayoutManager(mLayoutManager);
        LST_MOODSUMMARY.setItemAnimator(new DefaultItemAnimator());

        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(MoodSummaryActivity.this, General.PHPServices.GET_MOODCALENDER, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        LIST_MOOD = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Mood>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < LIST_MOOD.size(); i++) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = format.parse(LIST_MOOD.get(i).getMOOD_DATE());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            String _suffix = UTILS.getDayOfMonthSuffix(cal.get(Calendar.DAY_OF_MONTH));

                            SimpleDateFormat _getDay = new SimpleDateFormat("EEEE d'" + _suffix + "' ' of ' MMMM");

                            Log.d(":::: ", _getDay.format(date));

//                            int _day = cal.get(Calendar.DAY_OF_MONTH);
//                            int _Month = cal.get(Calendar.MONTH);
//                            String _strTime = "";
//                            switch (cal.get(Calendar.DAY_OF_WEEK)) {
//                                case 1:
//                                    _strTime = "Sunday ";
//                                    break;
//                                case 2:
//                                    _strTime = "Monday ";
//                                    break;
//                                case 3:
//                                    _strTime = "Tuesday ";
//                                    break;
//                                case 4:
//                                    _strTime = "Wednesday ";
//                                    break;
//                                case 5:
//                                    _strTime = "Thursday ";
//                                    break;
//                                case 6:
//                                    _strTime = "Friday ";
//                                    break;
//                                case 7:
//                                    _strTime = "Saturday ";
//                                    break;
//
//                            }
//                            _strTime = _strTime + String.valueOf(_day) + "th of ";
//                            switch (_Month) {
//                                case 0:
//                                    _strTime = _strTime + "January";
//                                    break;
//                                case 1:
//                                    _strTime = _strTime + "February";
//                                    break;
//                                case 2:
//                                    _strTime = _strTime + "March";
//                                    break;
//                                case 3:
//                                    _strTime = _strTime + "April";
//                                    break;
//                                case 4:
//                                    _strTime = _strTime + "May";
//                                    break;
//                                case 5:
//                                    _strTime = _strTime + "June";
//                                    break;
//                                case 6:
//                                    _strTime = _strTime + "July";
//                                    break;
//                                case 7:
//                                    _strTime = _strTime + "August";
//                                    break;
//                                case 8:
//                                    _strTime = _strTime + "September";
//                                    break;
//                                case 9:
//                                    _strTime = _strTime + "October";
//                                    break;
//                                case 10:
//                                    _strTime = _strTime + "November";
//                                    break;
//                                case 11:
//                                    _strTime = _strTime + "December";
//                                    break;
//                            }

                            int _month = cal.get(Calendar.MONTH);
                            Log.d(":::: ", String.valueOf(_month));
                            if (_month == MONTH) {
                                LIST_MOOD.get(i).setMOOD_DATE_STRING(_getDay.format(date));
                                LIST_MOOD_MONTH.add(LIST_MOOD.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ADAPTER = new MoodSummaryAdapter(MoodSummaryActivity.this, LIST_MOOD_MONTH);
                    LST_MOODSUMMARY.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MoodSummaryActivity.this.finish();
        }
        return false;
    }
}
