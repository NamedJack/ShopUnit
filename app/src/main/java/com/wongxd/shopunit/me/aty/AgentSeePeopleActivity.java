package com.wongxd.shopunit.me.aty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ganxin.library.LoadDataLayout;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wongxd.shopunit.App;
import com.wongxd.shopunit.R;
import com.wongxd.shopunit.base.BaseBindingActivity;
import com.wongxd.shopunit.databinding.AtySeePeopleBinding;
import com.wongxd.shopunit.me.bean.AgentSeePeopleBean;
import com.wongxd.shopunit.me.bean.ChargeBean;
import com.wongxd.shopunit.utils.conf.UrlConf;
import com.wongxd.shopunit.utils.glide.GlideCircleTransform;
import com.wongxd.shopunit.utils.net.WNetUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

public class AgentSeePeopleActivity extends BaseBindingActivity<AtySeePeopleBinding> {


    private AppCompatActivity thisActivity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_see_people);
        thisActivity = this;
        mContext = this.getApplicationContext();
        initClick();
        initRecycleView();
        getList(false);
    }

    private void initClick() {
        bindingView.ivReturn.setOnClickListener(v -> finish());
    }

    private ArrayList<ChargeBean.RecordBean.RecordsBean> list;
    private RvAdapter adapter;

    private void initRecycleView() {
        list = new ArrayList<>();
        adapter = new RvAdapter();
        bindingView.rvSeePeople.setAdapter(adapter);
        bindingView.rvSeePeople.setLayoutManager(new LinearLayoutManager(mContext));

        bindingView.srlSeePeople.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getList(false);
            }
        });


        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getList(true);
            }
        }, bindingView.rvSeePeople);

    }

    int pageNo = 1;

    private void getList(boolean isLoadMore) {
        if (!isLoadMore)
            bindingView.clLoaddata.setStatus(LoadDataLayout.LOADING);
        bindingView.clLoaddata.setOnReloadListener(new LoadDataLayout.OnReloadListener() {
            @Override
            public void onReload(View v, int status) {
                getList(isLoadMore);
            }
        });
        Logger.e(App.token);
        String url = UrlConf.AgentSeePeople_URL;
        WNetUtil.StringCallBack(OkHttpUtils.post().url(url)
                        .addParams("token", App.token)
                        .addParams("pageNo", pageNo + "")
                        .addParams("size", "10")
                , url, thisActivity, new WNetUtil.WNetStringCallback() {
                    @Override
                    public void success(String response, int id) {
                        bindingView.srlSeePeople.setRefreshing(false);
                        Logger.e(response);
                        Gson gson = new Gson();
                        AgentSeePeopleBean bean = gson.fromJson(response, AgentSeePeopleBean.class);
                        if (bean.getCode() == 200) {
                            bindingView.clLoaddata.setStatus(LoadDataLayout.SUCCESS);
                            int totalPage = bean.getData().getPages();
                            if (isLoadMore) {
                                if (pageNo > totalPage) {
                                    adapter.loadMoreEnd();
                                } else {
                                    adapter.addData(bean.getData().getRecords());
                                    adapter.loadMoreComplete();
                                }
                            } else {
                                bindingView.srlSeePeople.setRefreshing(false);
                                adapter.setNewData(bean.getData().getRecords());
                                // 检查是否满一屏，如果不满足关闭loadMore
                                adapter.disableLoadMoreIfNotFullPage(bindingView.rvSeePeople);
                            }
                            if (bean.getData().getRecords().size() != 0) {
                                pageNo++;
                            } else if (!isLoadMore)
                                bindingView.clLoaddata.setStatus(LoadDataLayout.EMPTY);
                            bindingView.tvListSize.setText("共 " + adapter.getData().size() + " 个用户");
                        } else {
                            if (isLoadMore) adapter.loadMoreFail();
                            else bindingView.clLoaddata.setStatus(LoadDataLayout.ERROR);
                        }
                    }

                    @Override
                    public void error(Call call, Exception e, int id) {
                        bindingView.srlSeePeople.setRefreshing(false);
                        bindingView.clLoaddata.setStatus(LoadDataLayout.ERROR);
                    }
                });
    }

    class RvAdapter extends BaseQuickAdapter<AgentSeePeopleBean.DataBean.RecordsBean, BaseViewHolder> {
        public RvAdapter() {
            super(R.layout.item_rv_see_people);
        }

        @Override
        protected void convert(BaseViewHolder helper, AgentSeePeopleBean.DataBean.RecordsBean item) {
            ImageView iv = helper.getView(R.id.iv);
            Glide.with(iv.getContext()).load(UrlConf.IMGHOST + item.getUserHead())
                    .placeholder(R.drawable.user_img)
                    .transform(new GlideCircleTransform(iv.getContext())).into(iv);
            helper.setText(R.id.tv_name, item.getName() + "")
                    .setText(R.id.tv_id, "ID:" + item.getUniqueId() + "");

        }


    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（14660689144）输出（"2016年6月16日 17时21分54秒"）
     *
     * @param time
     * @return
     */
    public String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        String times = sdr.format(new Date(lcc));
        return times;
    }
}
