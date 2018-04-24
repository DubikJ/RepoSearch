package com.gmail.vanyadubik.reposearch.app;


import android.app.Application;


import com.gmail.vanyadubik.reposearch.component.DIComponent;
import com.gmail.vanyadubik.reposearch.component.DaggerDIComponent;
import com.gmail.vanyadubik.reposearch.modules.ActivityUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.DataApiModule;
import com.gmail.vanyadubik.reposearch.modules.ErrorUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.NetworkUtilsApiModule;
import com.gmail.vanyadubik.reposearch.modules.ServiceApiModule;

public class RSApplication extends Application {

    DIComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponent = DaggerDIComponent.builder()
                .dataApiModule(new DataApiModule(this))
                .serviceApiModule(new ServiceApiModule(this))
                .activityUtilsApiModule(new ActivityUtilsApiModule())
                .networkUtilsApiModule(new NetworkUtilsApiModule(this))
                .errorUtilsApiModule(new ErrorUtilsApiModule(this))
                .build();

    }

    public DIComponent getComponent() {
        return diComponent;
    }

}
