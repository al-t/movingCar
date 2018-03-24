package ru.javabreeze.movingcar;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Алексей on 24.03.2018.
 * The class of static methods that are widely used in the app
 */

public class Utility {
    public static float convertDpToPixel(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static int convertPxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
