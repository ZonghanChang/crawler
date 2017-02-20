import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.crawler.CrawlController;
//import org.slf4j.

public class Controller {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/data/crawl";
        int numberOfCrawlers = 15;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        /*
         * Instantiate the controller for this crawl.
         */
        config.setIncludeBinaryContentInCrawling(true);
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(20000);
        config.setPolitenessDelay(200);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
/*
* For each crawl, you need to add some seed urls. These are the first
* URLs that are fetched and then the crawler starts following links
* which are found in these pages
*/
        controller.addSeed("http://www.nbcnews.com/");

/*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}
