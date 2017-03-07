package napps.saveanything.Database;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import napps.saveanything.Model.Builder;
import napps.saveanything.Model.Clip;
import napps.saveanything.Model.Image;
import napps.saveanything.Model.Note;
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
                }
            }/*   TODO: Need to implement OnError to retry insertion  */);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    public static void insertNote(final Builder.NoteBuilder builder){
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm backgroundReam) {
                    Note note = backgroundReam.createObject(Note.class, builder.id);
                    builder.build(note);
                }
            });
        } catch(Exception e){

        }finally {
            if(realm != null){
                realm.close();
            }
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

    public static synchronized RealmResults<Note> getAllNotesForDisplay(int sortType){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Note.class).findAllSorted(RealmContract.NoteFields.FIELD_NAME_TIMESTAMP, Sort.DESCENDING);
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

    public static Note queryNote(long Id){
        Realm realm = Realm.getDefaultInstance();
        Note note = realm.where(Note.class).equalTo(RealmContract.NoteFields.FIELD_NAME_ID, Id).findFirst();
        return note;
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

    public static void updateNote(long Id, final Builder.NoteBuilder builder){
        final Note note = queryNote(Id);
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {
                    builder.build(note);
                }
            });
        } catch(Exception e){
            //TODO: Error Handling in update Note
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
            });

        } catch (Exception e){
            //TODO: Delete clip error handling
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    public static void deleteNote(long Id){
        final Note note = queryNote(Id);
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction(){

                @Override
                public void execute(Realm realm) {

                }
            });
        } catch(Exception e){
            //TODO: Handle delete Note error case
        } finally {
            if(realm != null){
                realm.close();
            }
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
            });

        } catch (Exception e){
            //TODO: Delete Image Error handling
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

}
