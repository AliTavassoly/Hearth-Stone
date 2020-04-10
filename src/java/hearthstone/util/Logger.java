package hearthstone.util;

import hearthstone.data.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

import static hearthstone.HearthStone.currentAccount;
import static hearthstone.HearthStone.dataPath;

public class Logger {
    public static void saveLog(String title, String description, String username) throws Exception {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        FileWriter fileWriter = new FileWriter(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".log", true);
        String newLog;
        if (title.equals("ERROR"))
            newLog = title + " @ " + ts + "\n" + description + "\n" + "\n";
        else
            newLog = title + " @ " + ts + "\n" + "INFO : " + description + "\n" + "\n";
        fileWriter.append(newLog);

        fileWriter.close();
    }

    public static void saveLog(String title, String description) throws Exception {
        saveLog(title, description, currentAccount.getUsername());
    }

    public static void createAccountLog(String username) throws Exception {
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        File logFile = new File(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".log");
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();

        FileWriter fileWriter = new FileWriter(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".log", true);
        String newLog = "Log File" + "\n" + username + " @ Hearth Stone" + "\n" + "\n";
        fileWriter.append(newLog);
        fileWriter.close();

        saveLog("register", "signed up successfully!", username);
    }

    public static String exceptionToLog(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}