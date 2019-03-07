package com.umarqureshi.mytaxicodechallenge.ui.util;

import com.umarqureshi.api.utils.ApiError;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    protected MutableLiveData<ApiError> mApiError;

    public LiveData<ApiError> getLiveError(){
        if(mApiError  == null) {
            mApiError = new MutableLiveData<>();
        }

        return mApiError;
    }
}
