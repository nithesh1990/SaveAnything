package napps.saveanything.Model;

/**
 * Created by nithesh on 5/14/2016.
 */
public class ImageInfo {
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public int getSaveHeight() {
        return saveHeight;
    }

    public void setSaveHeight(int saveHeight) {
        this.saveHeight = saveHeight;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public int getSaveWidth() {
        return saveWidth;
    }

    public void setSaveWidth(int saveWidth) {
        this.saveWidth = saveWidth;
    }

    public int getSourceHeight() {
        return sourceHeight;
    }

    public void setSourceHeight(int sourceHeight) {
        this.sourceHeight = sourceHeight;
    }

    public int getSourceWidth() {
        return sourceWidth;
    }

    public void setSourceWidth(int sourceWidth) {
        this.sourceWidth = sourceWidth;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getScaleStatus() {
        return scaleStatus;
    }

    public void setScaleStatus(int scaleStatus) {
        this.scaleStatus = scaleStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
