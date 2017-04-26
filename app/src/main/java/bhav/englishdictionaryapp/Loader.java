package bhav.englishdictionaryapp;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Loader {

    interface onFailListener {
        void onFail();
    }

    interface BadWordListener {
        void onBadWord();
    }

    interface SuccessListener {
        void onLoad(Word w);
    }

    private final static String TAG= Loader.class.getSimpleName();

    private List<Call> calls;
    private OxfordService api;
    private onFailListener listener;
    private BadWordListener bad;
    private SuccessListener done;

    private boolean gettingMeaning = false;
    private boolean gettingSynonyms = false;

    public boolean isDownloading() {
        return gettingMeaning && gettingSynonyms;
    }

    public Loader() {
        calls = new ArrayList<>();
    }

    public void searchFor(String query) {
        getMeaning(query);
        getSynonyms(query);
    }

    void addOnFailListener(onFailListener listener) {
        this.listener = listener;
    }

    void addOnBadWordListener(BadWordListener bad) {
        this.bad = bad;
    }

    void addOnFinishListener(SuccessListener done) {
        this.done = done;
    }

    private void getMeaning(String query) {
        gettingMeaning = true;
        final Call<Word> meaningfulCall = getApi().getMeaning(query);
        meaningfulCall.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if(response.isSuccessful()) {
//                    Log.d(TAG, String.valueOf(response.body()));
                    if(done != null) done.onLoad(response.body());
                } else if(response.code() == 404) {
                    if(bad != null) bad.onBadWord();
                } else {
//                    Log.d(TAG, "onResponse: fail");
                    if (isDownloading()) cancelLoading();
                    if(listener != null) listener.onFail();
                }
                gettingMeaning = false;
                calls.remove(meaningfulCall);
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
//                Log.d(TAG, "onFailure: failure");
                calls.remove(meaningfulCall);
                if(listener != null) listener.onFail();
                if (isDownloading()) cancelLoading();
            }
        });
        calls.add(meaningfulCall);
    }

    private void getSynonyms(String query) {
        gettingSynonyms = true;
        final Call<Word> similarCall = getApi().getSynonyms(query);
        similarCall.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if(response.isSuccessful()) {
//                    Log.d(TAG, String.valueOf(response.body()));
                    if(done != null) done.onLoad(response.body());
                } else if(response.code() == 404) {
                    if(bad != null) bad.onBadWord();
                } else {
//                    Log.d(TAG, "onResponse: fails" + String.valueOf(response.code()));
                    if(listener != null) listener.onFail();
                    if (isDownloading()) cancelLoading();
                }
                gettingSynonyms = false;
                calls.remove(similarCall);
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
//                Log.d(TAG, "onFailure: failures");
                gettingSynonyms = false;
                calls.remove(similarCall);
                if(listener != null) listener.onFail();
                if (isDownloading()) cancelLoading();
            }
        });
        calls.add(similarCall);
    }

    public OxfordService getApi() {
        if (api == null) createApi();
        return api;
    }

    private void createApi() {
        api = new Retrofit.Builder()
                .baseUrl(OxfordService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OxfordService.class);
    }


    protected void cancelLoading() {
        if (calls.size() > 0) {
            for(Call c : calls) {
                c.cancel();
            }
            calls.clear();
        }
    }
}
