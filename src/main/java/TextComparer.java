import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TextComparer {

    //create log4j logger
    static final Logger rootLogger = LogManager.getRootLogger();

    public static void main(String[] args) {

        String originFilename = null;
        String modifiedFilename = null;

        //check if arguments provided
        if (args.length == 0){
            rootLogger.error("no any arguments found");//log process
            System.out.println("no arguments found. use \"/?\" for help ");
        } else {
            // check keys entered
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "/o": //get original file name
                        originFilename = args[i + 1];
                        rootLogger.debug("got original filename");//log process
                        break;
                    case "/m": //get modified file name
                        modifiedFilename = args[i + 1];
                        rootLogger.debug("got modified filename");//log process
                        break;
                    case "/?": //print out short help
                    case "/help":
                        System.out.println("TextComparer /o <original file name> /m <modified file name>");
                        System.exit(0);
                        break;
                }
            }

            try {
                //throw exception if filename is not defined
                assert originFilename != null;
                //read lines from file
                ArrayList<String> originalLines = (ArrayList<String>) Files.readAllLines(Paths.get(originFilename));
                rootLogger.debug("original file successfully read"); //log process
                assert modifiedFilename != null;
                ArrayList<String> modifiedLines = (ArrayList<String>) Files.readAllLines(Paths.get(modifiedFilename));
                rootLogger.debug("modified file successfully read");//log process
                Patch<String> patch = DiffUtils.diff(originalLines, modifiedLines);
                System.out.println("differences:");
                //iterate differences found and print them out
                for (AbstractDelta<String> delta : patch.getDeltas()) System.out.println(delta);
            } catch (IOException e) {
                rootLogger.error("file reading failed");//log process
                System.out.println("error reading file");
            } catch (NullPointerException e) {
                rootLogger.error("file not found");//log process
                System.out.println("File not found. use \"/?\" for help");
            } catch (Exception e) {
                rootLogger.error("unknown error : " + e.getMessage());//log process
                System.out.println("unknown error. read log file for more information");
            }
        }
    }


}
