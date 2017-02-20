import java.io.IOException;
import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Set;
import java.io.FileWriter;

public class MyCrawler extends WebCrawler {
    private final String fetchFileName = "fetch_NBC_News.csv";
    private final String visitFileName = "visit_NBC_News.csv";
    private final String urlsFileName = "urls_NBC_News.csv";

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|xml|mp3|mp3|zip|gz))$");
    private final static Pattern filters = Pattern.compile(".*(\\.(html|doc|pdf|jpg|png|gif|jpeg))$");
    private final static Pattern directoryFilters = Pattern.compile("http?:\\/\\/www\\.nbcnews[.]com\\/?.*(?=\\s|$)");
    private FileWriter fetchWriter;
    private FileWriter visitWriter;
    private FileWriter urlsWriter;

    // private HashSet<String> encountered = new HashSet<String>();
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
            if(directoryFilters.matcher(href).matches()){
                urlsWriter.append(href).append(",").append("OK").append("\n");
            }
            else{
                urlsWriter.append(href).append(",").append("N_OK").append("\n");
            }
            urlsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return !FILTERS.matcher(href).matches() && href.startsWith("http://www.nbcnews.com/");

        return (filters.matcher(href).matches()
                && href.startsWith("http://www.nbcnews.com/"))
                || (directoryFilters.matcher(href).matches() && !FILTERS.matcher(href).matches());

    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        Set<WebURL> links = page.getParseData().getOutgoingUrls();
        if(page.getParseData() instanceof HtmlParseData || page.getParseData() instanceof BinaryParseData) {
            try {
                visitWriter = new FileWriter(visitFileName, true);
                visitWriter.append(page.getWebURL().getURL()).append(",").append(String.valueOf(page.getContentData().length))
                        .append(",").append(String.valueOf(links.size())).append(",").append(page.getContentType()).append("\n");
                visitWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        try {
            fetchWriter = new FileWriter(fetchFileName, true);
            String url = webUrl.getURL();
            if(url.contains(",")) {
                url = url.replace(',', '-');
            }
            fetchWriter.append(url).append(",").append(String.valueOf(statusCode)).append("\n");
            fetchWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}