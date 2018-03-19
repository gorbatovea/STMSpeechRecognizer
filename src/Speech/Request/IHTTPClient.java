package Speech.Request;

import javafx.util.Pair;

import java.util.ArrayList;

public interface IHTTPClient {
    String postRequest(final String host, final ArrayList<Pair<String, String>> header, final byte[] body);
}
