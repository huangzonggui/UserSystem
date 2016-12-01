package BankerAlgorithm;

import javax.swing.*;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;

/**
 * Created by hzg on 2016/11/30.
 */
public class MainWindow extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    JLabel[] labels;
    JTextField[] tf;
    JButton[] bt;
    JPanel[] panel;

    int procNum;//添加的进程数
    int typeNum;//添加的资源种类数
    int[] available;//表示每类资源可用的数量
    int[][] max;
    int[][] allocation;
    int[][] need;
    int[] request;
    String requestP;//请求资源的名字

    String[] processName;
    String[] processName_Safety;//保存一个安全推进序列
    String[] resourceName;

    public MainWindow(String name) {
        super(name);
        setSize(320, 250);
        setLocation(500, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = getContentPane();
        container.setLayout(null);

        labels = new JLabel[3];
        tf = new JTextField[2];
        bt = new JButton[3];
        panel = new JPanel[3];

        panel[0] = new JPanel();
        panel[1] = new JPanel();
        panel[2] = new JPanel();

        panel[0].setBounds(5, 20, 300, 30);
        panel[1].setBounds(5, 60, 300, 30);
        panel[2].setBounds(5, 100, 300, 30);

        labels[0] = new JLabel("输入资源的种类数：");
        labels[1] = new JLabel("输入启动的进程数：");
        labels[2] = new JLabel("资源请求：");

        tf[0] = new JTextField(3);
        tf[1] = new JTextField(3);

        bt[0] = new JButton("添加资源");
        bt[0].addActionListener(this);
        bt[1] = new JButton("启动进程");
        bt[1].addActionListener(this);
        bt[2] = new JButton("请求资源");
        bt[2].addActionListener(this);

        panel[0].add(labels[0]);
        panel[0].add(tf[0]);
        panel[0].add(bt[0]);

        panel[1].add(labels[1]);
        panel[1].add(tf[1]);
        panel[1].add(bt[1]);

        panel[2].add(labels[2]);
        panel[2].add(bt[2]);

        container.add(panel[0]);
        container.add(panel[1]);
        container.add(panel[2]);

        setVisible(true);
    }

    /*-----------------------------------------------------------------*/
    class AddResourceWindow extends JFrame implements ActionListener {
        private static final long serialVersionUID = 1L;
        JLabel ResourceNameL = new JLabel("资源名");
        JLabel AvailableL = new JLabel("  available");
        JButton addResourceOK = new JButton("确定");
        JLabel[] resourceNameJLabel = new JLabel[typeNum];
        JTextField[] AvailableTextField = new JTextField[typeNum];
        JPanel[] addResourceJPanels = new JPanel[typeNum + 2];

        public AddResourceWindow() {
            super("添加资源");
            setSize(320, 400);
            setLocation(500, 150);
            Container container = getContentPane();
            container.setLayout(null);

            addResourceJPanels[typeNum + 1] = new JPanel();
            addResourceJPanels[typeNum + 1].setBounds(5, 20, 300, 30);
            addResourceJPanels[typeNum + 1].add(ResourceNameL);
            addResourceJPanels[typeNum + 1].add(AvailableL);
            container.add(addResourceJPanels[typeNum + 1]);

            //动态的添加组件
            int y = 60;
            char a = 'A';
            for (int i = 0; i < typeNum; i++, y = y + 40) {
                addResourceJPanels[i] = new JPanel();
                resourceNameJLabel[i] = new JLabel(String.valueOf(a) + ":");
                resourceName[i] = String.valueOf(a);
                a = (char) (a + 1);
                AvailableTextField[i] = new JTextField(4);
                AvailableTextField[i].setHorizontalAlignment(JTextField.RIGHT);
                addResourceJPanels[i].setBounds(5, y, 300, 30);
                addResourceJPanels[i].add(resourceNameJLabel[i]);
                addResourceJPanels[i].add(AvailableTextField[i]);
                container.add(addResourceJPanels[i]);
            }
            addResourceJPanels[typeNum] = new JPanel();
            addResourceJPanels[typeNum].setBounds(5, y, 300, 30);
            addResourceJPanels[typeNum].add(addResourceOK);
            addResourceOK.addActionListener(this);
            container.add(addResourceJPanels[typeNum]);

            setVisible(true);
        }

        //将输入的资源名和数量存入available数组，若不填资源名默认为A,B,C
        void getResource() {
            for (int i = 0; i < typeNum; i++) {
                try {
                    available[i] = Integer.parseInt(AvailableTextField[i].getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "available为正整数");
                    return;
                }
            }
            this.dispose();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
            if (o == addResourceOK) {
                getResource();
            }
        }
    }

    /*-----------------------------------------------------------------*/
    /*
     * 定义启动进程的窗口类，用来输入进程名，max数组和allocation数组
     * */
    class StartProcess extends JFrame implements ActionListener {
        private static final long serialVersionUID = 1L;

        JLabel resourceNmaeJLabel = new JLabel("  进程名");
        JLabel maxJLabel = new JLabel("max");
        JLabel allocationJLabel = new JLabel("allocation  ");
        JButton startProcessOK = new JButton("确定");
        JPanel[] startProcessJPanel = new JPanel[procNum + 2];
        JLabel[] processNameJLabel = new JLabel[procNum];
        JTextField[][] maxJTextField = new JTextField[procNum][typeNum];
        JTextField[][] allocationJTextField = new JTextField[procNum][typeNum];

        public StartProcess() {
            super("启动进程");
            setSize(320, 400);
            setLocation(500, 150);
            Container container = getContentPane();
            container.setLayout(null);

            //添加表格标题
            startProcessJPanel[procNum + 1] = new JPanel();
            startProcessJPanel[procNum + 1].setLayout(new GridLayout(1, 3));
            startProcessJPanel[procNum + 1].setBounds(5, 20, 300, 30);
            startProcessJPanel[procNum + 1].add(resourceNmaeJLabel);
            startProcessJPanel[procNum + 1].add(maxJLabel);
            startProcessJPanel[procNum + 1].add(allocationJLabel);
            container.add(startProcessJPanel[procNum + 1]);

            //添加表格（）textfield组 进程名默认为p1,p2,p3
            int y = 60;
            for (int i = 0; i < procNum; i++, y = y + 40) {
                startProcessJPanel[i] = new JPanel();
                processNameJLabel[i] = new JLabel("p" + i + ":");
                startProcessJPanel[i].setBounds(5, y, 300, 30);
                processName[i] = "p" + i;
                startProcessJPanel[i].add(processNameJLabel[i]);
                for (int j = 0; j < typeNum; j++) {
                    maxJTextField[i][j] = new JTextField(2);
                    maxJTextField[i][j].setHorizontalAlignment(JTextField.RIGHT);
                    startProcessJPanel[i].add(maxJTextField[i][j]);
                }
                startProcessJPanel[i].add(new JLabel("*"));
                for (int j = 0; j < typeNum; j++) {
                    allocationJTextField[i][j] = new JTextField((2));
                    allocationJTextField[i][j].setHorizontalAlignment(JTextField.RIGHT);
                    startProcessJPanel[i].add(allocationJTextField[i][j]);
                }
                container.add(startProcessJPanel[i]);
            }
            //添加确定按钮
            startProcessJPanel[procNum] = new JPanel();
            startProcessJPanel[procNum].setBounds(5, y, 300, 30);
            startProcessJPanel[procNum].add(startProcessOK);
            startProcessOK.addActionListener(this);
            container.add(startProcessJPanel[procNum]);

            setVisible(true);
        }

        //从textfield中获取进程信息
        void getProcessMassage() {
            try {
                for (int i = 0; i < procNum; i++) {
                    for (int j = 0; j < typeNum; j++) {
                        max[i][j] = Integer.parseInt(maxJTextField[i][j].getText());
                        allocation[i][j] = Integer.parseInt(allocationJTextField[i][j].getText());
                        need[i][j] = max[i][j] - allocation[i][j];
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "输入的信息必须为正整数");
                return;
            }
            if (isSafety() == false) {
                JOptionPane.showMessageDialog(null, "输入的进程信息使进程出在不安全状态！");
                return;
            }
            this.dispose();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
            if (o == startProcessOK) {
                getProcessMassage();
            }
        }
    }

    /*
     * 请求资源窗口类
     * */
    class RequestResourceWindow extends JFrame implements ActionListener {
        private static final long serialVersionUID = 1L;
        JLabel rProcessNameL = new JLabel("进程名：");
        JButton reqResOK = new JButton("确定");
        JButton reqResClose = new JButton("关闭");
        JTextField rProcessNameT = new JTextField(4);
        JLabel[] resNameL = new JLabel[typeNum];
        JTextField[] resNameT = new JTextField[typeNum];
        JPanel[] resJPanel = new JPanel[typeNum + 2];

        public RequestResourceWindow() {
            super("请求资源");
            setSize(320, 400);
            setLocation(500, 150);
            Container container = getContentPane();
            container.setLayout(null);

            resJPanel[typeNum] = new JPanel();
            resJPanel[typeNum].setBounds(5, 20, 300, 30);
            resJPanel[typeNum].add(rProcessNameL);
            resJPanel[typeNum].add(rProcessNameT);
            container.add(resJPanel[typeNum]);

            int y = 60;
            char a = 'A';
            for (int i = 0; i < typeNum; i++, y = y + 40) {
                resJPanel[i] = new JPanel();
                resNameL[i] = new JLabel(String.valueOf(a) + ":");
                resourceName[i] = String.valueOf(a);
                a = (char) (a + 1);
                resNameT[i] = new JTextField(2);
                resNameT[i].setHorizontalAlignment(JTextField.RIGHT);
                resJPanel[i].setBounds(5, y, 300, 30);
                resJPanel[i].add(resNameL[i]);
                resJPanel[i].add(resNameT[i]);
                container.add(resJPanel[i]);
            }
            resJPanel[typeNum + 1] = new JPanel();
            resJPanel[typeNum + 1].setBounds(5, y, 300, 30);
            resJPanel[typeNum + 1].add(reqResOK);
            resJPanel[typeNum + 1].add(reqResClose);
            reqResOK.addActionListener(this);
            reqResClose.addActionListener(this);
            container.add(resJPanel[typeNum + 1]);

            setVisible(true);
        }

        /*
         * 获取请求资源的信息,并计算请求资源后是否处于安全状态
         * */
        private void getRequestRes() {
            try {
                for (int i = 0; i < typeNum; i++) {
                    request[i] = Integer.parseInt(resNameT[i].getText());

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "输入请求资源数必须为正整数");
                return;
            }
            requestP = rProcessNameT.getText();//请求资源的名字

        /*
         * 判断输入的请求是否合法，完成核心算法的第一第二步
         * */
            if (P_id(requestP) == -1) {
                JOptionPane.showMessageDialog(null, "输入的进程不存在");
                return;
            } else if (R_N(P_id(requestP)) == false) {
                JOptionPane.showMessageDialog(null, "进程所需要的资源数量已超过其宣布的最大量");
                return;
            } else if (R_A() == false) {
                JOptionPane.showMessageDialog(null, "没有足够的资源分配给该进程");
                return;
            }
            String[] s = new String[2];
            s[0] = null;
            s[1] = "";

            if (bankerAlgorithm() == true) {
                s[0] = "此时系统出于安全状态！一个安全推进序列为：\n";
                for (int i = 0; i < procNum; i++) {
                    s[1] = s[1] + processName_Safety[i] + "  ";//安全序列
                }
                JOptionPane.showMessageDialog(null, s[0] + s[1]);
                return;
            } else {
                JOptionPane.showMessageDialog(null, "此时系统处于不安全状态！");
                return;
            }
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            Object ob = arg0.getSource();
            if (ob == reqResOK) {
                getRequestRes();
            } else if (ob == reqResClose) {
                this.dispose();
            }
        }

    }

    /*
     * 添加资源按钮功能，初始化资源种类数，弹出对话框初始化资源available
     * */
    void addResource() {
        try {
            typeNum = Integer.parseInt(tf[0].getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "资源数为正整数");
            return;
        }
        available = new int[typeNum];
        for (int i = 0; i < typeNum; i++) {
            available[i] = 0;
        }
        resourceName = new String[typeNum];

        new AddResourceWindow();
    }

    /*
     * 判断资源是否添加
     * */
    private boolean haveResouce() {
        for (int i = 0; i < typeNum; i++) {
            if (available[i] != 0) {
                return true;
            }
        }
        return false;
    }

    /*
     * 启动进程，添加进程max数组信息，给进程分配allocation
     * */
    void startProcess() {
        if (haveResouce() == false || typeNum == 0) {
            JOptionPane.showMessageDialog(null, "请先添加资源：资源数全为零或没有输入资源数");
        } else {
            try {
                procNum = Integer.parseInt(tf[1].getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "进程数为正整数");
                return;
            }
            processName = new String[procNum];
            max = new int[procNum][typeNum];
            allocation = new int[procNum][typeNum];
            need = new int[procNum][typeNum];
            new StartProcess();
        }
    }

    /*
     * 打开请求资源对话框，并且计算出是否出于安全状态
     * */
    void requestResource() {
        request = new int[typeNum];
        new RequestResourceWindow();
    }

    /************************************/
    //核心算法部分，就是计算出一个安全的序列
    public boolean bankerAlgorithm() {
        //通过资源申请的验证，尝试分配资源
        int i = P_id(requestP);//i表示进程的id
        for (int j = 0; j < typeNum; j++) {
            available[j] = available[j] - request[j];
            allocation[i][j] += request[j];
            need[i][j] -= request[j];
        }
        if (isSafety() == false) {
            for (int j = 0; j < typeNum; j++) {
                available[j] = available[j] + request[j];
                allocation[i][j] -= request[j];
                need[i][j] += request[j];//这个应该是+吧
            }
            return false;
        } else {
            return true;
        }
    }

    /*
         * 根据进程名返回进程ID
         * */
    int P_id(String PName) {
        for (int i = 0; i < procNum; i++) {
            //compareToIgnoreCase比较忽略大小写
            if (processName[i].compareToIgnoreCase(PName) == 0) return i;
        }
        return -1;
    }

    /*
     * 判断请求资源数request与所需资源数need之间的关系,如果所有request[j]<=need[i][j]返回true
     * */
    boolean R_N(int i) {
        for (int j = 0; j < typeNum; j++) {
            if (request[j] > need[i][j]) return false;
        }
        return true;
    }

    /*
         * 判断需求资源数request与可用资源数available之间的关系，如果所有request[j]<=available[j]返回ture
         * */
    boolean R_A() {
        for (int j = 0; j < typeNum; j++) {
            if (request[j] > available[j]) return false;
        }
        return true;
    }

    /*
     * 判断need[i][j]与availableLeft[j]的大小，小于返回true
     * */
    boolean N_W(int i, int[] availableLeft) {
        //有一个类型的资源不足都返回false
        for (int j = 0; j < typeNum; j++) {
            if (need[i][j] > availableLeft[j]) return false;
        }
        return true;
    }

    //找到满足need[i][j]<=availableLeft[j]且finish[i]==false的进程返回进程id，未找到返回-1
    int isNextP(int i, boolean[] finish, int[] availableLeft) {
        for (int j = i; j < procNum; j++) {
            if (finish[j] == false && N_W(j, availableLeft)) {
                return j;
            }
        }
        for (int k = 0; k < i; k++) {
            if (finish[k] == false && N_W(k, availableLeft)) {
                return k;
            }
        }
        return -1;
    }

    /*
     * 判断根据finish的值判断是否出于安全状态，全为true则处于安全状态
     * */
    boolean allTrue(boolean[] finish) {
        for (int i = 0; i < procNum; i++) {
            if (finish[i] == false) return false;
        }
        return true;
    }

    //判断是否安全
    boolean isSafety() {
        int[] availableLeft = new int[typeNum];//剩余可用资源
        boolean[] finish = new boolean[procNum];
        processName_Safety = new String[procNum];
        for (int j = 0; j < typeNum; j++) {
            availableLeft[j] = available[j];//将每类可用资源初始化到availableLeft中
        }
        for (int j = 0; j < procNum; j++) {
            finish[j] = false;//初始化
        }
        int i = 0;
        for (int k = 0; k < procNum; k++) {
//            isNextP找到满足need[i][j]<=work[j]且finish[i]==false的进程返回进程id，未找到返回-1
            i = isNextP(i, finish, availableLeft);
            if (i >= 0) {
                for (int j = 0; j < typeNum; j++) {
                    availableLeft[j] += allocation[i][j];//Allocation表示当前分给进程pi的资源
                }
                finish[i] = true;
                processName_Safety[k] = processName[i];
            } else {
                break;
            }
        }
        //根据finish的值判断是否出于安全状态
        if (allTrue(finish) == true) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        new MainWindow("银行家算法的实现");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == bt[0]) {
            addResource();
        } else if (o == bt[1]) {
            startProcess();
        } else if (o == bt[2]) {
            requestResource();
        }

    }
}
