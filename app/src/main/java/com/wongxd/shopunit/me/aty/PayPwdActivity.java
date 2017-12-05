package com.wongxd.shopunit.me.aty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wongxd.shopunit.App;
import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtyPayPwdBinding;
import com.wongxd.shopunit.utils.TU;
import com.wongxd.shopunit.utils.conf.UrlConf;
import com.wongxd.shopunit.utils.net.WNetUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class PayPwdActivity extends BaseBindingActivity<AtyPayPwdBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_pay_pwd);

        bindingView.ivReturn.setOnClickListener(v -> finish());
        bindingView.btnGetCode.setOnClickListener(v->getPhoneCode());

        bindingView.btnSubmit.setOnClickListener(v->doModify());
    }


    private int time = 120;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (time <= 0) {
                time = 120;
                bindingView.btnGetCode.setText("获取验证码");
                bindingView.btnGetCode.setEnabled(true);
            } else {
                bindingView.btnGetCode.setText(time + "秒后重试");
                bindingView.btnGetCode.setEnabled(false);
                time--;
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
            return true;
        }
    });

    private void getPhoneCode() {
        if (time != 120) return;
        mHandler.sendEmptyMessage(1);
        WNetUtil.StringCallBack(OkHttpUtils.post().url(UrlConf.GetModifyPwdCodeUrl).tag(netTag)
                        .addParams("token", App.token)
                , UrlConf.GetCodeUrl, PayPwdActivity.this, "获取验证码中", false, new WNetUtil.WNetStringCallback() {
                    @Override
                    public void success(String response, int id) {
                        Logger.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.optInt("code") != 200) {
                                time = 0;
                            }
                            TU.cT(jsonObject.optString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(Call call, Exception e, int id) {
                        Logger.e(e.getMessage());
                        time = 0;
                    }
                });

    }


    private void doModify() {
        String code = bindingView.etPhoneCode.getText().toString().trim();
        String newPwd = bindingView.etNew.getText().toString().trim();
        String confNewPwd = bindingView.etConfirmNew.getText().toString().trim();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confNewPwd)) {
            TU.cT("本页所有数据为必填");
            return;
        }

        if (!newPwd.equals(confNewPwd)) {
            TU.cT("两次密码输入不一致");
            return;
        }

        WNetUtil.StringCallBack(OkHttpUtils.post().url(UrlConf.ModifyPayPwdUrl).tag(netTag)
                        .addParams("token", App.token)
                        .addParams("payPassword", newPwd)
                        .addParams("payPassword1", confNewPwd)
                        .addParams("codel", code),
                UrlConf.ModifyPayPwdUrl, this, "修改支付密码中", true, new WNetUtil.WNetStringCallback() {
                    @Override
                    public void success(String response, int id) {
                        Logger.e(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.optInt("code") == 200) {
                                TU.cT("修改支付密码成功");
                                finish();
                            } else TU.cT(jsonObject.optString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(Call call, Exception e, int id) {

                    }
                }
        );
    }
}
