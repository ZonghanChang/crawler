/**
 * Created by zonghanchang on 2/18/17.
 */
import java.io.IOException;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Set;
import java.io.FileWriter;
import java.util.HashSet;
public class MyCrawler extends WebCrawler {
    private final String fetchFileName = "fetch_NBC_News.csv";
    private final String visitFileName = "visit_NBC_News.csv";
    private final String urlsFileName = "urls_NBC_News.csv";

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|mp3|zip|gz))$");
    private FileWriter fetchWriter;
    private FileWriter visitWriter;
    private FileWriter urlsWriter;

    private HashSet<String> encountered = new HashSet<String>();

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override

    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        try {
            urlsWriter = new FileWriter(urlsFileName, true);
            if(href.startsWith("http://www.nbcnews.com/")){
                urlsWriter.append(href).append(",").append("OK").append("\n");

                if(!encountered.contains(href)){
                    encountered.add(href);
                }
            }
            else{
                urlsWriter.append(href).append(",").append("N_OK").append("\n");

                if(!encountered.contains(href)){
                    encountered.add(href);

                }
            }
            urlsWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://www.nbcnews.com/");
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        System.out.println("URL: " + url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();


            try {
                fetchWriter = new FileWriter(fetchFileName, true);
                fetchWriter.append(page.getWebURL().getURL() + "," + page.getStatusCode() + "\n");
                fetchWriter.close();

                visitWriter = new FileWriter(visitFileName, true);
                visitWriter.append(page.getWebURL().getURL() + "," + page.getContentData().length + "," +
                                   links.size() + "," + page.getContentType() + "\n");
                visitWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

//            System.out.println("status code: " + page.getStatusCode());
//            System.out.println("content type: " + page.getContentType());
//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
        }
    }

    @Override
    public void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

    }
}