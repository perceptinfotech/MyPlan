package percept.myplan.Activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.SidaQuestion;
import percept.myplan.R;

public class SidaTestActivity extends AppCompatActivity {

    private TextView TV_TESTQUESTION, TV_TESTANSWER;
    Map<String, String> params;
    private List<SidaQuestion> LST_SIDAQUES;
    private SeekBar SEEK_SIDA;
    private Button BTN_NEXT_QUES;
    private int CURR_QUES, TOTAL_QUES;
    private String STRANSWER = "";
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sida_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.sidasfulltest));

        TV_TESTQUESTION = (TextView) findViewById(R.id.tvQuestions);
        TV_TESTANSWER = (TextView) findViewById(R.id.tvSidaPoints);
        BTN_NEXT_QUES = (Button) findViewById(R.id.btnNextQues);
        PB = (ProgressBar) findViewById(R.id.pbSidaTest);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        SEEK_SIDA = (SeekBar) findViewById(R.id.seekBarSidas);

        SEEK_SIDA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                TV_TESTANSWER.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        InfoDialog _dialog = new InfoDialog(SidaTestActivity.this);
        _dialog.setCanceledOnTouchOutside(true);
        _dialog.show();

        GetSidaTest();

        BTN_NEXT_QUES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CURR_QUES + 1 < TOTAL_QUES) {
                    CURR_QUES = CURR_QUES + 1;
                    TV_TESTQUESTION.setText(LST_SIDAQUES.get(CURR_QUES - 1).getQuestion());
                    BTN_NEXT_QUES.setText(getResources().getString(R.string.nextques) +
                            "(" + String.valueOf(CURR_QUES + 1) + "/" + String.valueOf(TOTAL_QUES) + ")");
                    if (STRANSWER.equals(""))
                        STRANSWER = TV_TESTANSWER.getText().toString();
                    else
                        STRANSWER += "," + TV_TESTANSWER.getText().toString();
                } else {
                    SubmitSidaTest();

                }
            }
        });
    }

    private void SubmitSidaTest() {
        try {
            STRANSWER += "," + TV_TESTANSWER.getText().toString();
            params.put("answer", STRANSWER);
            PB.setVisibility(View.VISIBLE);
            new General().getJSONContentFromInternetService(SidaTestActivity.this, General.PHPServices.SUBMIT_SIDATEST, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    SidaTestActivity.this.finish();
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
                            SubmitSidaTest();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void GetSidaTest() {
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(SidaTestActivity.this, General.PHPServices.GET_SIDATEST, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {

                    Gson gson = new Gson();
                    try {
                        LST_SIDAQUES = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<SidaQuestion>>() {
                        }.getType());
                        TOTAL_QUES = LST_SIDAQUES.size();
                        CURR_QUES = 0;
                        TV_TESTQUESTION.setText(LST_SIDAQUES.get(CURR_QUES).getQuestion());
                        BTN_NEXT_QUES.setText(getResources().getString(R.string.nextques) +
                                "(" + String.valueOf(CURR_QUES + 1) + "/" + String.valueOf(TOTAL_QUES) + ")");

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
                            GetSidaTest();
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
            SidaTestActivity.this.finish();
            return true;
        }
        return false;
    }

    public class InfoDialog extends Dialog {

        private TextView TV_NOTE, TV_CONTINUE;
        private String NOTE;
        private Context CONTEXT;

        public InfoDialog(Context context) {
            super(context, R.style.DialogTheme);
            CONTEXT = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.lay_info);

            TV_NOTE = (TextView) findViewById(R.id.tvNote);
            TV_CONTINUE = (TextView) findViewById(R.id.tvContinue);
            TV_NOTE.setText(NOTE);

            TV_NOTE.setText(Html.fromHtml("<b>" + "Sidas" + "</b>" + "consist of 5 questions"
                    + "<br />" + "<br />" + "<br />" + "You can use" + "<b>" + " SIDAS " + "</b>"
                    + "to measure your suicidal thoughts." + "<br />" + "<br />" + "<br />" +
                    "<b>" + "IMPORTANT " + "</b>" + "if you have a high score, you will get an automate " +
                    "notification to seek help" + "<br />" + "<br />" + "<br />"
                    + "It is important that you act and follow your crisis plan"));

            TV_CONTINUE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InfoDialog.this.dismiss();
                }
            });
        }
    }
}
