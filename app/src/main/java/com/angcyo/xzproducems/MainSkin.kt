package com.angcyo.xzproducems

import android.content.Context
import com.angcyo.uiview.resources.ResUtil
import com.angcyo.uiview.skin.BaseSkin

/**
 * Created by angcyo on 2017-07-23.
 */
class MainSkin(context: Context) : BaseSkin(context) {
    override fun getThemeColor(): Int {
        return ResUtil.getThemeColor(mContext, "colorPrimary")
    }

    override fun getThemeSubColor(): Int {
        return ResUtil.getThemeColor(mContext, "colorPrimaryDark")
    }

    override fun getThemeDarkColor(): Int {
        return ResUtil.getThemeColor(mContext, "colorPrimaryDark")
    }
}
