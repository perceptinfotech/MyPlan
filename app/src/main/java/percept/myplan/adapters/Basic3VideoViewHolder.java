package percept.myplan.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import percept.myplan.POJO.HopeDetail;
import percept.myplan.R;
import percept.myplan.media.Cineer;
import percept.myplan.player.ExoVideo;

/**
 * Created by percept on 5/8/16.
 */

public class Basic3VideoViewHolder extends Basic3BaseVideoViewHolder {

    // vh_toro_video_basic_3 is the updated version for vh_toro_video_basic, which has an extra
    // TextView to show the selective click event handling.
    public static final int LAYOUT_RES = R.layout.vh_toro_video_basic_3;

    private HopeDetail video;
    private final Cineer.Player videoPlayer;
    /* package */
    final View videoView; // package private so Adapter can judge the clicked View.
    final TextView dummyView, tvCardVideoEdit;


    public Basic3VideoViewHolder(View itemView) {
        super(itemView);
        dummyView = (TextView) itemView.findViewById(R.id.text);
        videoView = itemView.findViewById(R.id.video);
        tvCardVideoEdit = (TextView) itemView.findViewById(R.id.tvCardVideoEdit);
        if (getPlayerView() instanceof Cineer.Player) {
            videoPlayer = (Cineer.Player) getPlayerView();
        } else {
            throw new IllegalArgumentException("Illegal Video player widget. Requires a Cineer.Player");
        }
        // !IMPORTANT: Helper is helpful, don't forget it.
        videoPlayer.setOnPlayerStateChangeListener(helper);
    }

    @Override
    public void bind(RecyclerView.Adapter adapter, Object item, int posi) {
        if (!(item instanceof HopeDetail)) {
            throw new IllegalArgumentException("Invalid Object: " + item);
        }

        this.video = (HopeDetail) item;
        this.videoPlayer.setMedia(new ExoVideo(Uri.parse(this.video.getMEDIA()), this.video.getMEDIA_TITLE()));
        this.dummyView.setText(this.video.getMEDIA_TITLE());
        tvCardVideoEdit.setTag(posi);

    }

    /* BEGIN: ToroPlayer callbacks (partly) */
    @Override
    public void preparePlayer(boolean playWhenReady) {
        this.videoPlayer.preparePlayer(playWhenReady);
    }

    @Override
    public void start() {
        this.videoPlayer.start();
    }

    @Override
    public void pause() {
        this.videoPlayer.pause();
    }

    @Override
    public void stop() {
        this.videoPlayer.stop();
    }

    @Override
    public void releasePlayer() {
        this.videoPlayer.releasePlayer();
    }

    @Override
    public long getDuration() {
        return this.videoPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return this.videoPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long pos) {
        this.videoPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return this.videoPlayer.isPlaying();
    }

    // MEMO: Unique or null
    @Nullable
    @Override
    public String getMediaId() {
        return this.video != null ? this.video.getVIDEO() + "@" + getAdapterPosition() : null;
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return this.videoView;
    }

    @Override
    public boolean isLoopAble() {
        return true;
    }
  /* END: ToroPlayer callbacks (partly) */

    // Interaction setup
    @Override
    public void setOnItemClickListener(View.OnClickListener listener) {
        super.setOnItemClickListener(listener);
        videoView.setOnClickListener(listener);
        // HINT: Un-comment this to enable click event on this TextView.
        dummyView.setOnClickListener(listener);
        tvCardVideoEdit.setOnClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(final View.OnLongClickListener listener) {
        super.setOnItemLongClickListener(listener);
        // Additional support for long-press on Video.
        videoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return listener.onLongClick(view) &&  //
                        helper.onItemLongClick(Basic3VideoViewHolder.this, itemView, itemView.getParent());
            }
        });
    }
}
