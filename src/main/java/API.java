import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {
//    public static void main(String[] args) {
//        API api = new API();
//        System.out.println(api.getLinksFromPage("Train").body);
//    }

    public HttpResponse getLinksFromPage (String page){
        try{
            HttpRequest queryRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://en.wikipedia.org/w/api.php?action=query&prop=links&pllimit=max&format=xml&titles=" + page))
                    .header("RioVK", "?")
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> apiResponse = httpClient.send(queryRequest, HttpResponse.BodyHandlers.ofString());
            return apiResponse;

        }catch(Exception ex){
            System.out.println(ex+"");
        }

        return null;
    }





    public HttpResponse getLinksToPage (String page){
        try{
            HttpRequest queryRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://en.wikipedia.org/w/api.php?action=query&prop=linkshere&lhlimit=max&format=json&titles=" + page))
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> apiResponse = httpClient.send(queryRequest, HttpResponse.BodyHandlers.ofString());
            return apiResponse;

        }catch(Exception ex){
            System.out.println(ex+"");
        }

        return null;
    }

}



