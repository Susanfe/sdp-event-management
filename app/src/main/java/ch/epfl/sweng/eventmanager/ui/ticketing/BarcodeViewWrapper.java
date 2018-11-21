package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.view.KeyEvent;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * //FIXME Needed for the tests to pass on emulator but clearly not ideal, find some other solution
 */
public interface BarcodeViewWrapper {

    boolean isReady();

    void initialize(DecoratedBarcodeView v, TicketingScanActivity activity, BarcodeCallback callback);

    void pause();

    void resume();

    void setStatusText(String text);

    boolean onKeyDown(int keyCode, KeyEvent event);
}
