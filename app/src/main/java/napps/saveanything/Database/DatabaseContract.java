package napps.saveanything.Database;

import android.provider.BaseColumns;

/**
 * Created by nithesh on 5/14/2016.
 */
public final class DatabaseContract {


    public DatabaseContract() {
    }

    public static abstract class ClipBoard implements BaseColumns {
        public static final String TABLE_NAME = "clipData";
        public static final String COLUMN_NAME_CLIPID = "clipId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTTYPE = "contentType";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
    }

    public static abstract class ImageBoard implements BaseColumns {
        public static final String TABLE_NAME = "imageData";
        public static final String COLUMN_NAME_IMAGEID = "imageId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ORIGINALPATH = "imagePath";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_SAVEDPATH = "savedpath";
        public static final String COLUMN_NAME_SCALE_FACTOR = "scalefactor";
        public static final String COLUMN_NAME_IS_SCALED = "isscaled";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
        public static final String COLUMN_NAME_IMAGESIZE = "imageSize";
    }


}
