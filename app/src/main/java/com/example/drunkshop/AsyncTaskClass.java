package com.example.drunkshop;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class AsyncTaskClass extends AsyncTask<Void, Void, String> {

    private WeakReference<TextView> textView;

    public AsyncTaskClass(TextView view) {
        textView = new WeakReference<>(view);
    }

    @Override
    protected String doInBackground(Void... params) {
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textView.get().setText(s);
    }
}
