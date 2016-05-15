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
import napps.saveanything.view.adapters.TextListAdapter;

/**
 * Created by nithesh on 5/6/2016.
 */
public class ClipsFragment extends Fraggment {

    public TextListAdapter mTextListAdapter;
    public Context mContext;
    RecyclerView mRecyclerView;

    public ClipsFragment() {
        super.setTitle(Constants.CLIPFRAGMENT_TITLE);
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
        mTextListAdapter = new TextListAdapter(mContext, R.layout.text_card, null);
        mRecyclerView.setAdapter(mTextListAdapter);
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
        return Constants.CLIPFRAGMENT_TITLE;
    }
}
