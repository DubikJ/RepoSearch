package com.gmail.vanyadubik.reposearch.component;
import com.gmail.vanyadubik.reposearch.activity.MainActivity;
import com.gmail.vanyadubik.reposearch.modules.ActivityUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.DataApiModule;
import com.gmail.vanyadubik.reposearch.modules.ErrorUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.NetworkUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.ServiceApiModule;
import com.gmail.vanyadubik.reposearch.service.sync.SyncIntentService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataApiModule.class,
        ServiceApiModule.class, ActivityUtilsApiModule.class,
        NetworkUtilsApiModule.class, ErrorUtilsApiModule.class})
public interface DIComponent {
    void inject(MainActivity mainActivity);
    void inject(SyncIntentService syncIntentService);
}
