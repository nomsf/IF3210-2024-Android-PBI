package com.example.myapplication.ui.transactions;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TransactionViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public TransactionViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
            return (T) new TransactionViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}