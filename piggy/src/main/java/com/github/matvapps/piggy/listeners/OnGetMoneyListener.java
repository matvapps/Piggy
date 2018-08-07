package com.github.matvapps.piggy.listeners;

/**
 * Created by Alexandr.
 */
public interface OnGetMoneyListener extends BaseListener {
    void onStartWaitForDeviceRotate();
    void onDeviceRotateNotCompleted();
}
