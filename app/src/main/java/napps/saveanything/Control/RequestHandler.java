package napps.saveanything.Control;

/**
 * Created by nithesh on 6/16/2016.
 */
/*
    Singleton instance created using double checked locking mechanism. For the reason
    http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples

 */
public class RequestHandler {

    private static RequestHandler sInstance;

    public static RequestHandler getInstance(){
        if(sInstance == null){
            synchronized (RequestHandler.class){
                if(sInstance == null){
                    sInstance = new RequestHandler();
                }
            }
        }

        return sInstance;
    }

    private RequestHandler(){

    }

}
