package bhav.englishdictionaryapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface OxfordService {
    static final String ENDPOINT = "https://od-api.oxforddictionaries.com/api/v1/";

    @GET("entries/en/{query}")
    @Headers({
            "app_id: b3fa0198",
            "app_key: b63f88c68b2043e1fd57c7faeb11b4c0"
    })
    Call<Word> getMeaning(@Path("query") String query);

    @GET("entries/en/{query}/synonyms")
    @Headers({
            "app_id: b3fa0198",
            "app_key: b63f88c68b2043e1fd57c7faeb11b4c0"
    })
    Call<Word> getSynonyms(@Path("query") String query);
}
