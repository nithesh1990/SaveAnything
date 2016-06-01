package napps.saveanything.Model;

/**
 * Created by nithesh on 5/12/2016.
 */
public class ClipInfo {
    private int contentType;
    private long id;
    private String clipId;
    private String sourcePackage;
    private String content;
    private long timestamp;
    private int clipStatus;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public int getClipStatus() {
        return clipStatus;
    }

    public void setClipStatus(int clipStatus) {
        this.clipStatus = clipStatus;
    }
}
