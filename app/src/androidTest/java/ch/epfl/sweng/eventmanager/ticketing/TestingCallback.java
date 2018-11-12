package ch.epfl.sweng.eventmanager.ticketing;

import android.os.SystemClock;
import android.util.Log;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

/**
 * @author Louis Vialar
 */
public class TestingCallback<T> implements TicketingService.ApiCallback<T> {
    private AtomicBoolean ok = new AtomicBoolean(false);
    private AtomicBoolean finished = new AtomicBoolean(false);
    private Consumer<T> validateSuccess;
    private Consumer<List<ApiResult.ApiError>> validateErrors;

    private TestingCallback(Consumer<T> validateSuccess, Consumer<List<ApiResult.ApiError>> validateErrors) {
        this.validateSuccess = validateSuccess;
        this.validateErrors = validateErrors;
    }

    static <T> TestingCallback<T> expectSuccess(Consumer<T> validateSuccess) {
        return new TestingCallback<>(validateSuccess, s -> {
            Assert.fail("Expected success but got error");
        });
    }

    static <T> TestingCallback<T> expectErrors(Consumer<List<ApiResult.ApiError>> validateErrors) {
        return new TestingCallback<>(s -> {
            Assert.fail("Expected error but got success");
        }, validateErrors);
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
        while (total++ < 10 && !isFinished())
            SystemClock.sleep(200);

        assertTrue(message, isOk());
    }
}
