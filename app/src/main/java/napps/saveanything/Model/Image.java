package napps.saveanything.Model;

import io.realm.RealmObject;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class Image extends RealmObject {
    private int sourceWidth;
    private int sourceHeight;
    private int saveWidth;
    private int saveHeight;
    private int status;
    private int scaleStatus;
    private int scaleFactor;
    private long id;
    private long timestamp;
    private long imageSize;
    private String imageId;
    private String desc;
    private String originalPath;
    private String savedPath;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
}
