import java.net.URL;
import java.util.ArrayList;

public class Node {
    Node parent;
    String URL;

    public Node(Node parent, String URL){
        this.parent = parent;
        this.URL = URL;
    }

    public Node(){

    }

    public Node(String URL){
        this.URL = URL;
    }
}
