package ch.epfl.sweng.eventmanager.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import id.zelory.compressor.Compressor;

import java.io.*;

public final class ImageConverter {

    public static final String EXTENSION_PNG = "png";
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;


    private static File getFile(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = "event_image";
        File tempFile = File.createTempFile("event_image", EXTENSION_PNG);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static File convertToPng (Context context, Uri uri) throws IOException {
        File compressedImage = new Compressor(context).setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setMaxHeight(500).setMaxWidth(500).setQuality(100).compressToFile(getFile(context,uri));
        return compressedImage;
    }
}
