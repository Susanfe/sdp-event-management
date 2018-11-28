package ch.epfl.sweng.eventmanager.ui.ticketing.activities;

import android.Manifest;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.lang.reflect.Field;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.GrantPermissionRule;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.ui.ticketing.ScanningTest;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingScanActivity;
import ch.epfl.sweng.eventmanager.ui.ticketing.TicketingTestRule;

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

    BaseScanningActivityTest(int eventId) {
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

    private BarcodeCallback getCallback() {
        return getField(mActivityRule.getActivity(), "callback");
    }

    void sendScanSuccess(String code) {
        BarcodeResult barcodeResult =
                new BarcodeResult(new Result(code, code.getBytes(), code.getBytes().length, new ResultPoint[0], null, System.currentTimeMillis()), null);

        mActivityRule.getActivity().runOnUiThread(() -> getCallback().barcodeResult(barcodeResult)); // Don't require the camera to start, we don't need it
    }

}