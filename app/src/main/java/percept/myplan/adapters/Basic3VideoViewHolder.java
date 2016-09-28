package percept.myplan.adapters;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import im.ene.lab.flvv.FloppyVideoView;
import im.ene.lab.flvv.ScaleType;
import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3VideoViewHolder extends Basic3ViewHolder {

    // vh_toro_video_basic_3 is the updated version for vh_toro_video_basic, which has an extra
    // TextView to show the selective click event handling.
    public static final int LAYOUT_RES = R.layout.vh_toro_video_basic_3;
    final TextView dummyView, tvCardVideoEdit;
    final ScaleType[] SCALE_TYPES = {
            ScaleType.FIT_CENTER, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_END,
            ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE
    };
    int scaleTypeIndex = 0;
    int videoIndex = 0;
    private HopeDetail video;
    public FloppyVideoView videoView;

    public Basic3VideoViewHolder(View itemView) {
        super(itemView);

        dummyView = (TextView) itemView.findViewById(R.id.text);
        videoView = (FloppyVideoView) itemView.findViewById(R.id.video);
        tvCardVideoEdit = (TextView) itemView.findViewById(R.id.tvCardVideoEdit);

    }

    @Override
    public void bind(RecyclerView.Adapter adapter, Object item, int position) {
        if (!(item instanceof HopeDetail)) {
            throw new IllegalArgumentException("Invalid Object: " + item);
        }

        this.video = (HopeDetail) item;
        String url = this.video.getMEDIA();
//        videoView.setScaleType(SCALE_TYPES[scaleTypeIndex % SCALE_TYPES.length]);
        videoView.setVideoPath(url);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (videoView != null) {
                    videoView.start();
                    mp.setLooping(true);
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }


}
