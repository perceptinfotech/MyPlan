package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.POJO.Alarm;
import percept.myplan.R;
import percept.myplan.adapters.AlarmAdapter;
import percept.myplan.fragments.fragmentContacts;

import static percept.myplan.fragments.fragmentStrategies.ADDED_STRATEGIES;

public class AddStrategyActivity extends AppCompatActivity {

    private EditText EDT_TITLE, EDT_TEXT;
    private TextView TV_ALARM, TV_IMAGES, TV_LINKS, TV_NETWORK, TV_MUSIC;

    public static List<Alarm> LIST_ALARM;
    public static List<String> LIST_IMG;
    public static List<String> LIST_MUSIC;
    private final int SET_ALARM = 15;
    private final int SET_CONTACT = 18;
    private final int SET_IMAGE = 21;
    private final int SET_MUSIC = 24;
    private final int SET_LINK = 25;
    private String STR_LINK = "", STR_CONTACTID = "";

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

        TV_ALARM = (TextView) findViewById(R.id.tvAlarm);
        TV_IMAGES = (TextView) findViewById(R.id.tvImages);
        TV_LINKS = (TextView) findViewById(R.id.tvLinks);
        TV_NETWORK = (TextView) findViewById(R.id.tvNetwork);
        TV_MUSIC = (TextView) findViewById(R.id.tvMusic);

        LIST_ALARM = new ArrayList<>();
        LIST_IMG = new ArrayList<>();
        LIST_MUSIC = new ArrayList<>();

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
            new AddStrategy(EDT_TITLE.getText().toString().trim(), EDT_TEXT.getText().toString().trim(),
                    STR_CONTACTID, LIST_IMG, LIST_MUSIC, STR_LINK).execute();
            return true;
        }
        return false;
    }

    private class AddStrategy extends AsyncTask<Void, Integer, String> {

        private String TITLE, TEXT, CONTACT_ID, LINK;
        private List<String> LST_IMG, LST_MUSIC;

        public AddStrategy(String title, String text, String STR_CONTACTID, List<String> listImg, List<String> listMusic, String STR_LINK) {
            this.TITLE = title;
            this.TEXT = text;
            this.CONTACT_ID = STR_CONTACTID;
            this.LST_IMG = listImg;
            this.LST_MUSIC = listMusic;
            this.LINK = STR_LINK;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveStrategy");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                if (!FILE_PATH.equals("")) {
//                    File sourceFile = new File(FILE_PATH);

                // Adding file data to http body
                if (LST_IMG.size() > 0) {
                    for (int i = 0; i < LST_IMG.size(); i++) {
                        File _f = new File(LST_IMG.get(i));
                        entity.addPart("image" + (i + 1), new FileBody(_f));
                    }
                }
                if (LST_MUSIC.size() > 0) {
                    for (int i = 0; i < LST_MUSIC.size(); i++) {
                        File _f = new File(LST_MUSIC.get(i));
                        entity.addPart("music" + (i + 1), new FileBody(_f));
                    }
                }
//                }
                // Extra parameters if you want to pass to server
                try {

                    entity.addPart("sid", new StringBody(Constant.SID));
                    entity.addPart("sname", new StringBody(Constant.SNAME));
                    entity.addPart("image_count", new StringBody(String.valueOf(LST_IMG.size())));
                    entity.addPart("videos_count", new StringBody(String.valueOf(LST_MUSIC.size())));
                    entity.addPart(Constant.ID, new StringBody(""));
                    entity.addPart(Constant.TITLE, new StringBody(this.TITLE));
                    entity.addPart(Constant.DESC, new StringBody(this.TEXT));
                    entity.addPart(Constant.CONTACTID, new StringBody(this.CONTACT_ID));
                    entity.addPart(Constant.LINK, new StringBody(this.LINK));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try {
                Log.d(":::::: ", result);
                JSONObject _object = new JSONObject(result);
                JSONObject _ObjData = _object.getJSONObject(Constant.DATA);

                Toast.makeText(AddStrategyActivity.this,
                        getResources().getString(R.string.strategyadded), Toast.LENGTH_SHORT).show();
//                AddStrategyActivity.this.finish();
//                ADDED_STRATEGIES = true;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SET_ALARM) {
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
        } else if (requestCode == SET_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                STR_CONTACTID = data.getStringExtra("CONTACT_ID");
                Log.d(":::::: ", STR_CONTACTID);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == SET_LINK) {
            if (resultCode == Activity.RESULT_OK) {
                STR_LINK = data.getStringExtra("LINK");
                Log.d(":::::: ", STR_LINK);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
