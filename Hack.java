import java.io.*;
import twitter4j.*;
import twitter4j.conf.*;
import java.util.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * Shows most used hashtags on twitter
 *
 * @author Nick Elsasser
 * @version 1
 */
public class Hack extends JFrame
{
    private static final int CAP = 50000;
    //private static MyHashMap map = new MyHashMap(CAP);
    private static Map map = Collections.synchronizedMap(new HashMap(CAP));
    
    Drawer surface;
    
    public Hack() {
        init();
    }
    
    public void init() {
        surface = new Drawer();
        add(surface);
        setTitle("SuperDuperTrends");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public Drawer getSurface() {return surface;}
    
    public static void run() throws TwitterException, IOException{
        //clear out debugging terminal
        System.out.println("\f");
        
        //set up config for tracking tweets
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("*");
        cb.setOAuthConsumerSecret("*");
        cb.setOAuthAccessToken("*");
        cb.setOAuthAccessTokenSecret("*");
        
        //setup listener
        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                    storeWords(status.getText());
            }
            
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
            public void onStallWarning(StallWarning stall) {}
            public void onScrubGeo(long l, long g) {}
        };
        
        //start listening
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);
        twitterStream.sample();
        
        Hack h = new Hack();
        
        h.setVisible(true);
        
        while(true) {
            h.getSurface().update(getTopTen());
            h.repaint();
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static Object[] getTopTen() {
        String[] top10 = new String[10];
        int[] top10count = new int[10];
        
        Set keySet = map.keySet();
        for(int i = 0; i < 10; i++) {
            int max = Integer.MIN_VALUE;
            String m = "";
            Iterator iter = keySet.iterator();
            boolean found = false;
            
            while(iter.hasNext()) {
                String key = (String)iter.next();
                int n = (int)map.get(key);
                if(n > max && !Arrays.asList(top10).contains(key)) {
                    found = true;
                    max = n;
                    m = key;
                }
            }
            
            if(found) {
                top10[i] = m;
                top10count[i] = max;
            } else {
                top10[i] = null;
                top10count[i] = 0;
            }
            
        }
        
        Object[] o = {top10, top10count};
        return o;
    }
    
    private static void storeWords(String text) {
        StringTokenizer st = new StringTokenizer(text);
        
        while(st.hasMoreTokens()) {
            String key = st.nextToken().toLowerCase();
            if(key.length() >= 1 && key.substring(0, 1).equals("#")) {
                //keep going
            } else {
                continue;
            }
            if(!map.containsKey(key)) {
                map.put(key, 1);
            } else {
                map.put(key, (int) map.get(key) + 1);
            }
            
            //System.out.println(key + " : " + (int)map.get(key));
        }
    }
}
