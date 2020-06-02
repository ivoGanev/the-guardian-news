package android.ivo.newsapp;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

/**
 * <b>AfterTextChangeWatcher</b> executes only when text has changed
 * */
class AfterTextChangeWatcher implements TextWatcher {
    private Runnable runnable;

    public AfterTextChangeWatcher(@NonNull Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
