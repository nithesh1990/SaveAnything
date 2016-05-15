package napps.saveanything.Model;

/**
 * Created by nithesh on 5/14/2016.
 */
public class ImageInfo {
    private long id;
    private String imageId;
    private String title;
    private int contentType;
    private long timestamp;
    private String imagePath;
    private long imageSize;

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

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }
}
