package hearthstone.util;

import hearthstone.data.Data;

import java.io.File;
import java.io.FileWriter;
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
        String newLog = username + " @ " + "HearthStone" + "\n";
        fileWriter.append(newLog);

        newLog = title + " @ " + ts + " -> " + description + "\n";
        fileWriter.append(newLog);

        fileWriter.close();
    }

    public static void saveLog(String title, String description) throws Exception {
        saveLog(title, description, currentAccount.getUsername());
    }

    public static void createAccountLog(String username) throws Exception {
        File logFile = new File(dataPath + "/logs" + "/account_" + Data.getAccountId(username) + ".log");
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();
        saveLog("Register", "signed up successfully!", username);
    }

}
