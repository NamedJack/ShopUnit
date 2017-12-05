package com.wongxd.shopunit.me.aty;

import android.os.Bundle;

import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtyPerformanceStatisticsBinding;

/**
 * 业绩统计
 */
public class PerformanceStatisticsActivity extends BaseBindingActivity<AtyPerformanceStatisticsBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_performance_statistics);

        bindingView.ivReturn.setOnClickListener(v -> finish());
    }
}
