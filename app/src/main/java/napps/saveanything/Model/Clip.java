package napps.saveanything.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by "nithesh" on 11/12/2016.
 */

public class Clip extends RealmObject {

    @PrimaryKey
    private  long Id;

    private  int contentType;
    private  String clipId;
    private  String sourcePackage;
    private  String content;
    private  long timeStamp;
    private  int clipStatus;
    private  int clipAccess;
    private  String date;
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

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
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

    public int getClipStatus() {
        return clipStatus;
    }

    public void setClipStatus(int clipStatus) {
        this.clipStatus = clipStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getClipAccess() {
        return clipAccess;
    }

    public void setClipAccess(int clipAccess) {
        this.clipAccess = clipAccess;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
