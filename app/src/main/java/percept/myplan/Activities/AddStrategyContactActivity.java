package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.POJO.StrategyContact;
import percept.myplan.R;
import percept.myplan.adapters.StrategyContactAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static percept.myplan.Activities.StrategyDetailsOwnActivity.LIST_STRATEGYCONTACT;

public class AddStrategyContactActivity extends AppCompatActivity implements StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener {

    private StickyListHeadersListView LST_CONTACTS;
    private List<ContactDisplay> LIST_ALLCONTACTS;
    private StrategyContactAdapter ADAPTER;
    private String STR_CONTACTID = "";
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

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
        if (getIntent().hasExtra("FROM_SHARELOC"))
            mTitle.setText(getResources().getString(R.string.share_with));
        else mTitle.setText(getResources().getString(R.string.allcontact));
        PB = (ProgressBar) findViewById(R.id.pbAddStrategyContact);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        LST_CONTACTS = (StickyListHeadersListView) findViewById(R.id.listStrategyContact);
        LST_CONTACTS.setOnStickyHeaderChangedListener(this);
        LST_CONTACTS.setOnStickyHeaderOffsetChangedListener(this);
        LST_CONTACTS.setDrawingListUnderStickyHeader(true);
        LST_CONTACTS.setAreHeadersSticky(true);


        AddStrategyContacts();

    }

    private void AddStrategyContacts() {

        PB.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("sid", Constant.SID);
        params.put("sname", Constant.SNAME);
        try {
            new General().getJSONContentFromInternetService(AddStrategyContactActivity.this, General.PHPServices.GET_CONTACTS, params, true, false, false, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
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

                    if (getIntent().hasExtra("FROM_EDIT")) {
                        for (int i = 0; i < LIST_ALLCONTACTS.size(); i++) {
                            for (StrategyContact _obj : LIST_STRATEGYCONTACT) {
                                if (LIST_ALLCONTACTS.get(i).getId().equals(_obj.getID())) {
                                    LIST_ALLCONTACTS.get(i).setSelected(true);
                                    break;
                                }
                            }
                        }
                    }
                    PB.setVisibility(View.GONE);
                    if (LIST_ALLCONTACTS.size() > 0) {
                        Collections.sort(LIST_ALLCONTACTS);
                        ADAPTER = new StrategyContactAdapter(AddStrategyContactActivity.this, LIST_ALLCONTACTS, false);
                        LST_CONTACTS.setAdapter(ADAPTER);
                    }
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
                            AddStrategyContacts();
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
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {

    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_strategy_contact, menu);
        if (getIntent().hasExtra("FROM_SHARELOC")) {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
        } else {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
        }
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
        } else if (item.getItemId() == R.id.action_Next) {
            String strContactNos = "";
            for (ContactDisplay _obj : LIST_ALLCONTACTS) {
                if (_obj.isSelected()) {
                    if (strContactNos.equals("")) {
                        strContactNos += _obj.getPhone();
                    } else {
                        strContactNos += "," + _obj.getPhone();
                    }
                }
            }
            if (TextUtils.isEmpty(strContactNos)) {
                Intent intent = new Intent(AddStrategyContactActivity.this, SharePositionActivity.class);
                intent.putExtra("CONTACT_NOs", strContactNos);
                intent.putExtra("CURRENT_LOCATION", getIntent().getParcelableExtra("CURRENT_LOCATION"));
                startActivity(intent);
                AddStrategyContactActivity.this.finish();
            } else
                Toast.makeText(AddStrategyContactActivity.this, "Please Select atleast one contact to share", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
