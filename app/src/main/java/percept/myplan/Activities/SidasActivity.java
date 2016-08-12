package percept.myplan.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Mood;
import percept.myplan.R;
import percept.myplan.adapters.MoodSummaryAdapter;
import percept.myplan.adapters.SidaSummaryAdapter;

public class SidasActivity extends AppCompatActivity {

    private Button BTN_TAKETEST;
    private RecyclerView LST_SIDASUMMARY;
    SidaSummaryAdapter ADAPTER;
    private List<Mood> LIST_SIDA;
    Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.sidas));

        LST_SIDASUMMARY = (RecyclerView) findViewById(R.id.lstSidaSummary);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SidasActivity.this);
        LST_SIDASUMMARY.setLayoutManager(mLayoutManager);
        LST_SIDASUMMARY.setItemAnimator(new DefaultItemAnimator());
        LIST_SIDA = new ArrayList<>();
        BTN_TAKETEST = (Button) findViewById(R.id.btnTakeSidasTest);

        BTN_TAKETEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SidasActivity.this, SidaTestActivity.class));
            }
        });

        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(SidasActivity.this, General.PHPServices.GET_MOODCALENDER, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        LIST_SIDA = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<Mood>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < LIST_SIDA.size(); i++) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = format.parse(LIST_SIDA.get(i).getMOOD_DATE());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int _day = cal.get(Calendar.DAY_OF_MONTH);
                            int _Month = cal.get(Calendar.MONTH);
                            String _strTime = "";
                            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                                case 1:
                                    _strTime = "Sunday ";
                                    break;
                                case 2:
                                    _strTime = "Monday ";
                                    break;
                                case 3:
                                    _strTime = "Tuesday ";
                                    break;
                                case 4:
                                    _strTime = "Wednesday ";
                                    break;
                                case 5:
                                    _strTime = "Thursday ";
                                    break;
                                case 6:
                                    _strTime = "Friday ";
                                    break;
                                case 7:
                                    _strTime = "Saturday ";
                                    break;

                            }
                            _strTime = _strTime + String.valueOf(_day) + "th of ";
                            switch (_Month) {
                                case 0:
                                    _strTime = _strTime + "January";
                                    break;
                                case 1:
                                    _strTime = _strTime + "February";
                                    break;
                                case 2:
                                    _strTime = _strTime + "March";
                                    break;
                                case 3:
                                    _strTime = _strTime + "April";
                                    break;
                                case 4:
                                    _strTime = _strTime + "May";
                                    break;
                                case 5:
                                    _strTime = _strTime + "June";
                                    break;
                                case 6:
                                    _strTime = _strTime + "July";
                                    break;
                                case 7:
                                    _strTime = _strTime + "August";
                                    break;
                                case 8:
                                    _strTime = _strTime + "September";
                                    break;
                                case 9:
                                    _strTime = _strTime + "October";
                                    break;
                                case 10:
                                    _strTime = _strTime + "November";
                                    break;
                                case 11:
                                    _strTime = _strTime + "December";
                                    break;
                            }
                            LIST_SIDA.get(i).setMOOD_DATE_STRING(_strTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ADAPTER = new SidaSummaryAdapter(SidasActivity.this, LIST_SIDA);
                    LST_SIDASUMMARY.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SidasActivity.this.finish();
            return true;
        }
        return false;
    }
}
