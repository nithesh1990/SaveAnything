package napps.saveanything.Database;

import android.content.Context;
import android.support.design.widget.TextInputEditText;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import napps.saveanything.Model.Clip;
import napps.saveanything.Model.Image;
import napps.saveanything.R;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.view.Activities.SaveImageActivity;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class RealmContentProvider {


    /* The parameters are made final so as to avoid modification to them inside method */
    public static void insertClip(final Context context, final String packageName, final String content){
        //Even though we are creating a custom configuration in application class, we are still calling get default instance of realm.
        //This will return our custom configuration because we have set the custom configuration as default configuration.
        Realm realm = Realm.getDefaultInstance();
        /*Reason for using executeTransaction instead of begin and close transaction. Check using realm like a pro in android im points */
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                Clip newClip = backgroundRealm.createObject(Clip.class);
                newClip.setClipId(Utils.getUniqueId(context, Constants.PREFIX_CLIP));
                newClip.setContent(content);
                newClip.setClipStatus(Constants.STATUS_CLIP_SAVED);
                if(Utils.isNetworkUrl(content)){
                    newClip.setContentType(Constants.CLIP_HTTP_LINK);
                } else {
                    newClip.setContentType(Constants.CLIP_PLAIN_TEXT);
                }
                newClip.setSourcePackage(packageName);
                newClip.setTimestamp(System.currentTimeMillis());
            }
        }/*   TODO: Need to implement OnError to retry insertion  */);

    }

    /*
        This method is just for inserting basic details of the image
     */
    public static void insertImage(final Context context, final String desc, final int scaleFactor, final SaveImageActivity.SaveImageContent imageContent){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm backgroundRealm) {
                Image image = backgroundRealm.createObject(Image.class);
                image.setImageId(Utils.getUniqueId(context, Constants.PREFIX_IMAGE));
                image.setOriginalPath(imageContent.getImageUri().toString());
                image.setStatus(Constants.STATUS_IMAGE_ADDED);
                image.setTimestamp(System.currentTimeMillis());
                // Good approach
                // Get edit text reference only when it's required
                image.setDesc(desc);
                image.setScaleStatus(imageContent.getScaleStatus());
                image.setSourceHeight(imageContent.getHeight());
                image.setSourceWidth(imageContent.getWidth());

                // set the current selected sample
                image.setScaleFactor(scaleFactor);
            }
        }/*   TODO: Need to implement OnError to retry insertion  */);
    }

    public static synchronized RealmResults<Clip> getAllClipsForDisplay(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Clip.class).findAllSorted("Timestamp", Sort.DESCENDING);
    }

    public static synchronized RealmResults<Image> getAllImagesForDisplay(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Image.class).findAllSorted("Timestamp", Sort.DESCENDING);
    }

}
