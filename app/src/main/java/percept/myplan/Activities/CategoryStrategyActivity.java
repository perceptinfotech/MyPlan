package percept.myplan.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.POJO.InspirationCategory;
import percept.myplan.R;

public class CategoryStrategyActivity extends AppCompatActivity {

    private RecyclerView LST_CATEGORY_STRATEGY;
    private List<InspirationCategory> LIST_CATEGORY_STRATEGY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_strategy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        if (getIntent().hasExtra("CATEGORY_NAME"))
            mTitle.setText(getIntent().getExtras().getString("CATEGORY_NAME"));
        LST_CATEGORY_STRATEGY = (RecyclerView) findViewById(R.id.lstCategoryStrategy);
        LIST_CATEGORY_STRATEGY = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CategoryStrategyActivity.this);
        LST_CATEGORY_STRATEGY.setLayoutManager(mLayoutManager);
        LST_CATEGORY_STRATEGY.setItemAnimator(new DefaultItemAnimator());
        LST_CATEGORY_STRATEGY.addOnItemTouchListener(new RecyclerTouchListener(CategoryStrategyActivity.this, LST_CATEGORY_STRATEGY, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_CATEGORY_STRATEGY.get(position);
                Intent _intent = new Intent(CategoryStrategyActivity.this, SymptomDetailsActivity.class);
                _intent.putExtra("SYMPTOM_ID", LIST_CATEGORY_STRATEGY.get(position).getCategoryId());
                startActivity(_intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
