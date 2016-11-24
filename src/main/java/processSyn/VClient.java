package processSyn;

/**
 * Created by hzg on 2016/11/11.
 */
public class VClient implements Runnable {
    private SyncStack ss = null;

    public VClient(SyncStack ss) {
        this.ss = ss;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            ManTou mt = ss.pop();
            System.out.println("消费了：" + mt);
            try {
                //6秒钟吃一个馒头
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
