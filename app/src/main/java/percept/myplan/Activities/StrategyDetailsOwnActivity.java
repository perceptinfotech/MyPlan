package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Dialogs.dialogStrategyImg;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.Alarm;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.POJO.StrategyContact;
import percept.myplan.POJO.StrategyDetails;
import percept.myplan.R;
import percept.myplan.adapters.ImageAdapter;
import percept.myplan.adapters.ImageDeleteAdapter;
import percept.myplan.adapters.StrategyAlarmAdapter;
import percept.myplan.adapters.StrategyContactSimpleAdapter;

public class StrategyDetailsOwnActivity extends AppCompatActivity {


    public static List<StrategyContact> LIST_STRATEGYCONTACT;
    Map<String, String> params;
    private RecyclerView LST_OWNSTRATEGYIMG;
    private List<String> LIST_IMAGE;
    private ImageAdapter ADAPTER_IMG;
    private String STRATEGY_ID;
    private Utils UTILS;
    private HashMap<String, List<Alarm>> MAP_ALARM;
    private StrategyDetails clsStrategy;
    private EditText EDT_STRATEGYTITLE, EDT_STRATEGYDESC;
    private StrategyContactSimpleAdapter ADAPTER;
    private StrategyAlarmAdapter ADAPTER_ALARM;
    private RecyclerView LST_STRATEGYCONTACT, LST_STRATEGYALARM;
    private Button BTN_SHARESTRATEGY;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;
    private List<Alarm> LIST_ALARM;
    private TextView tvImages, tvNetwork, tvAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy_details_own);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (getIntent().hasExtra("STRATEGY_ID")) {
            STRATEGY_ID = getIntent().getExtras().getString("STRATEGY_ID");
            mTitle.setText(getIntent().getExtras().getString("STRATEGY_NAME"));
        }
        tvAlarms = (TextView) findViewById(R.id.tvAlarms);
        tvNetwork = (TextView) findViewById(R.id.tvNetwork);
        tvImages = (TextView) findViewById(R.id.tvImages);

        LST_OWNSTRATEGYIMG = (RecyclerView) findViewById(R.id.lstOwnStrategyImage);
        LST_STRATEGYCONTACT = (RecyclerView) findViewById(R.id.lstStrategyContacts);
        LST_STRATEGYALARM = (RecyclerView) findViewById(R.id.lstStrategyAlarm);

        EDT_STRATEGYTITLE = (EditText) findViewById(R.id.edtStrategyTitle);
        EDT_STRATEGYDESC = (EditText) findViewById(R.id.edtStrategyDesc);
        EDT_STRATEGYDESC.setEnabled(false);
        EDT_STRATEGYTITLE.setEnabled(false);

        LIST_STRATEGYCONTACT = new ArrayList<>();
        LIST_ALARM = new ArrayList<>();
        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        BTN_SHARESTRATEGY = (Button) findViewById(R.id.btnShareStrategyAnony);
        BTN_SHARESTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareStrategy();
            }
        });

        PB = (ProgressBar) findViewById(R.id.pbStrategyOwn);

        EDT_STRATEGYTITLE.setText(getIntent().getExtras().getString("STRATEGY_NAME"));

        UTILS = new Utils(StrategyDetailsOwnActivity.this);

        String _strAlarm = UTILS.getPreference("ALARMLIST");
        try {
            if (!TextUtils.isEmpty(_strAlarm)) {
                Type listType = new TypeToken<HashMap<String, List<Alarm>>>() {

                }.getType();
                try {
                    MAP_ALARM = new Gson().fromJson(_strAlarm, listType);
                } catch (JsonSyntaxException ex) {
                    MAP_ALARM = new HashMap<>();
                }
            } else {
                MAP_ALARM = new HashMap<>();
            }
        } catch (Exception ex) {

        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LST_OWNSTRATEGYIMG.setLayoutManager(mLayoutManager);
        LST_OWNSTRATEGYIMG.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager _mLayoutManager = new LinearLayoutManager(StrategyDetailsOwnActivity.this);
        LST_STRATEGYCONTACT.setLayoutManager(_mLayoutManager);
        LST_STRATEGYCONTACT.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager _mLayoutManagerAlarm = new LinearLayoutManager(StrategyDetailsOwnActivity.this);
        LST_STRATEGYALARM.setLayoutManager(_mLayoutManagerAlarm);
        LST_STRATEGYALARM.setItemAnimator(new DefaultItemAnimator());

        LIST_IMAGE = new ArrayList<>();

        LST_STRATEGYCONTACT.addOnItemTouchListener(new RecyclerTouchListener(StrategyDetailsOwnActivity.this, LST_STRATEGYCONTACT, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("::::::::", LIST_STRATEGYCONTACT.get(position).getNAME());
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid", Constant.SID);
                params.put("sname", Constant.SNAME);
                params.put(Constant.ID, LIST_STRATEGYCONTACT.get(position).getID());
                try {
                    new General().getJSONContentFromInternetService(StrategyDetailsOwnActivity.this, General.PHPServices.GET_CONTACT, params, true, false, true, new VolleyResponseListener() {
                        @Override
                        public void onError(VolleyError message) {

                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(":::::::: Response----", response.toString());

                            try {
                                ContactDisplay _contactDisplay = new Gson().fromJson(response.getJSONObject(Constant.DATA)
                                        .toString(), new TypeToken<ContactDisplay>() {
                                }.getType());
                                Intent intent = new Intent(StrategyDetailsOwnActivity.this, AddContactDetailActivity.class);
                                intent.putExtra("IS_FOR_EDIT", true);
                                intent.putExtra("DATA", _contactDisplay);
                                startActivity(intent);
                                /*JSONObject _jsonContactDetail=response.getJSONObject(Constant.DATA);
                               if (_jsonContactDetail!=null)
                               {
                                   ContactDisplay _contactDisplay=new ContactDisplay();
                                   _contactDisplay.setCon_image(_jsonContactDetail.getString("con_image"));
                                   _contactDisplay.setFirst_name(_jsonContactDetail.getString("first_name"));

                               }*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (ADDED_STRATEGIES) {
            getStrategy();
        }*/
        getStrategy();
    }

    private void getStrategy() {
        PB.setVisibility(View.VISIBLE);
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", STRATEGY_ID);
        try {
            LIST_IMAGE.clear();
            LIST_STRATEGYCONTACT.clear();
            new General().getJSONContentFromInternetService(StrategyDetailsOwnActivity.this, General.PHPServices.GET_STRATEGY, params, false, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    try {
                        clsStrategy = gson.fromJson(response.getJSONObject(Constant.DATA)
                                .toString(), new TypeToken<StrategyDetails>() {
                        }.getType());

                        LIST_STRATEGYCONTACT = gson.fromJson(response.getJSONObject(Constant.DATA).getJSONArray("contact")
                                .toString(), new TypeToken<List<StrategyContact>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (LIST_STRATEGYCONTACT != null && LIST_STRATEGYCONTACT.size() > 0) {
                        ADAPTER = new StrategyContactSimpleAdapter(LIST_STRATEGYCONTACT);
                        LST_STRATEGYCONTACT.setAdapter(ADAPTER);
                        tvNetwork.setVisibility(View.VISIBLE);
                    } else tvNetwork.setVisibility(View.GONE);
                    EDT_STRATEGYDESC.setText(clsStrategy.getDescription());
                    LIST_ALARM = MAP_ALARM.get(STRATEGY_ID);

                    if (LIST_ALARM != null && LIST_ALARM.size() > 0) {
                        ADAPTER_ALARM = new StrategyAlarmAdapter(LIST_ALARM);
                        LST_STRATEGYALARM.setAdapter(ADAPTER_ALARM);
                        tvAlarms.setVisibility(View.VISIBLE);
                    } else tvAlarms.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(clsStrategy.getImage())) {
                        String _images = clsStrategy.getImage();
                        String[] _arrImg = _images.split(",");
                        for (int i = 0; i < _arrImg.length; i++) {
                            LIST_IMAGE.add(_arrImg[i]);
                        }


                        if (LIST_IMAGE != null && LIST_IMAGE.size() > 0) {
                            ADAPTER_IMG = new ImageAdapter(StrategyDetailsOwnActivity.this, LIST_IMAGE);
                            LST_OWNSTRATEGYIMG.setAdapter(ADAPTER_IMG);
                            tvAlarms.setVisibility(View.VISIBLE);
                        }
                    } else tvImages.setVisibility(View.GONE);
                    PB.setVisibility(View.GONE);
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
                            getStrategy();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_strategy, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            StrategyDetailsOwnActivity.this.finish();
        } else if (item.getItemId() == R.id.action_editStrategy) {

            Intent _intent = new Intent(StrategyDetailsOwnActivity.this, StrategyEditActivity.class);
            _intent.putExtra("STRATEGY_ID", STRATEGY_ID);
            _intent.putExtra("STRATEGY_TITLE", EDT_STRATEGYTITLE.getText().toString());
            _intent.putExtra("STRATEGY_DESC", EDT_STRATEGYDESC.getText().toString());
            _intent.putExtra("STR_LINK", clsStrategy.getLink());
            startActivity(_intent);
//            Toast.makeText(StrategyDetailsOwnActivity.this, "Edit Strategy called", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void shareStrategy() {
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", STRATEGY_ID);
        try {
            new General().getJSONContentFromInternetService(StrategyDetailsOwnActivity.this,
                    General.PHPServices.SHARE_STRATEGIES, params, true, false, true, new VolleyResponseListener() {

                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            PB.setVisibility(View.GONE);
                            Toast.makeText(StrategyDetailsOwnActivity.this, getString(R.string.share_strategy_success), Toast.LENGTH_LONG).show();
                            Log.d("::::::share:::::", response.toString());
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
                            shareStrategy();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

    public void deleteImages(final int position) {
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", STRATEGY_ID);
        params.put("images", LIST_IMAGE.get(position).substring(LIST_IMAGE.get(position).lastIndexOf('/') + 1));
        try {
            new General().getJSONContentFromInternetService(StrategyDetailsOwnActivity.this,
                    General.PHPServices.DELETE_STRATEGY_IMAGES, params, true, false, true, new VolleyResponseListener() {

                        @Override
                        public void onError(VolleyError message) {
                            PB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(JSONObject response) {
                            PB.setVisibility(View.GONE);
                            LIST_IMAGE.remove(position);
                            ADAPTER_IMG.notifyDataSetChanged();
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
                            deleteImages(position);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
