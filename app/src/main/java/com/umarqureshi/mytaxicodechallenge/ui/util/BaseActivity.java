package com.umarqureshi.mytaxicodechallenge.ui.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.umarqureshi.mytaxicodechallenge.R;
import com.umarqureshi.mytaxicodechallenge.utils.DialogNetworkProgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;

public class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {

    private ProgressDialog dialogNetworkProgress;

    private AlertDialog alertDialog;

    protected T mViewModel;

    public void showDialogNetworkProgress() {
        if(dialogNetworkProgress==null){
            dialogNetworkProgress = DialogNetworkProgress.getDialog(this);
        }
        dialogNetworkProgress.show();
    }

    public void hideDialogNetworkProgress(){
        if(dialogNetworkProgress!=null && dialogNetworkProgress.isShowing()){
            dialogNetworkProgress.dismiss();
        }
    }

    public void showAlertDialog(String alertMessage) {
        if(alertDialog==null){
            alertDialog = new AlertDialog.Builder(this).setPositiveButton(getResources().getString(R.string.txt_ok), null).create();

        }
        alertDialog.setMessage(alertMessage);
        alertDialog.show();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if(dialogNetworkProgress!=null&&dialogNetworkProgress.isShowing())
        {
            dialogNetworkProgress.dismiss();
        }

        if(alertDialog!=null&& alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
        super.onDestroy();

    }

    private void applyErrorHandling()
    {
        mViewModel.getLiveError().observe(this, apiError -> {
            hideDialogNetworkProgress();
            showAlertDialog(apiError.translate(this));
        });
    }

    protected void initializeViewModel(Class<T> viewModelClass)
    {
        mViewModel = ViewModelProviders.of(this).get(viewModelClass);
        applyErrorHandling();
    }
}
