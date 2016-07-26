package percept.myplan.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Config;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

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
import java.util.Map;

import percept.myplan.AppController;
import percept.myplan.Classes.AndroidMultiPartEntity;
import percept.myplan.R;

/**
 * Created by percept on 25/7/16.
 */

public class General {

    private String getServiceName(PHPServices serviceName) {
        if (serviceName == PHPServices.LOGIN) {
            return ".login";
        } else if (serviceName == PHPServices.REGISTER) {
            return ".register";
        } else if (serviceName == PHPServices.GET_SYMPTOMS) {
            return ".getsymptoms";
        } else if (serviceName == PHPServices.GET_SYMPTOM) {
            return ".getsymptom";
        } else if (serviceName == PHPServices.GET_STRATEGIES) {
            return ".getStrategies";
        } else if (serviceName == PHPServices.GET_INSPIRATIONS) {
            return "";
        } else if (serviceName == PHPServices.GET_INSPIRATION) {
            return "";
        } else if (serviceName == PHPServices.GET_CONTACTS) {
            return "";
        } else if (serviceName == PHPServices.GET_CONTACT) {
            return "";
        } else if (serviceName == PHPServices.GET_SIDASTEST) {
            return "";
        } else if (serviceName == PHPServices.GET_SIDASCALENDER) {
            return "";
        } else if (serviceName == PHPServices.GET_MOODCALENDER) {
            return "";
        }
        return "";
    }


    public String getJSONContentFromInternetService(Context context, PHPServices servicesName, Map<String, String> params, Boolean checkInternetConnectivity, Boolean encryptedDataTransfer, final VolleyResponseListener volleyResponseListener) throws Exception {


        // CHeck internet connection
        if (checkInternetConnectivity == true && !checkInternetConnection(context)) {
            throw new Exception(context.getResources().getString(R.string.err_network_not_available));
        }
        String str = context.getResources().getString(R.string.server_url);
        String _str = str + getServiceName(servicesName);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                _str, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(":::::::::::: ", response.toString());
                        volleyResponseListener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("::::::::::::: ", "Error: " + error.getMessage());
                volleyResponseListener.onError(error);

            }

        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");
        return "";
    }


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager _connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean _isConnected = false;
        NetworkInfo _activeNetwork = _connManager.getActiveNetworkInfo();
        if (_activeNetwork != null) {
            _isConnected = _activeNetwork.isConnectedOrConnecting();
        }

        return _isConnected;
    }


    public enum PHPServices {
        LOGIN,
        REGISTER,
        GET_SYMPTOMS,
        GET_SYMPTOM,
        GET_STRATEGIES,
        GET_INSPIRATIONS,
        GET_INSPIRATION,
        GET_CONTACTS,
        GET_CONTACT,
        GET_SIDASTEST,
        GET_SIDASCALENDER,
        GET_MOODCALENDER
    }
}
