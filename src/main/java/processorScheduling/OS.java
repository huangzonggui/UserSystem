package processorScheduling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by hzg on 2016/11/23.
 */
public class OS extends JFrame implements ActionListener {
    JButton b1, b2, b3, b4;
    JTable table;
    JTextField j2, j3;
    String str = JOptionPane.showInputDialog("请输入进程数：");//JOptionPane代表Swing中的一些对话框
    int N = Integer.parseInt(str);
    String process_num = null;
    String arrive_time = null;
    String service_time = null;
    String start_time = null, end_time = null, total_time = null, weightotal_time = null;
    String a[][] = new String[N][7];
    String[] name = {"进程号", "提交时间", "运行时间", "开始时间", "结束时间", "周装时间", "带权周转时间"};
    double avgtotal_time, avgweightotal_time;

    public OS() {
        super("进程调度算法演示----这是构造方法中的super()");
        //“每个由 GridBagLayout 管理的组件都与 GridBagConstraints 的实例相关联。Constraints 对象指定组件在网格中的显示区域以及组件在其显示区域中的放置方式
        Container container = getContentPane();//this.getContentPane()将窗体转换为容器
        GridBagLayout gbl = new GridBagLayout();//网格布局管理器：以表格形式布置容器内的组件
        GridBagConstraints gbc = new GridBagConstraints();
        container.setLayout(gbl);

        //调度算法各个进程运行情况，通过table来显示
        table = new JTable(a, name);
        JScrollPane jsp1 = new JScrollPane(table);//带滚动条面板，也是一种容器，只能放一个组件。将table组件放入jsp1中
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 60;
        gbc.fill = GridBagConstraints.HORIZONTAL;//加宽组件直到它足以在水平方向上填满其显示区域，但不更改其高度
        gbl.setConstraints(jsp1, gbc);
        container.add(jsp1);

        //操作指南
        JLabel label6 = new JLabel("请输入" + N + "组[进程号]+[提交时间]+[运行时间]: ");
        label6.setFont(new Font("楷体", Font.CENTER_BASELINE, 16));
        label6.setForeground(new Color(190, 0, 0));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbl.setConstraints(label6, gbc);
        container.add(label6);

        //空格
        JLabel label7 = new JLabel("  ");
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbl.setConstraints(label7, gbc);
        container.add(label7);

        JLabel label2 = new JLabel("平均周转时间：");
        label2.setFont(new Font("楷体", Font.BOLD, 12));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(label2, gbc);
        container.add(label2);

        //计算平均周转时间
        j2 = new JTextField(10);
        //weightx,weighty ——用来设置窗口变大时，各组件跟着变大的比例
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        //gridwidth,gridheight —— 用来设置组件所占的单位长度与高度，默认值皆为1
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(j2, gbc);
        j2.setText("" + avgtotal_time);
        container.add(j2);

        JLabel label3 = new JLabel("平均带权周转时间");
        label3.setFont(new Font("楷体", Font.BOLD, 12));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(label3, gbc);
        container.add(label3);
        gbc.gridheight = 1;

        //计算平均带权周期时间
        j3 = new JTextField(1);
        gbc.ipadx = 1;//组件间的横向间距
        gbc.ipady = 1;//组件间的纵向间距
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbl.setConstraints(j3, gbc);
        j3.setText("" + avgweightotal_time);
        container.add(j3);

        b1 = new JButton("先来先服务");
        b1.setFont(new Font("楷体", Font.BOLD, 15));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(b1, gbc);
        container.add(b1);

        b2 = new JButton("短作业优先");
        b2.setFont(new Font("楷体", Font.BOLD, 15));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(b2, gbc);
        container.add(b2);

        b3 = new JButton("时间片轮转");
        b3.setFont(new Font("楷体", Font.BOLD, 15));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(b3, gbc);
        container.add(b3);

        b4 = new JButton("高响应比优先");
        b4.setFont(new Font("楷体", Font.BOLD, 15));
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbl.setConstraints(b4, gbc);
        container.add(b4);

        //监听button
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        setSize(500, 800);
        setVisible(true);//将JFrame对象显示出来
        setResizable(true);//设置窗口是否可以调整大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//用户单击窗口的关闭按钮时程序执行的操作
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i, j, n, p;
        int firstNum = 0;
        double comeTime, startTime, num;//comeTime提交时间、startTime运行时间、num进程号
        double b[][] = new double[N][7];

        //(1)、先来先服务
        if (e.getSource().equals(b1)) {
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
            //将各个进程运行的时间矩阵b填进表table里
            for (i = 0; i < N; i++) {
                for (j = 0; j < 7; j++) {
                    table.setValueAt("" + b[i][j], i, j);
                }
            }
        }

        //(2)、短作业优先
        if (e.getSource().equals(b2)) {
            for (i = 0; i < N; i++) {
                for (j = 0; j < 3; j++) {
                    b[i][j] = Double.parseDouble(a[i][j]);
                }
            }
            //提交时间按从小到大排列，从上往下排
            for (i = 0; i < N; i++) {
                for (j = 0; j < N; j++) {
                    if (b[i][1] < b[j][1]) {
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
            //如果存在提交相等而且提交时间是最早的
            for (i = 0; i < N; i++) {
                if (b[i][1] == b[0][1]) {//最早到达的进程有多个
                    firstNum = i + 1;//跟下面的j有点相似
                }
            }
            //将最早同时到达的短作业按从小到大排列，从上往下排
            for (i = 0; i < firstNum; i++) {
                for (j = 0; j < firstNum; j++) {
                    if (b[i][2] < b[j][2]) {
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
            //第一个进程工作
            b[0][3] = b[0][1];//开始时间=提交时间
            b[0][4] = b[0][3] + b[0][2];//结束时间=开始时间+运行时间
            b[0][5] = b[0][4] - b[0][1];//周转时间=结束时间-提交时间
            b[0][6] = b[0][5] / b[0][2];//带权周转时间=周转时间/运行时间
            //循环将所有进程执行
            for (i = 1; i < N; i++) {
                //如果前一个进程的结束时间>=第i行的提交时间（下面的进程在等待结束）
                if (b[i - 1][4] >= b[i][1]) {
                    //循环看看还有没有等待的进程
                    for (j = i; j < N; j++) {
                        //进程的结束时间<第j行的提交时间（找到不等待的j行）
                        if (b[i - 1][4] < b[j][1]) {
                            //(循环将所有等待线程结束的进程按短作业优先排序）
                            for (p = i; p < j; p++) {
                                for (n = i; n < j; n++) {
                                    //短作业优先，按作业长短排序
                                    if (b[p][2] < b[n][2]) {
                                        //交换提交时间d
                                        comeTime = b[p][1];
                                        b[p][1] = b[n][1];
                                        b[n][1] = comeTime;
                                        //交换运行时间
                                        startTime = b[p][2];
                                        b[p][2] = b[n][2];
                                        b[n][2] = startTime;
                                        //交换进程号
                                        num = b[p][0];
                                        b[p][0] = b[n][0];
                                        b[n][0] = num;
                                    }
                                }
                            }
                            //排完序后开始等待中的最短进程
                            b[i][3] = b[i - 1][4];
                            b[i][4] = b[i][3] + b[i][2];
                            b[i][5] = b[i][4] - b[i][1];
                            b[i][6] = b[i][5] / b[i][2];
                            break;
                        }
                    }
                    //判断所有进程是否全部提交，true：全部提交完成
                    if (b[i - 1][4] >= b[N - 1][1]) {
                        //若全部提交完，按短作业优先排序
                        for (p = i; p < N; p++) {
                            for (n = i; n < N; n++) {
                                if (b[p][2] < b[n][2]) {
                                    comeTime = b[p][1];
                                    b[p][1] = b[n][1];
                                    b[n][1] = comeTime;
                                    startTime = b[p][2];
                                    b[p][2] = b[n][2];
                                    b[n][2] = startTime;
                                    num = b[p][0];
                                    b[p][0] = b[n][0];
                                    b[n][0] = num;
                                }
                            }
                        }
                        //排完序后执行第i条，后面在等待的进程通过最外层的for循环来响应下面的代码来工作
                        b[i][3] = b[i - 1][4];//开始时间直接等于前一个进程的结束时间（提交了还没有结束）
                        b[i][4] = b[i][3] + b[i][2];
                        b[i][5] = b[i][4] - b[i][1];
                        b[i][6] = b[i][5] / b[i][2];
                    }
                }
                //如果没有等待结束的进程，按顺序做下一个到来的进程
                if (b[i - 1][4] < b[i][1]) {
                    //第i个进程的运行情况
                    b[i][3] = b[i][1];//开始时间=提交时间
                    b[i][4] = b[i][3] + b[i][2];//结束时间=开始时间+运行时间
                    b[i][5] = b[i][4] - b[i][1];//周转时间=结束时间-提交时间
                    b[i][6] = b[i][5] / b[i][2];//带权周转时间=周转时间/运行时间
                }
            }
            //将各个进程运行的时间矩阵b填进表table里
            for (i = 0; i < N; i++) {
                for (j = 0; j < 7; j++) {
                    table.setValueAt("" + b[i][j], i, j);
                }
            }
        }

        //（3）、时间片轮转
        if (e.getSource().equals(b3)) {
            JTextField txt = new JTextField(5);
            String val = JOptionPane.showInputDialog("请输入时间片：");
            txt.setText(val);
            Double slice = Double.parseDouble(txt.getText());//时间片

            boolean flag = true;
            int t1 = 0, m1 = 1, m0 = 0;
            Double c[] = new Double[N];
            for (i = 0; i < c.length; i++) {
                c[i] = 0.0;
            }
            //读取
            for (i = 0; i < N; i++) {
                for (j = 0; j < 3; j++) {
                    b[i][j] = Double.parseDouble(a[i][j]);
                }
            }
            //排序
            for (i = 0; i < N; i++) {
                for (j = 0; j < N; j++) {
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

            Double time = b[0][1];//从第一个提交时间开始，记录时间，累加

            b[0][3] = b[0][1];//开始时间
//           flag表示是否运行, t1 = 0, m1 = 1, m0 = 0;c[i] = 0.0;c[i]为i进程运行了几个时间单位
//            m0表示还没开始运行的进程
//            t1==1表示正在做的进程还没有工作完
//            m1表示进程正在运行到第m1个进程？
            while (flag) {
                t1 = 0;
                for (i = m0; i < m1 + m0; i++) {
                    //存在小于运行时间的话继续运行
                    if (c[i] < b[i][2]) {
                        t1 = 1;
                        break;
                    }
                }

                if (t1 == 1) {
                    for (i = m0; i < m1 + m0; i++) {
                        if (c[i] < b[i][2]) {
                            c[i] = c[i] + slice;//加时间片
                            time = time + slice;//记录时间累加
                            if (c[i] == b[i][2]) {//应该是>=?
                                b[i][4] = time;//结束时间
                            }
                        }
                    }
                    //把剩下的进程循环（m1从1开始）
                    while (m1 <= N - 1) {
                        //提交时间小于时间累加的话，都可以开始了
                        if (b[m1][1] <= time) {
                            b[m1][3] = time;//开始时间
                            c[m1] = c[m1] + slice;
                            time = time + slice;
                            m1 = m1 + 1;//进程正在运行的到的进程
                        }
                    }//m1最后等于N-1
                } else {
                    //循环，将提交时间全赋值给开始时间，判断，如果c[i]==0.0,证明还没运行
                    for (i = 0; i < N; i++) {
                        if (c[i] == 0.0) {
                            m0 = i;
                            time = time + b[i][1];//?
                            break;//发现未开始的进程
                        }
                        //开始时间=提交时间
                        b[m0][3] = b[m0][1];//将提交时间赋给开始时间,m0表示还没开始运行的进程的最后一个i
                    }
                }

                flag = false;
                for (i = 0; i < N; i++) {
                    //存在未运行完的进程，继续运行
                    if (c[i] < b[i][2]) {
                        flag = true;
                        break;
                    }
                }
            }

            //计算周转时间跟带权周转时间
            for (i = 0; i < N; i++) {
                b[i][5] = b[i][4] + b[i][1];
                b[i][6] = b[i][5] / b[i][2];
            }
            //填表
            for (i = 0; i < N; i++) {
                for (j = 0; j < 7; j++) {
                    table.repaint();
                    table.setValueAt("" + b[i][j], i, j);
                }
            }

        }
        //（4）、高响应比优先
        if (e.getSource().equals(b4)) {
            double dd,ss,mm;
            double d[] = new double[N];//响应比
            //读取
            for (i = 0; i < N; i++) {
                for (j = 0; j < 3; j++) {
                    b[i][j] = Double.parseDouble(a[i][j]);
                }
            }
            //排序
            for (i = 0; i < N; i++) {
                for (j = 0; j < N; j++) {
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
            //如果同时到达的进程的提交时间不为零，响应比就不一样
            if (b[0][1] != 0) {
                //如果存在提交相等而且提交时间是最早的
                for (i = 0; i < N; i++) {
                    if (b[i][1] == b[0][1]) {//最早到达的进程有多个
                        firstNum = i + 1;
                    }
                }
                for (i = 0; i < firstNum; i++) {
                    d[i] = (b[i][1] + b[i][2]) / b[i][2];//等待时间就是提交时间
                }

                for (i = 0; i < firstNum; i++) {
                    for (j = i; j < firstNum; j++) {
                        //如果i进程比j进程响应比大
                        if (d[j] >= d[i]) {
                            dd = d[j];
                            d[j] = d[i];
                            d[i] = dd;
                            comeTime = b[j][0];
                            b[j][0] = b[i][0];
                            b[i][0] = comeTime;
//                            ss = d[j];
//                            d[j] = d[i];
//                            d[i] = ss;
                            startTime = b[j][1];
                            b[j][1] = b[i][1];
                            b[i][1] = startTime;
//                            mm = d[j];
//                            d[j] = d[i];
//                            d[i] = mm;
                            num = b[j][2];
                            b[j][2] = b[i][2];
                            b[i][2] = num;
                        }
                    }
                }

            }


            b[0][3] = b[0][1];//开始时间=提交时间
            b[0][4] = b[0][3] + b[0][2];//结束时间=开始时间+运行时间
            b[0][5] = b[0][4] - b[0][1];//周转时间=结束时间-提交时间
            b[0][6] = b[0][5] / b[0][2];

            for (i = 1; i < N; i++) {
                for (j = i; j < N; j++) {
                    d[j] = (b[i - 1][4] - b[j][1] + b[j][2]) / b[j][2];//求出所有的响应比(响应比rr=(等待时间w+服务时间s）/s)//如果b[i - 1][4] - b[j][1]为负数？
                }
                //如果进程等待
                if (b[i - 1][4] >= b[i][1]) {
                    for (j = i; j < N; j++) {
                        for (p = i; p < N; p++) {
                            //如果j进程比p进程响应比大
                            if (d[j] >= d[p]) {
                                dd = d[j];
                                d[j] = d[p];
                                d[p] = dd;
                                comeTime = b[j][0];
                                b[j][0] = b[p][0];
                                b[p][0] = comeTime;
                                startTime = b[j][1];
                                b[j][1] = b[p][1];
                                b[p][1] = startTime;
                                num = b[j][2];
                                b[j][2] = b[p][2];
                                b[p][2] = num;
                            }
                        }
                    }
                    b[i][3] = b[i - 1][4];
                    b[i][4] = b[i][3] + b[i][2];
                    b[i][5] = b[i][4] - b[i][1];
                    b[i][6] = b[i][5] / b[i][2];
                } else {
                    //没有等待的话直接算
                    b[i][3] = b[i][1];
                    b[i][4] = b[i][3] + b[i][2];
                    b[i][5] = b[i][4] - b[i][1];
                    b[i][6] = b[i][5] / b[i][2];
                }
            }

            //填表
            for (i = 0; i < N; i++) {
                for (j = 0; j < 7; j++) {
                    table.repaint();//刷新
                    table.setValueAt("" + b[i][j], i, j);
                }
            }
        }

        //将平均周转时间和平均带权周转时间填进JTextField中
        for (i = 0; i < N; i++) {
            avgtotal_time = avgtotal_time + b[i][5];//求每个进程的周转时间之和
            avgweightotal_time = avgweightotal_time + b[i][6];//求每个进程的带权周转时间
        }
        avgtotal_time = avgtotal_time / N;
        j2.setText("" + avgtotal_time);
        avgweightotal_time = avgweightotal_time / N;
        j3.setText("" + avgweightotal_time);
    }

    public static void main(String args[]) {
        OS a = new OS();
    }
}
