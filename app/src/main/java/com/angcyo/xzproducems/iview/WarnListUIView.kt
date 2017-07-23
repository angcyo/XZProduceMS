package com.angcyo.xzproducems.iview

import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.recycler.adapter.RExBaseAdapter
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseRecycleUIView
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-23.
 */
class WarnListUIView : BaseRecycleUIView<String, String, String>() {
    override fun createAdapter(): RExBaseAdapter<String, String, String> {
        return object : RExBaseAdapter<String, String, String>(mActivity) {
            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.item_single_text
            }

            override fun onBindDataView(holder: RBaseViewHolder?, posInData: Int, dataBean: String?) {
                super.onBindDataView(holder, posInData, dataBean)
                dataBean?.let {
                    holder?.tv(R.id.text_view)?.text = it
                }
            }

        }
    }

    override fun onUILoadData(page: Int) {
        super.onUILoadData(page)
        //提醒
        Rx.base(object : RFunc<MutableList<String>>() {
            override fun onFuncCall(): MutableList<String> {
                return DbUtil.UP_WARN_PUR01()
            }
        }, object : RSubscriber<MutableList<String>>() {

            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                super.onEnd(isError, isNoNetwork, e)
                hideLoadView()
                if (isError) {
                    showNonetLayout()
                }
            }

            override fun onSucceed(bean: MutableList<String>?) {
                super.onSucceed(bean)
                onUILoadFinish(bean)
            }
        })
    }
}
