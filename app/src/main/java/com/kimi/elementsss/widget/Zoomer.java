
package com.kimi.elementsss.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Handles interpolating between arbitrary zoom levels.
 */
class Zoomer {
    /**
     * The interpolator to use
     */
    @NonNull
    private final Interpolator mInterpolator;

    /**
     * The animation duration in milliseconds
     */
    private final int mAnimationDuration;

    /**
     * Whether the current operation has finished
     */
    private boolean mZoomInProgress;

    /**
     * The start time of the current operation
     */
    private long mStartTime;

    /**
     * The starting zoom level
     */
    private float mStartZoom;

    /**
     * The target zoom level
     */
    private float mTargetZoom;

    /**
     * The current zoom level
     */
    private float mCurrentZoom;

    /**
     * @param context The Context
     */
    Zoomer(@NonNull Context context) {
        mInterpolator = new DecelerateInterpolator();
        mAnimationDuration = context.getResources()
                .getInteger(android.R.integer.config_shortAnimTime);
    }

    /**
     * Start a zoom operation.
     *
     * @param currentZoom The current zoom level
     * @param targetZoom  The target zoom level
     */
    void startZoom(float currentZoom, float targetZoom) {
        mStartTime = SystemClock.elapsedRealtime();
        mStartZoom = mCurrentZoom = currentZoom;
        mTargetZoom = targetZoom;

        mZoomInProgress = true;
    }

    /**
     * Stop the current operation
     */
    void forceFinished() {
        mZoomInProgress = false;
    }

    /**
     * Check whether a zoom operation has finished.
     *
     * @return Whether the zoom operation has finished
     */
    boolean isFinished() {
        return !mZoomInProgress;
    }

    /**
     * Compute the current zoom level.
     *
     * @return Whether a zoom operation is in progress
     */
    boolean computeZoom() {
        if(!mZoomInProgress) {
            return false;
        }

        final long timeElapsed = SystemClock.elapsedRealtime() - mStartTime;
        if(timeElapsed >= mAnimationDuration) {
            mZoomInProgress = false;
            mCurrentZoom = mTargetZoom;
            return true;
        }

        final float progress = timeElapsed * 1f / mAnimationDuration;
        mCurrentZoom =
                mStartZoom + (mTargetZoom - mStartZoom) * mInterpolator.getInterpolation(progress);
        return true;
    }

    /**
     * Get the current zoom level.
     *
     * @return The current zoom level
     */
    float getCurrZoom() {
        return mCurrentZoom;
    }
}
