package com.arnavdhamija;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    static int backersLineCnt = 0;
    static int supportLineCnt = 0;
    static int aboutLineCnt = 0;
    static int riskLineCnt = 0;
    static String processList(List<String> text) {
        String res="";
        for (String s : text) {
            String regex = "\\[(.*?)\\]";
            s = s.replaceAll(regex, "");
            if (!s.isEmpty()) {
                res += s + "\n";
            }
        }
        return res;
    }

    static ProjectDetails getFileJson(String fileid, List<String> fileText) {
        String json="";
        int backersLine = Integer.MAX_VALUE;
        int aboutLine = Integer.MAX_VALUE;
        int riskLine = Integer.MAX_VALUE;
        int supportLine = Integer.MAX_VALUE;
        boolean hasVideo = false;
        int imageCount = 0;
        int i = 0;
        for (String s : fileText) {
            if (s.contains("You'll need an HTML5 capable browser to see this content")) {
                hasVideo = true;
            }
            if (s.compareTo("   backers")==0) {
                backersLineCnt++;
                backersLine = i;
            }
            if (s.compareTo("About")==0) {
                aboutLineCnt++;
                aboutLine = i;
            }
            if (s.compareTo("Risks and challenges")==0) {
                riskLineCnt++;
                riskLine = i;
            }
            if (s.contains("Learn about accountability on Kickstarter")) {
                supportLineCnt++;
                supportLine = i;
            }
            if (s.contains(".gif")||s.contains(".png")||s.contains(".jpeg")||s.contains(".jpg")) {
                imageCount++;
            }
            i++;
        }
        if (aboutLine==Integer.MAX_VALUE || riskLine==Integer.MAX_VALUE || backersLine==Integer.MAX_VALUE|| supportLine==Integer.MAX_VALUE) {
            return null;
        }
        int startLine = Math.max(backersLine,aboutLine);
        int endLine = riskLine;
        List<String> aboutText = fileText.subList(startLine + 1, endLine);
        List<String> riskText = fileText.subList(riskLine + 1, supportLine);
        String aboutString = processList(aboutText);
        String riskString = processList(riskText);
        String id = fileid;
        ProjectDetails projectDetails = new ProjectDetails(id,imageCount,aboutString,riskString,hasVideo);

//        riskText.removeIf(s -> (s==""));
        return projectDetails;
    }

    public static void main(String[] args) {
        String dir = "/home/nic/ms-work/scraper/allpages/";
        File folder = new File(dir);
        String[] files = folder.list();
        Type listOfTestObject = new TypeToken<List<ProjectDetails>>(){}.getType();
        List<ProjectDetails> allProjects = new ArrayList<>();
        int i = 0;
        for (String file : files)
        {
            try {
                Path path = Paths.get(dir,file);
                List<String> lines = Files.readAllLines(path);//,charset);
                ProjectDetails pd = getFileJson(file,lines);
                if (pd != null) {
                    allProjects.add(pd);
                    System.out.println(i++);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Gson gson = new Gson();
        String allProjectsString = gson.toJson(allProjects, listOfTestObject);
        try (PrintWriter out = new PrintWriter("NewJSON")) {
            out.println(allProjectsString);
        } catch (Exception e) {
        }
        System.out.println("Done " + aboutLineCnt + " " + riskLineCnt +  " "+backersLineCnt+" "+supportLineCnt);
    }
}
