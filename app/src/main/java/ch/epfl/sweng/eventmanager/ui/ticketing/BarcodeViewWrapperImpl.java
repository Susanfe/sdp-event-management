package ch.epfl.sweng.eventmanager.ui.ticketing;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;

public class BarcodeViewWrapperImpl implements BarcodeViewWrapper {
    public static final int PERM_REQUEST_ID = 10;
    private DecoratedBarcodeView view;
    private BarcodeCallback callback;

    @Override
    public boolean isReady() {
        return view != null;
    }

    @Override
    public void initialize(DecoratedBarcodeView v, TicketingScanActivity activity, BarcodeCallback callback) {
        this.callback = callback;
        this.view = v;

        activity.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onStop() {
                view.pause();
                view = null;
            }
        });

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    PERM_REQUEST_ID);
        } else {
            view.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Arrays.asList(BarcodeFormat.values())));
            view.initializeFromIntent(new Intent());
        }

    }

    @Override
    public void pause() {
        if (view != null) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        if (view != null) {
            view.resume();
            view.decodeContinuous(callback);
        }
    }

    @Override
    public void setStatusText(String text) {
        if (view != null)
            view.setStatusText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (view != null)
            return view.onKeyDown(keyCode, event);
        return false;
    }
}
