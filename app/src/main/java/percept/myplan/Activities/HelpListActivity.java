package percept.myplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.POJO.Contact;
import percept.myplan.R;
import percept.myplan.adapters.ContactHelpListAdapter;

public class HelpListActivity extends AppCompatActivity {

    private TextView TV_ADDHELPLIST;
    private List<Contact> LIST_HELPCONTACT;
    private RecyclerView LST_HELP;
    private ContactHelpListAdapter ADPT_CONTACTHELPLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.title_activity_help_list));

        TV_ADDHELPLIST = (TextView) findViewById(R.id.tvAddHelpContact);
        LST_HELP = (RecyclerView) findViewById(R.id.lstHelpList);

        LIST_HELPCONTACT = new ArrayList<>();
        LIST_HELPCONTACT.add(new Contact("Children Phone", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Paul", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Mom", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Madelaine", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Kate", "1234567890", false));
        LIST_HELPCONTACT.add(new Contact("Jenna", "1234567890", false));
        ADPT_CONTACTHELPLIST = new ContactHelpListAdapter(LIST_HELPCONTACT);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HelpListActivity.this);
        LST_HELP.setLayoutManager(mLayoutManager);
        LST_HELP.setItemAnimator(new DefaultItemAnimator());
        LST_HELP.setAdapter(ADPT_CONTACTHELPLIST);

        TV_ADDHELPLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(HelpListActivity.this, AddContactActivity.class);
                startActivity(_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_help_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            HelpListActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_add_editHelpList) {
            Intent _intent = new Intent(HelpListActivity.this, AddContactActivity.class);
            startActivity(_intent);
        }
        return false;
    }
}
