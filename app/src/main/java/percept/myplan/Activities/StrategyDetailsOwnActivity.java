package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import percept.myplan.POJO.StrategyContact;
import percept.myplan.POJO.StrategyDetails;
import percept.myplan.R;
import percept.myplan.adapters.ImageAdapter;
import percept.myplan.adapters.StrategyContactSimpleAdapter;

import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

public class StrategyDetailsOwnActivity extends AppCompatActivity {


    private RecyclerView LST_OWNSTRATEGYIMG;
    private List<String> LIST_IMAGE;
    private ImageAdapter ADAPTER_IMG;
    Map<String, String> params;
    private String STRATEGY_ID;
    private Utils UTILS;
    private HashMap<String, List<Alarm>> MAP_ALARM;
    private StrategyDetails clsStrategy;
    private EditText EDT_STRATEGYTITLE, EDT_STRATEGYDESC;
    public static List<StrategyContact> LIST_STRATEGYCONTACT;
    private StrategyContactSimpleAdapter ADAPTER;
    private RecyclerView LST_STRATEGYCONTACT;
    private Button BTN_SHARESTRATEGY;
    private ProgressBar PB;

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
        LST_OWNSTRATEGYIMG = (RecyclerView) findViewById(R.id.lstOwnStrategyImage);
        LST_STRATEGYCONTACT = (RecyclerView) findViewById(R.id.lstStrategyContacts);

        EDT_STRATEGYTITLE = (EditText) findViewById(R.id.edtStrategyTitle);
        EDT_STRATEGYDESC = (EditText) findViewById(R.id.edtStrategyDesc);
        EDT_STRATEGYDESC.setEnabled(false);
        EDT_STRATEGYTITLE.setEnabled(false);

        BTN_SHARESTRATEGY = (Button) findViewById(R.id.btnShareStrategyAnony);
        BTN_SHARESTRATEGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        PB = (ProgressBar) findViewById(R.id.pbStrategyOwn);

        EDT_STRATEGYTITLE.setText(getIntent().getExtras().getString("STRATEGY_NAME"));

        UTILS = new Utils(StrategyDetailsOwnActivity.this);

        String _strAlarm = UTILS.getPreference("ALARMLIST");
        try {
            if (!_strAlarm.equals("") && !_strAlarm.equals("null")) {
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


        LIST_IMAGE = new ArrayList<>();

        getStrategy();

        LST_STRATEGYCONTACT.addOnItemTouchListener(new RecyclerTouchListener(StrategyDetailsOwnActivity.this, LST_STRATEGYCONTACT, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("::::::::", LIST_STRATEGYCONTACT.get(position).getNAME());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        LST_OWNSTRATEGYIMG.addOnItemTouchListener(new RecyclerTouchListener(StrategyDetailsOwnActivity.this, LST_STRATEGYCONTACT, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("::::::::", LIST_IMAGE.get(position));
                dialogStrategyImg _dialog = new dialogStrategyImg(StrategyDetailsOwnActivity.this, LIST_IMAGE.get(position));
                _dialog.setCanceledOnTouchOutside(true);
                _dialog.show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ADDED_STRATEGIES) {
            getStrategy();
        }
    }

    private void getStrategy()
    {
        PB.setVisibility(View.VISIBLE);
        params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("id", STRATEGY_ID);
        try {
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

                    ADAPTER = new StrategyContactSimpleAdapter(LIST_STRATEGYCONTACT);
                    LST_STRATEGYCONTACT.setAdapter(ADAPTER);

                    EDT_STRATEGYDESC.setText(clsStrategy.getDescription());
                    MAP_ALARM.get(STRATEGY_ID);

                    String _images = clsStrategy.getImage();
                    String[] _arrImg = _images.split(",");
                    for (int i = 0; i < _arrImg.length; i++) {
                        LIST_IMAGE.add(_arrImg[i]);
                    }
                    PB.setVisibility(View.GONE);
                    ADAPTER_IMG = new ImageAdapter(StrategyDetailsOwnActivity.this, LIST_IMAGE);
                    LST_OWNSTRATEGYIMG.setAdapter(ADAPTER_IMG);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
