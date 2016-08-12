package percept.myplan.Activities;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import percept.myplan.R;

public class SidaTestActivity extends AppCompatActivity {

    private TextView TV_TEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sida_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.sidasfulltest));

        TV_TEST = (TextView) findViewById(R.id.tvQuestions);

        InfoDialog _dialog = new InfoDialog(SidaTestActivity.this);
        _dialog.setCanceledOnTouchOutside(true);
        _dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SidaTestActivity.this.finish();
            return true;
        }
        return false;
    }

    public class InfoDialog extends Dialog {

        private TextView TV_NOTE;
        private String NOTE;
        private Context CONTEXT;

        public InfoDialog(Context context) {
            super(context, R.style.DialogTheme);
            CONTEXT = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.lay_info);

            TV_NOTE = (TextView) findViewById(R.id.tvNote);
            TV_NOTE.setText(NOTE);
        }
    }
}
