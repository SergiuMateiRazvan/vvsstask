package serenity.helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import serenity.model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {


    public static User readUser(String fileName){
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(fileName));
            JSONObject jsonObject = (JSONObject) obj;

            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String user = (String) jsonObject.get("user");
            String wrongPassword = (String) jsonObject.get("wrongPassword");
            return new User(username, password, user, wrongPassword);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
