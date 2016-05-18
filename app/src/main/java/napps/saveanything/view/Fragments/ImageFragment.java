package napps.saveanything.view.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import napps.saveanything.Control.DBQueryLoader;
import napps.saveanything.Data.Constants;
import napps.saveanything.R;
import napps.saveanything.view.adapters.ImageListAdapter;
import napps.saveanything.view.adapters.TextListAdapter;

/**
 * Created by nithesh on 5/6/2016.
 */
public class ImageFragment extends Fraggment implements LoaderManager.LoaderCallbacks<Cursor>{

    public ImageListAdapter mImageListAdapter;
    public Context mContext;
    RecyclerView mRecyclerView;
    ProgressBar mImageProgressBar;
    TextView mNoImagesTextView;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fraggment, null);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mImageProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        mNoImagesTextView = (TextView)view.findViewById(R.id.no_contents_text);
        mRecyclerView.setVisibility(View.GONE);
        mNoImagesTextView.setVisibility(View.GONE);
        mImageProgressBar.setVisibility(View.VISIBLE);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new DBQueryLoader(mContext, DBQueryLoader.QUERY_ALL_IMAGES, Constants.SORT_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        DBQueryLoader queryLoader = (DBQueryLoader) loader;
        //mClipsProgressBar.setVisibility(View.GONE);
        //mRecyclerView.setVisibility(View.VISIBLE);

        if(queryLoader.mLoaderId != DBQueryLoader.QUERY_ALL_IMAGES){
            //TODO
            // show error page or return
        } else if(data.getCount() == 0){
            //TODO
            //show empty page
        } else {
            //set adapter

            mImageListAdapter = new ImageListAdapter(mContext, R.layout.image_card, data);
            mRecyclerView.setAdapter(mImageListAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
