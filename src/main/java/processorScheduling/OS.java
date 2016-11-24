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

//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < 3; j++) {
//                System.out.print(a[i][j]+"  ");
//            }
//            System.out.println();
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i, j, n, p, h;
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
                        //交换提交时间
                        comeTime = b[i][1];
                        b[i][1] = b[j][1];
                        b[j][1] = comeTime;
                        //交换运行时间
                        startTime = b[i][2];
                        b[i][2] = b[j][2];
                        b[j][2] = startTime;
                        //交换进程号
                        num = b[i][0];
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
                        //交换提交时间
                        comeTime = b[i][1];
                        b[i][1] = b[j][1];
                        b[j][1] = comeTime;
                        //交换运行时间
                        startTime = b[i][2];
                        b[i][2] = b[j][2];
                        b[j][2] = startTime;
                        //交换进程号
                        num = b[i][0];
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

            for (i = 1; i < N; i++) {
                //如果前一个进程的结束时间>=提交时间
                if (b[i - 1][4] >= b[i][1]) {
                    for (j = i; j < N; j++) {

                        if (b[i - 1][4] < b[j][1]) {
                            h = j;

                            for (p = i; p < h; p++) {
                                for (n = i; n < h; n++) {
                                    if (b[p][2] < b[n][2]) {
                                        //交换提交时间
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

                            b[i][3] = b[i - 1][4];//开始时间直接等于前一个进程的结束时间（提交了还没有结束）
                            b[i][4] = b[i][3] + b[i][2];
                            b[i][5] = b[i][4] - b[i][1];
                            b[i][6] = b[i][5] / b[i][2];
                        }
                    }
                    if (b[i - 1][4] >= b[N - 1][1]) {
                        for (p = i; p < N; p++) {
                            for (n = i; n < N; n++) {
                                if (b[p][2] < b[n][2]) {
                                    //交换提交时间
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
                        b[i][3] = b[i - 1][4];//开始时间直接等于前一个进程的结束时间（提交了还没有结束）
                        b[i][4] = b[i][3] + b[i][2];
                        b[i][5] = b[i][4] - b[i][1];
                        b[i][6] = b[i][5] / b[i][2];
                    }
                }
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


        //时间片轮转
        if (e.getSource().equals(b3)) {

        }
        //高响应比优先
        if (e.getSource().equals(b4)) {

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
