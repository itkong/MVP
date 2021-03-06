package com.example.rxretrofitdaggermvp.ui.activities;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.example.rxretrofitdaggermvp.R;
import com.example.rxretrofitdaggermvp.mvp.contract.MainContract;
import com.example.rxretrofitdaggermvp.mvp.module.entity.TabItem;
import com.example.rxretrofitdaggermvp.mvp.presenter.impl.MainPresenterImpl;
import com.example.rxretrofitdaggermvp.ui.activities.base.BaseActivity;
import com.example.rxretrofitdaggermvp.utils.ForTestCommonComponent;
import com.example.rxretrofitdaggermvp.utils.MyClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity implements MainContract.View {

    @Bind(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @Bind(android.R.id.tabs)
    TabWidget tabs;
    @Bind(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Inject
    MainPresenterImpl mainPresenterImpl;
    private QBadgeView qBadgeView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        basePresenter = mainPresenterImpl;
    }

    @Override
    protected void initInjector() {
        activityComponent.inject(this);
    }

    @Override
    public void initTabHost(final List<TabItem> tabItems) {
        tabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabhost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < tabItems.size(); i++) {
            TabItem tabItem = tabItems.get(i);
            TabHost.TabSpec tabSpec = tabhost.newTabSpec(tabItem.getTitleString()).setIndicator(tabItem.getView(this));
            tabhost.addTab(tabSpec, tabItem.getFragmentClass(), null);
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.main_bottom_bg));
        }
        tabItems.get(0).setChecked(true);

        tabhost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toast.setText("点击啦。");
//                toast.show();
                new ForTestCommonComponent().showToast("点击啦。");
            }
        });

        View view = tabhost.getTabWidget().getChildTabViewAt(0);
        new MyClickListener(2, view)
                .setOnClickListener(new MyClickListener.OnClickListener() {
                    @Override
                    public void onSinleClick() {
                        tabhost.setCurrentTab(0);
                    }

                    @Override
                    public void onMultiClick() {
                        EventBus.getDefault().post(new EventBus());
                    }
                });

        qBadgeView = new QBadgeView(this);
        qBadgeView
                .bindTarget(tabItems.get(3).getView(this))
                .setBadgePadding(3, true)
                .setGravityOffset(8, true)
                .setBadgeNumber(100)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        switch (dragState) {
                            case STATE_SUCCEED:
                                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CANCELED:
                                Toast.makeText(MainActivity.this, "cancle", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                for (int i = 0; i < tabItems.size(); i++) {
                    TabItem tabitem = tabItems.get(i);
                    if (s.equals(tabitem.getTitleString())) {
                        tabitem.setChecked(true);
                        if (i == 3) {
                            qBadgeView.hide(true);
                        }
                    } else {
                        tabitem.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    protected boolean isSetStatusBar() {
        return true;
    }
}
