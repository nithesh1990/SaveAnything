package napps.saveanything.view.Fragments;

import android.support.v4.app.Fragment;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SpringSystem;

/**
 * Created by nithesh on 5/6/2016.
 */
public class CustomFragment extends Fragment {

    //We are not setting the values using constructor because use of deafult constructor is not recommended
    private String Title;
    public BaseSpringSystem baseSpringSystem;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String mTitle) {
        this.Title = mTitle;
    }

    public BaseSpringSystem getBaseSpringSystem() {
        return baseSpringSystem;
    }

    public void setUp(){
        baseSpringSystem = SpringSystem.create();
    }
}
