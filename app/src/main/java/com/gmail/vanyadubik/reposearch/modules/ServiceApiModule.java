package com.gmail.vanyadubik.reposearch.modules;


import android.app.Application;

import com.gmail.vanyadubik.reposearch.service.sync.SyncService;
import com.gmail.vanyadubik.reposearch.service.sync.SyncServiceFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ServiceApiModule {

    private Application application;

    public ServiceApiModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public SyncService getSyncService() {

        return SyncServiceFactory.createService(
                SyncService.class,
                application.getBaseContext());
    }
}
