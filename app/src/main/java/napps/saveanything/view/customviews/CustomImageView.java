package napps.saveanything.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import napps.saveanything.R;

/**
 * Created by nithesh on 7/2/2016.
 */
public class CustomImageView extends ImageView {

    private final int DEFAULT_ASPECT_RATIO = 0;
    private final int ONETOONE_ASPECT_RATIO_FIXED_WIDTH = 10;
    private final int SIXTEENTONINE_ASPECT_RATIO_FIXED_WIDTH = 11;
    private final int SIXTEENTOTEN_ASPECT_RATIO_FIXED_WIDTH = 12;
    private final int FOURTOTHREE_ASPECT_RATION_FIXED_WIDTH = 13;
    private final int ONETOONE_ASPECT_RATIO_FIXED_HEIGHT = 20;
    private final int SIXTEENTONINE_ASPECT_RATIO_FIXED_HEIGHT = 21;
    private final int SIXTEENTOTEN_ASPECT_RATIO_FIXED_HEIGHT = 22;
    private final int FOURTOTHREE_ASPECT_RATIO_FIXED_HEIGHT = 23;

    private int aspectRatio;
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        aspectRatio = typedArray.getInt(R.styleable.CustomImageView_aspectratio, DEFAULT_ASPECT_RATIO);

        typedArray.recycle();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        aspectRatio = typedArray.getInt(R.styleable.CustomImageView_aspectratio, DEFAULT_ASPECT_RATIO);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        switch (aspectRatio){
            case ONETOONE_ASPECT_RATIO_FIXED_HEIGHT:
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                break;
            case ONETOONE_ASPECT_RATIO_FIXED_WIDTH:
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                break;
            case SIXTEENTONINE_ASPECT_RATIO_FIXED_HEIGHT:
            case SIXTEENTONINE_ASPECT_RATIO_FIXED_WIDTH:
                this.setAdjustViewBounds(true);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;

            case SIXTEENTOTEN_ASPECT_RATIO_FIXED_HEIGHT:
                super.onMeasure((16*heightMeasureSpec)/10, heightMeasureSpec);
                break;
            case SIXTEENTOTEN_ASPECT_RATIO_FIXED_WIDTH:
                super.onMeasure(widthMeasureSpec, (10*heightMeasureSpec)/16);
                break;
            case FOURTOTHREE_ASPECT_RATIO_FIXED_HEIGHT:
                super.onMeasure((4*heightMeasureSpec)/3, heightMeasureSpec);
                break;
            case FOURTOTHREE_ASPECT_RATION_FIXED_WIDTH:
                super.onMeasure(widthMeasureSpec, (3*widthMeasureSpec)/4);
                break;
            default:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

      }

}
