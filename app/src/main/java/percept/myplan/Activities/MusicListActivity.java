package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import percept.myplan.Global.AndroidMultiPartEntity;
import percept.myplan.Global.Constant;
import percept.myplan.Global.MultiPartParsing;
import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.R;

import static percept.myplan.Activities.AddStrategyMusicActivity.CLOSE_PAGE;
import static percept.myplan.Activities.HopeDetailsActivity.GET_HOPE_DETAILS;

public class MusicListActivity extends AppCompatActivity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private String[] nameFile;
    private ImageAdapter imageAdapter;

    private String FROM = "";
    private String HOPE_TITLE = "";
    private String HOPE_ID = "";
    private boolean FROM_EDIT = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.musiclist));

        if (getIntent().hasExtra("FROM_HOPE")) {
            FROM = getIntent().getExtras().getString("FROM_HOPE");
            HOPE_TITLE = getIntent().getExtras().getString("HOPE_TITLE");
            HOPE_ID = getIntent().getExtras().getString("HOPE_ID");
        }

        if (getIntent().hasExtra("FROM_EDIT")) {
            FROM_EDIT = true;
        }

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Audio.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Audio.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.nameFile = new String[this.count];
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);


            arrPath[i] = imagecursor.getString(dataColumnIndex);


            nameFile[i] = arrPath[i].substring(arrPath[i].lastIndexOf("/") + 1, arrPath[i].length());

//			Log.d("soundPAth","");
        }
        ListView imagegrid = (ListView) findViewById(R.id.lstMusic);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagecursor.close();

//        final Button selectBtn = (Button) findViewById(R.id.selectBtn);
//        selectBtn.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                final int len = thumbnailsselection.length;
//                int cnt = 0;
//                String selectImages = "";
//                for (int i = 0; i < len; i++) {
//                    if (thumbnailsselection[i]) {
//                        cnt++;
//                        selectImages = selectImages + arrPath[i] + "|";
//                    }
//                }
//                if (cnt == 0) {
//                    Toast.makeText(getApplicationContext(),
//                            "Please select at least one image",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "You've selected Total " + cnt + " image(s).",
//                            Toast.LENGTH_LONG).show();
//                    Log.d("SelectedImages", selectImages);
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_strategy_music, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MusicListActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_AddStrategyMusic) {
            final int len = thumbnailsselection.length;

            String selectImages = "";
            if (FROM.equals("") || FROM_EDIT) {
                if (FROM_EDIT) {
                    for (int i = 0; i < len; i++) {
                        if (thumbnailsselection[i]) {
                            selectImages = selectImages + arrPath[i] + "|";
                            StrategyEditActivity.LIST_MUSIC.add(arrPath[i]);
                        }
                    }
                    CLOSE_PAGE = true;
                    MusicListActivity.this.finish();
                } else {
                    for (int i = 0; i < len; i++) {
                        if (thumbnailsselection[i]) {
                            selectImages = selectImages + arrPath[i] + "|";
                            AddStrategyActivity.LIST_MUSIC.add(arrPath[i]);
                        }
                    }
                    CLOSE_PAGE = true;
                    MusicListActivity.this.finish();
                }
            } else {
                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {
                        addHopeBoxMusicElement(HOPE_TITLE, HOPE_ID, arrPath[i], "music");
                        break;
                    }
                }
            }

            return true;
        }
        return false;
    }

    public void addHopeBoxMusicElement(String title, String hopeId, String musicpath, String type) {
        HashMap<String,String> params=new HashMap<>();
        params.put(Constant.URL,getResources().getString(R.string.server_url) + ".saveHopemedia");
        if (!TextUtils.isEmpty(musicpath)) {
            params.put("media", musicpath);
        }

            params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put(Constant.ID,"");
        params.put(Constant.HOPE_ID, hopeId);
        params.put(Constant.HOPE_TITLE, title);
        params.put(Constant.HOPE_TYPE, type);
        new MultiPartParsing(MusicListActivity.this, params, new AsyncTaskCompletedListener() {
            @Override
            public void onTaskCompleted(String response) {
                if (getIntent().hasExtra("FROM_HOPE")) {
                    GET_HOPE_DETAILS=true;
                }
                Log.d(":::::: ", response);
                CLOSE_PAGE = true;
                MusicListActivity.this.finish();
            }
        });
    }

//    private class AddHopeBoxMusicElement extends AsyncTask<Void, Integer, String> {
//
//        private String HOPE_TITLE, MUSIC_PATH, HOPE_ID, TYPE;
//
//        public AddHopeBoxMusicElement(String title, String hopeId, String musicpath, String type) {
//            this.HOPE_TITLE = title;
//            this.MUSIC_PATH = musicpath;
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
//                if (!MUSIC_PATH.equals("")) {
//                    File sourceFile = new File(MUSIC_PATH);
//                    entity.addPart("media", new FileBody(sourceFile));
//                }
//                try {
//
//                    entity.addPart("sid", new StringBody(Constant.SID));
//                    entity.addPart("sname", new StringBody(Constant.SNAME));
//                    entity.addPart(Constant.ID, new StringBody(""));
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
//            if (getIntent().hasExtra("FROM_HOPE")) {
//                GET_HOPE_DETAILS=true;
//            }
//            Log.d(":::::: ", result);
//            CLOSE_PAGE = true;
//            MusicListActivity.this.finish();
//        }
//
//    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.lay_music_list_item, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                holder.txtview = (TextView) convertView.findViewById(R.id.itemtxtview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.txtview.setText(nameFile[position]);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (!FROM.equals("")) {
                        for (int i = 0; i < count; i++) {
                            thumbnailsselection[i] = false;
                        }
                    }
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "audio/*");
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        TextView txtview;
        int id;
    }
}
