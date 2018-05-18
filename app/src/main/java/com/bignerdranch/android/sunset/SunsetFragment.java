package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mReflectionView;
    private View mSkyView;
    private View mSea;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean mHasSet;
    // private AnimatorSet animatorSet;
    private AnimatorSet pulsate;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mReflectionView = view.findViewById(R.id.reflection);
        mSkyView = view.findViewById(R.id.sky);
        mSea = view.findViewById(R.id.sea);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mHasSet = false;

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHasSet) {
                    startSunsetAnimation();
                    pulsate.end();
                    mSunView.setScaleX(1.0f);
                    mSunView.setScaleY(1.0f);
                    mReflectionView.setScaleX(1.0f);
                    mReflectionView.setScaleY(1.0f);
                }
                else {
                    startSunriseAnimation();
                    pulsate.start();
                }
            }
        });

        setupPulsateAnimator();
        pulsate.start();

        return view;
    }

    private void setupPulsateAnimator() {
        ObjectAnimator pulsateSun = ObjectAnimator.ofPropertyValuesHolder(mSunView,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.1f)
                , PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.1f))
                .setDuration(2000);
        pulsateSun.setRepeatCount(ObjectAnimator.INFINITE);
        pulsateSun.setRepeatMode(ValueAnimator.REVERSE);
        pulsateSun.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator pulsateReflection = ObjectAnimator.ofPropertyValuesHolder(mReflectionView,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.1f)
                , PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.1f))
                .setDuration(2000);
        pulsateReflection.setRepeatCount(ObjectAnimator.INFINITE);
        pulsateReflection.setRepeatMode(ValueAnimator.REVERSE);
        pulsateReflection.setInterpolator(new AccelerateDecelerateInterpolator());

        pulsate = new AnimatorSet();
        pulsate.play(pulsateSun).with(pulsateReflection);
    }

    private void startSunriseAnimation() {
        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSkyView.getHeight() / 2 - mSunView.getHeight() / 2; // Middle of screen
        float reflectionYStart = 0 - mReflectionView.getHeight();
        float reflectionYEnd =  mSea.getHeight() / 2 - mReflectionView.getHeight() / 2;

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "Y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator heightRAnimator = ObjectAnimator
                .ofFloat(mReflectionView, "Y", reflectionYStart, reflectionYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(heightRAnimator)
                .with(sunsetSkyAnimator)
                .after(nightSkyAnimator);

        animatorSet.start();
        mHasSet = !mHasSet;
    }

    private void startSunsetAnimation() {
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();
        float reflectionYStart = mReflectionView.getTop();
        float reflectionYEnd = 0 - mReflectionView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "Y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator heightRAnimator = ObjectAnimator
                .ofFloat(mReflectionView, "Y", reflectionYStart, reflectionYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(heightRAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);

        animatorSet.start();
        mHasSet = !mHasSet;
    }
}
