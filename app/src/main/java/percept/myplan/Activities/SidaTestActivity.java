package percept.myplan.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.SidaQuestion;
import percept.myplan.R;

public class SidaTestActivity extends AppCompatActivity {

    final private int REQUEST_CODE_CALL_PERMISSIONS = 123;
    Map<String, String> params;
    private TextView TV_TESTQUESTION, TV_TESTANSWER;
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

        InfoDialog _dialog = new InfoDialog(SidaTestActivity.this,
                getString(R.string.test_start_warning_msg), getString(R.string.cont), "");
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
                    Log.i(":::SidasTest::", response.toString());
                    PB.setVisibility(View.GONE);
                    try {
                        int score = response.getJSONObject(Constant.DATA).getInt("score");
                        if (score > 15 && score <= 21) {
                            new InfoDialog(SidaTestActivity.this, getString(R.string.test_result_btwn_15to21),
                                    getString(R.string.help), getString(R.string.inspiration)) {

                                @Override
                                public void onClickFirstButton() {
                                    Intent _intent = new Intent(SidaTestActivity.this, HelpListActivity.class);
                                    startActivity(_intent);
                                    SidaTestActivity.this.finish();
                                }

                                @Override
                                public void onClickSecondButton() {
                                    Intent _iIntent=new Intent();
                                    _iIntent.putExtra("SIDAS_OPEN_STRATEGIES",true);
                                    setResult(RESULT_OK,_iIntent);
                                    SidaTestActivity.this.finish();
                                }
                            }.show();
                        } else if (score > 21) {
                            new InfoDialog(SidaTestActivity.this, getString(R.string.test_result_greater_21),
                                    getString(R.string.help), getString(R.string.emergency)) {

                                @Override
                                public void onClickFirstButton() {

                                    Intent _intent = new Intent(SidaTestActivity.this, HelpListActivity.class);
                                    startActivity(_intent);
                                    SidaTestActivity.this.finish();
                                }

                                @Override
                                public void onClickSecondButton() {
                                    SidaTestActivity.this.finish();
                                    emergencyCall();
                                }
                            }.show();
                        } else {
                            new InfoDialog(SidaTestActivity.this, getString(R.string.test_result_less_15),
                                    getString(R.string.ok), "") {
                                @Override
                                public void onClickFirstButton() {
                                    super.onClickFirstButton();
                                    SidaTestActivity.this.finish();
                                }
                            }.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    SidaTestActivity.this.finish();
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

    public void emergencyCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(SidaTestActivity.this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SidaTestActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_CALL_PERMISSIONS);
        } else {
            if (TextUtils.isEmpty(new Utils(SidaTestActivity.this).getPreference("EMERGENCY_CONTACT_NAME"))) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "112"));
                phoneIntent.setPackage("com.android.phone");
                startActivity(phoneIntent);
            } else {
                String _phoneNo = new Utils(SidaTestActivity.this).getPreference("EMERGENCY_CONTACT_NO");
                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + _phoneNo));
                phoneIntent.setPackage("com.android.phone");
                startActivity(phoneIntent);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CALL_PERMISSIONS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    emergencyCall();
                break;
        }
    }

    public class InfoDialog extends Dialog {

        private TextView TV_NOTE, tvInfoBtn1;
        private String note, strFistTitle, strSecondTitle;
        private TextView tvInfoBtn2;

        public InfoDialog(Context context, String note, String strFistTitle, String strSecondTitle) {
            super(context, R.style.DialogTheme);
            this.note = note;
            this.strFistTitle = strFistTitle;
            this.strSecondTitle = strSecondTitle;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.lay_info);

            TV_NOTE = (TextView) findViewById(R.id.tvNote);
            tvInfoBtn1 = (TextView) findViewById(R.id.tvInfoBtn1);
            tvInfoBtn2 = (TextView) findViewById(R.id.tvInfoBtn2);
            View viewSeperator = findViewById(R.id.viewSeperator);
            TV_NOTE.setText(Html.fromHtml(note));
            tvInfoBtn1.setText(strFistTitle);
            if (TextUtils.isEmpty(strSecondTitle)) {
                tvInfoBtn2.setVisibility(View.GONE);
                viewSeperator.setVisibility(View.GONE);
            } else {
                tvInfoBtn2.setVisibility(View.VISIBLE);
                viewSeperator.setVisibility(View.VISIBLE);
            }
            tvInfoBtn2.setText(strSecondTitle);

            TV_NOTE.setMovementMethod(LinkMovementMethod.getInstance());
//            TV_NOTE.setText(Html.fromHtml("<b>" + "SIDAS" + "</b>" + " consist of 5 questions"
//                    + "<br />" + "<br />" + "<br />" + "You can use" + "<b>" + " SIDAS " + "</b>"
//                    + "to measure your suicidal thoughts." + "<br />" + "<br />" + "<br />" +
//                    "<b>" + "IMPORTANT " + "</b>" + "if you have a high note, you will get an automate " +
//                    "notification to seek help" + "<br />" + "<br />" + "<br />"
//                    + "It is important that you act and follow your crisis plan"));


            tvInfoBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    onClickFirstButton();
                }
            });
            tvInfoBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    onClickSecondButton();
                }
            });
        }

        public void onClickFirstButton() {
        }

        public void onClickSecondButton() {
        }
    }
}
