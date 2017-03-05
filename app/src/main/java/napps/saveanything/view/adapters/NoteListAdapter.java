package napps.saveanything.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.BaseSpringSystem;

import io.realm.RealmResults;
import napps.saveanything.Model.Clip;
import napps.saveanything.Model.Note;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by "nithesh" on 3/4/2017.
 */

public class NoteListAdapter extends RecyclerRealmAdapter<ClipListAdapter.ClipCardViewHolder, Note> implements View.OnClickListener {

    private Context mContext;

    public NoteListAdapter(Context context, int layout, RealmResults<Note> realmResults, BaseSpringSystem baseSpringSystem) {
        super(context, layout, realmResults, baseSpringSystem);
        mContext = context;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void bindView(ClipListAdapter.ClipCardViewHolder holder, Note realmObject, int position) {

    }

    @Override
    public ClipListAdapter.ClipCardViewHolder newView(ViewGroup parent, Context context, View view) {
        return null;
    }

    @Override
    public boolean changeFavorite(CustomImageView favoriteIcon) {
        return false;
    }

    public class NoteCardViewHolder extends RecyclerView.ViewHolder {

        public NoteCardViewHolder(View itemView) {
            super(itemView);
        }
    }
}
