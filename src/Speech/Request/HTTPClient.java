package Speech.Request;

import com.sun.org.apache.bcel.internal.classfile.CodeException;
import javafx.util.Pair;

import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HTTPClient implements IHTTPClient {

    @Override
    public String postRequest( final String host, final ArrayList<Pair<String, String>> contentType, final byte[] body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(host).openConnection();
            connection.setRequestMethod("POST");
            for(Pair<String, String> pair : contentType){
                connection.setRequestProperty(pair.getKey(), pair.getValue());
            }
            connection.setDoOutput(true);
            DataOutputStream writeStream = new DataOutputStream(connection.getOutputStream());
            writeStream.write(body);
            writeStream.flush();
            writeStream.close();
            if (connection.getResponseCode() != 200)
                return null;
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String output;
            StringBuffer response = new StringBuffer();
            while((output = input.readLine()) != null){
                response.append(output);
            };
            input.close();
            return response.toString();
        }catch (java.net.MalformedURLException mE){
            //Needs to be logged
            mE.printStackTrace();
            System.exit(1);
        }catch (java.net.ProtocolException pE){
            //Needs to be logged
            pE.printStackTrace();
            System.exit(1);
        }catch (IOException ioE){
            //Needs to be logged
            ioE.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
