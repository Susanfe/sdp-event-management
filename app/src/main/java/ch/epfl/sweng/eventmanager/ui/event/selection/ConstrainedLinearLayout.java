package ch.epfl.sweng.eventmanager.ui.event.selection;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class ConstrainedLinearLayout extends LinearLayout {

    private final int maxHeight;
    private final static double LAYOUT_SCREEN_PERCENTAGE = 0.7;

    public ConstrainedLinearLayout(Context context) {
        super(context);
        maxHeight = initScreenHeight();
    }

    public ConstrainedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        maxHeight = initScreenHeight();
    }

    public ConstrainedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        maxHeight = initScreenHeight();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConstrainedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        maxHeight = initScreenHeight();
    }

    private int initScreenHeight() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        return (int) (dpHeight * LAYOUT_SCREEN_PERCENTAGE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            int hMode = MeasureSpec.getMode(heightMeasureSpec);

            switch (hMode) {
                case MeasureSpec.AT_MOST:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
                    break;
                case MeasureSpec.EXACTLY:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeight), MeasureSpec.EXACTLY);
                    break;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
