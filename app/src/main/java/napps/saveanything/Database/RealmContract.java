package napps.saveanything.Database;

import android.provider.BaseColumns;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class RealmContract {

    public static abstract class ClipColumns {
        public static final String COLUMN_NAME_CLIPID = "clipId";
        public static final String COLUMN_NAME_SOURCE_PACKAGE = "sourcepackage";
        public static final String COLUMN_NAME_CLIPSTATUS = "clipStatus";
        public static final String COLUMN_NAME_CONTENTTYPE = "contentType";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
        public static final String COLUMN_NAME_SECONDARY_CONTENT = "secondaryContent";
    }


    public static abstract class ImageColumns {
        public static final String COLUMN_NAME_IMAGEID = "imageId";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String COLUMN_NAME_ORIGINALPATH = "imagePath";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_SAVEDPATH = "savedpath";
        public static final String COLUMN_NAME_SCALE_FACTOR = "scalefactor";
        public static final String COLUMN_NAME_SCALE_STATUS = "scalestatus";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
        public static final String COLUMN_NAME_IMAGESIZE = "imageSize";
    }
}
