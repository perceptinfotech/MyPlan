package percept.myplan.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import percept.myplan.Activities.HopeDetailsActivity;
import percept.myplan.Activities.ImageDetailActivity;
import percept.myplan.POJO.HopeDetail;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3Adapter extends RecyclerView.Adapter<Basic3ViewHolder> {

    private static final String TAG = "Basic3Adapter";

    private List<HopeDetail> LST_HOPE;
    private String HOPE_TITLE;
    private HopeDetailsActivity hopeDetailsActivity;


    public Basic3Adapter() {
        super();
        setHasStableIds(true);  // MUST have this.
    }

    public Basic3Adapter(HopeDetailsActivity hopeDetailsActivity, List<HopeDetail> hopeList, String hopeTitle) {
        this.LST_HOPE = hopeList;
        setHasStableIds(true);  // MUST have this.
        HOPE_TITLE = hopeTitle;
        this.hopeDetailsActivity = hopeDetailsActivity;
    }

    @Override
    public Basic3ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        final Basic3ViewHolder viewHolder;
        if (viewType == Basic3ViewHolder.TYPE_VIDEO) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(Basic3VideoViewHolder.LAYOUT_RES, parent, false);
            viewHolder = new Basic3VideoViewHolder(view);
        } else if (viewType == Basic3ViewHolder.TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(Basic3ImageViewHolder.LAYOUT_RES, parent, false);
            viewHolder = new Basic3ImageViewHolder(view);
        } else if (viewType == Basic3ViewHolder.TYPE_AUDIO) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(Basic3MediaViewHolder.LAYOUT_RES, parent, false);
            viewHolder = new Basic3MediaViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(Basic3NormalViewHolder.LAYOUT_RES, parent, false);
            viewHolder = new Basic3NormalViewHolder(view);
        }

        if (viewHolder instanceof Basic3VideoViewHolder) {
            viewHolder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Do this for for videoView only.
                    if (view == ((Basic3VideoViewHolder) viewHolder).videoView) {
//                        // 1. Temporary disable the playback.
//                        Toro.rest(true);
//                        new AlertDialog.Builder(parent.getContext()).setTitle(R.string.app_name)
//                                .setMessage("Sample")
//                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss(DialogInterface dialogInterface) {
//                                        // 2. Resume the playback.
//                                        Toro.rest(false);
//                                    }
//                                })
//                                .create()
//                                .show();
//                        Snackbar.make(parent, "Clicked to VIDEO", Snackbar.LENGTH_LONG).show();
                    } else if (view == ((Basic3VideoViewHolder) viewHolder).dummyView) {
//                        Snackbar.make(parent, "Clicked to TEXT", Snackbar.LENGTH_LONG).show();
                    } else if (view == ((Basic3VideoViewHolder) viewHolder).tvCardVideoEdit) {
//                        Snackbar.make(parent, "Clicked to Edit", Snackbar.LENGTH_LONG).show();
                        hopeDetailsActivity.editHopeElement((Integer) view.getTag());
                    }

                }
            });

            viewHolder.setOnItemLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Snackbar.make(parent, "Long pressed to VIDEO", Snackbar.LENGTH_LONG).show();
                    return true;
                }
            });
        }

        if (viewHolder instanceof Basic3ImageViewHolder) {
            viewHolder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Do this for for videoView only.
                    if (view == ((Basic3ImageViewHolder) viewHolder).imgCardImage) {
//                        // 1. Temporary disable the playback.
//                        Toro.rest(true);
//                        new AlertDialog.Builder(parent.getContext()).setTitle(R.string.app_name)
//                                .setMessage("Sample")
//                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss(DialogInterface dialogInterface) {
//                                        // 2. Resume the playback.
//                                        Toro.rest(false);
//                                    }
//                                })
//                                .create()
//                                .show();
//                        Snackbar.make(parent, "Clicked to VIDEO", Snackbar.LENGTH_LONG).show();
                        int _pos = (int) ((Basic3ImageViewHolder) viewHolder).imgCardImage.getTag();
                        Intent _intent = new Intent(parent.getContext(), ImageDetailActivity.class);
                        _intent.putExtra("IMG_LINK", LST_HOPE.get(_pos).getMEDIA());
                        _intent.putExtra("TITLE", HOPE_TITLE);
                        parent.getContext().startActivity(_intent);
                    } else if (view == ((Basic3ImageViewHolder) viewHolder).tvCardImage) {
//                        Snackbar.make(parent, "Clicked to TEXT", Snackbar.LENGTH_LONG).show();
                    } else if (view == ((Basic3ImageViewHolder) viewHolder).tvCardImageEdit) {
//                        Snackbar.make(parent, "Clicked to Edit", Snackbar.LENGTH_LONG).show();
                        hopeDetailsActivity.editHopeElement((Integer) view.getTag());
                    }
                }
            });

            viewHolder.setOnItemLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Snackbar.make(parent, "Long pressed to IMAGE", Snackbar.LENGTH_LONG).show();
                    return true;
                }
            });
        }

        if (viewHolder instanceof Basic3VideoViewHolder) {
            viewHolder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == ((Basic3MediaViewHolder) viewHolder).tvCardImageEdit) {
//                        Snackbar.make(parent, "Clicked to Edit", Snackbar.LENGTH_LONG).show();
                        hopeDetailsActivity.editHopeElement((Integer) view.getTag());
                    }
                }
            });
            viewHolder.setOnItemLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Snackbar.make(parent, "Long pressed to MEDIA", Snackbar.LENGTH_LONG).show();
                    return true;
                }
            });
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Basic3ViewHolder holder, int position) {
        holder.bind(this, getItem(position), position);
    }

    Object getItem(int position) {
        if (LST_HOPE.get(position).getTYPE().equals("video")) {
            return new HopeDetail(LST_HOPE.get(position));
        } else if (LST_HOPE.get(position).getTYPE().equals("image")) {
            return new HopeDetail(LST_HOPE.get(position));
        } else if (LST_HOPE.get(position).getTYPE().equals("music")) {
            return new HopeDetail(LST_HOPE.get(position));
        }
        return new HopeDetail(LST_HOPE.get(position));

//        if (position % 3 == 0) {
//            return new SimpleVideoObject("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//        } else {
//            return new SimpleObject();
//        }
    }

    @Override
    public int getItemViewType(int position) {

        if (LST_HOPE.get(position).getTYPE().equals("video")) {
            return Basic3ViewHolder.TYPE_VIDEO;
        } else if (LST_HOPE.get(position).getTYPE().equals("image")) {
            return Basic3ViewHolder.TYPE_IMAGE;
        } else if (LST_HOPE.get(position).getTYPE().equals("music")){
            return Basic3ViewHolder.TYPE_AUDIO;
        }
        return 2;
//        return position % 3 == 0 ? Basic3ViewHolder.TYPE_VIDEO : Basic3ViewHolder.TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return LST_HOPE.size();
    }

    // Toro requires this method to return item's unique Id.
    @Override
    public long getItemId(int position) {
        return position;
    }
}