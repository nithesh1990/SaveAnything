package napps.saveanything.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by nithesh on 5/18/2016.
 */
public class CustomTextView extends TextView {

    private int ROBOTO_FONT = 1;
    private int ROBOTO_BLACKITALIC = 2;
    private int ROBOTO_BOLD = 3;
    private int ROBOTO_BOLDITALIC = 4;
    private int ROBOTO_ITALIC = 5;
    private int ROBOTO_LIGHT = 6;
    private int ROBOTO_LIGHTITALIC = 7;
    private int ROBOTO_MEDIUM = 8;
    private int ROBOTO_MEDIUMITALIC = 9;
    private int ROBOTO_REGULAR = 10;
    private int ROBOTO_THIN = 11;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //AttributeSet is the set of attributes that we receive from xml and we can use those attributes to customize views
        //If we want some of the custom attributes that should show up in xml we need to define then in declare-stylable
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //TypedArray typedArray = context.obtainStyledAttributes(attrs, attrs, R.styleable.CustomTextView_fontinfo);

        //int typeface =

        //switch(typeface){

        //}
    }


 }
