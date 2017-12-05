package com.wongxd.shopunit.me.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wongxd.shopunit.AtyMainActivity;
import com.wongxd.shopunit.LoginActivity;
import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtyBecomeMerchantSuccessBinding;

public class BecomeMerchantSuccessActivity extends BaseBindingActivity<AtyBecomeMerchantSuccessBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_become_merchant_success);
        AppCompatActivity thisActivity = this;
        Context mContext = this.getApplicationContext();

        boolean isChange = getIntent().getBooleanExtra("isChange", false);

        if (isChange) {
            bindingView.tvTitle.setText("修改成功");
            bindingView.btnRelogin.setText("返回个人中心");
        }

        bindingView.ivReturn.setOnClickListener(v -> {
            if (isChange) {
                startActivity(new Intent(thisActivity, AtyMainActivity.class));
            } else {
                Intent intent = new Intent(thisActivity, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        bindingView.btnRelogin.setOnClickListener(v -> {
            if (isChange) {
                startActivity(new Intent(thisActivity, AtyMainActivity.class));
            } else {
                Intent intent = new Intent(thisActivity, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
