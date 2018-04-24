package com.gmail.vanyadubik.reposearch.modules;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import com.gmail.vanyadubik.reposearch.model.db.DataBase;
import com.gmail.vanyadubik.reposearch.model.db.RepositoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataApiModule {

    private Application application;

    public DataApiModule(Application application) {
        this.application = application;
    }

    @Singleton @Provides
    public Application provideContext(){
        return application;
    }

    @Provides
    @Singleton
    public DataBase getDataRepository() {
        return Room.databaseBuilder(application.getBaseContext(), DataBase.class, "reposearch-db").build();
    }

    @Singleton
    @Provides
    public RepositoryDao provideRepositoryDao(DataBase dataBase){
        return dataBase.repositoryDao();
    }
}
