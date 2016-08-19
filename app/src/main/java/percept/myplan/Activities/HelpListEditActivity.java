package percept.myplan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import percept.myplan.Global.Constant;
import percept.myplan.Global.General;
import percept.myplan.Interfaces.VolleyResponseListener;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;
import percept.myplan.fragments.fragmentContacts;

import static percept.myplan.fragments.fragmentContacts.HELP_CONTACT_NAME;
import static percept.myplan.fragments.fragmentContacts.LIST_HELPCONTACTS;

public class HelpListEditActivity extends AppCompatActivity {

    private TextView TV_ADDHELPLIST;
    private RecyclerView LST_HELP;
    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;
    private ProgressBar PB;
    private CoordinatorLayout REL_COORDINATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_list_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_help_list));

        TV_ADDHELPLIST = (TextView) findViewById(R.id.tvAddHelpContact);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);
        PB = (ProgressBar) findViewById(R.id.pbHelpListEdit);

        REL_COORDINATE = (CoordinatorLayout) findViewById(R.id.snakeBar);

        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACTS, "HELP");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HelpListEditActivity.this);
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

        TV_ADDHELPLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HelpListEditActivity.this, AddContactActivity.class);
                _intent.putExtra("ADD_TO_HELP", "true");
                startActivity(_intent);
                if (getIntent().hasExtra("FROM_HELP")) {
                    HelpListEditActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentContacts.GET_CONTACTS) {
            GetContacts();
        }
    }

    private void GetContacts() {
        try {
            PB.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<String, String>();
            params.put("sid", Constant.SID);
            params.put("sname", Constant.SNAME);
            LIST_HELPCONTACTS.clear();
            HELP_CONTACT_NAME.clear();
            new General().getJSONContentFromInternetService(HelpListEditActivity.this, General.PHPServices.GET_CONTACTS, params, true, false, true, new VolleyResponseListener() {
                @Override
                public void onError(VolleyError message) {
                    PB.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    List<ContactDisplay> _LSTALL = new ArrayList<ContactDisplay>();
                    try {
                        _LSTALL = gson.fromJson(response.getJSONArray(Constant.DATA)
                                .toString(), new TypeToken<List<ContactDisplay>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (ContactDisplay _obj : _LSTALL) {
                        if (_obj.getHelplist().equals("0")) {
//                                CONTACT_NAME.put(_obj.getId(), _obj.getFirst_name());
//                                LIST_CONTACTS.add(_obj);
                        } else {
                            HELP_CONTACT_NAME.put(_obj.getId(), _obj.getFirst_name());
                            LIST_HELPCONTACTS.add(_obj);
                        }
                    }
                    PB.setVisibility(View.GONE);

                    ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACTS, "HELP");
                    LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);
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
                            GetContacts();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_help_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HelpListEditActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_add_editHelpList) {
            Intent _intent = new Intent(HelpListEditActivity.this, AddContactActivity.class);
            _intent.putExtra("ADD_TO_HELP", "true");
            startActivity(_intent);
        }
        return false;
    }
}
