package android.ivo.newsapp;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsItemAnimator extends DefaultItemAnimator {
    private static final String TAG = DefaultItemAnimator.class.getSimpleName();
    private boolean mAnimationHasEndend = true;

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        final NewsElementHeadlinesAdapter.ViewHolder holder = (NewsElementHeadlinesAdapter.ViewHolder) oldHolder;

        Animation a = new AlphaAnimation(0, 1);
        a.setInterpolator(new DecelerateInterpolator());
        a.setDuration(1500);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dispatchAnimationFinished(holder);
                mAnimationHasEndend = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View extras = holder.itemView.findViewById(R.id.news_extras);
        extras.setAnimation(a);
        a.start();
        return true;
    }
}
