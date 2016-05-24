package napps.saveanything.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import napps.saveanything.R;


/**
 * Created by nithesh on 5/18/2016.
 */
public class CustomTextView extends TextView {

    private final int ROBOTO_FONT = 1;
    private final int ROBOTO_BLACKITALIC = 2;
    private final int ROBOTO_BOLD = 3;
    private final int ROBOTO_BOLDITALIC = 4;
    private final int ROBOTO_ITALIC = 5;
    private final int ROBOTO_LIGHT = 6;
    private final int ROBOTO_LIGHTITALIC = 7;
    private final int ROBOTO_MEDIUM = 8;
    private final int ROBOTO_MEDIUMITALIC = 9;
    private final int ROBOTO_REGULAR = 10;
    private final int ROBOTO_THIN = 11;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //AttributeSet is the set of attributes that we receive from xml and we can use those attributes to customize views
        //If we want some of the custom attributes that should show up in xml we need to define then in declare-stylable
        //R.styleable.CustomTextView contains an array of attributes of customtextview
        //R.styleable.CustomTextView_fontinfo points directly to the fontinfo attribute


        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        int fontStyle = typedArray.getInt(R.styleable.CustomTextView_fontinfo, ROBOTO_REGULAR);
        Typeface typeFace = getTypeFace(fontStyle);
        this.setTypeface(typeFace);

        //should be used so that typedarray can be reused later. This typedarray should not be touched later so we make it final
        typedArray.recycle();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

     }

    private Typeface getTypeFace(int fontStyle){
        Typeface typeFace;

        switch(fontStyle){
             case ROBOTO_BLACKITALIC :
                 //typeface can be created from assets/file/existing default fonts
                 typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-BlackItalic.ttf");
                break;
            case ROBOTO_BOLD :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Bold.ttf");
                break;
            case ROBOTO_BOLDITALIC :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-BoldItalic.ttf");
                break;
            case ROBOTO_ITALIC :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Italic.ttf");
                break;
            case ROBOTO_LIGHT :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Light.ttf");
                break;
            case ROBOTO_LIGHTITALIC :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-LightItalic.ttf");
                break;
            case ROBOTO_MEDIUM :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Medium.ttf");
                break;
            case ROBOTO_MEDIUMITALIC :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-MediumItalic.ttf");
                break;
            case ROBOTO_REGULAR :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Regular.ttf");
                break;
            case ROBOTO_THIN :
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Thin.ttf");
                break;
            default:
                typeFace = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Regular.ttf");
                break;

        }

        return  typeFace;
    }


 }
