package Speech.Service;

import java.util.Properties;

public interface ISpeechService {
    String recognize(byte[] speechLine, String language);
}
