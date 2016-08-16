package percept.myplan.Global;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import percept.myplan.Interfaces.AsyncTaskCompletedListener;
import percept.myplan.R;

/**
 * Created by percept on 16/8/16.
 */
public class MultiPartParsing {

    private Context context;
    private HashMap<String, String> map;
    private AsyncTaskCompletedListener completedListener;
//    private int serviceCode;

    public MultiPartParsing(Context _context, HashMap<String, String> _map, AsyncTaskCompletedListener _completedListener) {
        this.context = _context;
        this.map = _map;
        this.completedListener = _completedListener;
        new MultiPartAsyncTask().execute();
    }

    class MultiPartAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(getResources().getString(R.string.server_url) + ".saveStrategy");
            HttpPost httppost = new HttpPost(map.get("url"));
            map.remove("url");
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
                for (String key : map.keySet()) {
                    if (key.contains("image")) {
                        File _f = new File(map.get(key));
                        entity.addPart(key, new FileBody(_f));
                    } else if (key.contains("video")) {
                        File _f = new File(map.get(key));
                        entity.addPart(key, new FileBody(_f));
                    } else if (key.contains("audio")) {
                        File _f = new File(map.get(key));
                        entity.addPart(key, new FileBody(_f));
                    } else
                        entity.addPart(key, new StringBody(map.get(key)));

                }
           /* if (LST_IMG.size() > 0) {
                for (int i = 0; i < LST_IMG.size(); i++) {
                    File _f = new File(LST_IMG.get(i));
                    entity.addPart("image" + (i + 1), new FileBody(_f));
                }
            }
            if (LST_MUSIC.size() > 0) {
                for (int i = 0; i < LST_MUSIC.size(); i++) {
                    File _f = new File(LST_MUSIC.get(i));
                    entity.addPart("videos" + (i + 1), new FileBody(_f));
                }
            }
//                }
            // Extra parameters if you want to pass to server
            try {

                entity.addPart("sid", new StringBody(Constant.SID));
                entity.addPart("sname", new StringBody(Constant.SNAME));
                entity.addPart("image_count", new StringBody(String.valueOf(LST_IMG.size())));
                entity.addPart("music_count", new StringBody(String.valueOf(LST_MUSIC.size())));
                entity.addPart(Constant.ID, new StringBody(""));
                entity.addPart(Constant.TITLE, new StringBody(this.TITLE));
                entity.addPart(Constant.DESC, new StringBody(this.TEXT));
                entity.addPart(Constant.CONTACTID, new StringBody(this.CONTACT_ID));
                entity.addPart(Constant.LINK, new StringBody(this.LINK));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/


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
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (completedListener != null)
                completedListener.onTaskCompleted(response);
        }
    }
}
