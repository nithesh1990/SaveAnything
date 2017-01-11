package napps.saveanything.Model;

import java.util.Date;

/**
 * Created by "nithesh" on 12/14/2016.
 */

public abstract class Builder {

    /*
        The reason for using buildpattern

        http://stackoverflow.com/questions/328496/when-would-you-use-the-builder-pattern

        The builder pattern is a good choice when designing classes whose constructors or static factories would have more than a handful of parameters.
        We've all at some point encountered a class with a list of constructors where each addition adds a new option parameter:

        Pizza(int size) { ... }
        Pizza(int size, boolean cheese) { ... }
        Pizza(int size, boolean cheese, boolean pepperoni) { ... }
        Pizza(int size, boolean cheese, boolean pepperoni, boolean bacon) { ... }
        This is called the Telescoping Constructor Pattern. The problem with this pattern is that once constructors are 4 or 5 parameters long it becomes difficult to remember the required order of the parameters as well as what particular constructor you might want in a given situation.

        One alternative you have to the Telescoping Constructor Pattern is the JavaBean Pattern where you call a constructor with the mandatory parameters and then call any optional setters after:

        Pizza pizza = new Pizza(12);
        pizza.setCheese(true);
        pizza.setPepperoni(true);
        pizza.setBacon(true);
        The problem here is that because the object is created over several calls it may be in an inconsistent state partway through its construction. This also requires a lot of extra effort to ensure thread safety.
        Note that Pizza is immutable and that parameter values are all in a single location. Because the ClipBuilder's setter methods return the ClipBuilder object they are able to be chained.

        Pizza pizza = new Pizza.ClipBuilder(12)
                               .cheese(true)
                               .pepperoni(true)
                               .bacon(true)
                               .build();
        This results in code that is easy to write and very easy to read and understand. In this example, the build method could be modified to check parameters after they have been copied from the builder to the Pizza object and throw an IllegalStateException if an invalid parameter value has been supplied. This pattern is flexible and it is easy to add more parameters to it in the future. It is really only useful if you are going to have more than 4 or 5 parameters for a constructor. That said, it might be worthwhile in the first place if you suspect you may be adding more parameters in the future.
     */

    public static class ClipBuilder {

        /*We make sure that before building the clip we start with assigning a proper Id. We might forget and that's not the right design.*/
        public ClipBuilder(long id) {
            this.id = id;
        }

        public ClipBuilder(Clip clip){
            id = clip.getId();
            clipId = clip.getClipId();
            contentType = clip.getContentType();
            content = clip.getContent();
            clipAccess = clip.getClipAccess();
            clipStatus = clip.getClipStatus();
            timestamp = clip.getTimeStamp();
            date = clip.getDate();
            sourcePackage = clip.getSourcePackage();
            isFavorite = clip.isFavorite();
        }
        public void build(Clip clip){
            //clip.setId(id);
            if(clipId != null && !clipId.isEmpty()){
                clip.setClipId(clipId);
            }
            if(contentType > 0){
                clip.setContentType(contentType);
            }
            if(content != null && !content.isEmpty()) {
                clip.setContent(content);
            }
            if(clipAccess > 0) {
                clip.setClipAccess(clipAccess);
            }
            if(clipStatus > 0) {
                clip.setClipStatus(clipStatus);
            }
            if(timestamp > 0) {
                clip.setTimeStamp(timestamp);
                clip.setDate(new Date(timestamp).toString());
            }
            if(sourcePackage != null && !sourcePackage.isEmpty()){
                clip.setSourcePackage(sourcePackage);
            }

            if(isFavorite){
                clip.setFavorite(true);
            }
        }
        /* We are making all variables public because we need to access these in other classes */
        public  long id;

        public  int contentType = -1;
        public  String clipId = "";
        public  String sourcePackage = "";
        public  String content = "";
        public  long timestamp = 0;
        public  int clipStatus = -1;
        public  int clipAccess = 0;
        public  String date = "";
        public  boolean isFavorite = false;

        public ClipBuilder Content(String content) {
            this.content = content;
            return this;
        }


        public ClipBuilder Timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }


        public ClipBuilder ContentType(int contentType) {
            this.contentType = contentType;
            return this;
        }


        public ClipBuilder SourcePackage(String sourcePackage) {
            this.sourcePackage = sourcePackage;
            return this;
        }


        public ClipBuilder ClipId(String clipId) {
            this.clipId = clipId;
            return this;
        }


        public ClipBuilder ClipStatus(int clipStatus) {
            this.clipStatus = clipStatus;
            return this;
        }

        public ClipBuilder date(String date) {
            this.date = date;
            return this;
        }

        public ClipBuilder clipAccess(int clipAccess){
            this.clipAccess = clipAccess;
            return this;
        }

