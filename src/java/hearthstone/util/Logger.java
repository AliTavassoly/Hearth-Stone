package hearthstone.util;

import hearthstone.client.HSClient;
import hearthstone.server.Main;
import hearthstone.server.data.ServerData;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

public class Logger {
    public static void saveLog(String title, String description, String username)
            throws Exception {
        /*Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        FileWriter fileWriter = new FileWriter(Main.dataPath + "/logs" +
                "/account_" + ServerData.getAccountId(username) + ".log", true);
        String newLog;
        if (title.equals("ERROR"))
            newLog = title + " @ " + ts + "\n" + description + "\n" + "\n";
        else
            newLog = title + " @ " + ts + "\n" + "INFO : " + description + "\n" + "\n";
        fileWriter.append(newLog);

        fileWriter.close();*/
    }

    public static void saveLog(String title, String description) throws Exception {
        //saveLog(title, description, HSClient.currentAccount.getUsername());
    }

    public static void createAccountLog(String username) throws Exception {
        /*Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        File logFile = new File(Main.dataPath + "/logs" + "/account_" + ServerData.getAccountId(username) + ".log");
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();

        FileWriter fileWriter = new FileWriter(Main.dataPath + "/logs" + "/account_" + ServerData.getAccountId(username) + ".log", true);
        String newLog = "Log File" + "\n" + username + " @ Hearth Stone" + "\n" + "\n";
        fileWriter.append(newLog);
        fileWriter.close();

        saveLog("register", "signed up successfully!", username);*/
    }

    public static String exceptionToLog(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
