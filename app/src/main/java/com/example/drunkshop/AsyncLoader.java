package com.example.drunkshop;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Random;

public class AsyncLoader extends AsyncTaskLoader<String> {

    public AsyncLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Random random = new Random();
        int num = random.nextInt(13);
        int ms = num * 600;

        try {
            Thread.sleep(ms);
        } catch (InterruptedException err){
            err.printStackTrace();
        }
        return "Tudom, hogy inni akarsz ;)";
    }
}
