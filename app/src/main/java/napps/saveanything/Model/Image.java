package napps.saveanything.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class Image extends RealmObject {

    @PrimaryKey
    private long id;

    private int sourceWidth;
    private int sourceHeight;
    private int saveWidth;
    private int saveHeight;
    private int status;
    private int scaleStatus;
    private int scaleFactor;
    private int imageAccess;
    private long timeStamp;
    private long imageSize;
    private String imageId;
    private String desc;
    private String originalPath;
    private String savedPath;
    private String sourcePackage;
    private boolean isFavorite;

    public int getSourceWidth() {
        return sourceWidth;
    }

    public void setSourceWidth(int sourceWidth) {
        this.sourceWidth = sourceWidth;
    }

    public int getSourceHeight() {
        return sourceHeight;
    }

    public void setSourceHeight(int sourceHeight) {
        this.sourceHeight = sourceHeight;
    }

    public int getSaveWidth() {
        return saveWidth;
    }

    public void setSaveWidth(int saveWidth) {
        this.saveWidth = saveWidth;
    }

    public int getSaveHeight() {
        return saveHeight;
    }

    public void setSaveHeight(int saveHeight) {
        this.saveHeight = saveHeight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScaleStatus() {
        return scaleStatus;
    }

    public void setScaleStatus(int scaleStatus) {
        this.scaleStatus = scaleStatus;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public int getImageAccess() {
        return imageAccess;
    }

    public void setImageAccess(int imageAccess) {
        this.imageAccess = imageAccess;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }
}
