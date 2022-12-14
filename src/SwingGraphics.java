//Rio Voss-Kernan, Nov 2022, HTML Scraper
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class SwingGraphics implements ActionListener {
    private JFrame mainFrame;

    private JLabel startURLLabel;
    private JLabel endURLLabel;

    private JPanel topPanel;
    private JPanel startURLPanel;
    private JPanel endURLPanel;
    private JPanel textPanel;

    private JButton goButton;

    private JTextArea ta;
    private JTextField startURLta;
    private JTextField endURLta;

    public String targetURL;

    private int WIDTH=800;
    private int HEIGHT=700;

    public SwingGraphics() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("HTML Scraper");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    //Instantiate Panels
        textPanel = new JPanel();
        topPanel = new JPanel();
        startURLPanel = new JPanel();
        endURLPanel = new JPanel();
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,1));

    //text area
        textPanel.setLayout(new GridLayout());
        ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Poppins",Font.PLAIN,12));
    //set scrollable
        JScrollPane scroll = new JScrollPane(ta);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textPanel.add(scroll);
    //color
        //ta.setBackground(Color.black);
        //ta.setForeground(Color.GREEN);

    //button
        goButton = new JButton("GO");
        goButton.setFont(new Font("Poppins",Font.BOLD,20));
        goButton.setPreferredSize(new Dimension(200,50));
        goButton.setActionCommand("GO");
        goButton.addActionListener(new ButtonClickListener());

    //set labels
        endURLLabel = new JLabel("Target:", JLabel.CENTER);
        startURLLabel = new JLabel("Start:    ", JLabel.CENTER);
        endURLLabel.setFont(new Font("Poppins",Font.BOLD,20));
        startURLLabel.setFont(new Font("Poppins",Font.BOLD,20));

    //set URL panel
        startURLPanel.setLayout(new FlowLayout());
        startURLPanel.add(startURLLabel);
        startURLta = new JTextField();
        startURLta.setPreferredSize(new Dimension(400,20));
        startURLPanel.add(startURLta);
        inputPanel.add(startURLPanel);

    //set endURLta panel
        endURLPanel.setLayout(new FlowLayout());
        endURLPanel.add(endURLLabel);
        endURLta = new JTextField();
        endURLta.setPreferredSize(new Dimension(400,20));
        endURLPanel.add(endURLta);
        inputPanel.add(endURLPanel);

    //set topPanel Layout
        topPanel.setLayout(new FlowLayout());
        topPanel.add(inputPanel);
        topPanel.add(goButton);

    // set Frame layout
        mainFrame.add(textPanel,BorderLayout.CENTER);
        mainFrame.add(topPanel,BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }


    public boolean findTarget(String URL, String path, int depth){
        ArrayList<String> linksOnPage = scrapePageForLinks(URL);
        ArrayList<String> linksVisited = new ArrayList<>();

        //BASE CASE (Success)
        if(URL.equals(targetURL)){
            path += "\n" + URL;
            //System.out.println("\nPATH TO TARGET URL:" + path);
            ta.setText("\nPATH TO TARGET URL:" + path);
            return true;

        //BASE CASE (Max Depth)
        }else if(depth == 0) {
            return false;

        //CONTINUE CASE
        }else{
            for(String link: linksOnPage){
                if(!path.contains(link)) {
                    System.out.println("begin at " + depth + " depth: " + link);
                    ta.append("begin at " + depth + " depth: " + link + "\n");
                    if(findTarget(link, path + "\n" + URL, depth-1)){
                        return true;
                    }
                }
            }
            return false;
        }
    }


    public ArrayList<String> scrapePageForLinks(String pURL){
        ArrayList<String> links = new ArrayList<>();
            try {
                //Set up inputStream
                URL url = new URL(pURL);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line;

                //Read line-by-line until end
                while ((line = reader.readLine()) != null) {
                    //get Wiki Links
                    int index = line.indexOf("href=\"/wiki");
                    while(index > 0){

                        int endOfLink = line.indexOf("\"",index+13);
                        String link = line.substring(index + 6,endOfLink);

                        if (!link.contains(":") && !link.contains("#") && !link.contains("Main_Page")) { //some links are .json or just #. This avoids that
                            links.add(wikiLinkToURL(link));
                        }

                        index = line.indexOf("href=\"/wiki",index+13);
                    }
                }
                reader.close();
            } catch (Exception ex) {
                ta.setText(ex + "");
            }
        return links;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("GO")) {
                ta.setText("");

                String pURL = startURLta.getText();
                if(!pURL.contains("https://en.wikipedia.org/wiki")){
                    pURL = "https://en.wikipedia.org/wiki/" + pURL;
                }

                targetURL = endURLta.getText();
                if(!targetURL.contains("https://en.wikipedia.org/wiki")){
                    targetURL = "https://en.wikipedia.org/wiki/" + targetURL;
                }

                findTarget(pURL, "", 1);
                System.out.println("search ended");
            }
        }
    }

    public String wikiLinkToURL(String wikiLink){
        return "https://en.wikipedia.org".concat(wikiLink);
    }
}