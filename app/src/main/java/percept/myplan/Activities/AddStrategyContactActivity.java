package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;
import percept.myplan.adapters.ContactFromPhoneAdapter;
import percept.myplan.adapters.StrategyContactAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AddStrategyContactActivity extends AppCompatActivity implements StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private StickyListHeadersListView LST_CONTACTS;
    private List<ContactDisplay> LIST_ALLCONTACTS;
    private StrategyContactAdapter ADAPTER;
    private String STR_CONTACTID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.allcontact));

        LST_CONTACTS = (StickyListHeadersListView) findViewById(R.id.listStrategyContact);
        LST_CONTACTS.setOnStickyHeaderChangedListener(this);
        LST_CONTACTS.setOnStickyHeaderOffsetChangedListener(this);
        LST_CONTACTS.setDrawingListUnderStickyHeader(true);
        LST_CONTACTS.setAreHeadersSticky(true);

        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(AddStrategyContactActivity.this, General.PHPServices.GET_CONTACTS, params, false, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {

                }

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(":::::::::::::: ", response.toString());

                    Gson gson = new Gson();
                    try {
                        LIST_ALLCONTACTS = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<ContactDisplay>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ADAPTER = new StrategyContactAdapter(AddStrategyContactActivity.this, LIST_ALLCONTACTS, false);
                    LST_CONTACTS.setAdapter(ADAPTER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_strategy_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyContactActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_AddStrategyContact) {
            for (ContactDisplay _obj : LIST_ALLCONTACTS) {
                if (_obj.isSelected()) {
                    if (STR_CONTACTID.equals("")) {
                        STR_CONTACTID += _obj.getId();
                    } else {
                        STR_CONTACTID += "," + _obj.getId();
                    }
                }
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("CONTACT_ID", STR_CONTACTID);
            setResult(Activity.RESULT_OK, returnIntent);
            AddStrategyContactActivity.this.finish();
            return true;
        }
        return false;
    }
}