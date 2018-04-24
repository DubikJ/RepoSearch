package com.gmail.vanyadubik.reposearch.modules;


import android.app.Application;

import com.gmail.vanyadubik.reposearch.utils.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkUtilsApiModule {

    private Application application;

    public NetworkUtilsApiModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public NetworkUtils getNetworkUtils() {
        return new NetworkUtils(application.getBaseContext());
    }
}
