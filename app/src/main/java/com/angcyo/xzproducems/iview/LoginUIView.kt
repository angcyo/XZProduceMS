package com.angcyo.xzproducems.iview

import android.view.View
import com.angcyo.library.utils.L
import com.angcyo.uiview.base.Item
import com.angcyo.uiview.base.SingleItem
import com.angcyo.uiview.dialog.UILoading
import com.angcyo.uiview.model.TitleBarPattern
import com.angcyo.uiview.net.RException
import com.angcyo.uiview.net.RFunc
import com.angcyo.uiview.net.RSubscriber
import com.angcyo.uiview.net.Rx
import com.angcyo.uiview.recycler.RBaseViewHolder
import com.angcyo.uiview.utils.RUtils
import com.angcyo.uiview.utils.T_
import com.angcyo.uiview.view.DelayClick
import com.angcyo.uiview.widget.ExEditText
import com.angcyo.xzproducems.BuildConfig
import com.angcyo.xzproducems.LoginControl
import com.angcyo.xzproducems.R
import com.angcyo.xzproducems.base.BaseItemUIView
import com.angcyo.xzproducems.bean.LoginBean
import com.angcyo.xzproducems.utils.DbUtil
import com.orhanobut.hawk.Hawk

/**
 * Created by angcyo on 2017-07-23.
 */
class LoginUIView : BaseItemUIView() {

    override fun getTitleBar(): TitleBarPattern {
        return super.getTitleBar()
    }

    override fun getItemLayoutId(position: Int): Int {
        return R.layout.view_login_layout
    }

    override fun initOnShowContentLayout() {
        super.initOnShowContentLayout()
        LoginControl.reset()
    }

    override fun createItems(items: MutableList<SingleItem>?) {
        items?.add(object : SingleItem() {
            override fun onBindView(holder: RBaseViewHolder, posInData: Int, dataBean: Item?) {
                val nameView: ExEditText = holder.v(R.id.name_text)
                val pwView: ExEditText = holder.v(R.id.pw_text)

                if (BuildConfig.DEBUG) {
                    nameView.setInputText("admin")
                    pwView.setInputText("666666")
                }

                val lastName = Hawk.get<String>("last_name", "")
                if (!lastName.isNullOrEmpty()) {
                    nameView.setInputText(lastName)
                }

                holder.delayClick(R.id.login_button, object : DelayClick() {
                    override fun onRClick(view: View?) {
                        if (BuildConfig.DEBUG) {
                            DbUtil.demo()
                            L.e("call: demo -> ${RUtils.getScreenInches(mActivity)}")
                        }

                        if (nameView.checkEmpty() || pwView.checkEmpty()) {
                            return
                        }

                        Rx.base(object : RFunc<LoginBean>() {
                            override fun onFuncCall(): LoginBean {
                                return DbUtil.login(nameView.string(), pwView.string())
                            }

                        }, object : RSubscriber<LoginBean>() {
                            override fun onStart() {
                                super.onStart()
                                UILoading.show2(mILayout).setLoadingTipText("登录中...")
                            }

                            override fun onSucceed(bean: LoginBean) {
                                super.onSucceed(bean)
                                L.e("call: onSucceed -> $bean")
                                if (bean.GXID == -10_000) {
                                    T_.error("账号不存在.")
                                } else if (bean.GXID == 0) {
                                    T_.error("账户密码不匹配")
                                } else if (bean.GXID > 0) {
                                    //T_.info("登录成功")
                                    Hawk.put<String>("last_name", nameView.string())
                                    LoginControl.userid = nameView.string()
                                    LoginControl.loginBean = bean
                                    replaceIView(MainUIView(bean))
                                } else {
                                    T_.error("登录失败")
                                }
                            }

                            override fun onEnd(isError: Boolean, isNoNetwork: Boolean, e: RException?) {
                                super.onEnd(isError, isNoNetwork, e)
                                UILoading.hide()
                            }
                        })
                    }
                })
            }
        })
    }
}
