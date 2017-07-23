package com.angcyo.xzproducems

import android.content.Intent
import com.angcyo.uiview.base.UIBaseView
import com.angcyo.uiview.base.UILayoutActivity
import com.angcyo.xzproducems.iview.LoginUIView

class MainActivity : UILayoutActivity() {

    override fun onLoadView(intent: Intent) {
        checkPermissions()
        startIView(LoginUIView().setEnableClipMode(UIBaseView.ClipMode.CLIP_START))
    }
}
