package Speech.Service;

import Speech.Request.HTTPClient;
import Speech.Response.BingResponse;
import com.google.gson.Gson;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Properties;

public class SpeechService implements ISpeechService{
    private String preBuildedHostEndPoint;
    private ArrayList<Pair<String, String>> requestHeader;
    HTTPClient httpClient;
    Properties configuration;

    public SpeechService(Properties props){
        this.configuration = props;
        this.preBuildedHostEndPoint =
                props.getProperty("speech.platform.scheme") +
                props.getProperty("speech.platform.host") +
                props.getProperty("speech.platform.mode") +
                props.getProperty("speech.platform.endpoint");
        this.requestHeader = new ArrayList<>();
        this.requestHeader.add(
                new Pair<String, String>(
                        props.getProperty("post.header.param.content-type"),
                        props.getProperty("post.header.value.content-type")));
        this.httpClient = new HTTPClient();
        this.requestHeader.add(
                new Pair<String, String>(
                        props.getProperty("post.header.param.subscription-key"),
                        props.getProperty("post.header.value.subscription-key")));
    }

    @Override
    public String recognize(byte[] speechLine, String language) throws NullPointerException{
        String host =
                this.preBuildedHostEndPoint +
                        this.configuration.getProperty("speech.platform.param.lang") +
                        language +
                        "&" +
                        this.configuration.getProperty("speech.platform.param.format") +
                        this.configuration.getProperty("format.value.simple");
        String responseString = httpClient.postRequest(host, requestHeader, speechLine);
        if (responseString == null)
            throw new NullPointerException();
        BingResponse bingResponse = new Gson().fromJson(responseString, BingResponse.class);
        return bingResponse.getDisplayText();
    }
}
