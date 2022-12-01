//Rio Voss-Kernan, Nov 2022, HTML Scraper
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


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
        //scrapePageForLinks(convertToApiURL("Ryan Reynolds"));
        //prepareGUI();
        //findTarget("train","",1);

        targetURL = "20th Century Fox";
        String url = "Ryan Reynolds";
        Queue<String> queue = new LinkedList<>(scrapePageForLinks(url));
        ArrayList<String> discovered = new ArrayList<>();
        discovered.add(url);

        recursiveBFS("",queue, discovered);
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


    public void recursiveBFS(String path, Queue<String> q, ArrayList<String> discovered) {
        if (q.isEmpty()) {
            return;
        }

        // dequeue front node and print it
        String v = q.poll();
        path += v + "\n";

        if(v.equalsIgnoreCase(targetURL)){
            System.out.println("\n" + "PATH: \n" + path);
            return;

        }else{
            System.out.println(v + " ");
        }




        // do for every edge (v, u)
        for(String u: scrapePageForLinks(v)) {
            if (!discovered.contains(u)) {
                // mark it as discovered and enqueue it
                discovered.add(u);
                q.add(u);
            }
        }

        recursiveBFS(path, q, discovered);
    }

    public boolean findTarget(String URL, String path, int depth){
        ArrayList<String> linksOnPage = scrapePageForLinks(URL);

        //BASE CASE (Success)
        if(URL.equals(targetURL)){
            path += "\n" + URL;
            System.out.println("\nPATH TO TARGET URL:" + path);
            //ta.setText("\nPATH TO TARGET URL:" + path);
            return true;

        //BASE CASE (Max Depth)
        }else if(depth > 2) {
            return false;

        //CONTINUE CASE
        }else{
            for(String link: linksOnPage){
                if(!path.contains(link)) {
                    //ta.append("begin at " + depth + " depth: " + link + "\n");
                    //ta.update(ta.getGraphics());
                    System.out.println(depth + " depth: " +  link);

                    if(findTarget(link, path + "\n" + URL, depth+1)){
                        return true;
                    }
                }
            }
            return false;
        }
    }


    public ArrayList<String> scrapePageForLinks(String pURL){
        ArrayList<String> links = new ArrayList<>();

            String convertedTitle = pURL.replaceAll(" ", "_");
            String URL = "https://en.wikipedia.org/w/api.php?action=query&prop=links&format=json&pllimit=max&titles=".concat(convertedTitle);

            try {
                //Set up inputStream
                URL url = new URL(URL);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = reader.readLine();

                int index = line.indexOf("\"title\":\"", line.indexOf("\"links\":")) + 9;
                while(index > 20){

                    String link = line.substring(index,line.indexOf("\"",index+1));

                    if (!link.contains(":") && !link.contains("#") && !link.contains("Main_Page")
                            && !link.contains("Category:") && !link.contains("Template talk:")
                            && !link.contains("pURL") && !link.contains("Portal:") && !link.contains("Wikipedia:")){

                        links.add(link);
                        //System.out.println(link);
                    }

                    index = line.indexOf("\"title\":\"", index) + 9;
                }
                reader.close();
            } catch (Exception ex) {
                System.out.println(ex+"");
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

                targetURL = endURLta.getText();

                findTarget(pURL, "", 2);
                System.out.println("search ended");
            }
        }
    }

    public String wikiLinkToURL(String wikiLink){
        return "https://en.wikipedia.org".concat(wikiLink);
    }

}