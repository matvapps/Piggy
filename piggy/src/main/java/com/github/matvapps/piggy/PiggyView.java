package com.github.matvapps.piggy;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.matvapps.piggy.listeners.OnAddMoney;
import com.github.matvapps.piggy.listeners.OnCryListener;
import com.github.matvapps.piggy.listeners.OnGetMoneyListener;
import com.github.matvapps.piggy.listeners.OnJoyListener;
import com.github.matvapps.piggy.util.AnimationType;
import com.github.matvapps.piggy.util.CustomAnimationDrawable;
import com.github.matvapps.piggyview.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Alexandr.
 */
public class PiggyView extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener, View.OnTouchListener {

    private final String TAG = PiggyView.class.getSimpleName();

    private final int PIGGY_GET_MONEY_WAIT_DURATION = 3000;

    private CustomAnimationDrawable piggyJoyAnimation;
    private CustomAnimationDrawable piggyCryAnimation;
    private CustomAnimationDrawable piggyAddMoneyAnimation;
    private CustomAnimationDrawable piggyGetMoneyAnimation;
    private AnimationDrawable piggyBlinkingAnimation;

    private OnGetMoneyListener onGetMoneyListener;
    private OnJoyListener onJoyListener;
    private OnCryListener onCryListener;
    private OnAddMoney onAddMoney;

    private SensorManager sensorManager;
    private MediaPlayer FXPlayer;


    public PiggyView(@NonNull Context context) {
        super(context);
        initPiggy();
    }

    public PiggyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPiggy();
    }

    public PiggyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPiggy();
    }


    private void initPiggy() {

        this.setBackgroundResource(R.drawable.piggy_pattern);
        FXPlayer = new MediaPlayer();

        startBlinking();

        setSoundEffectsEnabled(false);
        setOnClickListener(this);
        setOnTouchListener(this);

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);

    }

    public void startBlinking() {
        this.setBackgroundResource(R.drawable.piggy_blinking_animation);
        piggyBlinkingAnimation = (AnimationDrawable) this.getBackground();
        piggyBlinkingAnimation.start();
    }

    public void startCry() {
        if (onCryListener != null)
            onCryListener.onAnimationStart();
        if (!isPlayingAnimation()) {
            piggyCryAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_cry_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyCryAnimation.stop();
                    piggyCryAnimation = null;
                    if (onCryListener != null)
                        onCryListener.onAnimationStop();
                    startBlinking();
                }
            };

            this.setBackground(piggyCryAnimation);
            piggyBlinkingAnimation.stop();
            piggyCryAnimation.start();

//            playSound(R.raw.piggy_cry_sound);
        }
    }

    private void playSound(int _id) {
        if (FXPlayer != null) {
            FXPlayer.stop();
            FXPlayer.release();
        }
        FXPlayer = MediaPlayer.create(getContext(), _id);
        if (FXPlayer != null)
            FXPlayer.start();
    }

    public void startJoy() {
        if (onJoyListener != null)
            onJoyListener.onAnimationStart();
        if (!isPlayingAnimation()) {
            piggyJoyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_joy_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyJoyAnimation.stop();
                    piggyJoyAnimation = null;
                    if (onJoyListener != null)
                        onJoyListener.onAnimationStop();
                    startBlinking();
                }
            };
            this.setBackground(piggyJoyAnimation);
            piggyBlinkingAnimation.stop();
            piggyJoyAnimation.start();

//            playSound(R.raw.piggy_joy_sound);

        }
    }

    public void startAddingMoney() {
        if (onAddMoney != null)
            onAddMoney.onAnimationStart();
        if (!isPlayingAnimation()) {
            piggyAddMoneyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_add_money_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyAddMoneyAnimation.stop();
                    piggyAddMoneyAnimation = null;
                    if (onAddMoney != null)
                        onAddMoney.onAnimationStop();
                    startJoy();
                }
            };
            this.setBackground(piggyAddMoneyAnimation);
            piggyBlinkingAnimation.stop();
            piggyAddMoneyAnimation.start();

//            playSound(R.raw.adding_money_sound);

        }
    }

    public void startGetMoney() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(eventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (onGetMoneyListener != null)
            onGetMoneyListener.onStartWaitForDeviceRotate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (onGetMoneyListener != null)
                    if (piggyGetMoneyAnimation == null)
                        onGetMoneyListener.onDeviceRotateNotCompleted();
                sensorManager.unregisterListener(eventListener);
            }
        }, PIGGY_GET_MONEY_WAIT_DURATION);


    }

    private void startGettingMoney() {
        sensorManager.unregisterListener(eventListener);

        if (onGetMoneyListener != null)
            onGetMoneyListener.onAnimationStart();
        if (!isPlayingAnimation()) {
            piggyGetMoneyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.piggy_get_money_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyGetMoneyAnimation.stop();
                    piggyGetMoneyAnimation = null;
                    if (onGetMoneyListener != null)
                        onGetMoneyListener.onAnimationStop();
                    startBlinking();
                }
            };
            this.setBackground(piggyGetMoneyAnimation);
            piggyBlinkingAnimation.stop();
            piggyGetMoneyAnimation.start();
//            playSound(R.raw.getting_money_sound);
        }
    }

    private void stopAnimation(AnimationType animType) {
        switch (animType) {
            case ADDING:
                piggyAddMoneyAnimation.stop();
                break;

            case CRYING:
                piggyCryAnimation.stop();
                break;

            case JOYING:
                piggyJoyAnimation.stop();
                break;

            case GETTING:
                piggyGetMoneyAnimation.stop();
                break;

            case BLINKING:
                piggyBlinkingAnimation.stop();
                break;
        }
    }

    private boolean isPlayingAnimation() {
        return piggyJoyAnimation != null || piggyCryAnimation != null || piggyAddMoneyAnimation != null || piggyGetMoneyAnimation != null;
    }

    @Override
    public void onClick(View view) {
        startCry();
    }

    long then = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            then = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if ((System.currentTimeMillis() - then) > 300) {
                startJoy();
            }
        }
        return false;
    }

    private SensorEventListener eventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if ((event.values[0] > 0 && event.values[0] < 5 || event.values[0] < 0 && event.values[0] > -5)
                    && (event.values[1] < -6 && event.values[1] > -10)) {
                startGettingMoney();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public OnGetMoneyListener getOnGetMoneyListener() {
        return onGetMoneyListener;
    }

    public void setOnGetMoneyListener(OnGetMoneyListener onGetMoneyListener) {
        this.onGetMoneyListener = onGetMoneyListener;
    }

    public OnJoyListener getOnJoyListener() {
        return onJoyListener;
    }

    public void setOnJoyListener(OnJoyListener onJoyListener) {
        this.onJoyListener = onJoyListener;
    }

    public OnCryListener getOnCryListener() {
        return onCryListener;
    }

    public void setOnCryListener(OnCryListener onCryListener) {
        this.onCryListener = onCryListener;
    }

    public OnAddMoney getOnAddMoney() {
        return onAddMoney;
    }

    public void setOnAddMoney(OnAddMoney onAddMoney) {
        this.onAddMoney = onAddMoney;
    }
}
