package com.angcyo.xzproducems

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import com.angcyo.library.utils.L
import com.angcyo.uiview.RCrashHandler
import com.angcyo.uiview.base.UIBaseView
import com.angcyo.uiview.base.UILayoutActivity
import com.angcyo.xzproducems.iview.LoginUIView

class MainActivity : UILayoutActivity() {

    override fun initScreenOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun onLoadView(intent: Intent) {
        checkPermissions()
        startIView(LoginUIView().setEnableClipMode(UIBaseView.ClipMode.CLIP_START))

        RCrashHandler.checkCrash(mLayout)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        L.e("call: onConfigurationChanged -> $newConfig")
    }
}
