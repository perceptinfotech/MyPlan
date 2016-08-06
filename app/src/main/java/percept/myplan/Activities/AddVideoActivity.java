package percept.myplan.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.R;

import static percept.myplan.Activities.HopeDetailsActivity.GET_HOPE_DETAILS;

public class AddVideoActivity extends AppCompatActivity {

    private TextView TV_CHOOSEVIDEO, TV_RECORDVIDEO, TV_CHOOSEVIDLINK;

    private final static int REQ_PICK_VID_GALLERY = 10;
    private final static int REQ_TAKE_VIDEO = 11;
    private Uri videFileUri;
    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private ProgressBar PB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Video");

        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
        }


        TV_CHOOSEVIDEO = (TextView) findViewById(R.id.tvChooseExistingVideo);
        TV_RECORDVIDEO = (TextView) findViewById(R.id.tvRecordVideo);
        TV_CHOOSEVIDLINK = (TextView) findViewById(R.id.tvChooseFromLink);
        PB = (ProgressBar) findViewById(R.id.pbVideos);

        TV_CHOOSEVIDEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("video/*");
                startActivityForResult(intent, REQ_PICK_VID_GALLERY);
            }
        });

        TV_RECORDVIDEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intents.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
                intents.putExtra(MediaStore.EXTRA_OUTPUT, videFileUri);
                startActivityForResult(intents, REQ_TAKE_VIDEO);
            }
        });

        TV_CHOOSEVIDLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Calendar cal = Calendar.getInstance();
        int seconds = cal.get(Calendar.SECOND);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        String currentDateTimeString = new SimpleDateFormat("ddMMMyyyy").format(new Date());
        if (requestCode == REQ_PICK_VID_GALLERY) {
            //region PICK_VID_GALLERY
            Uri selectedVideos = data.getData();
            String[] videoFilePath = {MediaStore.Video.Media.DATA};
            Cursor c = getContentResolver().query(selectedVideos, videoFilePath,
                    null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(videoFilePath[0]);
            String videosPath = c.getString(columnIndex);
            c.close();

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videosPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long _timeInmillisec = Long.parseLong(time);
            long _duration = _timeInmillisec / 1000;
            long _hours = _duration / 3600;
            long _minutes = (_duration - _hours * 3600) / 60;
            long _seconds = _duration - (_hours * 3600 + _minutes * 60);

            if (_seconds > 20) {
                Toast.makeText(AddVideoActivity.this, R.string.pickagainwithlesstime, Toast.LENGTH_SHORT).show();
                return;
            }


            int end = videosPath.toString().lastIndexOf("/");
            String str2 = videosPath.toString().substring(end + 1, videosPath.length());

            String name = "VID_" + currentDateTimeString + seconds + hour + min + ".mp4";

            File mediaStorageDir = new File(Constant.APP_MEDIA_PATH + File.separator + "VIDEOS");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {

                }
            }

            boolean success = Constant.copyFile(videosPath, Constant.APP_MEDIA_PATH + File.separator + "VIDEOS", name);

            if (success) {
                String _Path = Constant.APP_MEDIA_PATH + File.separator + "VIDEOS" + File.separator + name;
                Log.d("::::::::::: ", _Path);
                new AddHopeBoxVideoElement(HOPE_TITLE, HOPE_ID, videosPath, "video").execute();
            }
            //endregion
        } else if (requestCode == REQ_TAKE_VIDEO) {
            //region VIDEO_CAPTURE
            Uri videoUris = data.getData();
            String[] filePaths = {MediaStore.Video.Media.DATA};
            Cursor cu = getContentResolver().query(videoUris, filePaths, null, null, null);
            cu.moveToFirst();
            int columnIndex = cu.getColumnIndex(filePaths[0]);
            String picturePaths = cu.getString(columnIndex);
            cu.close();
            int end = picturePaths.toString().lastIndexOf("/");
            String str2 = picturePaths.toString().substring(end + 1, picturePaths.length());
            File mediaStorageDir = new File(Constant.APP_MEDIA_PATH + File.separator + "VIDEOS");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {

                }
            }

            String name = "VID_" + currentDateTimeString + seconds + hour + min + ".mp4";

            Constant.copyFile(picturePaths, Constant.APP_MEDIA_PATH + File.separator + "VIDEOS", name);

//                File file = new File(_imgPath);
//                if (file.exists()) {
//                    file.delete();
//                }

            String _path = Constant.APP_MEDIA_PATH + File.separator + "VIDEOS" + File.separator + name;
            Log.d("::::::::::: ", _path);
            new AddHopeBoxVideoElement(HOPE_TITLE, HOPE_ID, _path, "video").execute();
        }
    }


    private class AddHopeBoxVideoElement extends AsyncTask<Void, Integer, String> {

        private String HOPE_TITLE, VID_PATH, HOPE_ID, TYPE;

        public AddHopeBoxVideoElement(String title, String hopeId, String vidpath, String type) {
            this.HOPE_TITLE = title;
            this.VID_PATH = vidpath;
            this.HOPE_ID = hopeId;
            this.TYPE = type;

        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            PB.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveHopemedia");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (!VID_PATH.equals("")) {
                    File sourceFile = new File(VID_PATH);
                    entity.addPart("media", new FileBody(sourceFile));
                }
                try {

                    entity.addPart("sid", new StringBody(Constant.SID));
                    entity.addPart("sname", new StringBody(Constant.SNAME));
                    entity.addPart(Constant.ID, new StringBody(""));
                    entity.addPart(Constant.HOPE_ID, new StringBody(HOPE_ID));
                    entity.addPart(Constant.HOPE_TITLE, new StringBody(HOPE_TITLE));
                    entity.addPart(Constant.HOPE_TYPE, new StringBody(TYPE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


//                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                long totalLength = entity.getContentLength();
                System.out.println("TotalLength : " + totalLength);

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
            PB.setVisibility(View.GONE);
            super.onPostExecute(result);
            Log.d(":::::: ", result);
            if (getIntent().hasExtra("FROM_HOPE")) {
                GET_HOPE_DETAILS = true;
            }
            AddVideoActivity.this.finish();
        }

    }
}
