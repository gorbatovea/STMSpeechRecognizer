package Speech.Service;

import java.util.Properties;

public interface ISpeechService {
    public String recognize(byte[] speechLine, String language);
}
