package percept.myplan.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import percept.myplan.AppController;
import percept.myplan.R;
import percept.myplan.Global.VolleyResponseListener;

/**
 * Created by percept on 25/7/16.
 */

public class General {

    private String getServiceName(PHPServices serviceName) {
        if (serviceName == PHPServices.LOGIN) {
            return ".login";
        }else if(serviceName == PHPServices.REGISTER){
            return ".register";
        }
        return "";
    }


    public String getJSONContentFromInternetService(Context context, PHPServices servicesName,Map<String, String> params, Boolean checkInternetConnectivity, Boolean encryptedDataTransfer, final VolleyResponseListener volleyResponseListener) throws Exception {


        // CHeck internet connection
        if (checkInternetConnectivity == true && !checkInternetConnection(context)) {
            throw new Exception(context.getResources().getString(R.string.err_network_not_available));
        }
        JSONObject parameters = new JSONObject(params);
        String str = context.getResources().getString(R.string.server_url);
        String _str = str + getServiceName(servicesName);

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
        REGISTER
    }
}
