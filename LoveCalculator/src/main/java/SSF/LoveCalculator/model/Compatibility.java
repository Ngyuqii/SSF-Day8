package SSF.LoveCalculator.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Compatibility {
    private String fName;
    private String sName;
    private Integer percentage;
    private String result;
    private String id;

    //Getters
    public String getfName() {
        return fName;
    }
    public String getsName() {
        return sName;
    }
    public Integer getPercentage() {
        return percentage;
    }
    public String getResult() {
        return result;
    }
    public String getId() {
        return id;
    }

    //Setters
    public void setfName(String fName) {
        this.fName = fName;
    }
    public void setsName(String sName) {
        this.sName = sName;
    }
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public void setId(String id) {
        this.id = id;
    }

    //Constructor
    public Compatibility() {
        this.id = generateId(8);
    }

    //Generate random id
    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder strBuilder = new StringBuilder();
        while (strBuilder.length() < numChars) {
            strBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return strBuilder.toString().substring(0, numChars);
    }

    public static Compatibility create(String json) throws IOException {
        Compatibility c = new Compatibility();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();

            // remove encoding chars from API
            String person1Name = URLDecoder.decode(o.getString("fname"), "UTF-8");
            String person2Name = URLDecoder.decode(o.getString("sname"), "UTF-8");

            c.setfName(o.getString(person1Name));
            c.setsName(o.getString(person2Name));
            c.setPercentage(Integer.parseInt(o.getString("percentage")));
            c.setResult(o.getString("result"));
        }
        return c;
    
    }

}