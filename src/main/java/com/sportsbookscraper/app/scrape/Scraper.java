package com.sportsbookscraper.app.scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class Scraper {
    
    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
            "org.apache.commons.logging.impl.NoOpLog");
        
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
            .setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient")
            .setLevel(Level.OFF);
    }
    
    /**
     * By default the backing {@code WebClient} uses
     * {@link BrowserVersion#FIREFOX}
     */
    public static final BrowserVersion DEFAULT_BROWSER = BrowserVersion.FIREFOX;
    
    private static final int DEFAULT_TIMEOUT = 10000;
    
    private WebClient client;
    private boolean closed;
    private List<Bookie> bookies;
    private List<MatchGroup> matchGroups;
    
    /**
     * Creates a {@code Scraper} instance backed by a {@code WebClient} using
     * the default {@code BrowserVersion}.
     * 
     * @see {@link Scraper#DEFAULT_BROWSER}
     */
    public Scraper() { this(DEFAULT_BROWSER); }
    
    /**
     * Creates a {@code Scraper} instance using a {@code WebClient} with the
     * specified {@code BrowserVersion}.
     * 
     * @param browser
     *                the browser version to use, for example
     *                {@code BrowserVersion.FIREFOX}.
     */
    public Scraper(BrowserVersion browser) {
        client = new WebClient(browser);
        
        closed = false;
        
        // ensure that WebClient's javascript engine is enabled
        client.getOptions().setJavaScriptEnabled(true);
        
        // disable certain WebClient features to try and cut down on run-time
        client.getCookieManager().setCookiesEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setDownloadImages(false);
        client.getOptions().setGeolocationEnabled(false);
        client.getOptions().setAppletEnabled(false);
        
        // DON'T DISABLE THE FOLLOWING
        // client.getOptions().setRedirectEnabled(false);
    }
    
    /**
     * 
     * @return
     */
    public List<Bookie> getBookies() { return bookies; }
    
    /**
     * 
     * @param site
     *             - the Sports Book Review site to scrape
     */
    public void scrape(String site) { scrape(site, DEFAULT_TIMEOUT); }
    
    /**
     * 
     * @param site
     *                - the Sports Book Review site to scrape
     * @param timeout
     *                - time in milliseconds to wait before failing
     */
    public void scrape(String site, int timeout) {
        if (closed) {
            throw new RuntimeException(
                "this scraper instance has been closed.");
        }
        
        HtmlPage page = openHtmlPage(site, timeout);
        
        bookies = scrapeBookies(getCarouselBooksList(page));
        
        try {
            scrapeMatches(site, bookies.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        page = selectUserSettingsOddsFormatDec(page);
        page = clickNumberSortRotText(page);
        // one = getOddsGridContainer(page).outerHtml();
        // two = clickCarouselNextAndGetOddsGridContainer(page).outerHtml();
        
        // matchGroups = scrapeMatches(site, bookies.size());
    }
    
    String one;
    String two;
    
    public String getOne() { return one; };
    
    public String getTwo() { return two; };
    
    /**
     * Closes this scraper instance and the underlying WebClient instance.
     */
    public void close() {
        
        if (!closed) {
            client.close();
            closed = true;
        }
    }
    
    /* helper function to get HtmlPage from WebClient and handle exceptions */
    private HtmlPage openHtmlPage(String site, int timeout) {
        
        // have to use client.getOptions() to set connection timeout
        client.getOptions().setTimeout(timeout);
        
        HtmlPage page = null;
        try {
            page = client.getPage(site);
        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        }
        
        return page;
    }
    
    /* */
    private Document getCarouselBooksList(HtmlPage page) {
        HtmlElement carouselBooksList = page
            .getFirstByXPath("//div[@class='carousel-bookslist']");
        
        return Jsoup.parse(carouselBooksList.asXml());
    }
    
    /* */
    private List<Bookie> scrapeBookies(Document document) {
        List<Bookie> tmpBookies = new ArrayList<Bookie>();
        Elements divElementsColumn = document.select("ul#booksCarousel");
        
        int index = 0;
        for (Element e : divElementsColumn.select("a#bookName")) {
            if (e.text().trim().isEmpty())
                continue;
            
            tmpBookies.add(new Bookie(e.text().trim(), index));
            
            index += 1;
        }
        
        return tmpBookies;
    }
    
    /* */
    private Document clickCarouselNextAndGetOddsGridContainer(HtmlPage page) {
        HtmlElement carouselNext = page.getFirstByXPath(
            "//div[@class='carousel-control']/a[@class='next']");
        
        if (carouselNext == null) {
            System.err.println("[ERROR] carouselNext is null!");
        }
        
        if (carouselNext instanceof HtmlAnchor) {
            try {
                page = ((HtmlAnchor) carouselNext).click();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            client.waitForBackgroundJavaScript(1000);
        } else {
            System.err.println("[ERROR] carouselNext is not an HtmlAnchor!");
        }
        
        return getOddsGridContainer(page);
    }
    
    /* */
    private HtmlPage selectUserSettingsOddsFormatDec(HtmlPage page) {
        HtmlSelect userSettingsOddsFormatSelect = (HtmlSelect) page
            .getElementById("usersetting_ODDS_FORMAT");
        
        HtmlOption option = userSettingsOddsFormatSelect.getOptionByValue("2");
        HtmlPage newPage = userSettingsOddsFormatSelect
            .setSelectedAttribute(option, true);
        
        client.waitForBackgroundJavaScript(1000);
        
        return newPage;
    }
    
    /* */
    private HtmlPage clickNumberSortRotText(HtmlPage page) {
        List<DomElement> divSortLink = page.getElementsById("rotText");
        
        if (divSortLink.isEmpty()) {
            System.out.println("No matches being displayed at this time.");
            return null;
        }
        
        System.out.println(divSortLink.get(0).asXml());
        DomElement first = divSortLink.get(0);
        
        HtmlAnchor sortAnchor = (HtmlAnchor) first.getFirstElementChild();
        System.out.println(sortAnchor.asXml());
        
        HtmlPage newPage = null;
        
        try {
            newPage = sortAnchor.click();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return newPage;
    }
    
    /* */
    private Document getOddsGridContainer(HtmlPage page) {
        return Jsoup
            .parse(page.getHtmlElementById("oddsGridContainer").asXml());
    }
    
    /* */
    private List<MatchGroup> scrapeMatches(String site, int numBookies)
        throws IOException {
        Document doc = Jsoup.connect(site).timeout(DEFAULT_TIMEOUT).get();
        
        List<MatchGroup> tmpMatches = new ArrayList<MatchGroup>();
        Element prev = doc.selectFirst(".carousel-control.prev");
        Element next = doc.selectFirst(".carousel-control.next");
        
        Elements dateGroup = doc.select(".data .dateGroup");
        System.out.println(dateGroup.size());
        
        for (Element group : dateGroup) {
            String date = group.select(".date").text().trim();
            MatchGroup mg = new MatchGroup(date);
            
            System.out.println(date);
            
        }
        return null;
    }
    
}
