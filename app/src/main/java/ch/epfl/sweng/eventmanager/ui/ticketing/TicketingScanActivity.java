package ch.epfl.sweng.eventmanager.ui.ticketing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.eventmanager.R;
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
    private static final String TAG = "TicketingScanActivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    public static final String SELECTED_CONFIG_ID = "ch.epfl.sweng.SELECTED_CONFIG_ID";

    private int configId = -1;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            // TODO: handle codes correctly
            barcodeView.pause();
            lastText = result.getText();
            barcodeView.setStatusText(result.getText());

            service.scan(configId, lastText, new TicketingService.ApiCallback<ScanResult>() {
                @Override
                public void onSuccess(ScanResult data) {
                    beepManager.playBeepSoundAndVibrate();
                    TextView view = findViewById(R.id.barcodePreview);

                    StringBuilder html = new StringBuilder();
                    if (data.isSuccess()) {
                        html.append("<b style='color: green;'>")
                                .append(getResources().getString(R.string.ticketing_scan_success))
                                .append("</b>");


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
                    } else {
                        html.append("<b style='color: red;'>")
                                .append(getResources().getString(R.string.ticketing_scan_failure))
                                .append("</b><br>");

                        if (data.getErrors().size() == 1 && data.getErrors().get(0).getMessages().size() == 1) {
                            html.append(data.getErrors().get(0).getMessages().get(0));
                        } else {
                            html.append("<ul>");
                            for (ApiResult.ApiError err : data.getErrors()) {
                                for (String msg : err.getMessages()) {
                                    html.append("<li>").append(msg).append("</li>");
                                }
                            }
                            html.append("</ul>");
                        }
                    }

                    view.setText(Html.fromHtml(html.toString()));
                }

                @Override
                public void onFailure(List<ApiResult.ApiError> errors) {
                    // TODO: proper error handling
                    Toast.makeText(TicketingScanActivity.this, errors.get(0).getKey(), Toast.LENGTH_LONG).show();
                }
            });


            barcodeView.resume();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startScan();
                } else {
                    startActivity(backToShowcase());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
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
