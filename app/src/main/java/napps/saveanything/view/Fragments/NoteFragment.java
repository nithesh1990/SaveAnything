package napps.saveanything.view.Fragments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import io.realm.RealmResults;
import napps.saveanything.Model.Clip;
import napps.saveanything.Model.Note;

/**
 * Created by "nithesh" on 3/4/2017.
 */

public class NoteFragment extends CustomFragment implements LoaderManager.LoaderCallbacks<RealmResults<Note>> {
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
