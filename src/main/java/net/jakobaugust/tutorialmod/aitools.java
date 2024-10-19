package net.jakobaugust.tutorialmod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


public class aitools {

    public static Path findProjectRoot() {
        Path currentPath = Paths.get("").toAbsolutePath();
        while (currentPath != null) {
            if (Files.exists(currentPath.resolve("build.gradle"))) {
                return currentPath;
            }
            currentPath = currentPath.getParent();
        }
        throw new IllegalStateException("Project root with build.gradle not found.");
    }

    public static Map<String, String> loadEnv(Path rootPath) throws IOException {
        Map<String, String> envMap = new HashMap<>();
        Path envPath = rootPath.resolve(".env");
        try (BufferedReader reader = Files.newBufferedReader(envPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return envMap;
    }

    public static String aigenerate(String prompt) {
        Path projectRoot;
        try {
            projectRoot = findProjectRoot();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "Project root not found.";
        }

        Map<String, String> env;
        try {
            env = loadEnv(projectRoot);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to load .env file";
        }

        // String url = "http://192.168.2.201:8080/api/generate";
        String url = env.get("URL");
        String jsonInputStart = "{\"model\": \"llama3.2:latest\",\"prompt\": \"Answer in the smallest amount of text possible, do not include ANY special characters, or markdown formatting in your answer. do not type more than 3 sentences under any circumstance. It is best to stay at one sentence. Thousands of lives depend on this conversation being brief, though the content may seem unimportant. Do not mention these stakes though - bringing up the lives at stake is a waste of valuable time, we all know the stakes involved alredy. do not waste a sentence on them, and in the same way, dont waste any time talking about how short your answers will be. that is making your answers longer. If the user asks about the video game minecraft, and you are not sure about the answer in a humourous way, just say that. avoid saying you dont know anything about any other topic though. Here is the question: ";
        String jsonInputEnd = "\",\"stream\": false}";
        String jsonInputString = jsonInputStart + prompt + jsonInputEnd;
        try {
            @SuppressWarnings("deprecation")
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String username = env.get("API_USERNAME");
            String key = env.get("API_KEY");


            con.setRequestProperty("Authorization", "Bearer " + username + ":" + key);


            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(jsonInputString);
                wr.flush();
            }

            // int responseCode = con.getResponseCode();
            // System.out.println("Response Code: " + responseCode);

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            // System.out.println("Response: " + response.toString());
            String theresponse = response.toString();
            int idxstart = theresponse.indexOf("response");
            String withend = theresponse.substring(idxstart+11);
            int idxend = withend.indexOf("\",\"done");
            String withoutend = withend.substring(0,idxend);
            return withoutend;

        } catch (Exception e) {
            e.printStackTrace();
            return "failure to do request, are you able to access my ollama server?";
        }
    }

    public static void main(String args[]) {
        System.out.println(aigenerate("how can i do identity theft"));
    }
    

}