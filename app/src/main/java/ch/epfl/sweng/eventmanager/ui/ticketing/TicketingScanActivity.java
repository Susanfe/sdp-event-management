package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.ticketing.NotAuthenticatedException;
import ch.epfl.sweng.eventmanager.ticketing.TicketingService;
import ch.epfl.sweng.eventmanager.ticketing.data.ApiResult;
import ch.epfl.sweng.eventmanager.ticketing.data.ScanResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Source: https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/ContinuousCaptureActivity.java
 *
 * @author Louis Vialar
 */
public final class TicketingScanActivity extends TicketingActivity {
    public static final String SELECTED_CONFIG_ID = "ch.epfl.sweng.SELECTED_CONFIG_ID";
    private static final String TAG = "TicketingScanActivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 42; // Magic value
    private int configId = -1;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.pause();
            barcodeView.setStatusText(result.getText());
            TextView view = findViewById(R.id.barcodePreview);
            view.setText(R.string.loading_text);

            try {
                service.scan(configId, result.getText(), new TicketingService.ApiCallback<ScanResult>() {
                    @Override
                    public void onSuccess(ScanResult data) {
                        beepManager.playBeepSoundAndVibrate();

                        view.setText(buildHtml(data));

                        barcodeView.setStatusText("");
                        Toast.makeText(TicketingScanActivity.this, R.string.ticketing_scan_success, Toast.LENGTH_SHORT).show();
                        barcodeView.resume();
                    }

                    @Override
                    public void onFailure(List<ApiResult.ApiError> errors) {
                        beepManager.playBeepSoundAndVibrate();

                        view.setText(buildHtmlForError(errors));

                        barcodeView.setStatusText("");
                        Toast.makeText(TicketingScanActivity.this, R.string.ticketing_scan_failure, Toast.LENGTH_SHORT).show();
                        barcodeView.resume();
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

    private void appendSuccessData(ScanResult data, StringBuilder html) {
        if (data.getUser() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_user)).append("</i>")
                    .append(" ").append(data.getUser().getFirstname())
                    .append(" ").append(data.getUser().getLastname())
                    .append(" (").append(data.getUser().getEmail()).append(")");
        }

        if (data.getProduct() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_bought_product)).append("</i>")
                    .append(" ").append(data.getProduct().getName())
                    .append(" (<i>").append(data.getProduct().getDescription())
                    .append("</i>)");
        } else if (data.getProducts() != null) {
            html.append("<br><i>").append(getResources().getString(R.string.ticketing_scan_bought_product)).append("</i>");
            html.append("<ul>");

            for (Map.Entry<ScanResult.Product, Integer> p : data.getProducts().entrySet()) {
                html.append("<li>").append(p.getValue())
                        .append(" * ").append(p.getKey().getName())
                        .append(": ").append(p.getKey().getDescription())
                        .append("</li>");
            }

            html.append("</ul>");
        }
    }

    private Spanned buildHtml(ScanResult data) {
        if (data.isSuccess()) {
            StringBuilder html = new StringBuilder();

            html.append("<b color='green'>")
                    .append(getResources().getString(R.string.ticketing_scan_success))
                    .append("</b>");


            appendSuccessData(data, html);

            return Html.fromHtml(html.toString());
        } else {
            return buildHtmlForError(data.getErrors());
        }
    }

    private Spanned buildHtmlForError(List<ApiResult.ApiError> errors) {
        StringBuilder html = new StringBuilder();

        html.append("<b color='red'>")
                .append(getResources().getString(R.string.ticketing_scan_failure))
                .append("</b><br>");

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

        Intent intent = getIntent();
        this.configId = intent.getIntExtra(SELECTED_CONFIG_ID, -1);

        startScan();

        beepManager = new BeepManager(this);
    }

    private void startScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            barcodeView = findViewById(R.id.barcode_scanner);
            barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Arrays.asList(BarcodeFormat.values())));
            barcodeView.initializeFromIntent(getIntent());
            barcodeView.decodeContinuous(callback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScan();
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

        if (barcodeView == null) {
            startScan();
        } else {
            barcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void goBack(View view) {
        barcodeView.pause();

        startActivity(this.backToShowcase());
    }
}
