package napps.saveanything.Database;

import android.provider.BaseColumns;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class RealmContract {

    public static abstract class ClipFields {
        public static final String FIELD_NAME_ID = "Id";
        public static final String FIELD_NAME_CLIPID = "clipId";
        public static final String FIELD_NAME_SOURCE_PACKAGE = "sourcePackage";
        public static final String FIELD_NAME_CLIPSTATUS = "clipStatus";
        public static final String FIELD_NAME_CONTENTTYPE = "contentType";
        public static final String FIELD_NAME_CONTENT = "content";
        public static final String FIELD_NAME_TIMESTAMP = "timeStamp";
        public static final String FIELD_NAME_CLIPACCESS = "clipAccess";
        public static final String FIELD_NAME_SECONDARY_CONTENT = "secondaryContent";
    }


    public static abstract class ImageFields {
        public static final String FIELD_NAME_ID = "id";
        public static final String FIELD_NAME_IMAGEID = "imageId";
        public static final String FIELD_NAME_DESC = "desc";
        public static final String FIELD_NAME_ORIGINALPATH = "originalPath";
        public static final String FIELD_NAME_STATUS = "status";
        public static final String FIELD_NAME_SAVEDPATH = "savedPath";
        public static final String FIELD_NAME_SCALE_FACTOR = "scaleFactor";
        public static final String FIELD_NAME_SCALE_STATUS = "scaleStatus";
        public static final String FIELD_NAME_TIMESTAMP = "timeStamp";
        public static final String FIELD_NAME_IMAGESIZE = "imageSize";
        public static final String FIELD_NAME_SOURCEWIDTH = "sourceWidth";
        public static final String FIELD_NAME_SOURCEHEIGHT = "sourceHeight";
        public static final String FIELD_NAME_SAVEWIDTH = "saveWidth";
        public static final String FIELD_NAME_SAVEHEIGHT = "saveHeight";
        public static final String FIELD_NAME_IMAGEACCESS = "imageAccess";

    }
}
