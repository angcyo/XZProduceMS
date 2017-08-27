package com.angcyo.xzproducems

import com.angcyo.uiview.RApplication
import com.angcyo.uiview.Root
import com.angcyo.xzproducems.utils.Jtds

/**
 * Created by angcyo on 2017-07-23.
 */
class App : RApplication() {
    override fun onInit() {
        super.onInit()
        Root.APP_FOLDER = "XZProduceMS"
        Jtds.init("112.29.171.138:21006", "bssjk", "xzsoft", "xzsoft")
    }
}
