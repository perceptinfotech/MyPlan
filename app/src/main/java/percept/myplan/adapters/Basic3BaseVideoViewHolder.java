package percept.myplan.adapters;

import android.view.View;

import percept.myplan.media.Cineer;
import percept.myplan.media.PlaybackException;
import percept.myplan.toro.ToroPlayer;
import percept.myplan.toro.ToroPlayerViewHelper;
import percept.myplan.toro.ToroUtil;
import percept.myplan.toro.ToroViewHolder;

/**
 * Created by percept on 5/8/16.
 */

public abstract class Basic3BaseVideoViewHolder extends Basic3ViewHolder
        implements ToroPlayer, ToroViewHolder {

    protected final ToroPlayerViewHelper helper;
    protected boolean isPlayable = false;

    public Basic3BaseVideoViewHolder(View itemView) {
        super(itemView);
        helper = new ToroPlayerViewHelper(this, itemView);
    }

    /* BEGIN: ToroViewHolder callbacks */
    @Override
    public void onAttachedToParent() {
        helper.onAttachedToParent();
    }

    @Override
    public void onDetachedFromParent() {
        helper.onDetachedFromParent();
    }
  /* END: ToroViewHolder callbacks */

    /* BEGIN: ToroPlayer callbacks (partly) */
    @Override
    public void onActivityActive() {

    }

    @Override
    public void onActivityInactive() {

    }

    @Override
    public void onVideoPreparing() {

    }

    @Override
    public void onVideoPrepared(Cineer mp) {
        this.isPlayable = true;
    }

    @Override
    public void onPlaybackStarted() {

    }

    @Override
    public void onPlaybackPaused() {

    }

    @Override
    public void onPlaybackCompleted() {
        this.isPlayable = false;
    }

    @Override
    public boolean onPlaybackError(Cineer mp, PlaybackException error) {
        this.isPlayable = false;
        return true;
    }

    @Override
    public boolean wantsToPlay() {
        return isPlayable && visibleAreaOffset() >= 0.85;
    }

    @Override
    public float visibleAreaOffset() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent());
    }

    @Override
    public boolean isLoopAble() {
        return false;
    }

    @Override
    public int getPlayOrder() {
        return getAdapterPosition();
    }
  /* END: ToroPlayer callbacks (partly) */
}