package bhav.englishdictionaryapp;


class WordPresenter implements Loader.onFailListener, Loader.BadWordListener, Loader.SuccessListener {

    @Override
    public void onFail() {
        handleNetworkError();
    }

    @Override
    public void onBadWord() {
        viewContract.showBadWordError();
    }

    @Override
    public void onLoad(Word w) {
        if(!loader.isDownloading()) {
            viewContract.onWordLoaded(w);
        }
    }

    interface ViewContract {
        void onWordLoaded(Word word);
        void showNetworkError();
        void showBadWordError();
    }

    private ViewContract viewContract;
    private Loader loader;

    void attachView(ViewContract viewContract) {
        this.viewContract = viewContract;
        onAttach();
    }

    void detachView() {
        onDetach();
        this.viewContract = null;
    }

    private void onAttach() {
        loader = new Loader();
        loader.addOnFailListener(this);
        loader.addOnBadWordListener(this);
        loader.addOnFinishListener(this);
    }

    private void onDetach() {
        if(loader.isDownloading()) {
            loader.cancelLoading();
        }
    }

    void callSearch(String query) {
        if(!loader.isDownloading()) {
            loader.searchFor(query);
        }
    }

    private void handleNetworkError() {
        if (viewContract != null) {
            viewContract.showNetworkError();
        }
    }
}
