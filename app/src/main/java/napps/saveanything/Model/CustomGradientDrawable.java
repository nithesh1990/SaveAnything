package napps.saveanything.Model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import napps.saveanything.Utilities.ColorUtilities;

/**
 * Created by nithesh on 7/10/2016.
 */
public class CustomGradientDrawable extends GradientDrawable {

    private CustomGradientDrawable(GradientDrawable.Orientation orientation, int[] colors){
        super(orientation, colors);
    }
    public static class Builder {

        int startColorAttribute = Color.WHITE;
        int centerColorAttribute = -1;
        int endColorAttribute = Color.WHITE;
        int type = GradientDrawable.RECTANGLE;
        GradientDrawable.Orientation orientation = Orientation.TOP_BOTTOM;

        public CustomGradientDrawable build(Context context, CustomGradientDrawable.Builder builder){
            if(centerColorAttribute == -1){
                int startColor = ColorUtilities.getColorFromAttribute(context, startColorAttribute);
                int endColor = ColorUtilities.getColorFromAttribute(context, startColorAttribute);
                int colors[] = new int[]{startColor, endColor};
                CustomGradientDrawable customGradientDrawable = new CustomGradientDrawable(orientation, colors);
                customGradientDrawable.setGradientType(type);
                return customGradientDrawable;
            } else {
                int startColor = ColorUtilities.getColorFromAttribute(context, startColorAttribute);
                int centerColor = ColorUtilities.getColorFromAttribute(context, centerColorAttribute);
                int endColor = ColorUtilities.getColorFromAttribute(context, startColorAttribute);
                int colors[] = new int[]{startColor, centerColor, endColor};
                CustomGradientDrawable customGradientDrawable = new CustomGradientDrawable(orientation, colors);
                customGradientDrawable.setGradientType(type);
                return customGradientDrawable;
            }

        }

        public void type(int type){
            this.type = type;
        }

        public void setStartColorAttribute(int startColorAttribute){
            this.startColorAttribute = startColorAttribute;
        }

        public void setCenterColorAttribute(int centerColorAttribute){
            this.centerColorAttribute = centerColorAttribute;
        }

        public void setEndColorAttribute(int endColorAttribute){
            this.endColorAttribute = endColorAttribute;
        }

        public void Orientation(GradientDrawable.Orientation orientation){
            this.orientation = orientation;
        }

    }
}
