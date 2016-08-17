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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

import static percept.myplan.Activities.HopeDetailsActivity.GET_HOPE_DETAILS;


public class AddStrategyLinksActivity extends AppCompatActivity {

    private Button BTN_INSERTLINK;
    private EditText EDT_LINK;
    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_links);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addlink));

        BTN_INSERTLINK = (Button) findViewById(R.id.btnInsertLink);
        EDT_LINK = (EditText) findViewById(R.id.edtLink);
        PB = (ProgressBar) findViewById(R.id.pbAddStrategyLink);

        BTN_INSERTLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("LINK", EDT_LINK.getText().toString().trim());
                setResult(Activity.RESULT_OK, returnIntent);
                AddStrategyLinksActivity.this.finish();
            }
        });

        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_strategy_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyLinksActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_AddStrategyLink) {
            if (getIntent().hasExtra("FROM_HOPE")) {
                addHopeBoxLinkElement(HOPE_TITLE, HOPE_ID, EDT_LINK.getText().toString().trim(), "link");
            } else {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("LINK", EDT_LINK.getText().toString().trim());
                setResult(Activity.RESULT_OK, returnIntent);
                AddStrategyLinksActivity.this.finish();
            }
            return true;
        }
        return false;
    }

    private void addHopeBoxLinkElement(String title, String hopeId, String link, String type) {
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put(Constant.ID, "");
        params.put("media", link);
        params.put(Constant.HOPE_ID, hopeId);
        params.put(Constant.HOPE_TITLE, title);
        params.put(Constant.HOPE_TYPE, type);
        try {
            new General().getJSONContentFromInternetService(AddStrategyLinksActivity.this, General.PHPServices.SAVE_HOPE_MEDIA, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    PB.setVisibility(View.GONE);
                    Log.d(":::::: ", response.toString());
                    if (getIntent().hasExtra("FROM_HOPE")) {
                        GET_HOPE_DETAILS = true;
                    }
                    AddStrategyLinksActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private class AddHopeBoxLinkElement extends AsyncTask<Void, Integer, String> {
//
//        private String HOPE_TITLE, LINK, HOPE_ID, TYPE;
//
//        public AddHopeBoxLinkElement(String title, String hopeId, String link, String type) {
//            this.HOPE_TITLE = title;
//            this.LINK = link;
//            this.HOPE_ID = hopeId;
//            this.TYPE = type;
//
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
//            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveHopemedia");
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
////                if (!MUSIC_PATH.equals("")) {
////                    File sourceFile = new File(MUSIC_PATH);
////                    entity.addPart("media", new FileBody(sourceFile));
////                }
//                try {
//
//                    entity.addPart("sid", new StringBody(Constant.SID));
//                    entity.addPart("sname", new StringBody(Constant.SNAME));
//                    entity.addPart(Constant.ID, ""));
//                    entity.addPart("media", new StringBody(LINK));
//                    entity.addPart(Constant.HOPE_ID, new StringBody(HOPE_ID));
//                    entity.addPart(Constant.HOPE_TITLE, new StringBody(HOPE_TITLE));
//                    entity.addPart(Constant.HOPE_TYPE, new StringBody(TYPE));
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
//
//            super.onPostExecute(result);
//
//            Log.d(":::::: ", result);
//            if (getIntent().hasExtra("FROM_HOPE")) {
//                GET_HOPE_DETAILS=true;
//            }
//            AddStrategyLinksActivity.this.finish();
//        }
//
//    }
}