        public ClipBuilder isFavorite(boolean isFavorite){
            this.isFavorite = isFavorite;
            return this;
        }
    }

    public static class ImageBuilder {

        public ImageBuilder(long id){
            this.id = id;
        }

        public ImageBuilder(Image image){
            id = image.getId();
            sourceHeight = image.getSourceHeight();
            sourceWidth = image.getSourceWidth();
            saveHeight = image.getSaveHeight();
            saveWidth = image.getSaveWidth();
            status = image.getStatus();
            scaleStatus = image.getScaleStatus();
            scaleFactor = image.getScaleFactor();
            imageAccess = image.getImageAccess();
            timestamp = image.getTimeStamp();
            imageSize = image.getImageSize();
            imageId = image.getImageId();
            desc = image.getDesc();
            originalPath = image.getOriginalPath();
            savedPath = image.getSavedPath();
            sourcePackage = image.getSourcePackage();
            isFavorite = image.isFavorite();
        }

        public void build(Image image){
            //image.setId(id);
            if(sourceWidth > 0) {
                image.setSourceWidth(sourceWidth);
            }
            if(sourceHeight > 0){
                image.setSourceHeight(sourceHeight);
            }
            if(saveWidth > 0){
                image.setSaveWidth(saveWidth);
            }
            if(saveHeight > 0){
                image.setSaveHeight(saveHeight);
            }
            if(status > 0){
                image.setStatus(status);
            }
            if(scaleFactor > 0){
                image.setScaleFactor(scaleFactor);
            }
            if(scaleStatus > 0){
                image.setScaleStatus(scaleStatus);
            }
            if(imageAccess > 0){
                image.setImageAccess(imageAccess);
            }
            if(timestamp > 0){
                image.setTimeStamp(timestamp);
            }
            if(imageSize > 0){
                image.setImageSize(imageSize);
            }
            if(imageId != null && !imageId.isEmpty()){
                image.setImageId(imageId);
            }
            if(desc != null && !desc.isEmpty()){
                image.setDesc(desc);
            }
            if(originalPath != null && !originalPath.isEmpty()){
                image.setOriginalPath(originalPath);
            }
            if(savedPath != null && !savedPath.isEmpty()){
                image.setSavedPath(savedPath);
            }
            if(sourcePackage != null && !sourcePackage.isEmpty()){
                image.setSourcePackage(sourcePackage);
            }
            if(isFavorite){
                image.setFavorite(true);
            }
        }
        public long id;

        public int sourceWidth = -1;
        public int sourceHeight = -1;
        public int saveWidth = -1;
        public int saveHeight = -1;
        public int status = -1;
        public int scaleStatus = -1;
        public int scaleFactor = -1;
        public int imageAccess = 0;
        public long timestamp = 0;
        public long imageSize = 0;
        public String imageId = "";
        public String desc = "";
        public String originalPath = "";
        public String savedPath = "";
        public String sourcePackage = "";
        public boolean isFavorite = false;


        public ImageBuilder sourceWidth(int sourceWidth){
            this.sourceWidth = sourceWidth;
            return this;
        }

        public ImageBuilder sourceHeight(int sourceHeight){
            this.sourceHeight = sourceHeight;
            return this;
        }

        public ImageBuilder saveWidth(int saveWidth){
            this.saveWidth = saveWidth;
            return this;
        }

        public ImageBuilder saveHeight(int saveHeight){
            this.saveHeight = saveHeight;
            return this;
        }

        public ImageBuilder status(int status){
            this.status = status;
            return this;
        }

        public ImageBuilder scaleStatus(int scaleStatus){
            this.scaleStatus = scaleStatus;
            return this;
        }

        public ImageBuilder scaleFactor(int scaleFactor){
            this.scaleFactor = scaleFactor;
            return this;
        }

        public ImageBuilder imageAccess(int imageAccess){
            this.imageAccess = imageAccess;
            return this;
        }

        public ImageBuilder timeStamp(long timeStamp){
            this.timestamp = timeStamp;
            return this;
        }

        public ImageBuilder imageSize(long imageSize){
            this.imageSize = imageSize;
            return this;
        }

        public ImageBuilder imageId(String imageId){
            this.imageId = imageId;
            return this;
        }

        public ImageBuilder desc(String desc){
            this.desc = desc;
            return this;
        }

        public ImageBuilder originalPath(String originalPath){
            this.originalPath = originalPath;
            return this;
        }

        public ImageBuilder savedPath(String savedPath){
            this.savedPath = savedPath;
            return this;
        }

        public ImageBuilder sourcePackage(String sourcePackage){
            this.sourcePackage = sourcePackage;
            return this;
        }
        public ImageBuilder isFavorite(boolean isFavorite){
            this.isFavorite = isFavorite;
            return this;
        }
    }


}
