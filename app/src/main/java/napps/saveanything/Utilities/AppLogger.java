package napps.saveanything.Utilities;

import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Created by nithesh on 6/1/2016.
 */
public class AppLogger {

    //Static Design:
    //This list is made static because it should be accessed from service without object creation
    //so as to clear memory by saving Log data to a text file in 2 conditions
    //1. When device is on LowMemory()
    //2. It reaches limit
    //3. When the app goes background
    //4. Writing each log message to file everytime has computation overhead
    //This can be replaced with List<String> holding only list of strings which saves lot of memory
    //but for robust model design, this class is kept
    public static List<String> logsList;

    private static int LOG_MESSAGE_LIMIT = 50;

    //We could have kept all the static final constants in one file.
    //To access static final variables class is just loaded without object creation
    //We could have avoided little overhead of class loading by moving it to Constants file
    //Since these constants are required for static method addLogMessage which requires class loading so constants are kept here
    public static final String LOG = "Log";

    public static final String VERBOSE = LOG + "/Verbose: ";

    public static final String DEBUG = LOG + "/Debug: ";

    public static final String INFO = LOG + "/Info: ";

    public static final String WARN = LOG +"/Warning: ";

    public static final String ERROR = LOG +"/Error: ";


    static {
        //Design : Singleton Pattern
        //Since this is a class used many times for logging messages so can't create objects each and every time which creates object creation overhead
        //Lazy Initialization is not effective as it keeps checking value and should be called everytime while accessing class
        //This creates instance as soon as the class is loaded and can directly call methods
        //we can also use public static logMessagesList = new ArrayList<LogMessages>; but better is yet to be decided
        //LinkedList Design:
        //1. ArrayList initially creates and allocates memory for default initial capacity of objects which we don't need since we have static reference
        //2. We only need add and iterate functionalities which is best in linKedList case
        logsList = new LinkedList<String>();
    }

    public static void addLogMessage(String logType, String sourceClass, String methodName, String message){
        LogMessage log = new LogMessage();
        log.logType = logType;
        log.timeStamp = System.currentTimeMillis();
        log.sourceClass = sourceClass;
        log.methodName = methodName;
        log.message = message;
        if(logsList.size() <= LOG_MESSAGE_LIMIT){
            //Start or bind to connected Service to save log messages and empty arraylist
            logsList.add(log.toString());
        }

        Log.d(Constants.APPLICATION_TAG, log.toString());
    }




    //Class Design
    // 1. Since LogMessage objects get created frequently and not a part of app data/ operations it is kept here
    // 2. Getters and Setters are not used because frequent messages arrive and getting/setting would consume extra overhead of method stack

    public static class LogMessage {

        public long timeStamp = 0;
        public String logType = "";

        public String sourceClass = "";
        public String methodName = "";
        public String message = "";

        //These are not essentially required as they consume memory when the object is in heap
        //These are added only for the soul purpose of code readability. Can be removed later
        public String TAB_SEPARATOR = "\t";

        public String SPACE_SEPARATOR = " ";

        public String CLASS_OPEN_BRACE = "[";

        public String CLASS_CLOSE_BRACE = "]";

        public String LOG_START = "[START]";

        public String LOG_END = "[END]\n";

        public String METHOD_INDICATOR = "()";

        @Override
        public String toString() {
            String textLogMessage;
            String date = DateFormat.getInstance().format(new Date(timeStamp));
            textLogMessage =            LOG_START       +   SPACE_SEPARATOR     +
                                        date            +   SPACE_SEPARATOR     +
                                        logType         +   TAB_SEPARATOR       +   CLASS_OPEN_BRACE+
                                        sourceClass     +   CLASS_CLOSE_BRACE   +   SPACE_SEPARATOR +
                                        methodName      +   METHOD_INDICATOR    +   SPACE_SEPARATOR +
                                        message         +   SPACE_SEPARATOR     +
                                        LOG_END;

            return textLogMessage;
        }
    }


 }
