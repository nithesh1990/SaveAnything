package napps.saveanything.Model;

/**
 * Created by nithesh on 5/14/2016.
 */
public class ImageInfo {
    private long id;
    private String imageId;
    private String title;
    private long timestamp;
    private String originalPath;
    private int sourceWidth;
    private int sourceHeight;
    private int saveWidth;
    private int saveHeight;
    private String savedPath;
    private long imageSize;
    private boolean isScaled;
    private int scaleFactor;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


    public boolean isScaled() {
        return isScaled;
    }

    public void setScaled(boolean scaled) {
        isScaled = scaled;
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
}
