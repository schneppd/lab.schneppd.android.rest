package lab.schneppd.restapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;


import lab.schneppd.restapp.rest.github.Contributor;
import lab.schneppd.restapp.rest.github.GitHubService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvDebug = (TextView) findViewById(R.id.txtDebug);
        tvDebug.setText("start of debug");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHubService.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        GitHubService apiService = retrofit.create(GitHubService.class);
        Observable<List<Contributor>> contributorsCall = apiService.GetContributors("square", "retrofit");

        // To define where the work is done, we can use `observeOn()` with Retrofit
        // This means the result is handed to the subscriber on the main thread
        contributorsCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Contributor>>() {
                    @Override
                    public void onNext(List<Contributor> data) {
                        // Called once the object is available
                        StringBuilder output = new StringBuilder();
                        output.append("data found:");
                        output.append(System.getProperty("line.separator"));
                        for (Contributor contributor : data) {
                            output.append(contributor.login);
                            output.append(contributor.contributions);
                            output.append(System.getProperty("line.separator"));
                        }
                        TextView txtDebug = (TextView) findViewById(R.id.txtDebug);
                        txtDebug.setText(output.toString());
                    }

                    @Override
                    public void onCompleted() {
                        // Nothing to do here
                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                        }
                    }
                });

    }
}
