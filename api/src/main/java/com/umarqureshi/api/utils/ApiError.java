package com.umarqureshi.api.utils;

import android.content.Context;

import com.umarqureshi.api.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiError {

    private String mResponseErrorMessage;
    private Code mResponseError;

    public void setResponseErrorMessage(String responseErrorMessage) {
        this.mResponseErrorMessage = responseErrorMessage;
    }

    public void setResponseError(Code responseError) {
        this.mResponseError = responseError;
    }

    public String translate(Context context)
    {
        switch (mResponseError)
        {
            case CONNECTION_FAILED:
                return context.getResources().getString(R.string.error_msg_connection);
            case CALL_FAILED:
                return context.getResources().getString(R.string.error_msg_call);
            case FAILED_RESPONSE:
                return (mResponseErrorMessage != null)? mResponseErrorMessage : context.getResources().getString(R.string.error_msg_response);
            default:
                return context.getResources().getString(R.string.error_msg_invalid);
        }
    }

    public enum  Code {
        CONNECTION_FAILED,
        CALL_FAILED,
        FAILED_RESPONSE,
        UNKNOWN;

    }

    public static ApiError translateFailureToApiError(Throwable throwable){
        ApiError apiError = new ApiError();
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException)throwable).response().errorBody();
            apiError.setResponseError(ApiError.Code.FAILED_RESPONSE);
            try {
                apiError.setResponseErrorMessage(responseBody.string());
            } catch (Exception e) {
                apiError.setResponseError(ApiError.Code.CALL_FAILED);
            }
        } else if (throwable instanceof SocketTimeoutException) {
            apiError.setResponseError(ApiError.Code.CONNECTION_FAILED);
        } else if (throwable instanceof IOException) {
            apiError.setResponseError(ApiError.Code.CALL_FAILED);
        } else {
            apiError.setResponseError(ApiError.Code.UNKNOWN);
        }

        return apiError;
    }

}
