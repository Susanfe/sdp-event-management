package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.SoundAlertManager;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Source: https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing
 * /ContinuousCaptureActivity.java
 *
 * @author Louis Vialar
 */
public final class TicketingScanActivity extends TicketingActivity {
    public static final String SELECTED_CONFIG_ID = "ch.epfl.sweng.SELECTED_CONFIG_ID";
    private static final String TAG = "TicketingScanActivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 42; // Magic value
    private static final int OVERLAY_DELAY = 2000;
    @Inject
    BarcodeViewWrapper viewWrapper;
    @BindView(R.id.barcodePreview)
    TextView view;
    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView scanner;
    @BindView(R.id.scan_result_overlay)
    View overlay;
    private int configId = -1;
    private SoundAlertManager soundAlertManager;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            viewWrapper.pause();
            viewWrapper.setStatusText(result.getText());
            view.setTextColor(Color.BLACK);
            view.setText(R.string.loading_text);

            try {
                service.scan(configId, result.getText(), new TicketingService.ApiCallback<ScanResult>() {
                    @Override
                    public void onSuccess(ScanResult data) {
                        soundAlertManager.success();

                        setScanResult(buildHtml(data), true);

                        viewWrapper.setStatusText("");
                        viewWrapper.resume();
                    }

                    @Override
                    public void onFailure(List<ApiResult.ApiError> errors) {
                        soundAlertManager.failure();

                        setScanResult(buildHtmlForError(errors), false);

                        viewWrapper.setStatusText("");
                        viewWrapper.resume();
                    }
                });
            } catch (NotAuthenticatedException e) {
                Toast.makeText(TicketingScanActivity.this, R.string.ticketing_requires_login, Toast.LENGTH_LONG).show();
                e.printStackTrace();

                startActivity(switchActivity(TicketingLoginActivity.class));
                finish();
            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private void setScanResult(Spanned text, Boolean success) {
        int color;
        if (success) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        view.setText(text);
        view.setTextColor(color);
        overlay.setBackgroundColor(color);
        overlay.setVisibility(View.VISIBLE);
        overlay.postDelayed(() -> overlay.setVisibility(View.INVISIBLE), OVERLAY_DELAY);
    }

    private void appendSuccessData(ScanResult data, StringBuilder html) {
        if (data.getUser() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_user)).append("</i>").append(" ").append(data.getUser().getFirstname()).append(" ").append(data.getUser().getLastname()).append(" (").append(data.getUser().getEmail()).append(")");
        }

        if (data.getProduct() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_bought_product)).append(
                    "</i>").append(" ").append(data.getProduct().getName()).append(" (<i>").append(data.getProduct().getDescription()).append("</i>)");
        } else if (data.getProducts() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_bought_product)).append(
                    "</i>");
            html.append("<ul>");

            for (Map.Entry<ScanResult.Product, Integer> p : data.getProducts().entrySet()) {
                html.append("<li>").append(p.getValue()).append(" * ").append(p.getKey().getName()).append(": ").append(p.getKey().getDescription()).append("</li>");
            }

            html.append("</ul>");
        }
    }

    private Spanned buildHtml(ScanResult data) {
        if (data.isSuccess()) {
            StringBuilder html = new StringBuilder();

            html.append("<b color='green'>").append(getResources().getString(R.string.ticketing_scan_success)).append("</b>");


            appendSuccessData(data, html);

            return Html.fromHtml(html.toString());
        } else {
            return buildHtmlForError(data.getErrors());
        }
    }

    private Spanned buildHtmlForError(List<ApiResult.ApiError> errors) {
        StringBuilder html = new StringBuilder();

        html.append("<b color='red'>").append(getResources().getString(R.string.ticketing_scan_failure)).append("</b" + "><br>");

        if (errors.size() == 1 && errors.get(0).getMessages().size() == 1) {
            html.append(errors.get(0).getMessages().get(0));                        // TODO: proper error handling
        } else {
            html.append("<ul>");
            for (ApiResult.ApiError err : errors) {
                for (String msg : err.getMessages()) {                        // TODO: proper error handling
                    html.append("<li>").append(msg).append("</li>");
                }
            }
            html.append("</ul>");
        }

        return Html.fromHtml(html.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_scan);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        this.configId = intent.getIntExtra(SELECTED_CONFIG_ID, -1);

        initScan();

        soundAlertManager = new SoundAlertManager(this);
    }

    private void initScan() {
        viewWrapper.initialize(scanner, this, callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initScan();
                } else if (grantResults.length > 0) {
                    Log.e(TAG, "Refused CAMERA access (result " + grantResults[0] + ")");
                    startActivity(backToShowcase());
                } else {
                    Log.w(TAG, "Permission result with no result");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!viewWrapper.isReady()) initScan();
        viewWrapper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewWrapper.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return viewWrapper.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        viewWrapper.pause();
        super.onBackPressed();
    }
}
