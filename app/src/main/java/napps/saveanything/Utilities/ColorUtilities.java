package napps.saveanything.Utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by nithesh on 7/10/2016.
 */
public class ColorUtilities {

    public static int getColorFromAttribute(Context context, int attribute){
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attribute});
        String iconColor = typedArray.getString(0);

        if(iconColor != null){
            return Color.parseColor(iconColor);
        } else {
            //default color white
        }
            return Color.parseColor("#ffffff");
    }
}



