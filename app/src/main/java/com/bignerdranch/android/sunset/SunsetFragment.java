package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean mHasSet;
    private AnimatorSet animatorSet;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mHasSet = false;

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHasSet)
                    startSunsetAnimation();
                else
                    startSunriseAnimation();
            }
        });

        return view;
    }

    private void startSunriseAnimation() {
        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSkyView.getHeight() / 2 - mSunView.getHeight() / 2; // Middle of screen

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "Y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .after(nightSkyAnimator);

        animatorSet.start();
        mHasSet = !mHasSet;
    }

    private void startSunsetAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "Y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);

        animatorSet.start();
        mHasSet = !mHasSet;
    }
}
