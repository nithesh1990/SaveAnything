package napps.saveanything.view.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;

import io.realm.RealmResults;
import napps.saveanything.Model.Note;
import napps.saveanything.R;
import napps.saveanything.view.customviews.CustomImageView;

/**
 * Created by "nithesh" on 3/4/2017.
 */

public class NoteListAdapter extends RecyclerRealmAdapter<NoteListAdapter.NoteCardViewHolder, Note> implements View.OnClickListener {

    private Context mContext;

    public NoteListAdapter(Context context, int layout, RealmResults<Note> realmResults, BaseSpringSystem baseSpringSystem) {
        super(context, layout, realmResults, baseSpringSystem);
        mContext = context;
    }

    @Override
    public void bindView(NoteCardViewHolder holder, Note note, int position) {
        NoteCardViewHolder noteHolder = (NoteCardViewHolder) holder;
        holder.attachedView.setTag(note);
        holder.favoriteButton.setTag(note);
        holder.copyButton.setTag(note);
        holder.moreButton.setTag(note);
        holder.specialButton.setTag(note);
        //clipHolder.mainTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_CONTENT)));
        noteHolder.mainTextView.setText(note.getContent());
        //String timeText = getTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_TIMESTAMP)));
        String timeText = getTimeText(note.getTimeStamp());
        noteHolder.timeTextView.setText(timeText);
        setFavoriteIcon(noteHolder.favoriteButton, note.isFavorite());

        //String sourcePackage = cursor.getString(cursor.getColumnIndex(DatabaseContract.ClipBoard.COLUMN_NAME_SOURCE_PACKAGE));

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public NoteCardViewHolder newView(ViewGroup parent, Context context, View view) {
        return new NoteCardViewHolder(view);
    }

    @Override
    public boolean changeFavorite(CustomImageView favoriteIcon) {
        return false;
    }

    public class NoteCardViewHolder extends RecyclerView.ViewHolder {

        View attachedView;
        TextView mainTextView;
        TextView timeTextView;
        CustomImageView moreButton;
        CustomImageView copyButton;
        CustomImageView favoriteButton;
        CustomImageView specialButton;
        
        public NoteCardViewHolder(View itemView) {
            super(itemView);
            attachedView = itemView;
            attachedView.setOnClickListener(NoteListAdapter.this);
            this.mainTextView = (TextView) itemView.findViewById(R.id.nc_main_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.nc_time_text);
            //int iconColor = getIconColorFilter(R.styleable.CustomIconColors_iconColorNormal);

            this.copyButton = (CustomImageView) itemView.findViewById(R.id.nc_copy_button);
            this.copyButton.setOnClickListener(NoteListAdapter.this);
            //this.copyButton.setColorFilter(iconColor);
            this.moreButton = (CustomImageView) itemView.findViewById(R.id.nc_more_button);
            this.moreButton.setOnClickListener(NoteListAdapter.this);
            //this.moreButton.setColorFilter(iconColor);
            this.favoriteButton = (CustomImageView) itemView.findViewById(R.id.nc_favorite_button);
            this.favoriteButton.setOnTouchListener(NoteListAdapter.this);
            //this.favoriteButton.setColorFilter(iconColor);
            this.specialButton = (CustomImageView) itemView.findViewById(R.id.nc_special_button);
            this.specialButton.setOnClickListener(NoteListAdapter.this);
            //this.specialButton.setColorFilter(iconColor);

        }
    }
}
