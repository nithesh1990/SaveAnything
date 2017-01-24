package napps.saveanything.view.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.RealmResults;
import napps.saveanything.Control.RealmQueryLoader;
import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Clip;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.R;
import napps.saveanything.view.adapters.ClipListAdapter;

/**
 * Created by nithesh on 5/6/2016.
 */
public class ClipsFragment extends CustomFragment implements LoaderManager.LoaderCallbacks<RealmResults<Clip>>{

    public ClipListAdapter mClipListAdapter;
    public Context mContext;
    RecyclerView mClipsRecyclerView;
    RelativeLayout mClipsProgressLayout;
    TextView mNoClipssTextView;
    RealmResults<Clip> clipsList;

    //static factory design pattern
    public static ClipsFragment newInstance(/*we can pass the parameters that need to be set in fragment*/){
        ClipsFragment clipsFragment = new ClipsFragment();
        //use the below methods to set arguments passed in constructor
        //Bundle bundle = new Bundle();
        //bundle.putsomething()
        //allFragment.setArguments(bundle);
        return clipsFragment;
    }

    public ClipsFragment() {
        super.setTitle(Constants.CLIPFRAGMENT_TITLE);
        super.setUp();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_clipsfragment, null);
        mClipsRecyclerView = (RecyclerView)view.findViewById(R.id.clips_recycler_view);
        mClipsProgressLayout = (RelativeLayout) view.findViewById(R.id.clips_progress_layout);
        mNoClipssTextView = (TextView)view.findViewById(R.id.no_clips_text);
        mClipsRecyclerView.setVisibility(View.GONE);
        mNoClipssTextView.setVisibility(View.GONE);
        mClipsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //show progress bar
        mClipsProgressLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getLoaderManager().initLoader(RealmQueryLoader.QUERY_ALL_CLIPS, null, this);
        clipsList = RealmContentProvider.getAllClipsForDisplay(Constants.SORT_DEFAULT);
    }

    @Override
    public void onStart() {
        super.onStart();
        mClipListAdapter = new ClipListAdapter(mContext, R.layout.clip_card, clipsList, getBaseSpringSystem());
        mClipsProgressLayout.setVisibility(View.GONE);
        mClipsRecyclerView.setVisibility(View.VISIBLE);
        mClipsRecyclerView.setAdapter(mClipListAdapter);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.clips_menu, menu);
        //This searchview has some problem
        final MenuItem searchMenu = (MenuItem) menu.findItem(R.id.clips_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    public String getTitle(){
        return Constants.CLIPFRAGMENT_TITLE;
    }

    @Override
    public Loader<RealmResults<Clip>> onCreateLoader(int id, Bundle args) {
        return new RealmQueryLoader(mContext, RealmQueryLoader.QUERY_ALL_CLIPS, Constants.SORT_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<RealmResults<Clip>> loader, RealmResults<Clip> data) {
        RealmQueryLoader queryLoader = (RealmQueryLoader) loader;
        mClipsProgressLayout.setVisibility(View.GONE);
        mClipsRecyclerView.setVisibility(View.VISIBLE);

        if(queryLoader.mLoaderId != RealmQueryLoader.QUERY_ALL_CLIPS){
            //TODO
            // show error page or return
        } else if(data != null && data.size() > 0){

            mClipListAdapter = new ClipListAdapter(mContext, R.layout.clip_card, data, getBaseSpringSystem());
            mClipsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mClipsRecyclerView.setAdapter(mClipListAdapter);

        } else {
            //TODO
            //show empty page


         }

    }

    @Override
    public void onLoaderReset(Loader<RealmResults<Clip>> loader) {
        //this is called when we reset the loader to requery before it finishes the existing query


    }

}


