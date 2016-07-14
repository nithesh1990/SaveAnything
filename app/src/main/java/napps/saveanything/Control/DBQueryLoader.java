package napps.saveanything.Control;

import android.content.Context;
import android.database.Cursor;
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
    //4. (Current Best Solution)Using FTS with Indexing and Union
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
        switch (mLoaderId){
            case QUERY_ALL_CLIPS:
                cursor = DBContentProvider.getAllClipsforDisplay(dbHelper, mSortType);
                break;
            case QUERY_ALL_IMAGES:
                cursor = DBContentProvider.getAllImagesforDisplay(dbHelper, mSortType);
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
