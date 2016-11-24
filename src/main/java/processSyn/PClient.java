package processSyn;

public class PClient implements Runnable {

    private SyncStack ss=null;
    public PClient(SyncStack ss){
        this.ss=ss;
    }

    @Override
    public void run() {
        for(int i=0; i<20; i++) {
            ManTou mt = new ManTou(i);
            ss.push(mt);
            System.out.println("生产了：" + mt);
            try {
                //1秒钟吃一个馒头
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}