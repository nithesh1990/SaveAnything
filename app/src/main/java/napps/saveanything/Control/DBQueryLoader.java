package napps.saveanything.Control;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;

import napps.saveanything.Database.DBContentProvider;
import napps.saveanything.Database.DBHelper;

/**
 * Created by nithesh on 5/16/2016.
 */
/*
    Android's CursorLoader also extends AsyncTaskLoader<Cursor>. We could have used that.
    The reason for using a separate Loader is
    1. CursorLoader requires query to be provided in content uri format with projection.
    2. Since we already have a query method written specifically, using a new one doesn't make sense
    3. Moreover we can use any methods to run in the background
    4. If we initialize cursorLoader it simply initializes many variables and objects which we don't require
    5. Here we initialize only required variables and objects
 */
public class DBQueryLoader extends AsyncTaskLoader<Cursor> {

    CursorLoader mLoader;
    public int mLoaderId;
    private Context mContext;
    private int mSortType;
    public static final int QUERY_ALL_CLIPS = 10;

    //Efficient Search can be done using the following ways
    //1. Using FTS(Full Text Search) functionality in SQLite. This is a robust search technique where a virtual table
    //      is created to hold the data needed for search. All thes columns will be stored as text so that all are searchable.
    //      We only need to search the clip content to be searched.
    //      Advantages
    //      1.Any column can be searched including source package name
    //      Disadvantages
    //      1. Searches all columns which is not required.
    //      2. If we enter number then timestamps, clipIds will also be searched which are numbers.
    //      3. If we create a new virtual table then there will be waste of space having duplicate values.
    //      4. If we keep default table as virtual table then sorting will be difficult
    //      5. If we search 'linux' in linuxphobo , do linux, LINUX only the second one is returned first.
    //2. Loading all of them into an arraylist and later filter this arraylist to give search results.
    //      Advantages
    //      1. Easy for sorting and searching in the form of collections
    //      Disadvantages
    //      1. Lot of overhead when requerying, adding those to list and and populating which may take lot of time because
    //          for each record a new object is created and cursor moved to next adn then all the data is copied by querying each column in
    //          in selected row. This hangs when the user starts typing fast in searchbox.
    //      2. For every requery arraylist should be refreshed which means new list should be created either discarding old list or
    //          copying from that list
    //      3.
    //3. Realm Database - Realm is an object oriented database and stores the data in objects
    //      Advantages
    //      1. Faster in querying,
    //      2. No need of writing sqlite statements, object oriented access
    //      Disadvantages
    //      1. Inserts are slower
    //      2. Currently this is still in development and still there are lot of issues. Team is concentrating more on IOS
    //          and less on android. According a realm team member lot of code cleaning required.
    //      3. Increases lot of code and creation of each object for each record
    //4. Usual SQLite Queries
    //      Advantages
    //      1. Very less coding, no changes required for current implementation
    //      Disadvantages
    //      1. Slow and for large tables like 500, 000 rows it may take up to 2 minutes for a single query

