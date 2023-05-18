package Nonjron;

import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Download {
    private static final String indexPath = "src/JSON/game.json";  //索引文件
    private static final String downloadPath = "src/HTML/";  //html文件下载目录
    private final int maxRetries = 10;  //最大重试数

    private int from;
    private int to;
    private static List<String> arrayList = (new JSONArray( readJson(indexPath) ).getArray() );

    public Download(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public void lootFromRange() {
        int retryCount = 0;
        boolean success = false;

        for (int i = this.from; i <= this.to && !success; i++) {
            while (!success && retryCount < maxRetries) {
                try {
                    lootById(i);
                    success = true;
                    System.out.println("i:" +i+" to:"+this.to);
                } catch (Exception e) {
                    e.printStackTrace();
                    retryCount++;
                }
            }
            if (success) {
                retryCount = 0;
                success = false;
            }
        }
    }
    public void lootFromRange(int from,int to) {
        int maxRetries = 3;
        int retryCount = 0;
        boolean success = false;

        for (int i = from; i <= to && !success; i++) {
            while (!success && retryCount < maxRetries) {
                try {
                    lootById(i);
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    retryCount++;
                }
            }
            if (success) {
                retryCount = 0;
                success = false;
            }
        }
    }

    public void lootById(int id) throws Exception {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        URL url = new URL(arrayList.get(id));
        System.out.println(arrayList.get(id));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.35");

        inputStream = connection.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        bufferedReader = new BufferedReader(inputStreamReader);
        String s;
        File dest = new File(downloadPath + arrayList.get(id).split("/")[4]+ ".html");
        fileOutputStream = new FileOutputStream(dest);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        while ((s = bufferedReader.readLine()) != null) {
            outputStreamWriter.write(s);
        }
        outputStreamWriter.close();
        fileOutputStream.close();
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();

    }

    private static String readJson(String jsonPath) {
        File jsonFile = new File(jsonPath);
        try {
            FileReader fileReader = new FileReader(jsonFile);
            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            while (true) {
                int ch = reader.read();
                if (ch != -1) {
                    sb.append((char) ch);
                } else {
                    break;
                }
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }


}
