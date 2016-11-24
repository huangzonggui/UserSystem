package processSyn;

/**
 * Created by hzg on 2016/11/11.
 * 同步块，碗类
 * 碗中消息来通知生产者该干嘛，消费者该干嘛
 */
public class SyncStack {
    private int index = 0;
    ManTou[] arrMT = new ManTou[6];

    //添加馒头
    public synchronized void push(ManTou mt) {
        //篮子满了
        while (index == arrMT.length) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notifyAll();//唤醒对象
        arrMT[index] = mt;
        index++;
    }

    //吃馒头
    public synchronized ManTou pop() {
        //馒头吃完了
        while (index == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notifyAll();
        index--;
        return arrMT[index];
    }
}
