import java.net.*;
import javax.swing.BorderFactory;           
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.lang.Exception;
import javax.swing.text.DefaultCaret;
import java.io.*;

public class Client extends JFrame {
      
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    
    //declaring components
    private JLabel heading =new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    private ImageIcon image=new ImageIcon("chat3.png");
    public int res;
    JOptionPane p=new JOptionPane();
    
    //constructer starts   
 public Client(){
       
   try {
              
      String port= p.showInputDialog("Please Enter the port number ","7778");
      int PORT=Integer.parseInt(port);
      String IP= p.showInputDialog("Please Enter the IP address ","127.0.0.1");
      p.showMessageDialog(null, "Sending request to server ");
       
      System.out.println("Sending request to server ");   
      socket=new Socket(IP,PORT);
      System.out.println("connection done");
              
      createGUI(); 
      handlEvents();
     
      br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());
               
      startReading();         //celling reading method
   // startWriting();         //celling writing method
        
                     } 
    catch (Exception e) {
        // e.printStackTrace();
        p.showMessageDialog(null, "Sorry Port number is incorrect ");
        new Client();
        }
    }
    
 private void createGUI(){
            
    this.setTitle("Client Massager [END]");
    this.setSize(650,650);
    this.setLocationRelativeTo(null);
    this.setIconImage(image.getImage());

    //  coding for components
    heading.setFont(font);
    messageArea.setFont(font);
    messageInput.setFont(font);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    heading.setIcon(new ImageIcon("2.png"));
    heading.setHorizontalTextPosition(SwingConstants.CENTER);
    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);             
    messageArea.setEditable(false);
                                      
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //seting of layout
    this.setLayout(new BorderLayout());
    //adding components
    DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            
    JScrollPane jScrollPane=new JScrollPane();

    jScrollPane.add(messageArea);
    jScrollPane.setViewportView(messageArea);
            
    this.add(jScrollPane,BorderLayout.CENTER);
    this.add(heading,BorderLayout.NORTH);
    this.add(messageInput,BorderLayout.SOUTH);
     
    this.setVisible(true);
      
     }  
    
 @Override
 public void dispose(){
      
    if(messageInput.isEnabled()){ 
          
       res = JOptionPane.showConfirmDialog(null, 
       "Do you want to terminate the chat with Server..?",
       "Question", JOptionPane.YES_NO_OPTION);
       
       if(res == 0){
          
          out.println("exit");
          out.flush();
          
          try {
              socket.close();
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            System.exit(10);
           }
          }
          else {
          System.exit(1);
          
          }
    }


 private void handlEvents(){
              
    messageInput.addKeyListener(new KeyListener(){

    @Override
    public void keyTyped(KeyEvent e) {
          // TODO Auto-generated method stub          
        }
    @Override
    public void keyPressed(KeyEvent e) {
          // TODO Auto-generated method stub          
        }

    @Override
    public void keyReleased(KeyEvent e) {
          // TODO Auto-generated method stub
        if(e.getKeyCode()==10){      
                  
          String sendMessage=messageInput.getText();
          out.println(sendMessage);
          out.flush();
          messageArea.append("Me : "+sendMessage+"\n");
          messageInput.setText("");
          messageInput.requestFocus();
                          }          
                       }
                 });
           }

 public void startReading(){
       
    //Thread for reading 
    Runnable r1=()->{

       System.out.println("reader started....");
       try {
          
         while(true){
              String msg = br.readLine();
              
              if(msg.equals("exit")){
                  System.out.println("Server terminated the chat");
                  JOptionPane.showMessageDialog(this, "Server terminated the chat");
                  messageInput.setEnabled(false);
                              
                  socket.close();
                  break;
                              }
        // System.out.println("Server :"+msg);
           messageArea.append("Server : "+msg+"\n");
      } 
      
    }
       catch (Exception e) {
            // e.printStackTrace();
            System.out.println("connection is closed");  
                        } 
          };  
    new Thread(r1).start(); // starting r1 thread  
       }

 public void startWriting(){
    //Thread for writing
    Runnable r2=()->{
        System.out.println("writer stated");
        BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
 
        try {
          
          while(!socket.isClosed()){
           
            String content =br1.readLine();
            out.println(content);
            out.flush();
                            
            if(content.equals("exit")){
              socket.close();
              break;
                    }
               }   
           }
          catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
              }

    };

      new Thread(r2).start();//starting r2 thread
      }
    
 public static void main(String[] args) {
        new Client();
    }
                    }