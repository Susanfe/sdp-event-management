package ch.epfl.sweng.eventmanager.ui.vectorsupport;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import ch.epfl.sweng.eventmanager.R;

/**
 * Custom Text view that enables the use of VectorDrawables unsupported by native SDK
 * Taken from stackoverflow answer 40250753
 */
public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomView);

            Drawable drawableStart = null;
            Drawable drawableEnd = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableStart = attributeArray.getDrawable(R.styleable.CustomView_drawableStartCompat);
                drawableEnd = attributeArray.getDrawable(R.styleable.CustomView_drawableEndCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.CustomView_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.CustomView_drawableTopCompat);
            } else {
                final int drawableStartId = attributeArray.getResourceId(R.styleable.CustomView_drawableStartCompat, -1);
                final int drawableEndId = attributeArray.getResourceId(R.styleable.CustomView_drawableEndCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.CustomView_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.CustomView_drawableTopCompat, -1);

                if (drawableStartId != -1)
                    drawableStart = AppCompatResources.getDrawable(context, drawableStartId);
                if (drawableEndId != -1)
                    drawableEnd = AppCompatResources.getDrawable(context, drawableEndId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }

            // to support rtl
            setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom);
            attributeArray.recycle();
        }
    }
}
