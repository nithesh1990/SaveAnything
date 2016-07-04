package napps.saveanything.view.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import napps.saveanything.Control.DBQueryLoader;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.R;
import napps.saveanything.view.adapters.ImageCursorAdapter;
import napps.saveanything.view.adapters.ImageListAdapter;

/**
 * Created by nithesh on 5/6/2016.
 */
public class ImageFragment extends CustomFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LIST_MODE = 11;
    public static final int GRID_MODE = 12;

    private int viewMode;
    private static final String CLASS_TAG = ImageFragment.class.getSimpleName();
    public ImageListAdapter mImageListAdapter;
    public Context mContext;
    RecyclerView mImageRecyclerView;
    RelativeLayout mImageProgressLayout;
    TextView mNoImagesTextView;

    ListView imagesListView;

    ImageCursorAdapter mImageCursorAdapter;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_imagesfragment, null);
        mImageRecyclerView = (RecyclerView)view.findViewById(R.id.images_recycler_view);
        mImageProgressLayout = (RelativeLayout) view.findViewById(R.id.images_progress_layout);
        mNoImagesTextView = (TextView)view.findViewById(R.id.no_images_text);
        //imagesListView = (ListView) view.findViewById(R.id.images_listview);
        mImageRecyclerView.setVisibility(View.GONE);
        mNoImagesTextView.setVisibility(View.GONE);
        mImageProgressLayout.setVisibility(View.VISIBLE);
        viewMode = LIST_MODE;
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

        getLoaderManager().initLoader(DBQueryLoader.QUERY_ALL_IMAGES, null, this);
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
    public void onDestroyView() {
        super.onDestroyView();
        //Called to cleanup resources that are created in oncreateview because after this step, keeping view references are of no use
        //It is best practice to set all resources to null here, since there is no view no need to keep data
        //Ondestroy might be called or may not be called
        //We can shutdown the executor based
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO: Release all remaining resouces apart from OnDestroyView
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
        mImageProgressLayout.setVisibility(View.GONE);
        mImageRecyclerView.setVisibility(View.VISIBLE);

        if(queryLoader.mLoaderId != DBQueryLoader.QUERY_ALL_IMAGES){
            //TODO
            // show error page or return
        } else if(data.getCount() == 0){
            //TODO
            //show empty page
        } else {
            //set adapter


        }
        //DBQueryLoader makes sure the query is refreshed everytime on state change screenoff/on
        //Everytime this is called after query is finished. Here we have to handle the following cases
        //1. Is it first time loading the list (mImageListAdapter will be null)
        //2. Screen On/Off (mImageListAdapter will not be null so no need to assign new adapter
        //3. Data changed adapter (Here also mImageListAdapter will not be null but if we not assign new adapter
        //      then new data won't be refreshed. So we need to differentiate between data refresh calls and screen on/off calls
        //4. If new data is added, adapter should be refreshed then the position should be set to top
        //5. If the data is deleted, adapter should be refreshed but position should not change
        //6. If the refresh is just because screen on or off the position should not change
        //7. This can be achieved keeping a reference of cursor per fragment and comparing new cursor with old cursor,
        //  check for data changes and change behaviour accordingly
        //8. Close this cursor onDestroy or OnDestroyView
        int gridSize = 1;
        //if(mImageListAdapter == null){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            if(viewMode == GRID_MODE){

                mImageListAdapter = new ImageListAdapter(mContext, mImageRecyclerView, R.layout.image_card_grid_view, data, deviceWidth, deviceHeight, viewMode);
                gridSize = 2;
            } else {

                mImageListAdapter = new ImageListAdapter(mContext, mImageRecyclerView, R.layout.image_card_list_view, data, deviceWidth, deviceHeight, viewMode);
                gridSize = 1;
            }
        //}
        GridLayoutManager gridManager = new GridLayoutManager(mContext, gridSize);
        mImageRecyclerView.setLayoutManager(gridManager);
        //mImageListAdapter.setGridLayoutManager(gridManager);
        mImageRecyclerView.setAdapter(mImageListAdapter);

        //if(mImageCursorAdapter == null){
//            mImageCursorAdapter = new ImageCursorAdapter(mContext, data, deviceWidth, deviceHeight);
        //}
//        mImageCursorAdapter.setImagesListViewforBM(imagesListView);
//        imagesListView.setAdapter(mImageCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
