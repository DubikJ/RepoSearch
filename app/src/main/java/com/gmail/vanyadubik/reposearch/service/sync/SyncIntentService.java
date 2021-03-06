package com.gmail.vanyadubik.reposearch.service.sync;

import android.app.IntentService;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.gmail.vanyadubik.reposearch.R;
import com.gmail.vanyadubik.reposearch.app.RSApplication;
import com.gmail.vanyadubik.reposearch.model.APIError;
import com.gmail.vanyadubik.reposearch.model.db.DataBase;
import com.gmail.vanyadubik.reposearch.model.db.Repository;
import com.gmail.vanyadubik.reposearch.model.db.RepositoryDao;
import com.gmail.vanyadubik.reposearch.model.json.DownloadResponse;
import com.gmail.vanyadubik.reposearch.model.json.ResultItemDTO;
import com.gmail.vanyadubik.reposearch.utils.ErrorUtils;
import com.gmail.vanyadubik.reposearch.utils.NetworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;
import retrofit2.Response;

import static com.gmail.vanyadubik.reposearch.common.Consts.STATUS_ERROR_SYNC;
import static com.gmail.vanyadubik.reposearch.common.Consts.STATUS_FINISHED_SYNC;
import static com.gmail.vanyadubik.reposearch.common.Consts.STATUS_STARTED_SYNC;
import static com.gmail.vanyadubik.reposearch.utils.Json2DbModelConverter.convertRepository;


public class SyncIntentService extends IntentService {
    public static final String SYNC_RECEIVER = "sync_receiver";
    public static final String SYNC_FILTER_SEARCH = "sync_filter_search";

    @Inject
    DataBase dataBase;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    ErrorUtils errorUtils;

    private SyncService syncService;
    private String searchFilter;

    public SyncIntentService() {
        super(SyncIntentService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((RSApplication) getApplication()).getComponent().inject(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(SYNC_RECEIVER);
        searchFilter = intent.getStringExtra(SYNC_FILTER_SEARCH);
        final Bundle bundle = new Bundle();

        if (!networkUtils.checkEthernet()) {
            bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_internet_connecting));
            receiver.send(STATUS_ERROR_SYNC, bundle);
            return;
        }

        Map<String, String> paramsUrl = new HashMap<String, String>();
        paramsUrl.put("q", searchFilter);
        paramsUrl.put("sort", "stars");
        paramsUrl.put("order", "desc");

        syncService = SyncServiceFactory.createService(
                SyncService.class,
                this.getBaseContext());
        receiver.send(STATUS_STARTED_SYNC, bundle);
        try {
            Response<DownloadResponse> downloadResponse = syncService.download1(paramsUrl).execute();
            if (downloadResponse.isSuccessful()) {
                DownloadResponse body = downloadResponse.body();
                if(body == null) {
                    bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_no_data_search));
                    receiver.send(STATUS_ERROR_SYNC, bundle);
                    return;
                }
                if(body.getItems().size()==0) {
                    bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_no_data_search));
                    receiver.send(STATUS_ERROR_SYNC, bundle);
                    return;
                }
                updateDb(body);
                bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.sync_success));
                receiver.send(STATUS_FINISHED_SYNC, bundle);
            } else {
                APIError error = errorUtils.parseErrorCode(downloadResponse.code());
                bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
                receiver.send(STATUS_ERROR_SYNC, bundle);

            }
        } catch (Exception exception) {
            APIError error = errorUtils.parseErrorMessage(exception);
            bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
            receiver.send(STATUS_ERROR_SYNC, bundle);
        }

    }

    private void updateDb(DownloadResponse response) {
        List<ResultItemDTO> resultItemDTOList = response.getItems();

        dataBase.repositoryDao().deleteAll();

        for (ResultItemDTO resultItemDTO : resultItemDTOList) {
            dataBase.repositoryDao().insert(convertRepository(resultItemDTO, searchFilter));
        }
    }

}
