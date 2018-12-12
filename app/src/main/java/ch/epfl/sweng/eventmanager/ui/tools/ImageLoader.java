package ch.epfl.sweng.eventmanager.ui.tools;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import jp.wasabeef.glide.transformations.BitmapTransformation;

/**
 * @author Louis Vialar
 */
public interface ImageLoader {

    /**
     * Displays an image coming from the given uri in the given imageView
     * @param context the context of the activity
     * @param uri the uri of the image to display
     * @param imageView the view in which the image should be displayed
     */
    void displayImage(Context context, Uri uri, ImageView imageView);

    /**
     * Load the image for an event to a given imageView, applying to it a transformation if needed.
     * It also displays a spinner while the image is still loading.
     *
     * @param event          the event to fetch the image from
     * @param context        the context of the activity
     * @param imageView      the view in which the image should be displayed
     * @param transformation a transformation to apply to the image before displaying it
     */
    void loadImageWithSpinner(Event event, Context context, ImageView imageView, @Nullable BitmapTransformation transformation);

}