    //Proposed Solutions
    //1. Using FTS4 Virtual Table along with indexing
    //      http://stackoverflow.com/questions/18413682/how-can-i-get-faster-fts4-query-results-ordered-by-a-field-in-another-table
    //2. Since Virtual Table doesn't allow multiple column search(because we need searching in content and also date columns)
    //      Searching date column is a unique feature which is not found in any of the applications available in market.
    //      So we need 2 column or maybe 3 or more column search later when we keep adding more columns in future release.
    //3. Using Sub String Matching Algorithms - Store the values in hashcode/numbers/any format that makes searching easy in sqlite engine(text search is slower in sqite). If we index these number columns
    //      it will be lot more easier. This is the right time to learn Sub string matching algorithms which are most asked interview questions. These are the list of best sub string
    //      searching algorithms available
    //      1. Boyer-Moore
    //      2. Boyer-Moore-Horspool
    //      3. Knuth-Morris-Pratt(Not suitable) - Complex
    //      4. Rabin-Karp(Not suitable) - This is interesting - It will create a sliding window of the length of the text to be searched. Then it creates a hash of the pattern to be searched.
    //                      Then it starts sliding the window by one position, computing hashes and comparing it with pattern hash. It adds all the matching hashes to a list
    //                      and then applies string comparison to check exact matching value and returns its index (ofCourse index should be stored in filtered hashed list)
    //                      Here hash computing algorithm also plays an important role. It shouldn't take long time.
    //      which is mentioned in this post http://stackoverflow.com/questions/3183582/what-is-the-fastest-substring-search-algorithm
    //4. (Current Best Solution)Using FTS with Indexing and Union - Only applicable for clips table
    //      For fastest searching we have to index the contents table. The columns that are unique in our case is
    //      Timestamp, Primary Key, and clipId.
    //      We have an issue that a clip which has same contents can be copied again and again. Even though this happens our application should not allow duplicates.
    //      To make sure this doesn't happen when we are going to save a clip we have to see if table already contains a clip with similar content. This can be easily
    //      done using a simple sql query. But when the application is scaled to contain very large number of entries, and if the string is at the bottom block as in actual storage
    //      it takes lot of time because it might require a full table scan and sometimes the query might fail and the app will be waiting to save the clip and meantime
    //      some other clip might be copied and current clip object is replaced which becomes messy. So the plan is to generate unique hashes and store alongside contents to improve
    //      scanning. But this also requires full table scan if the hash value is at the bottom. This is where Column indexing comes into picture.
    //      Database Indexing - It is nothing but storing the values of columns along with memory references.
    //                  Index is a separate data structure that is created and stored apart from actual database in the database storage
    //                  So this will actually require a separate space. The main use of index is to make searching and sorting faster.
    //                  In actual database data is stored in serial blocks which is nothing but linked list. But if we separate values and keep it in an ordered way
    //                  we can search and store easily. So the indexed values are stored in a B-Tree data structure. For more information check this link
    //                  http://www.programmerinterview.com/index.php/database-sql/what-is-an-index/
    //                  There are also hash indexes available but sqlite doesn't support hash indexing.
    //      (2nd part in optimization)
    //      It's found that indexing FTS tables is useless as they are already  indexed. FTS Tables create data duplicacy and consume lot of memory.
    //      So we have decided to make a contentless table. What is this contentless table and where is it used specifically and how it is implemented ? See below
    //      http://cocoamine.net/blog/2015/09/07/contentless-fts4-for-large-immutable-documents/
    //      http://www.sqlite.org/fts3.html#section_6_2_2
    //      So contentless table
    //
    //      Once we are done with indexing of hash values we can search and sort easily. Actually sql search first checks if the specified query column is indexed and if it is indexed
    //      it will search in the indexed data structure which makes search faster.
    //      Searching of clip contents can be optimized by using indexed hash values.
    //
    //      Now comes the need for hashing. Generally hashing performs 2 major functions, generate unique value and encryption.
    //      Here we are not concerned with
    //
    public static final int QUERY_CLIPS_BY_CONTENT = 11;

    public static final int QUERY_ALL_IMAGES = 20;


    //This is Dependency Injection Design Pattern
    //Dependency injection is basically providing the objects that an object needs (its dependencies) instead of having it construct them itself.
    //It's a very useful technique for testing, since it allows dependencies to be mocked or stubbed out.
    //Whenever possible pass object instances through constructor or getter setter methods
    //which becomes useful like once you have set all the available instance variables through setters or getters method
    //we can directly call the methods in test classes and do rigorous testing by passing arbitrary values.

    public DBQueryLoader(Context context, int loaderId, int sortType) {
        super(context);
        this.mContext = context;
        this.mLoaderId = loaderId;
        this.mSortType = sortType;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        switch (mLoaderId){
            case QUERY_ALL_CLIPS:
                cursor = DBContentProvider.getAllClipsforDisplay(sqLiteDatabase, mSortType);
                break;
            case QUERY_ALL_IMAGES:
                cursor = DBContentProvider.getAllImagesforDisplay(sqLiteDatabase, mSortType);
                break;
            case QUERY_CLIPS_BY_CONTENT:

            default:
                cursor = null;
        }

        return cursor;
    }

    @Override
    protected Cursor onLoadInBackground() {
        return super.onLoadInBackground();
    }

    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }
}
