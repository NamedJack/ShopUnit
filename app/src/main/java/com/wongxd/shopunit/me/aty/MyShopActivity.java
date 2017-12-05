package com.wongxd.shopunit.me.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtyMyshopBinding;
import com.wongxd.shopunit.me.bean.UserDataBean;

public class MyShopActivity extends BaseBindingActivity<AtyMyshopBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_myshop);

        AppCompatActivity thisActivity = this;
        Context mContext = this.getApplicationContext();

        Bundle b = getIntent().getExtras();
        UserDataBean.EntityshopBean entityshopBean = (UserDataBean.EntityshopBean) b.getSerializable("shopInfo");


        bindingView.ivReturn.setOnClickListener(v -> finish());
        bindingView.rlShopInfo.setOnClickListener(v -> {
            Intent intent = new Intent(thisActivity, ShopInfoActivity.class);
            intent.putExtra("shopInfo", entityshopBean);
            startActivity(intent);
        });
        bindingView.rlShopImg.setOnClickListener(v -> {
            Intent intent = new Intent(thisActivity, ShopImgActivity.class);
            intent.putExtra("shopInfo", entityshopBean);
            startActivity(intent);
        });
    }
}
