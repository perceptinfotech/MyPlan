package percept.myplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import percept.myplan.R;


public class AddStrategyLinksActivity extends AppCompatActivity {

    private Button BTN_INSERTLINK;
    private EditText EDT_LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strategy_links);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.addlink));

        BTN_INSERTLINK = (Button) findViewById(R.id.btnInsertLink);
        EDT_LINK = (EditText) findViewById(R.id.edtLink);

        BTN_INSERTLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("LINK", EDT_LINK.getText().toString().trim());
                setResult(Activity.RESULT_OK, returnIntent);
                AddStrategyLinksActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_strategy_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AddStrategyLinksActivity.this.finish();
            return true;
        } else if (item.getItemId() == R.id.action_AddStrategyLink) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("LINK", EDT_LINK.getText().toString().trim());
            setResult(Activity.RESULT_OK, returnIntent);
            AddStrategyLinksActivity.this.finish();
            return true;
        }
        return false;
    }
}
