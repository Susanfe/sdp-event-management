package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.view.KeyEvent;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import ch.epfl.sweng.eventmanager.ui.ticketing.BarcodeViewWrapper;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import dagger.Module;
import dagger.Provides;

/**
 * @author Louis Vialar
 */
@Module
public class NoOpBarcodeViewManagerModule {
    @Provides
    public BarcodeViewWrapper barcodeViewManager() {
        return new BarcodeViewWrapper() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void initialize(DecoratedBarcodeView v, TicketingScanActivity activity, BarcodeCallback callback) {
                // Do nothing
            }

            @Override
            public void pause() {
                // Do nothing
            }

            @Override
            public void resume() {
                // Do nothing
            }

            @Override
            public void setStatusText(String text) {

            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                return false;
            }
        };
    }
}

