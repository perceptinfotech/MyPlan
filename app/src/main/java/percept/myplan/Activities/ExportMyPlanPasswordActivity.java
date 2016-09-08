package percept.myplan.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.R;
import percept.myplan.customviews.PinEntryEditText;

/**
 * Created by percept on 8/9/16.
 */

public class ExportMyPlanPasswordActivity extends AppCompatActivity {
    private PinEntryEditText editText;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_myplan_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.exportmyplan));

        editText = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        PB = (ProgressBar) findViewById(R.id.progressBar);

        REL_COORDINET = (CoordinatorLayout) findViewById(R.id.snakeBar);

        editText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    exportingPDF(str.toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                    PB.setVisibility(View.GONE);
                }
            }
        });

    }

    private void exportingPDF(final String pdfPassword) {
        PB.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        params.put("password", pdfPassword);
        try {
            new General().getJSONContentFromInternetService(ExportMyPlanPasswordActivity.this, General.PHPServices.GET_EXPORT_PDF, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("::::PDF", response.toString());
                    PB.setVisibility(View.GONE);
                    Toast.makeText(ExportMyPlanPasswordActivity.this, getString(R.string.mail_success_msg), Toast.LENGTH_LONG).show();
                    ExportMyPlanPasswordActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(REL_COORDINET, getString(R.string.nointernet), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exportingPDF(pdfPassword);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ExportMyPlanPasswordActivity.this.finish();
            return true;
        }
        return false;
    }
}
