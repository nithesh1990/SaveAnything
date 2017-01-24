package napps.saveanything.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import napps.saveanything.Database.DatabaseContract;
import napps.saveanything.R;
import napps.saveanything.Utilities.AppLogger;
import napps.saveanything.view.adapters.ImageCursorAdapter;
import napps.saveanything.view.adapters.ImageListAdapter;

/**
 * Created by nithesh on 6/18/2016.
 */
public class BitmapManager extends TaskManager<Integer, Bitmap> implements ResponseListener<Integer, Bitmap>{

    /*

     */
    private static final String CLASS_TAG = BitmapManager.class.getSimpleName();
//  Let's keep this constant capacity as 15 where 10 is for actual storage and remaining 5 is for error handling
    // Let's say if the view at position 15 is queried the window might be in between
    //1. 4 to 14 which means user is scrolling down
    //2. 16 to 26 which means user is scrolling up
    // In both the cases we replace the cache item that is equal to  pos%10
    public static final int BITMAP_HOLDING_CAPACITY = 10;

    private Context mContext;

    private int topPos;

    private int bottomPos;

    private QueueHashCache<Integer , Bitmap> mCache;

    private RecyclerView bitmapsViewHolder;

    private ListView imagesListview;

    private GridLayoutManager mLayoutManager;

    private static BitmapManager sInstance;

    private int firstVisiblePos;

    private int lastVisiblePos;

    public static BitmapManager getInstance(){
        if(sInstance == null){
            synchronized (BitmapManager.class){
                if(sInstance == null){
                    sInstance = new BitmapManager();
                }
            }
        }
        return sInstance;
    }

    private BitmapManager(){

    }

    public void initializeBM(Context context) {
        this.mContext = context;
        initialize(mContext, BITMAP_HOLDING_CAPACITY);
        mCache = QueueHashCache.getsInstance();
        mCache.initialize(BITMAP_HOLDING_CAPACITY);
        topPos = 0;
        bottomPos = 0;
    }

    public void setBitmap(String uri, int position, ImageView imageView, int requiredWidth, int requiredHeight){

        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "CustomLib setBitmap()", "");
        Bitmap cachedBitMap = (Bitmap) getCachedResultIfavailable(position);
        if(cachedBitMap != null){

            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "CustomLib", " this image is in cache position: "+position);

