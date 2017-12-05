package com.wongxd.shopunit.me.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wongxd.shopunit.AtyMainActivity;
import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtyChangeSuccessBinding;

public class ChangeSuccessActivity extends BaseBindingActivity<AtyChangeSuccessBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_change_success);
        AppCompatActivity thisActivity = this;
        Context mContext = this.getApplicationContext();

        bindingView.ivReturn.setOnClickListener(v -> finish());

        bindingView.btnReturnPersonCenter.setOnClickListener(v->startActivity(new Intent(thisActivity, AtyMainActivity.class)));
    }
}
