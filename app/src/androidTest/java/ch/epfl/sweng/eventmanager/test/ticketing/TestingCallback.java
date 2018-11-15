package ch.epfl.sweng.eventmanager.test.ticketing;

import android.os.SystemClock;
import android.util.Log;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class TestingCallback<T> implements TicketingService.ApiCallback<T> {
    private AtomicBoolean ok = new AtomicBoolean(false);
    private AtomicBoolean finished = new AtomicBoolean(false);
    private Consumer<T> validateSuccess;
    private Consumer<List<ApiResult.ApiError>> validateErrors;

    public static <T> Consumer<T> accept() {
        return t -> {};
    }

    public static interface Consumer<T> {
        void accept(T t);
    }

    private TestingCallback(Consumer<T> validateSuccess, Consumer<List<ApiResult.ApiError>> validateErrors) {
        this.validateSuccess = validateSuccess;
        this.validateErrors = validateErrors;
    }

    public static <T> TestingCallback<T> expectSuccess(Consumer<T> validateSuccess) {
        return new TestingCallback<>(validateSuccess, s -> {
            Assert.fail("Expected success but got error");
        });
    }

    public static <T> TestingCallback<T> expectErrors(Consumer<List<ApiResult.ApiError>> validateErrors) {
        return new TestingCallback<>(s -> {
            Assert.fail("Expected error but got success");
        }, validateErrors);
    }

    public static <T> TestingCallback<T> alwaysFail() {
        return new TestingCallback<>(s -> Assert.fail("Expected nothing but got success"), s -> Assert.fail("Expected nothing but got error"));
    }

    boolean isOk() {
        return ok.get();
    }

    boolean isFinished() {
        return finished.get();
    }

    @Override
    public void onSuccess(T data) {
        Log.i("TicketingServiceTest", "Got result " + data);

        try {
            validateSuccess.accept(data);
            ok.set(true);
        } catch (AssertionError e) {
            e.printStackTrace();
        } finally {
            finished.set(true);
        }
    }

    @Override
    public void onFailure(List<ApiResult.ApiError> errors) {
        Log.i("TicketingServiceTest", "Got failure " + errors);

        try {
            validateErrors.accept(errors);
            ok.set(true);
        } catch (AssertionError e) {
            e.printStackTrace();
        } finally {
            finished.set(true);
        }
    }

    public void assertOk(String message) {
        int total = 0;
        while (total++ < 150 && !isFinished())
            SystemClock.sleep(200);

        assertTrue(message, isOk());
    }
}
