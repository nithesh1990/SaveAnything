package napps.saveanything.view.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import napps.saveanything.Data.Constants;
import napps.saveanything.R;
import napps.saveanything.view.Fragments.Fraggment;
import napps.saveanything.view.adapters.ImageListAdapter;
import napps.saveanything.view.adapters.TextListAdapter;

/**
 * Created by nithesh on 5/6/2016.
 */
public class ImageFragment extends Fraggment {

    public ImageListAdapter mImageListAdapter;
    public Context mContext;
    RecyclerView mRecyclerView;

    //static factory design pattern
    public static ImageFragment newInstance(/*we can pass the parameters that need to be set in fragment*/){
        ImageFragment imageFragment = new ImageFragment();
        //use the below methods to set arguments passed in constructor
        //Bundle bundle = new Bundle();
        //bundle.putsomething()
        //allFragment.setArguments(bundle);
        return imageFragment;
    }

    public ImageFragment() {
        super.setTitle(Constants.IMAGEFRAGMENT_TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_all, null);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mContext = getContext();
        mImageListAdapter = new ImageListAdapter(mContext, R.layout.image_card, null);
        mRecyclerView.setAdapter(mImageListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        return view;
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
        return Constants.IMAGEFRAGMENT_TITLE;
    }
}
