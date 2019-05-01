package dai.android.utility;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dai.android.utility.log.Logger;

public final class TextResourceReader {
    private static final String TAG = TextResourceReader.class.getSimpleName();

    private TextResourceReader() {
    }

    public static String readTextFileFromResource(Context context, int resourceId) {
        if (null == context || resourceId <= 0) {
            return null;
        }

        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
            return body.toString();

        } catch (IOException e) {
            Logger.e(TAG, "Could not open resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe) {
            Logger.e(TAG, "Resource not found: " + resourceId, nfe);
        }

        return null;
    }

    public static String readTextFileFromAsset(Context context, String path) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getAssets().open(path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
            return body.toString();

        } catch (IOException e) {
            Logger.e(TAG, "Could not open asset file: " + path, e);
        }
        return null;
    }


}