            if(imageView != null){
                imageView.setImageBitmap(cachedBitMap);
            }
        } else {
            imageView.setImageResource(R.drawable.background_image_loading);
            int storagepref;
            if(position > bottomPos){
                bottomPos = position;
                storagepref = QueueHashCache.STORAGE_PREF_BOTTOM;
            } else {
                topPos = position;
                storagepref = QueueHashCache.STORAGE_PREF_TOP;
            }

            BitmapTask bitmapTask = new BitmapTask(mContext, position, mCache, storagepref);
            bitmapTask.setImageResources(requiredWidth, requiredHeight, uri);
            bitmapTask.setResponseListener(this);
            bitmapTask.setTASK_ID(position);
            addTask(bitmapTask);
        }
    }
    //TODO:
    //This is a costly operation which we are doing in UI thread and hence there was a hit in the performance
    //and since different tasks complete at different times we can't say that tasks get finished serially and this
    //was causing issues because we were expecting it serially.
    //We don't need a separate Node reference. We can just keep Integer references to simulate queue functions i.e top and bottom add/remove
    //and we can put/retreive those corresponding values from the linkedHashmap which will save lot of memory and space.
    //Simulating queue functionality due to items arriving non serially after results. There can be 2 possible solutions for this
    //1. Implement an array functionality and simulate those queue operations
    //2. Perform these add/remove top/bottom during the add of tasks itself
    @Override
    public void postResult(long taskId, Bitmap value) {
        //if(taskId >=firstVisiblePos && taskId <=lastVisiblePos){
        //    setImageforPosition((int)taskId, value);
        //}//        //This is check for initial case when both topKey and bottomKey will be null

        ImageCursorAdapter.IVHolder ivHolder = (ImageCursorAdapter.IVHolder)imagesListview.getChildAt((int)taskId).getTag();
        if(ivHolder != null){
            ivHolder.mainImage.setImageBitmap(value);
        }
//        //if either of them is null
//        if(mCache.getTopKey() == null || mCache.getBottomKey() == null){
//            isTop = false;
//        } else {
//            isTop = insertOntop(mCache.getTopKey(), (K)key, mCache.getBottomKey());
//        }
//        if(isTop){
//            mCache.addtoTop((K)key, (V)value);
//        } else {
//            mCache.addtoBottom((K)key, (V)value);
//        }

        //TODO: Now the value is added to the cache and it is available for retrieval from the imageListadapter
        // We can notify to the listAdapter


    }

    //This will return true if it should be inserted on top or false if it should be insertedinBottom


    public boolean insertOntop(Integer topKey, Integer keytobeInserted, Integer bottomKey) {
        AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "insertOnTop() ", "actual key value: "+keytobeInserted.intValue());
        int top = topKey.intValue();
        int actual = keytobeInserted.intValue();
        int bottom = bottomKey.intValue();
        if(actual > bottom){
            return false;
        } else if(actual < top){
            return true;
        }
        //TODO:
        //Here we have to handle else case where actual value is between top and bottom which should not happen

        return false;
    }

    public Bitmap getCachedResultIfavailable(Integer key){
        return mCache.get(key);
    }

    public RecyclerView getBitmapsViewHolder() {
        return bitmapsViewHolder;
    }

    public void setBitmapsViewHolder(RecyclerView bitmapsViewHolder) {
        this.bitmapsViewHolder = bitmapsViewHolder;
        bitmapsViewHolder.addOnScrollListener(new BitmapManager.RVScrollListener());
    }

    public GridLayoutManager getmLayoutManager() {
        return mLayoutManager;
    }

    public void setmLayoutManager(GridLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    private void setImageforPosition(int position, Bitmap bitmap){
        ImageListAdapter.ImageCardViewHolder ivHolder = (ImageListAdapter.ImageCardViewHolder)bitmapsViewHolder.findViewHolderForLayoutPosition(position); //        boolean isTop;
        if(ivHolder != null){
            //ivHolder.mainImage.setImageBitmap(bitmap);
        }

    }

    public ListView getImagesListview() {
        return imagesListview;
    }

    public void setImagesListview(ListView imagesListview) {
        this.imagesListview = imagesListview;
    }

    private class RVScrollListener extends RecyclerView.OnScrollListener {

        private String CLASS_TAG = RVScrollListener.class.getSimpleName();
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "Start");
            firstVisiblePos = lm.findFirstCompletelyVisibleItemPosition();
            Bitmap bm = getCachedResultIfavailable(firstVisiblePos);
            if(bm != null){
                setImageforPosition(firstVisiblePos, bm);
            } else {

            }
            lastVisiblePos = lm.findLastCompletelyVisibleItemPosition();
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "FirstVisibleItemPos: "+lm.findFirstVisibleItemPosition());
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "FirstCompletelyVisibleItemPos: "+firstVisiblePos);
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "LastVisibleItemPos: "+lm.findLastVisibleItemPosition());
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "LastCompletelyVisibleItemPos: "+lastVisiblePos);

            switch(newState){
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "Dragging");
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "Idle");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "Settling");
                    break;
            }
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrollStateChanged", "End");

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            AppLogger.addLogMessage(AppLogger.DEBUG, CLASS_TAG, "OnScrolled", "dy: "+dy);

        }
    }
    @Override
    public void OnFailure(Integer key, int errorCode) {

    }

    @Override
    public void OnSuccess(Integer key, Bitmap bitmap) {
        int position = key.intValue();
        View holderView = getmLayoutManager().findViewByPosition(position);
        if(holderView != null){
            ImageView imageView = (ImageView)holderView.findViewById(R.id.ic_main_image);
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void OnStatus(Integer key, int statusCode) {
        if(statusCode == CLEAR_BITMAP_SPACE){
            int position = key.intValue();
            View holderView = getmLayoutManager().findViewByPosition(position);
            if(holderView != null){
                ImageView imageView = (ImageView)holderView.findViewById(R.id.ic_main_image);
                imageView.setImageBitmap(null);
            }
        }

    }

}
