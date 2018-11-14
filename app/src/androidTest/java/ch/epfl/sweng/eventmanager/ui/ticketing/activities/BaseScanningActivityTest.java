package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.GrantPermissionRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ToastMatcher;
import ch.epfl.sweng.eventmanager.test.ticketing.MockStacks;
import ch.epfl.sweng.eventmanager.test.ticketing.TestingCallback;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;
import ch.epfl.sweng.eventmanager.ui.eventShowcase.EventShowcaseActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingLoginActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.AUTHORIZED_USER;
import static ch.epfl.sweng.eventmanager.test.ticketing.MockStacks.PASSWORD;

public abstract class BaseScanningActivityTest extends ActivityTest<TicketingScanActivity> {
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    public BaseScanningActivityTest(int eventId) {
        super(eventId, TicketingScanActivity.class);
    }

    private static <T> T getField(Object o, String field) {
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    private DecoderThread getDecoderThread() {
        DecoratedBarcodeView dView = getField(mActivityRule.getActivity(), "barcodeView");
        BarcodeView view = getField(dView, "barcodeView");
        return getField(view, "decoderThread");
    }

    private Handler getResultHandler() {
        return getField(getDecoderThread(), "resultHandler");
    }

    protected void sendScanSuccess(String code) {
        BarcodeResult barcodeResult =
                new BarcodeResult(new Result(code, code.getBytes(), code.getBytes().length, new ResultPoint[0], null, System.currentTimeMillis()), null);

        Message message = Message.obtain(getResultHandler(), com.google.zxing.client.android.R.id.zxing_decode_succeeded, barcodeResult);
        Bundle bundle = new Bundle();
        message.setData(bundle);
        message.sendToTarget();
    }

    protected void waitCameraReady() {
        boolean cont = true;
        for (int i = 0; i < 20 && cont; ++i) {
            try {
                onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));
                cont = getDecoderThread() == null; // Wait for the decoder thread
            } catch (NoMatchingViewException e) {
                SystemClock.sleep(1000);
            }
        }

        onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));
    }

}