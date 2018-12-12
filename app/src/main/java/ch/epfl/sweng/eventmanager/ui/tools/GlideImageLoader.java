package ch.epfl.sweng.eventmanager.ui.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import ch.epfl.sweng.eventmanager.inject.GlideApp;
import ch.epfl.sweng.eventmanager.inject.GlideRequest;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.BitmapTransformation;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Louis Vialar
 */
@Singleton
public class GlideImageLoader implements ImageLoader {

    @Inject
    public GlideImageLoader() {
    }

    @Override
    public void displayImage(Context context, Uri uri, ImageView imageView) {
        GlideApp.with(context).load(uri).into(imageView);
    }

    @Override
    public void loadImageWithSpinner(Event event, Context context, ImageView imageView, @Nullable BitmapTransformation transformation) {
        if (event.hasAnImage()) {
            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();
            GlideRequest<Drawable> request = GlideApp.with(context).load(event.getImageURLasURI());

            if (transformation != null)
                request = request.apply(RequestOptions.bitmapTransform(transformation));

            request.placeholder(progress).into(imageView);
        }
    }

}
