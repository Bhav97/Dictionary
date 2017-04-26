package bhav.englishdictionaryapp;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;


public class Word implements Parcelable {

    public String text;
    public List<String> synonyms;

    public Word() {}

    public Word(String text, List<String> synonyms) {
        this.text = text;
        this.synonyms = synonyms;
    }

    /* Parcelable stuff */

    protected Word(Parcel in) {
        text = in.readString();
        synonyms = in.createStringArrayList();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeStringList(synonyms);
    }
}
