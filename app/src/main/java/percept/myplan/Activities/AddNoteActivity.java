package percept.myplan.Activities;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;

import static percept.myplan.Activities.HopeDetailsActivity.GET_HOPE_DETAILS;

public class AddNoteActivity extends AppCompatActivity {

    private EditText ED_NOTE;
    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        mTitle.setText(R.string.leavenote);
        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
        }
        ED_NOTE = (EditText) findViewById(R.id.edtNote);
        PB = (ProgressBar) findViewById(R.id.pbAddNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddNoteActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_saveNote) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (!ED_NOTE.getText().toString().trim().equals("")) {
                addHopeBoxNoteElement(ED_NOTE.getText().toString().trim(), "note");

            } else {
                Toast.makeText(AddNoteActivity.this, R.string.pleaseaddnote, Toast.LENGTH_SHORT).show();
            }

        }
        return false;
    }

    private void addHopeBoxNoteElement(final String NOTE, final String TYPE) {
        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put(Constant.ID, "");
        params.put("media", NOTE);
        params.put(Constant.HOPE_ID, HOPE_ID);
        params.put(Constant.HOPE_TITLE, HOPE_TITLE);
        params.put(Constant.HOPE_TYPE, TYPE);
        try {
            new General().getJSONContentFromInternetService(AddNoteActivity.this, General.PHPServices.SAVE_HOPE_MEDIA, params, true, false, true, new VolleyResponseListener() {
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
                    AddNoteActivity.this.finish();
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
                            addHopeBoxNoteElement(NOTE,TYPE);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

//    private class AddHopeBoxNoteElement extends AsyncTask<Void, Integer, String> {
//
//        private String HOPE_TITLE, NOTE, HOPE_ID, TYPE;
//
//        public AddHopeBoxNoteElement(String title, String hopeId, String note, String type) {
//            this.HOPE_TITLE = title;
//            this.NOTE = note;
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
//                    entity.addPart(Constant.ID, new StringBody(""));
//                    entity.addPart("media", new StringBody(NOTE));
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
//                GET_HOPE_DETAILS = true;
//            }
//            AddNoteActivity.this.finish();
//        }
//
//    }
}
