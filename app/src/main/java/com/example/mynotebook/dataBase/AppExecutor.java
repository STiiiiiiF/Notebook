package com.example.mynotebook.dataBase;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static AppExecutor instants;
    private final Executor mainIO;
    private final Executor subIO;

    public AppExecutor(Executor mainIO, Executor subIO) {
        this.mainIO = mainIO;
        this.subIO = subIO;
    }

    public static AppExecutor getInstance() {
        if (instants == null)
            instants = new AppExecutor(new MainThreadHandler(), Executors.newSingleThreadExecutor());
        return instants;
    }

    public static class MainThreadHandler implements Executor {
        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);
        }
    }

    public Executor getMainIO() {
        return mainIO;
    }

    public Executor getSubIO() {
        return subIO;
    }
}
