package com.angcyo.xzproducems.iview

import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView

/**
 * Created by angcyo on 2017-08-02.
 */
class AboutMeUIView : BaseItemUIView() {

    override fun getTitleString(): String {
        return "关于我们"
    }

    override fun getItemLayoutId(position: Int): Int {
        return R.layout.view_about_me_layout
    }

    override fun createItems(items: MutableList<SingleItem>) {
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                holder.click(R.id.email_view) {
                    RUtils.emailTo(mActivity, "xcz102@163.com")
                }
                holder.click(R.id.phone_view) {
                    RUtils.callTo("133184022322")
                }
            }
        })
    }
}
