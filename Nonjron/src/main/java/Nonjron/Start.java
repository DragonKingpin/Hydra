package Nonjron;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Start {
    private final static String HtmlRootPath = "src/HTML";
    private static final String FILENAME = "src/SQL/gamedata.sql";


    public static void main(String[] args) {
        Download download = new Download(0, 2);
        download.lootFromRange();





        readFolder(HtmlRootPath);
    }

    public static void readFolder(String rootPath) {               //解析文件夹下的hmtl文件
        File folder = new File(rootPath);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".html")) {
                List<String> lines = null;
                try {
                    lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (lines != null) {
                    String content = String.join("\n", lines);
                    readHtml(content);
                }
            }
        }
    }

    public static void readHtml(String content) {
        Document document = Jsoup.parse(content);
        String coverUrl = document.select("#cover img").attr("src");
        String gameName = document.select(".mb h1").text();
        String score = document.select(".info-score .mobyscore").text();
        String description = document.select("#description-text p").text();
        Elements releaseData = document.select("#platformLinks li");
        Elements publishersData = document.select("#publisherLinks li");
        Elements DevelopersData = document.select("#developerLinks li");
        Elements genreData = document.select(".info-genres .metadata dd").eq(0).select("a");
        Elements priceData = document.select("#gamePrices li");
        ArrayList<Object> releaseRoot = new ArrayList<>();
        ArrayList<Object> publishersRoot = new ArrayList<>();
        ArrayList<Object> DevelopersRoot = new ArrayList<>();
        ArrayList<Object> genreRoot = new ArrayList<>();
        HashMap<String, Object> priceRoot = new HashMap<>();
        for (Element release : releaseData) {
            HashMap<String, String> child = new HashMap<>();
            child.put("time", release.select("a").eq(0).text());
            child.put("platform", release.select("span a").text());
            releaseRoot.add(child);
        }
        for (Element publish : publishersData) {
            HashMap<String, String> child = new HashMap<>();
            child.put("publishers", publish.select("a").text());
            publishersRoot.add(child);
        }
        for (Element developer : DevelopersData) {
            HashMap<String, String> child = new HashMap<>();
            child.put("developer", developer.select("a").text());
            DevelopersRoot.add(child);
        }
        for (Element genre : genreData) {
            genreRoot.add(genre.text());
        }
        for (Element price : priceData) {
            String type = price.select("small").text();
            Elements payPlatformList = price.select("a");
            ArrayList<String> child = new ArrayList<>();
            for (Element payPlatform : payPlatformList) {
                child.add(payPlatform.text());
            }
            priceRoot.put(type, child);
        }
        writeDataToSql(coverUrl,gameName,description,releaseRoot,publishersRoot,DevelopersRoot,genreRoot,priceRoot);
    }

    public static void writeDataToSql(String coverUrl, String gameName, String description, ArrayList<Object> releaseRoot,
                                      ArrayList<Object> publishersRoot, ArrayList<Object> DevelopersRoot, ArrayList<Object> genreRoot,
                                      HashMap<String, Object> priceRoot) {
        try{
            String sql = "";
            FileWriter writer = new FileWriter(FILENAME,true);
            sql = String.format("INSERT INTO spe_game_videogame VALUES (0,'%s','%s','%s','%s','%s');\n",
                    gameName.replace("'", "''"),new JSONObject(priceRoot).toString().replace("'", "''"),coverUrl.replace("'", "''"),
                    description.replace("'", "''"),new JSONArray(genreRoot).toString().replace("'", "''"));
            writer.write(sql);
            for(int i=0;i<DevelopersRoot.size();i++){
                HashMap<String,String> developerMap = (HashMap<String, String>) DevelopersRoot.get(i);
                sql = String.format("INSERT INTO spe_game_developer (developer_name) " +
                                "SELECT '%s' " +
                                "WHERE NOT EXISTS (SELECT * FROM spe_game_developer WHERE developer_name = '%s');\n",
                        developerMap.get("developer").replace("'", "''"),developerMap.get("developer").replace("'", "''"));
                writer.write(sql);
                sql = String.format("INSERT INTO spe_game_videogame_developer (developer_id, videogame_id) " +
                                "SELECT developer.id, videogame.id " +
                                "FROM spe_game_developer developer " +
                                "CROSS JOIN spe_game_videogame videogame " +
                                "WHERE developer.developer_name = '%s' " +
                                "  AND videogame.token = '%s';\n",
                        developerMap.get("developer").replace("'", "''"),gameName.replace("'", "''"));
                writer.write(sql);
            }
            for(int i=0;i<releaseRoot.size();i++){
                HashMap<String,String> releaseMap = (HashMap<String, String>) releaseRoot.get(i);
                sql = String.format("INSERT INTO spe_game_platform (platform_name) " +
                                "SELECT '%s' " +
                                "WHERE NOT EXISTS (SELECT * FROM spe_game_platform WHERE platform_name = '%s'););\n",
                        releaseMap.get("platform").replace("'", "''"),releaseMap.get("platform").replace("'", "''"));
                writer.write(sql);
                sql=String.format("INSERT INTO spe_game_release (videogame_id, platform_id, release_date) " +
                                "SELECT videogame.id, platform.id, '%s' " +
                                "FROM spe_game_videogame videogame " +
                                "CROSS JOIN spe_game_platform platform " +
                                "WHERE videogame.token = '%s' " +
                                "  AND platform.platform_name = '%s';\n",
                        releaseMap.get("time").replace("'", "''"),gameName.replace("'", "''"),
                        releaseMap.get("platform").replace("'", "''"));
                writer.write(sql);
            }
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
