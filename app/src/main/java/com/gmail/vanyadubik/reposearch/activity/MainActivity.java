package com.gmail.vanyadubik.reposearch.activity;
import android.arch.persistence.room.ForeignKey;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.vanyadubik.reposearch.R;
import com.gmail.vanyadubik.reposearch.adapter.RepositoryListAdapter;
import com.gmail.vanyadubik.reposearch.app.RSApplication;
import com.gmail.vanyadubik.reposearch.model.db.DataBase;
import com.gmail.vanyadubik.reposearch.model.db.Repository;
import com.gmail.vanyadubik.reposearch.model.db.RepositoryDao;
import com.gmail.vanyadubik.reposearch.model.json.DownloadResponse;
import com.gmail.vanyadubik.reposearch.model.json.ResultItemDTO;
import com.gmail.vanyadubik.reposearch.service.sync.SyncService;
import com.gmail.vanyadubik.reposearch.service.sync.SyncServiceFactory;
import com.gmail.vanyadubik.reposearch.utils.ActivityUtils;
import com.gmail.vanyadubik.reposearch.utils.ErrorUtils;
import com.gmail.vanyadubik.reposearch.utils.NetworkUtils;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import static com.gmail.vanyadubik.reposearch.utils.Json2DbModelConverter.convertRepositoryList;

public class MainActivity extends AppCompatActivity{

    @Inject
    DataBase dataBase;
    @Inject
    RepositoryDao repositoryDao;
    @Inject
    ActivityUtils activityUtils;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    ErrorUtils errorUtils;

    private ListView listView;
    private LinearLayout contSearch, contSync;
    private EditText searchET;
    private Observable observable;
    private ImageButton searchButton;
    private TextView cancelTV;
    private RepositoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((RSApplication) getApplication()).getComponent().inject(this);

        contSearch = (LinearLayout) findViewById(R.id.container_search);
        contSync = (LinearLayout) findViewById(R.id.container_sync);
        contSync.setVisibility(View.GONE);

        searchET = (EditText) findViewById(R.id.search);
        searchET.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = searchET.getRight()
                            - searchET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        searchET.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        searchET = (EditText) findViewById(R.id.search);
        searchButton = (ImageButton) findViewById(R.id.searh_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new Request().execute();
                searchData();
            }
        });
        cancelTV = (TextView) findViewById(R.id.cancel);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(observable != null){
                    observable.unsubscribeOn(AndroidSchedulers.mainThread());;
                    observable = null;
                }
            }
        });


        listView = (ListView)findViewById(R.id.list_result);

    }

    private void startSync(){

        if (!networkUtils.checkEthernet()) {
            activityUtils.showMessage(getResources().getString(R.string.error_internet_connecting), this);
            return;
        }

        SyncService syncService = SyncServiceFactory.createService(
                SyncService.class, MainActivity.this);

        Map<String, String> paramsUrl = new HashMap<String, String>();
        paramsUrl.put("q", searchET.getText().toString());
        paramsUrl.put("sort", "stars");
        paramsUrl.put("order", "desc");

        observable = syncService.download(paramsUrl);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadResponse>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(DownloadResponse downloadResponse) {
                        if(downloadResponse == null) {
                            activityUtils.showMessage(getResources().getString(R.string.error_no_data_search), MainActivity.this);
                            contSync.setVisibility(View.GONE);
                            contSearch.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(downloadResponse.getItems().size()==0) {
                            activityUtils.showMessage(getResources().getString(R.string.error_no_data_search), MainActivity.this);
                            contSync.setVisibility(View.GONE);
                            contSearch.setVisibility(View.VISIBLE);
                            return;
                        }
                        updateDb(downloadResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!networkUtils.checkEthernet()) {
                            activityUtils.showMessage(
                                    getResources().getString(R.string.error_internet_connecting), MainActivity.this);
                            return;
                        }
                        activityUtils.showMessage(e.getMessage(),
                                MainActivity.this);

                        contSync.setVisibility(View.GONE);
                        contSearch.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void searchData(){

        if (TextUtils.isEmpty(searchET.getText().toString())) {
            activityUtils.showMessage(getResources().getString(R.string.text_search_empry), this);
            return;
        }

        contSync.setVisibility(View.VISIBLE);
        contSearch.setVisibility(View.GONE);
        repositoryDao.getByTextSearch(searchET.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(List<Repository> repositories) throws Exception {
                        if (repositories.size() == 0){
                            startSync();
                            return;
                        }

                        adapter = new RepositoryListAdapter(MainActivity.this, repositories);
                        listView.setAdapter(adapter);

                        contSync.setVisibility(View.GONE);
                        contSearch.setVisibility(View.VISIBLE);
                    }

                });

    }

    private void updateDb(final DownloadResponse response) {

        Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    repositoryDao.deleteAll();
                    repositoryDao.insertList(convertRepositoryList(response.getItems(), searchET.getText().toString()));
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {}

                @Override
                public void onComplete() {}

                @Override
                public void onError(Throwable e) {}
            });
        }
}
