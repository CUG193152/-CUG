package Client;

import java.awt.*;  
import java.awt.event.*; 
import java.io.*;  
import java.net.Socket;  
import java.util.*;  
  
import javax.swing.*;
import javax.swing.border.*;
import Tools.*;
public class ClientChat{  
  
    private JFrame frame;  
    private JList userList;  
    private JTextArea textArea;  
    private JTextField textField;  
    private JTextField txt_port;  
    private JTextField txt_hostIp;  
    private JTextField txt_name;  
    private JButton btn_start;  
    private JButton btn_stop;  
    private JButton btn_send;  
    private JPanel northPanel;  
    private JPanel southPanel;  
    private JScrollPane rightScroll;  
    private JScrollPane leftScroll;  
    private JSplitPane centerSplit;  
  
    private DefaultListModel listModel;  
    private boolean isConnected = false;  
  
    private Socket socket=null;  
    private PrintWriter writer;  
    private BufferedReader reader;  
    private MessageThread messageThread;// ���������Ϣ���߳�  
    private Map<String, User> onLineUsers = new HashMap<String, User>();// ���������û�  
  
    // ִ�з���  
    public void send() {  
        if (!isConnected) {  
            JOptionPane.showMessageDialog(frame, "��û�����ӷ��������޷�������Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = textField.getText().trim();  
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendMessage(frame.getTitle() + "@" + "ALL" + "@" + message);  
        textField.setText(null);  
    }  
  
    // ���췽��  
    public ClientChat(Socket socket, String ID_C,String ID_V) {  
    	this.socket = socket;
        textArea = new JTextArea();  
        textArea.setEditable(false);  
        textArea.setForeground(Color.blue);  
        textField = new JTextField();  
        txt_port = new JTextField(ID_C);  
        txt_hostIp = new JTextField(ID_V);  
        txt_name = new JTextField("xiaoqiang");  
        btn_start = new JButton("����");  
        btn_stop = new JButton("�Ͽ�");  
        btn_send = new JButton("����");  
        listModel = new DefaultListModel();  
        userList = new JList(listModel);  
        
       
        northPanel = new JPanel();  
        northPanel.setLayout(new GridLayout(1, 7));  
        northPanel.add(new JLabel("�ͻ�ID"));  
        northPanel.add(txt_port);  
        northPanel.add(new JLabel("������ID"));  
        northPanel.add(txt_hostIp);  
        northPanel.add(new JLabel("�û���"));  
        northPanel.add(txt_name);  
        northPanel.add(btn_start);  
        northPanel.add(btn_stop);  
        northPanel.setBorder(new TitledBorder("������Ϣ"));  
  
        rightScroll = new JScrollPane(textArea);  
        rightScroll.setBorder(new TitledBorder("��Ϣ��ʾ��"));  
        leftScroll = new JScrollPane(userList);  
        leftScroll.setBorder(new TitledBorder("�����û�"));  
        southPanel = new JPanel(new BorderLayout());  
        southPanel.add(textField, "Center");  
        southPanel.add(btn_send, "East");  
        southPanel.setBorder(new TitledBorder("д��Ϣ"));  
  
        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll,  
                rightScroll);  
        centerSplit.setDividerLocation(100);  
  
        frame = new JFrame("�ͻ���");  
        // ����JFrame��ͼ�꣺  
        //frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("qq.png")));  
        frame.setLayout(new BorderLayout());  
        frame.add(northPanel, "North");  
        frame.add(centerSplit, "Center");  
        frame.add(southPanel, "South");  
        frame.setSize(600, 400);  
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;  
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;  
        frame.setLocation((screen_width - frame.getWidth()) / 2,  
                (screen_height - frame.getHeight()) / 2);  
        frame.setVisible(true);  
  
        // д��Ϣ���ı����а��س���ʱ�¼�  
        textField.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent arg0) {  
                send();  
            }  
        });  
  
        // �������Ͱ�ťʱ�¼�  
        btn_send.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                send();  
            }  
        });  
  
        // �������Ӱ�ťʱ�¼�  
        btn_start.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                int port;  
                if (isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ���������״̬����Ҫ�ظ�����!",  
                            "����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {
                    String name = txt_name.getText().trim();  
                    if (name.equals("") ) {  
                        throw new Exception("��������Ϊ��!");  
                    }  
                    boolean flag = connectServer(name);  
                    if (flag == false) {  
                        throw new Exception("�����������ʧ��!");  
                    }  
                    frame.setTitle(name);  
                    JOptionPane.showMessageDialog(frame, "�ɹ�����!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
  
        // �����Ͽ���ťʱ�¼�  
        btn_stop.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (!isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ��ڶϿ�״̬����Ҫ�ظ��Ͽ�!",  
                            "����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    boolean flag = closeConnection();// �Ͽ�����  
                    if (flag == false) {  
                        throw new Exception("�Ͽ����ӷ����쳣��");  
                    }  
                    JOptionPane.showMessageDialog(frame, "�ɹ��Ͽ�!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
  
        // �رմ���ʱ�¼�  
        frame.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                if (isConnected) {  
                    closeConnection();// �ر�����  
                }  
                //System.exit(0);// �˳�����  
            }  
        });  
    }  
  
    /**  
     * ���ӷ�����  
     *   
     * @param port  
     * @param hostIp  
     * @param name  
     */  
    public boolean connectServer(String name) {  
        // ���ӷ�����  
        try {  
           // socket = new Socket(hostIp, port);// ���ݶ˿ںźͷ�����ip�������� 
            writer = new PrintWriter(socket.getOutputStream());  
            reader = new BufferedReader(new InputStreamReader(socket  
                    .getInputStream()));  
            System.out.println();
            // ���Ϳͻ����û�������Ϣ(�û�����ip��ַ)  
            sendMessage(name + "@" + socket.getLocalAddress().toString());  
            // ����������Ϣ���߳�  
            messageThread = new MessageThread(reader, textArea);  
            messageThread.start();  
            isConnected = true;// �Ѿ���������  
            return true;
        } catch (Exception e) {  
        	System.out.println(e.getMessage());
            isConnected = false;// δ������  
            return false;  
        }  
    }  
  
    /**  
     * ������Ϣ  
     *   
     * @param message  
     */  
    public void sendMessage(String message) {  
        writer.println(message);  
        writer.flush();  
    }  
  
    /**  
     * �ͻ��������ر�����  
     */  
    @SuppressWarnings("deprecation")  
    public synchronized boolean closeConnection() {  
        try {  
            sendMessage("CLOSE");// ���ͶϿ����������������  
            messageThread.stop();// ֹͣ������Ϣ�߳�  
            // �ͷ���Դ  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;  
            return true;  
        } catch (IOException e1) {  
            e1.printStackTrace();  
            isConnected = true;  
            return false;  
        }  
    }  
  
    // ���Ͻ�����Ϣ���߳�  
    class MessageThread extends Thread {  
        private BufferedReader reader;  
        private JTextArea textArea;  
  
        // ������Ϣ�̵߳Ĺ��췽��  
        public MessageThread(BufferedReader reader, JTextArea textArea) {  
            this.reader = reader;  
            this.textArea = textArea;  
        }  
  
        // �����Ĺر�����  
        public synchronized void closeCon() throws Exception {  
            // ����û��б�  
            listModel.removeAllElements();  
            // �����Ĺر������ͷ���Դ  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;// �޸�״̬Ϊ�Ͽ�  
        }  
  
        public void run() {  
            String message = "";  
            while (true) {  
                try {  
                    message = reader.readLine();  
                    StringTokenizer stringTokenizer = new StringTokenizer(  
                            message, "/@");  
                    String command = stringTokenizer.nextToken();// ����  
                    if (command.equals("CLOSE"))// �������ѹر�����  
                    {  
                        textArea.append("�������ѹر�!\r\n");  
                        closeCon();// �����Ĺر�����  
                        return;// �����߳�  
                    } else if (command.equals("ADD")) {// ���û����߸��������б�  
                        String username = "";  
                        String userIp = "";  
                        if ((username = stringTokenizer.nextToken()) != null  
                                && (userIp = stringTokenizer.nextToken()) != null) {  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("DELETE")) {// ���û����߸��������б�  
                        String username = stringTokenizer.nextToken();  
                        User user = (User) onLineUsers.get(username);  
                        onLineUsers.remove(user);  
                        listModel.removeElement(username);  
                    } else if (command.equals("USERLIST")) {// ���������û��б�  
                        int size = Integer  
                                .parseInt(stringTokenizer.nextToken());  
                        String username = null;  
                        String userIp = null;  
                        for (int i = 0; i < size; i++) {  
                            username = stringTokenizer.nextToken();  
                            userIp = stringTokenizer.nextToken();  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("MAX")) {// �����Ѵ�����  
                        textArea.append(stringTokenizer.nextToken()  
                                + stringTokenizer.nextToken() + "\r\n");  
                        closeCon();// �����Ĺر�����  
                        JOptionPane.showMessageDialog(frame, "������������������", "����",  
                                JOptionPane.ERROR_MESSAGE);  
                        return;// �����߳�  
                    } else {// ��ͨ��Ϣ  
                        textArea.append(message + "\r\n");  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}  