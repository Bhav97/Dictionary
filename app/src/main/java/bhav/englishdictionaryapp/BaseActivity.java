package bhav.englishdictionaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements WordPresenter.ViewContract {

    private EditText input;
    WordPresenter presenter;

    public void search(View view) {
        if (!input.getText().toString().isEmpty()) {
            presenter.callSearch(input.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        presenter = new WordPresenter();
        presenter.attachView(this);
        input = (EditText) findViewById(R.id.input);


    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onWordLoaded(Word word) {
        Log.d("onWordLoaded: ", word.text + "");
        startActivity(new Intent(this, WordActivity.class).putExtra("word", word));
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.err_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBadWordError() {
        Toast.makeText(this, R.string.err_bad_word, Toast.LENGTH_SHORT).show();
    }
}
