package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.GrantPermissionRule;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import java.lang.reflect.Field;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public abstract class BaseScanningActivityTest extends ScanningTest {


    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);
    // @Rule public EventTestRule<EventShowcaseActivity> mActivityRule = new EventTestRule<>(EventShowcaseActivity.class, eventId);
    @Rule public TicketingTestRule<TicketingScanActivity> mActivityRule = new TicketingTestRule<>(TicketingScanActivity.class, eventId, MockEventsRepository.CONFIG_BY_EVENT.get(eventId)).withConfigId(3);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    public BaseScanningActivityTest(int eventId) {
        super(eventId);
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
        for (int i = 0; i < 60 && cont; ++i) {
            try {
                onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));
                cont = getDecoderThread() == null; // Wait for the decoder thread

                if (cont)
                    SystemClock.sleep(1000);
            } catch (NoMatchingViewException e) {
                SystemClock.sleep(1000);
            }
        }

        onView(withId(R.id.barcode_scanner)).check(matches(isDisplayed()));

        Assert.assertNotNull(getDecoderThread());
    }

}