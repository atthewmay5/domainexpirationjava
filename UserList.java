import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UserList {
		
		public User userObject = new User();
	
		public ArrayList<String>Users = new ArrayList<String>();
		public ArrayList<User>UsersList = new ArrayList<User>();
		
		public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a");  
		public LocalDateTime now = LocalDateTime.now();
		public String nowString = now.format(dtf);
		public LocalDateTime today = LocalDateTime.parse(nowString,dtf);
		public LocalDateTime twoWeeks = today.plusDays(14);
		
		String content = "<html>";
		private final JScrollPane scrollPane = new JScrollPane();
	    private JLabel lblContent = new JLabel();
	    static JFrame f;
	    
	    int currentValue = 0;

				
		Process netuserdomain;
		Process netuserdomainuser;


	public void getUsers() {
	
		final int MAX = 100;
        final JFrame barFrame = new JFrame("Getting Users");
 
        // creates progress bar
        final JProgressBar pb = new JProgressBar();
        pb.setMinimum(0);
        pb.setMaximum(MAX);
        pb.setStringPainted(true);
 
        // add progress bar
        barFrame.setLayout(new FlowLayout());
        barFrame.getContentPane().add(pb);
 
        barFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        barFrame.setSize(300, 200);
        barFrame.setVisible(true);
		
	    try {
	      netuserdomain = Runtime.getRuntime().exec("net user /domain");
	      currentValue +=10;
	      pb.setValue(currentValue);
	      netuserdomain.waitFor(); 
	      BufferedReader reader=new BufferedReader(new InputStreamReader(
	                  netuserdomain.getInputStream())); 
	      String userlist; 
	      pb.setValue(currentValue);
	      while((userlist = reader.readLine()) != null) { 
	    	userlist = userlist.replace("\n", "").replace("-", "");
	    	String[] u2 = userlist.split("        ");
	    	currentValue +=5;
	    	pb.setValue(currentValue);
	
	    	for(String a : u2) {
	    		a = a.replace(" ", "").replace("\r\n", "").replace("\r", "");

	    		if(!a.equals("")&&a!="\r\n"&&a!=" ") {
	    			try {
	    			      Users.add(a);
	    			} catch(Exception e) {
	    				System.err.println("error");
	    			}
	    		}
	    	}
	      }
	    }
	       catch (IOException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    } catch (InterruptedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    }
			currentValue = 100;
			pb.setValue(currentValue);
			barFrame.setVisible(false);
	        currentValue = 0;
	      } 
	      
	    
	    
	    public void formatandprint() {

	    	final int MAX = 100;
	        final JFrame barFrame = new JFrame("Calculating Expiring Users");
	 
	        // creates progress bar
	        final JProgressBar pb = new JProgressBar();
	        pb.setMinimum(0);
	        pb.setMaximum(MAX);
	        pb.setStringPainted(true);
	 
	        // add progress bar
	        barFrame.setLayout(new FlowLayout());
	        barFrame.getContentPane().add(pb);
	 
	        barFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        barFrame.setSize(300, 200);
	        barFrame.setVisible(true);
	    	
	    	for(String user : Users) {
	    	
	    	int active = 1;
	    		
	    	  try {
	    	      netuserdomain = Runtime.getRuntime().exec("net user /domain "+user);
	    	
	    	      netuserdomain.waitFor(); 
	    	      BufferedReader reader2=new BufferedReader(new InputStreamReader(
	    	                  netuserdomain.getInputStream())); 
	    	      String userlist2; 
	    	      
	    	      currentValue =+10;
	    	      pb.setValue(currentValue);
	    	      
	    	      while((userlist2 = reader2.readLine()) != null) { 
	    	    	userlist2 = userlist2.replace("?", "");
	    	    	
	    	    	currentValue+=3;
	    	    	pb.setValue(currentValue);
    	    		
	    	    	
	    	    	if(userlist2.contains("User name")) {
	    	    		userlist2 = (userlist2.replace("Password expires             ","").replace("User name                    ", ""));
	    	    		userObject.setUsername(userlist2);
	    	    	
	    	    	}else if(userlist2.contains("Account active               No")) {
	    	    			active=0;
	    	    		
	    	    		
	    	    	}else if((userlist2.contains("Password expires"))&&(active==1)) {
	    	    		userlist2 = (userlist2.replace("Password expires             ","").replace("User name                    ", ""));
	    	    		userObject.setExpiration(userlist2);
	    	    			
	    	    		LocalDateTime userDate = LocalDateTime.parse(userObject.getExpiration(),dtf);
	    	    				    	    			
	    	    			if(userDate.compareTo(twoWeeks)<=0) {
	    	    				UsersList.add(userObject);
	    	    				System.out.println(userObject.getUsername());
	    	    				System.out.println(userObject.getExpiration());	 
	    	    				System.out.println(" ");
	    	    				content = 	content + userObject.getUsername() + "<br/>" + userObject.getExpiration() + "<br/><br/>";
	    	    				}
	    	    			
	    	    		}
	    	    	

	    	      }
	    	    currentValue +=1;
  				pb.setValue(currentValue);
	    	      } catch (Exception e) {
	    	    	  
	    	      }
	    	  
	      }
	    	content = content + "</html>";
	    	pb.setValue(MAX);
	    	barFrame.setVisible(false);
	    }
	    
		public void Dialog() { 
	    	
	    	f = new JFrame("frame"); 
	  
	    	lblContent = new JLabel(content);
	        // create a panel 
	        JPanel p = new JPanel();
	        
	        scrollPane.setViewportView(lblContent);
	  	  
	        p.setLayout(new BorderLayout(0,0));
	        p.add(scrollPane,BorderLayout.CENTER); 
	  
	        f.add(p); 
	  
	        // set the size of frame 
	        f.setSize(400, 400); 
	        f.setVisible(true);; 
	    } 
	    
	    
}
