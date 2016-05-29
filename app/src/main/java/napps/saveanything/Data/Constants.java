package napps.saveanything.Data;

/**
 * Created by nithesh on 5/6/2016.
 */
public class Constants {

    //Fragments Title
    public static final String ALLFRAGMENT_TITLE = "ALL";
    public static final String IMAGEFRAGMENT_TITLE = "IMAGES";
    public static final String CLIPFRAGMENT_TITLE = "CLIPBOARD";

    //Content Types
    public static final int CONTENT_CLIP = 100;
    public static final int CONTENT_IMAGE = 101;
    public static final int CONTENT_LOCATION = 102;
    public static final int CONTENT_DOCUMENTS = 103;

    //Content Sub Types
    public static final int CLIP_PLAIN_TEXT = 1001;
    public static final int CLIP_HTTP_LINK = 1002;
    public static final int CLIP_TEXT_HTTP = 1002;
    public static final int IMAGE_PLAIN = 1011;
    public static final int IMAGE_HTTP_LINK = 1012;
    public static final int LOCATION_PLAIN = 1021;
    public static final int LOCATION_HTTP_LINK = 1022;
    public static final int DOCUMENT_PLAIN = 1031;
    public static final int DOCUMENT_HTTP_LINK = 1032;

    //Clips Process States
    public static final int STATUS_CLIP_SAVED = 10001;
    public static final int STATUS_CLIP_PROCESSING = 10001;
    public static final int STATUS_CLIP_PROCESSED = 10002;
    public static final int STATUS_CLIP_CATEGORIZED = 10003;

    //Image Process States
    public static final int STATUS_IMAGE_ADDED = 10101;
    public static final int STATUS_IMAGE_PROCESSING = 10102;
    public static final int STATUS_IMAGE_SAVED = 10103;
    public static final int STATUS_IMAGE_PROCESS_ERROR = 10104;

    //scale status
    public static final int IMAGE_SCALED_UP = 1;
    public static final int IMAGE_SCALED_DOWN = 2;
    public static final int IMAGE_NOT_SCALED = 3;


    //Unique Id's
    public static final String CLIP_PREFIX = "C";
    public static final String IMAGE_PREFIX = "I";

    //mime types
    public static final String MIME_TYPE_IMAGE = "image/";
    public static final String MIME_TYPE_TEXT = "text/";
    public static final String MIME_TYPE_DOCUMENT = "application/";

    //extension types
    public static final String IMAGE_FORMAT_PNG = ".png";
    //image source
    public static final String SCHEME_CONTENT = "content";
    public static final String SCHEME_FILE = "file";
    //List Sort Orders
    public static final int SORT_CONTENTS_ASCENDING = 2001;
    public static final int SORT_CONTENTS_DESCENDING = 2002;
    public static final int SORT_TITLE_ASCENDING = 2003;
    public static final int SORT_TITLE_DESCENDING = 2004;
    public static final int SORT_TIME_OLD_FIRST = 2005;
    public static final int SORT_TIME_NEW_FIRST = 2006;
    public static final int SORT_MEDIA_SIZE_ASCENDING = 2007;
    public static final int SORT_MEDIA_SIZE_DESCENDING = 2008;
    public static final int SORT_DEFAULT = 2010;

    //storage constants
    public static final String STORAGE_MAIN_DIRECTORY = "/SaveAnything";
    public static final String STORAGE_CLIPS_DIRECTORY = "/SaveAnything/Clips";
    public static final String STORAGE_IMAGES_DIRECTORY = "/SaveAnything/Images";
//    public static final long MILLISECONDS = 1000;
//    public static final long SECONDS = 60 * MILLISECONDS;
//    public static final long MINUTES = 60 * SECONDS;
//    public static final long HOURS = 60 * MINUTES;
//    public static final long DAYS = 24 * HOURS;
//    public static final long WEEKS = 7 * DAYS;
//    public static final long MONTHS =
}
