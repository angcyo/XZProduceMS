package com.angcyo.xzproducems.iview

import android.support.design.widget.TextInputLayout
import android.util.ArrayMap
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.view.DelayClick
import com.angcyo.uiview.widget.ExEditText
import com.angcyo.xzproducems.BuildConfig
import com.angcyo.xzproducems.LoginControl
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView
import com.angcyo.xzproducems.utils.DbUtil

/**
 * Created by angcyo on 2017-07-24.
 */
class AddOrderUIView : BaseItemUIView() {

    private val editMap = ArrayMap<Int, ExEditText>()

    override fun getTitleString(): String {
        return "添加订单"
    }

    override fun getItemLayoutId(position: Int): Int {
        return when (position) {
            16 -> R.layout.item_single_button
            else -> 0
        }
    }

    override fun createItemView(parent: ViewGroup?, position: Int): View {
        val inputLayout = TextInputLayout(mActivity)
        inputLayout.id = R.id.input_layout

        val editText = ExEditText(mActivity)
        editText.apply {
            id = R.id.edit_text
            setSingleLine(true)
            maxLines = 1

            when (position) {
                1, 9, 10, 11, 12, 13, 14, 15 -> setIsPhone(true, 100)
            }

            if (BuildConfig.DEBUG) {
                setInputText("111")
            }
        }

        when (position) {
            0 -> editText.hint = "订单号"
            1 -> editText.hint = "工序"
            2 -> editText.hint = "物料编码"
            3 -> editText.hint = "名称"
            4 -> editText.hint = "规格"
            5 -> editText.hint = "PNAME3"
            6 -> editText.hint = "PNAME4"
            7 -> editText.hint = "PNAME5"
            8 -> editText.hint = "PNAME6"
            9 -> editText.hint = "订单投产数量"
            10 -> editText.hint = "已完工数量"
            11 -> editText.hint = "返工数量"
            12 -> editText.hint = "当前投产数量即为还有多少未生产数量"
            13 -> editText.hint = "订单数量"
            14 -> editText.hint = "QTY6"
            15 -> editText.hint = "QTY7"
        }

        inputLayout.addView(editText, LinearLayout.LayoutParams(-1, -2))
        inputLayout.layoutParams = ViewGroup.LayoutParams(-1, -2)

        val padding = getDimensionPixelOffset(R.dimen.base_xhdpi)
        if (position == 0) {
            inputLayout.setPadding(padding, padding, padding, 0)
        } else {
            inputLayout.setPadding(padding, 0, padding, 0)
        }
        return inputLayout
    }

    override fun createItems(items: MutableList<SingleItem>) {
        for (i in 0..15) {
            items?.add(object : SingleItem() {
                override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                    editMap.put(posInData, holder.exV(R.id.edit_text))
                }
            })
        }

        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                holder.tv(R.id.button_view).text = "添加"
                holder.delayClick(R.id.button_view, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        Rx.base(object : RFunc<Boolean>() {
                            override fun onFuncCall(): Boolean {
                                return DbUtil.UP_MODI_PUR01_D1(
                                        editMap[0]!!.string(),
                                        editMap[1]!!.string(),
                                        editMap[2]!!.string(),
                                        editMap[3]!!.string(),
                                        editMap[4]!!.string(),
                                        editMap[5]!!.string(),
                                        editMap[6]!!.string(),
                                        editMap[7]!!.string(),
                                        editMap[8]!!.string(),
                                        editMap[9]!!.string(),
                                        editMap[10]!!.string(),
                                        editMap[11]!!.string(),
                                        editMap[12]!!.string(),
                                        editMap[13]!!.string(),
                                        editMap[14]!!.string(),
                                        editMap[15]!!.string(),
                                        LoginControl.userid
                                )
                            }
                        }, object : RSubscriber<Boolean>() {

                            override fun onStart() {
                                super.onStart()
                                UILoading.show2(mILayout).setLoadingTipText("操作中...")
                            }

                            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                                super.onEnd(isError, isNoNetwork, e)
                                UILoading.hide()
                            }

                            override fun onSucceed(bean: Boolean) {
                                super.onSucceed(bean)
                                if (bean) {
                                    T_.ok("添加成功.")
                                } else {
                                    T_.error("添加失败.")
                                }
                            }
                        })
                    }
                })
            }
        })
    }

}
