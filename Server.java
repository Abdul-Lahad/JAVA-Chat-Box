import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;  
import java.util.Date;
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

public class Server extends JFrame{
    
    //declaring the objects
    ServerSocket server; 
    Socket socket;

    BufferedReader br;
    PrintWriter out;
     
    //declaring components
    private JLabel heading =new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    private ImageIcon image=new ImageIcon("chat3.png");
    JOptionPane p=new JOptionPane();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
    Date date = new Date();  
    


    // constructer starts
public Server(){
        
  try {
      String port= p.showInputDialog("Please Enter the port number ","7778");
      int P=Integer.parseInt(port);
        
      server = new ServerSocket(P);
        
      p.showMessageDialog(null, "server is ready to accept connection \nwaiting....");
          
      System.out.println("server is ready to accept connection ");
      System.out.println("waiting....");
      socket = server.accept();
          
      createGUI();
      handlEvents();
        
      br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream());
           
          
      startReading();         //celling reading method
    //   startWriting();     //celling writing method

          } 
  catch (Exception e) {
        e.printStackTrace();           
                          }
             }// constructer ends
    
    private void createGUI(){
            
        this.setTitle("Server Massager [END]");
        this.setSize(650,650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                                   
        //seting of layout
        this.setLayout(new BorderLayout());
                       
        DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        //adding components
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

        int res = JOptionPane.showConfirmDialog(null, "Do you want to terminate the chat with Client..?",
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
        else
          System.exit(10);
        
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
        
    try{        
              
      while(true){        
                          
        String msg = br.readLine();
        if(msg.equals("exit")){
              
          System.out.println("Client terminated the chat");
          JOptionPane.showMessageDialog(this, "Client terminated the chat");
          messageInput.setEnabled(false);
          socket.close();
          break;
                  }  //if body ends
                //  System.out.println("Client : "+msg);
        messageArea.append("Client : "+msg+"\n");
                         }
                }
    catch(Exception e){
              //  e.printStackTrace();
        System.out.println(" connection closed");
                             }
                        };
    
  new Thread(r1).start(); // starting r1 thread
                           }
                         
public void startWriting(){
       
    //Thread for writing
    Runnable r2=()->{
          System.out.println("writer stated");
          BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
          try{ 
           
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
            System.out.println(" connection is closed "); 
              }
          };
    
    new Thread(r2).start();//starting r2 thread
       
       }

public static void main(String[] args) {
        
  new Server();

        }
  }