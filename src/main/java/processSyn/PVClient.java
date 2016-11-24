package processSyn;

/**
 * Created by hzg on 2016/11/11.
 */
public class PVClient {
    public static void main(String[] args) {
        SyncStack ss = new SyncStack();
        PClient p = new PClient(ss);
        VClient v = new VClient(ss);

        new Thread(p).start();
        new Thread(v).start();
    }
}
