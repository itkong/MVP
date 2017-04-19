package com.example.rxretrofitdaggermvp.injector.module;

import android.content.Context;

import com.example.rxretrofitdaggermvp.injector.scope.PerApplication;
import com.example.rxretrofitdaggermvp.manager.AppManager;
import com.example.rxretrofitdaggermvp.manager.HttpManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MrKong on 2017/4/1.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    /**
     * 提供全局单例context
     * @return
     */
    @Provides
    @PerApplication
    Context provideContext() {
        return context;
    }

    /**
     * 提供全局AppManager单例
     * @return
     */
    @Provides
    @PerApplication
    AppManager provideAppManager() {
        return new AppManager(context);
    }

    /**
     * 提供全局单例HttpManager
     * @return
     */
    @Provides
    @PerApplication
    HttpManager provideHttpManager() {
        return new HttpManager();
    }
}