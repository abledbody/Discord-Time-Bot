package com.github.hehe3301.time_handler;
import com.github.hehe3301.bot.Command;
import com.github.hehe3301.conditional_print.CP;
import com.github.hehe3301.configs.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hehe3 on 10/7/2018.
 */
public class TimeHandler{

    private Map<String, ArrayList<String>> aliasMap = new HashMap<>();

    public String now()
    {
        return now("UTC");
    }

    public String now(String time_zone)
    {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(time_zone.toUpperCase()));
        //Time in GMT

        //Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        SimpleDateFormat justTime= new SimpleDateFormat("HHmm");
        Date time;
        try {
             time = dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return "!PARSE ERROR!";
        }

        return "The time is now: "+justTime.format(time) +" "+time_zone;
    }

    private void loadAliases(String pFilename)
    {
        String csvFile = Settings.alias_file;
        String line = "";
        String cvsSplitBy = ",";

        String basePath = new File("").getAbsolutePath();
        System.out.println(basePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] alias_line = line.split(cvsSplitBy);
                String alias = alias_line[0].replaceAll("\"","").toUpperCase();
                aliasMap.put(alias, new ArrayList() );
                aliasMap.get(alias).add(0, alias_line[1].replaceAll("\"","").toUpperCase());
                aliasMap.get(alias).add(1, alias_line[2].replaceAll("\"","").toUpperCase());
                CP.cLog(Settings.debug_enabled, "Adding: " + alias + " as " + aliasMap.get(alias).get(0) + ", " + aliasMap.get(alias).get(1) + "\n" );

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CP.cLog(Settings.debug_enabled, "Aliases loaded: "+ aliasMap.size() +"\n");
    }

    public TimeHandler() {
        loadAliases(Settings.alias_file);
    }
}
