package napps.saveanything.view.Fragments;

import android.content.Context;
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
import napps.saveanything.Database.RealmContentProvider;
import napps.saveanything.Model.Note;
import napps.saveanything.Model.Note;
import napps.saveanything.R;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.view.adapters.NoteListAdapter;
import napps.saveanything.view.adapters.NoteListAdapter;

/**
 * Created by "nithesh" on 3/4/2017.
 */

public class NotesFragment extends CustomFragment implements LoaderManager.LoaderCallbacks<RealmResults<Note>> {

    public NoteListAdapter mNotesListAdapter;
    public Context mContext;
    RecyclerView mNotesRecyclerView;
    RelativeLayout mNotesProgressLayout;
    TextView mNoNotessTextView;
    RealmResults<Note> NotesList;

    //static factory design pattern
    public static NotesFragment newInstance(/*we can pass the parameters that need to be set in fragment*/){
        NotesFragment NotesFragment = new NotesFragment();
        //use the below methods to set arguments passed in constructor
        //Bundle bundle = new Bundle();
        //bundle.putsomething()
        //allFragment.setArguments(bundle);
        return NotesFragment;
    }

    public NotesFragment() {
        super.setTitle(Constants.NOTEFRAGMENT_TITLE);
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
        View view = inflater.inflate(R.layout.layout_notesfragment, null);
        mNotesRecyclerView = (RecyclerView)view.findViewById(R.id.notes_recycler_view);
        mNotesProgressLayout = (RelativeLayout) view.findViewById(R.id.notes_progress_layout);
        mNoNotessTextView = (TextView)view.findViewById(R.id.no_notes_text);
        mNotesRecyclerView.setVisibility(View.GONE);
        mNoNotessTextView.setVisibility(View.GONE);
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //show progress bar
        mNotesProgressLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getLoaderManager().initLoader(RealmQueryLoader.QUERY_ALL_NoteS, null, this);
        NotesList = RealmContentProvider.getAllNotesForDisplay(Constants.SORT_DEFAULT);
    }

    @Override
    public void onStart() {
        super.onStart();
        mNotesListAdapter = new NoteListAdapter(mContext, R.layout.note_card, NotesList, getBaseSpringSystem());
        mNotesProgressLayout.setVisibility(View.GONE);
        mNotesRecyclerView.setVisibility(View.VISIBLE);
        mNotesRecyclerView.setAdapter(mNotesListAdapter);
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
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_menu, menu);
        //This searchview has some problem
        //final MenuItem searchMenu = (MenuItem) menu.findItem(R.id.Notes_search);
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId){
            case R.id.notes_search :
                break;

            case R.id.notes_sort_content :
                break;

            case R.id.notes_sort_favorites :
                break;

            default:

                break;

        }
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    public String getTitle(){
        return Constants.NOTEFRAGMENT_TITLE;
    }

    @Override
    public Loader<RealmResults<Note>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<RealmResults<Note>> loader, RealmResults<Note> data) {

    }

    @Override
    public void onLoaderReset(Loader<RealmResults<Note>> loader) {

    }
}
