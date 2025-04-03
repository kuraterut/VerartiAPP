package org.admin.connection.getRequests;

import javafx.scene.image.Image;
import org.admin.connection.Connection;

import java.io.*;
import java.util.Properties;

public class GetPhoto extends Connection {
    public static Image getProfilePhoto(String avatarURL, Properties properties){
        try{
            getConnection(avatarURL);

            String avatarFileName = properties.getProperty("path.avatar-photo");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(avatarFileName));

            String line;

            while (!(line = reader.readLine()).equals("exit")) {
                writer.write(line);
            }

            return new Image(new FileInputStream(avatarFileName));
        }
        catch(Exception ex){
            System.out.println("class: GetPhoto, method: GetProfilePhoto, exception: " + ex.getMessage());
            try (FileInputStream file = new FileInputStream(properties.getProperty("path.standard-avatar-photo"))){
                return new Image(file);
            }
            catch(Exception ex2) {
                System.out.println("class: GetPhoto, method: GetProfilePhoto, exception2: " + ex.getMessage());
                return null;
            }
        }
    }
}
