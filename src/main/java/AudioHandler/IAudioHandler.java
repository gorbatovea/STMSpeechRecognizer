package AudioHandler;

public interface IAudioHandler {
    public void listen();
    public String getResult();
    public boolean isFetched();
    public void stop();
}
