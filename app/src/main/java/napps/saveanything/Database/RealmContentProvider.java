package napps.saveanything.Database;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Clip;
import napps.saveanything.Model.Image;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.view.Activities.SaveImageActivity;

/**
 * Created by "nithesh" on 11/13/2016.
 */

public class RealmContentProvider {


    /* The parameters are made final so as to avoid modification to them inside method */
    /*This does not require synchronized keyword because the transaction is thread safe */
    public static void insertClip(final Builder.ClipBuilder builder){
        //Even though we are creating a custom configuration in application class, we are still calling get default instance of realm.
        //This will return our custom configuration because we have set the custom configuration as default configuration.
        Realm realm = Realm.getDefaultInstance();
        /*Reason for using executeTransaction instead of begin and close transaction. Check using realm like a pro in android im points */
        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm backgroundRealm) {
                    Clip newClip = backgroundRealm.createObject(Clip.class, builder.id);
                    builder.build(newClip);
//                    newClip.setId(builder.id);
//                    newClip.setClipId(builder.clipId);
//                    newClip.setContent(builder.content);
//                    newClip.setClipStatus(builder.clipStatus);
//                    newClip.setContentType(builder.contentType);
//                    newClip.setSourcePackage(builder.sourcePackage);
//                    newClip.setTimeStamp(builder.timestamp);
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    /*
        This method is just for inserting basic details of the image
     */
    public static void insertImage(final Builder.ImageBuilder builder){

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundRealm) {
                    Image image = backgroundRealm.createObject(Image.class, builder.id);
                    builder.build(image);
//                    image.setImageId(Utils.getUniqueId(context, Constants.PREFIX_IMAGE));
//                    image.setOriginalPath(imageContent.getImageUri().toString());
//                    image.setStatus(Constants.STATUS_IMAGE_ADDED);
//                    image.setTimeStamp(System.currentTimeMillis());
//                    // Good approach
//                    // Get edit text reference only when it's required
//                    image.setDesc(desc);
//                    image.setScaleStatus(imageContent.getScaleStatus());
//                    image.setSourceHeight(imageContent.getHeight());
//                    image.setSourceWidth(imageContent.getWidth());
//
//                    // set the current selected sample
//                    image.setScaleFactor(scaleFactor);
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);

        } catch (Exception e){

        } finally {
            realm.close();
        }
    }

    public static synchronized RealmResults<Clip> getAllClipsForDisplay(int sortType){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Clip.class).findAllSorted(RealmContract.ClipFields.FIELD_NAME_TIMESTAMP, Sort.DESCENDING);
    }

    public static synchronized RealmResults<Image> getAllImagesForDisplay(int sortType){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Image.class).findAllSorted(RealmContract.ImageFields.FIELD_NAME_TIMESTAMP, Sort.DESCENDING);
    }

    /* Realm has the concept of managed and unmanaged objects. Managed objects are created using realm create object where as
    unmanaged objects are created by normal java object creation and then copied to realm to change to managed objects
     */
    public static Clip queryClip(long Id){
        Realm realm = Realm.getDefaultInstance();
        Clip clip = realm.where(Clip.class).equalTo(RealmContract.ClipFields.FIELD_NAME_ID, Id).findFirst();
        return clip;
    }
    public static Image queryImage(long Id){
        Realm realm = Realm.getDefaultInstance();
        Image image = realm.where(Image.class).equalTo(RealmContract.ImageFields.FIELD_NAME_ID, Id).findFirst();
        return image;
    }

    public static void updateClip(long Id, final Builder.ClipBuilder builder){
        final Clip clip = queryClip(Id);
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundRealm) {
                    builder.build(clip);
               }
            }/*   TODO: Need to implement OnError to retry insertion  */);

        } catch (Exception e){
            //TODO: Put Logs
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }

    public static void updateImage(long Id, final Builder.ImageBuilder builder){
        final Image image = queryImage(Id);
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundRealm) {
                    builder.build(image);
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);

        } catch (Exception e){
            //TODO: Put Logs
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    public static void deleteClip(long Id){
        final Clip clip = queryClip(Id);
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundRealm) {
                    clip.deleteFromRealm();
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);

        } catch (Exception e){
            //TODO: Put Logs
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    public static void deleteImage(long Id){
        final Image image = queryImage(Id);
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundRealm) {
                    image.deleteFromRealm();
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);

        } catch (Exception e){
            //TODO: Put Logs
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

}
