package napps.saveanything.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import napps.saveanything.Utilities.Constants;
import napps.saveanything.R;

/**
 * Created by nithesh on 5/6/2016.
 */
public class AllFragment extends Fraggment {

    public static AllFragment newInstance(/*we can pass the parameters that need to be set in fragment*/){
        AllFragment allFragment = new AllFragment();
        //use the below methods to set arguments passed in constructor
        //Bundle bundle = new Bundle();
        //bundle.putsomething()
        //allFragment.setArguments(bundle);

        return allFragment;
    }

    public AllFragment() {
        super.setTitle(Constants.ALLFRAGMENT_TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fraggment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public String getTitle(){
        return Constants.ALLFRAGMENT_TITLE;
    }
}
