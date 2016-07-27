package percept.myplan.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import percept.myplan.Activities.AddHopeBoxActivity;
import percept.myplan.Activities.CreateQuickMsgActivity;
import percept.myplan.POJO.Hope;
import percept.myplan.R;
import percept.myplan.adapters.HopeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentHopeBox extends Fragment {

    public static final int INDEX = 4;

    private RecyclerView LST_HOPE;
    private HopeAdapter ADAPTER;
    private List<Hope> LIST_HOPE;

    public fragmentHopeBox() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Hope Box");
        View _View = inflater.inflate(R.layout.fragment_hope_box, container, false);
        LST_HOPE = (RecyclerView) _View.findViewById(R.id.recycler_hope);
        LIST_HOPE = new ArrayList<>();
        ADAPTER = new HopeAdapter(getActivity(), LIST_HOPE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        LST_HOPE.setLayoutManager(mLayoutManager);
        LST_HOPE.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        LST_HOPE.setItemAnimator(new DefaultItemAnimator());
        LST_HOPE.setAdapter(ADAPTER);
        prepareAlbums();

        LST_HOPE.addOnItemTouchListener(new CreateQuickMsgActivity.RecyclerTouchListener(getActivity(), LST_HOPE, new CreateQuickMsgActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LIST_HOPE.get(position);
                Toast.makeText(getActivity(), LIST_HOPE.get(position).getTITLE(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getActivity(), AddHopeBoxActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return _View;
    }

    private void prepareAlbums() {


        Hope a = new Hope("1", "http://www.lgrc.us/wp-content/uploads/2014/04/family-silhouette1.png", "Family");
        LIST_HOPE.add(a);

        a = new Hope("2", "http://kingofwallpapers.com/friend/friend-005.jpg", "Friends");
        LIST_HOPE.add(a);

        a = new Hope("3", "http://www.partnersforhealthypets.org/healthypetcheckup/assets/pets-fcb53b73523cd42be71be807ca0d6aaf.jpg", "Pets");
        LIST_HOPE.add(a);

        a = new Hope("4", "", "Add New Box");
        LIST_HOPE.add(a);

        ADAPTER.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private CreateQuickMsgActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CreateQuickMsgActivity.ClickListener clickListener) {
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
