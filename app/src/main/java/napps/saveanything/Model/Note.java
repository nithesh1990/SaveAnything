package napps.saveanything.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by "nithesh" on 3/4/2017.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private  long Id;

    private  int contentType;
    private  String noteId;
    private  String content;
    private  long timeStamp;
    private  int noteAccess;
    private  String createDate;
    private boolean isFavorite;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getNoteAccess() {
        return noteAccess;
    }

    public void setNoteAccess(int noteAccess) {
        this.noteAccess = noteAccess;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
