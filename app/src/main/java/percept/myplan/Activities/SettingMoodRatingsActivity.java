package percept.myplan.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.SidaSchedule;
import percept.myplan.R;
import percept.myplan.adapters.SidaScheduleAdapter;

public class SettingMoodRatingsActivity extends AppCompatActivity {

    private SwitchCompat SWITCH_SIDAS, SWITCH_MOOD;
    private LinearLayout LAY_SIDAS;
    private RecyclerView rcvSidas;
    private ArrayList<SidaSchedule> listSidasSchedule = new ArrayList<>();
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_ratings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_moodratings));

        SWITCH_MOOD = (SwitchCompat) findViewById(R.id.switchMood);
        SWITCH_SIDAS = (SwitchCompat) findViewById(R.id.switchSidas);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);
        PB = (ProgressBar) findViewById(R.id.progressBar);

        LAY_SIDAS = (LinearLayout) findViewById(R.id.laySidas);
        rcvSidas = (RecyclerView) findViewById(R.id.rcvSidas);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SettingMoodRatingsActivity.this);
        rcvSidas.setLayoutManager(mLayoutManager);
        rcvSidas.setItemAnimator(new DefaultItemAnimator());
        getSetting();

        SWITCH_SIDAS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    LAY_SIDAS.setVisibility(View.VISIBLE);
                else
                    LAY_SIDAS.setVisibility(View.GONE);
            }
        });

        SWITCH_MOOD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveSetting();
            return true;
        }
        return false;
    }

    private void saveSetting() {
        int _sidas = SWITCH_SIDAS.isChecked() ? 1 : 0;
        int _mood = SWITCH_MOOD.isChecked() ? 1 : 0;
        int _interval = 0;
        for (int i = 0; i < listSidasSchedule.size(); i++) {
            if (listSidasSchedule.get(i).isSelected()) {
                _interval = i;
                break;
            }
        }
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("sidas", String.valueOf(_sidas));
        params.put("interval", String.valueOf(_interval));
        params.put("mood", String.valueOf(_mood));
        try {
            new General().getJSONContentFromInternetService(SettingMoodRatingsActivity.this, General.PHPServices.SAVE_SETTINGS, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    SettingMoodRatingsActivity.this.finish();
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
                            saveSetting();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void getSetting() {
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(SettingMoodRatingsActivity.this,
                    General.PHPServices.GET_SETTINGS, params, true, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            PB.setVisibility(View.GONE);
                            Log.d(":::GET SETTING", response.toString());
                            try {
                                if (response.has(Constant.DATA)) {

                                    JSONObject jsonObject = response.getJSONObject(Constant.DATA);
                                    if (jsonObject.getInt("mood") == 1)
                                        SWITCH_MOOD.setChecked(true);
                                    else SWITCH_MOOD.setChecked(false);

                                    if (jsonObject.getInt("sidas") == 1)
                                        SWITCH_SIDAS.setChecked(true);
                                    else SWITCH_SIDAS.setChecked(false);
                                    int _interval = 0;
                                    if (jsonObject.has("interval"))
                                        _interval = jsonObject.getInt("interval");

                                    String _sidasInterval[] = getResources().getStringArray(R.array.sidas_interval);
                                    for (int i = 0; i < _sidasInterval.length; i++) {
                                        if (i == _interval)
                                            listSidasSchedule.add(new SidaSchedule(_sidasInterval[i], true));
                                        else
                                            listSidasSchedule.add(new SidaSchedule(_sidasInterval[i], false));
                                    }

                                    rcvSidas.setAdapter(new SidaScheduleAdapter(SettingMoodRatingsActivity.this, listSidasSchedule));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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
                            getSetting();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

}
