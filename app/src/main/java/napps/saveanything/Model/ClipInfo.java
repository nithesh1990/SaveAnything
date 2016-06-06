package napps.saveanything.Model;

/**
 * Created by nithesh on 5/12/2016.
 */
/*
    Builder Pattern along with getter methods
    Dependency Injection :
        Using getters and setters comes under Dependency Injection Design Pattern
        Dependency injection is basically providing the objects that an object needs (its dependencies) instead of having it construct them itself.
        It's a very useful technique for testing, since it allows dependencies to be mocked or stubbed out.
        Whenever possible pass object instances through constructor or getter setter methods
        which becomes useful like once you have set all the available instance variables through setters or getters method
        we can directly call the methods in test classes and do rigorous testing by passing arbitrary values.

 */
public class ClipInfo {
    private final int contentType;
    private final long id;
    private final String clipId;
    private final String sourcePackage;
    private final String content;
    private final long timestamp;
    private final int clipStatus;

    private ClipInfo(Builder builder){
        this.contentType = builder.contentType;
        this.id = builder.id;
        this.clipId = builder.clipId;
        this.sourcePackage = builder.sourcePackage;
        this.content = builder.content;
        this.timestamp = builder.timestamp;
        this.clipStatus = builder.clipStatus;
    }

    public String getClipId() {
        return clipId;
    }

    public String getContent() {
        return content;
    }

    public int getClipStatus() {
        return clipStatus;
    }

    public int getContentType() {
        return contentType;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public static class Builder {

        public ClipInfo build(){
            return new ClipInfo(this);
        }
        private  int contentType;
        private  long id;
        private  String clipId;
        private  String sourcePackage;
        private  String content;
        private  long timestamp;
        private  int clipStatus;

        public void Content(String content) {
            this.content = content;
        }


        public void Timestamp(long timestamp) {
            this.timestamp = timestamp;
        }


        public void ContentType(int contentType) {
            this.contentType = contentType;
        }


        public void Id(long id) {
            this.id = id;
        }


        public void SourcePackage(String sourcePackage) {
            this.sourcePackage = sourcePackage;
        }


        public void ClipId(String clipId) {
            this.clipId = clipId;
        }


        public void ClipStatus(int clipStatus) {
            this.clipStatus = clipStatus;
        }

    }


}
