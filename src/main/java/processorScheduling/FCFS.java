package processorScheduling;

/**
 * Created by hzg on 2016/11/24.
 */
public class FCFS {
    public FCFS() {
    }

    public int fcfs(int N, String a[][]) {
        int i, j, n, p;
        int firstNum = 0;
        double comeTime, startTime, num;//comeTime提交时间、startTime运行时间、num进程号
        double b[][] = new double[N][7];
        // 读取进程号，提交时间，运行时间
        for (i = 0; i < N; i++) {
            for (j = 0; j < 3; j++) {
                b[i][j] = Double.parseDouble(a[i][j]);
            }
        }

        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                //比较提交时间，提交时间按从小到大排列，从上往下排
                if (b[i][1] <= b[j][1]) {
                    comeTime = b[i][1];//交换提交时间
                    b[i][1] = b[j][1];
                    b[j][1] = comeTime;
                    startTime = b[i][2];//交换运行时间
                    b[i][2] = b[j][2];
                    b[j][2] = startTime;
                    num = b[i][0];//交换进程号
                    b[i][0] = b[j][0];
                    b[j][0] = num;
                }
            }
        }

        //第一个进程的运行情况
        b[0][3] = b[0][1];//开始时间=提交时间
        b[0][4] = b[0][3] + b[0][2];//结束时间=开始时间+运行时间
        b[0][5] = b[0][4] - b[0][1];//周转时间=结束时间-提交时间
        b[0][6] = b[0][5] / b[0][2];//带权周转时间=周转时间/运行时间
        //第二个以后的进程运行的情况
        for (i = 1; i < N; i++) {
            //如果前一个进程的结束时间>=提交时间
            if (b[i - 1][4] >= b[i][1]) {
                b[i][3] = b[i - 1][4];//开始时间直接等于前一个进程的结束时间（提交了还没有结束）
                b[i][4] = b[i][3] + b[i][2];
                b[i][5] = b[i][4] - b[i][1];
                b[i][6] = b[i][5] / b[i][2];
            } else {//结束时间<提交时间
                b[i][3] = b[i][1];//开始时间等于提交时间（结束了还没有提交）
                b[i][4] = b[i][3] + b[i][2];
                b[i][5] = b[i][4] - b[i][1];
                b[i][6] = b[i][5] / b[i][2];
            }
        }
        return 1;
    }
}
