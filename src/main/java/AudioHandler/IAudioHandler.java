package AudioHandler;

public interface IAudioHandler {
    void listen();
    String getResult();
    boolean isFetched();
    void stop();
}
