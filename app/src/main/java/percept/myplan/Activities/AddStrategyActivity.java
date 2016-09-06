package percept.myplan.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Global.MultiPartParsing;
import percept.myplan.Global.Utils;
import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.POJO.Alarm;
import percept.myplan.R;

import static percept.myplan.Activities.AddStrategyToSymptomActivity.GET_STRATEGIES;
import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

public class AddStrategyActivity extends AppCompatActivity {

    public static List<Alarm> LIST_ALARM;
    public static List<String> LIST_IMG;
    public static List<String> LIST_MUSIC;
    private final int SET_ALARM = 15;
    private final int SET_CONTACT = 18;
    private final int SET_IMAGE = 21;
    private final int SET_MUSIC = 24;
    private final int SET_LINK = 25;
    private EditText EDT_TITLE, EDT_TEXT;
    private TextView TV_ALARM, TV_IMAGES, TV_LINKS, TV_NETWORK, TV_MUSIC;
    private String STR_LINK = "", STR_CONTACTID = "";
    private HashMap<String, List<Alarm>> MAP_ALARM;
    private Utils UTILS;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addastrategy));

        EDT_TITLE = (EditText) findViewById(R.id.edtTitle);
        EDT_TEXT = (EditText) findViewById(R.id.edtText);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        PB = (ProgressBar) findViewById(R.id.pbAddStrategy);

        TV_ALARM = (TextView) findViewById(R.id.tvAlarm);
        TV_IMAGES = (TextView) findViewById(R.id.tvImages);
        TV_LINKS = (TextView) findViewById(R.id.tvLinks);
        TV_NETWORK = (TextView) findViewById(R.id.tvNetwork);
        TV_MUSIC = (TextView) findViewById(R.id.tvMusic);

        LIST_ALARM = new ArrayList<>();
        LIST_IMG = new ArrayList<>();
        LIST_MUSIC = new ArrayList<>();
        MAP_ALARM = new HashMap<>();
        UTILS = new Utils(AddStrategyActivity.this);
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

        TV_ALARM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyActivity.this, AlarmListActivity.class), SET_ALARM);
            }
        });

        TV_IMAGES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyActivity.this, AddStrategyImageActivity.class), SET_IMAGE);
            }
        });

        TV_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyActivity.this, AddStrategyLinksActivity.class), SET_LINK);
            }
        });

        TV_NETWORK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyActivity.this, AddStrategyContactActivity.class), SET_CONTACT);
            }
        });

        TV_MUSIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStrategyActivity.this, AddStrategyMusicActivity.class), SET_MUSIC);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_strategy, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_saveStrategy) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            /*new AddStrategy(EDT_TITLE.getText().toString().trim(), EDT_TEXT.getText().toString().trim(),
                    STR_CONTACTID, LIST_IMG, LIST_MUSIC, STR_LINK).execute();*/
            addStrategy();
            return true;
        }
        return false;
    }

    private void addStrategy() {

        if (!UTILS.isNetConnected()) {
            Snackbar snackbar = Snackbar
                    .make(REL_COORDINATE, getResources().getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addStrategy();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
            return;
        }
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        if (LIST_IMG.size() > 0) {
            for (int i = 0; i < LIST_IMG.size(); i++) {
                map.put("image" + (i + 1), LIST_IMG.get(i));
            }
        }
        if (LIST_MUSIC.size() > 0) {
            for (int i = 0; i < LIST_MUSIC.size(); i++) {
                map.put("video" + (i + 1), LIST_IMG.get(i));
            }
        }
//                }
        // Extra parameters if you want to pass to server

        //   map.put(Constant.URL, getResources().getString(R.string.server_url) + ".saveStrategy");
        map.put("sid", Constant.SID);
        map.put("sname", Constant.SNAME);
        map.put("image_count", String.valueOf(LIST_IMG.size()));
        map.put("music_count", String.valueOf(LIST_MUSIC.size()));
        map.put(Constant.ID, "");
        map.put(Constant.TITLE, EDT_TITLE.getText().toString().trim());
        map.put(Constant.DESC, EDT_TEXT.getText().toString().trim());
        map.put(Constant.CONTACTID, STR_CONTACTID);
        map.put(Constant.LINK, STR_LINK);


        new MultiPartParsing(this, map, General.PHPServices.SHARE_STRATEGIES, new AsyncTaskCompletedListener() {
            @Override
            public void onTaskCompleted(String response) {
                PB.setVisibility(View.GONE);
                try {
                    Log.d(":::::: ", response);
                    String _id = "";
                    JSONObject _object = new JSONObject(response);
                    JSONObject _ObjData = _object.getJSONObject(Constant.DATA);
                    _id = _ObjData.getString(Constant.ID);
                    MAP_ALARM.put(_id, LIST_ALARM);
                    Gson gson = new Gson();
                    String _alarmList = gson.toJson(MAP_ALARM);
                    UTILS.setPreference("ALARMLIST", _alarmList);
                    Toast.makeText(AddStrategyActivity.this,
                            getResources().getString(R.string.strategyadded), Toast.LENGTH_SHORT).show();


                    if (getIntent().hasExtra("FROM_SYMPTOM")) {
                        GET_STRATEGIES = true;
                    } else {
                        ADDED_STRATEGIES = true;
                    }
                    AddStrategyActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });
    }

//    private class AddStrategy extends AsyncTask<Void, Integer, String> {
//
//        private String TITLE, TEXT, CONTACT_ID, LINK;
//        private List<String> LST_IMG, LST_MUSIC;
//
//        public AddStrategy(String title, String text, String STR_CONTACTID, List<String> listImg, List<String> listMusic, String STR_LINK) {
//            this.TITLE = title;
//            this.TEXT = text;
//            this.CONTACT_ID = STR_CONTACTID;
//            this.LST_IMG = listImg;
//            this.LST_MUSIC = listMusic;
//            this.LINK = STR_LINK;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // setting progress bar to zero
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected String doInBackground(Void... params) {
//            return uploadFile();
//        }
//
//        @SuppressWarnings("deprecation")
//        private String uploadFile() {
//            String responseString = null;
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveStrategy");
//
//            try {
//                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//
//                            @Override
//                            public void transferred(long num) {
////                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//
////                if (!FILE_PATH.equals("")) {
////                    File sourceFile = new File(FILE_PATH);
//
//                // Adding file data to http body
//                if (LST_IMG.size() > 0) {
//                    for (int i = 0; i < LST_IMG.size(); i++) {
//                        File _f = new File(LST_IMG.get(i));
//                        entity.addPart("image" + (i + 1), new FileBody(_f));
//                    }
//                }
//                if (LST_MUSIC.size() > 0) {
//                    for (int i = 0; i < LST_MUSIC.size(); i++) {
//                        File _f = new File(LST_MUSIC.get(i));
//                        entity.addPart("videos" + (i + 1), new FileBody(_f));
//                    }
//                }
////                }
//                // Extra parameters if you want to pass to server
//                try {
//
//                    entity.addPart("sid", new StringBody(Constant.SID));
//                    entity.addPart("sname", new StringBody(Constant.SNAME));
//                    entity.addPart("image_count", new StringBody(String.valueOf(LST_IMG.size())));
//                    entity.addPart("music_count", new StringBody(String.valueOf(LST_MUSIC.size())));
//                    entity.addPart(Constant.ID, new StringBody(""));
//                    entity.addPart(Constant.TITLE, new StringBody(this.TITLE));
//                    entity.addPart(Constant.DESC, new StringBody(this.TEXT));
//                    entity.addPart(Constant.CONTACTID, new StringBody(this.CONTACT_ID));
//                    entity.addPart(Constant.LINK, new StringBody(this.LINK));
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//
////                totalSize = entity.getContentLength();
//                httppost.setEntity(entity);
//                long totalLength = entity.getContentLength();
//                System.out.println("TotalLength : " + totalLength);
//
//                // Making server call
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity r_entity = response.getEntity();
//
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(r_entity);
//
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//
//            } catch (ClientProtocolException e) {
//                responseString = e.toString();
//            } catch (IOException e) {
//                responseString = e.toString();
//            }
//
//            return responseString;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            PB.setVisibility(View.GONE);
//            super.onPostExecute(result);
//            try {
//                Log.d(":::::: ", result);
//                String _id = "";
//                JSONObject _object = new JSONObject(result);
//                JSONObject _ObjData = _object.getJSONObject(Constant.DATA);
//                _id = _ObjData.getString(Constant.ID);
//                MAP_ALARM.put(_id, LIST_ALARM);
//                Gson gson = new Gson();
//                String _alarmList = gson.toJson(MAP_ALARM);
//                UTILS.setPreference("ALARMLIST", _alarmList);
//                Toast.makeText(AddStrategyActivity.this,
//                        getResources().getString(R.string.strategyadded), Toast.LENGTH_SHORT).show();
//
//
//                if (getIntent().hasExtra("FROM_SYMPTOM")) {
//                    GET_STRATEGIES = true;
//                } else {
//                    ADDED_STRATEGIES = true;
//                }
//                AddStrategyActivity.this.finish();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SET_ALARM:
                if (resultCode == Activity.RESULT_OK) {
                    String _strAlarm = data.getStringExtra("ALARMS");
                    Log.d(":::::: ", _strAlarm);
//
//                try {
//                    if (!_strAlarm.equals("") && !_strAlarm.equals("null")) {
//                        Type listType = new TypeToken<List<Alarm>>() {
//
//                        }.getType();
//                        try {
//                            LST_ALARM = new Gson().fromJson(_strAlarm, listType);
//                        } catch (JsonSyntaxException ex) {
//                            LST_ALARM = new ArrayList<Alarm>();
//                        }
//                    } else {
//                        LST_ALARM = new ArrayList<Alarm>();
//                    }
//                } catch (Exception ex) {
//
//                }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            case SET_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    STR_CONTACTID = data.getStringExtra("CONTACT_ID");
                    Log.d(":::::: ", STR_CONTACTID);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            case SET_LINK:
                if (resultCode == Activity.RESULT_OK) {
                    STR_LINK = data.getStringExtra("LINK");
                    Log.d(":::::: ", STR_LINK);

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            case SET_MUSIC:

                if (data!=null && data.hasExtra("LINK")) {
                    if (resultCode == Activity.RESULT_OK) {
                        STR_LINK = data.getStringExtra("LINK");
                        Log.d(":::::: ", STR_LINK);

                    }
                }
                break;
        }
    }
}
