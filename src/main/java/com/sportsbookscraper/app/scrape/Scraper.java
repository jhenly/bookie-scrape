package com.sportsbookscraper.app.scrape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.sportsbookscraper.app.scrape.Match.MatchBuilder;

public class Scraper {
    
    private static File logFile;
    private static BufferedWriter logger;
    
    private static void openLogFile(String sheetName) {
        String fileName = "./log/" + sheetName + "scraper.log";
        System.out.println(
            "\nOpenening [" + sheetName + "] logfile [" + fileName + "]");
        
        logFile = new File("./log/" + sheetName + "scraper.log");
        
        OpenOption[] options = new StandardOpenOption[] {
                StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING };
        
        BufferedWriter tmpLog = null;
        try {
            tmpLog = Files.newBufferedWriter(logFile.toPath(), options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = tmpLog;
        
        log("created logfile [" + fileName + "] for sheet [" + sheetName + "]");
    }
    
    private static void log(String header, String msg, int data) {
        System.out.println("Logging [" + header + "] ...");
        
        log(String.format("%s %s %d", header, msg, data));
    }
    
    private static void log(String header, int data) {
        System.out.println("Logging [" + header + "] ...");
        
        log(String.format("%s %d", header, data));
    }
    
    private static void log(String header, String msg) {
        System.out.println("Logging [" + header + "] ...");
        
        log(header + msg);
    }
    
    private static void log(String header, boolean msgIsHeader) {
        System.out.println("Logging [" + header + "] ...");
        
        log(header);
    }
    
    private static void log(String msg) {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd@hh:mm:ss");
        String nowStr = sdf.format(now);
        
        logMsg(String.format("[%s]:%n%s%n", nowStr, msg));
    }
    
    private static void logMsg(String msg) {
        try {
            logger.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void log(StackTraceElement[] st) {
        for (int i = 0, n = st.length; i < n; i++) {
            if (i == 0)
                log(st[i].toString());
            else
                logMsg(st[i].toString() + "\n");
        }
    }
    
    private static void flushAndCloseLog() {
        try {
            logger.flush();
            logger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
    private List<DateGroup> matchGroups;
    
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
        client.getOptions().setCssEnabled(false);
        client.getOptions().setDownloadImages(false);
        client.getOptions().setGeolocationEnabled(false);
        client.getOptions().setAppletEnabled(false);
        
        // DON'T DISABLE THE FOLLOWING
        client.getCookieManager().setCookiesEnabled(true);
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
     * <b><span style="background-color:red">INTERNAL API METHOD -- THIS METHOD
     * IS ONLY USED FOR DEBUGGING!</span></b>
     * <p>
     * <b><span style="color:red">DO NOT USE THIS METHOD!</span></b>
     * <p>
     * 
     * @apiNote <span style="color:pink">TODO:</span> delete this method
     * 
     * @param site
     * @param sheetName
     */
    public void debugScrape(String site, String sheetName) {
        openLogFile(sheetName);
        scrape(site, DEFAULT_TIMEOUT);
    }
    
    /**
     * 
     * @param site
     *                - the Sports Book Review site to scrape
     * @param timeout
     *                - time in milliseconds to wait before failing
     */
    public void scrape(String site, int timeout) {
        Exception exc = null;
        Error err = null;
        
        try {
            if (closed) {
                throw new RuntimeException(
                    "this scraper instance has been closed.");
            }
            
            log("Scraping from url: ", site);
            
            HtmlPage page = openHtmlPage(site, timeout);
            
            // get list of all bookies
            bookies = scrapeBookies(getCarouselBooksList(page));
            
            // set page up for scraping
            // try {
            log("scrape: enabling correct options", true);
            page = enableCorrectOptionsOnPage(page);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            
            
            if (page == null) {
                /* TODO actually check why page is null, does it actually not
                 * contain any matches */
                // the page probably doesn't have any matches, so return
                return;
            }
            
            // try {
            // scrapeMatches(page, bookies.size());
            scrapeMatchesHU(page, bookies.size());
            // } catch (IOException ioe) {
            // ioe.printStackTrace();
            // }
            
            // matchGroups = scrapeMatches(site, bookies.size());
            
        } catch (Exception e) {
            exc = e;
        } catch (Error er) {
            err = er;
        } finally {
            if (exc != null) {
                System.out.println("An exception occured, logging it.");
                log(exc.getStackTrace());
                log(exc.getMessage());
            }
            if (err != null) {
                System.out.println("An error occured, trying to log it.");
                log(err.getStackTrace());
                log(err.getMessage());
            }
            
            System.out.println("Flushing and closing logger.\n");
            flushAndCloseLog();
            
            if (exc != null)
                exc.printStackTrace();
            if (err != null)
                err.printStackTrace();
            
        }
        
    }
    
    /* scrape helper that enables all needed options on page before scraping */
    private HtmlPage enableCorrectOptionsOnPage(HtmlPage page)
        throws IOException {
        // check the 'ROT #' checkbox so we can sort teams on their ROT's
        page = checkShowRotationsCheckBox(page);
        // need a page reload after checking this box
        page.refresh();
        
        // click on far most '#' icon to sort teams by ROT
        page = clickNumberSortRotText(page);
        
        // choose DEC from odds format drop down menu
        page = selectUserSettingsOddsFormatDec(page);
        // for some reason select won't take effect without a page refresh
        page.refresh();
        
        // all done setting options on page, time to scrape
        return page;
    }
    
    /**
     * Closes this scraper instance and the underlying WebClient instance.
     * <p>
     * <b>Note:</b> multiple calls to this method have no effect, only the first
     * call to {@code close()} has an effect.
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
            if (e.text().strip().isEmpty())
                continue;
            
            tmpBookies.add(new Bookie(e.text().strip(), index));
            
            index += 1;
        }
        
        return tmpBookies;
    }
    
    /* */
    private Document clickCarouselNextAndGetOddsGridContainer(HtmlPage page) {
        HtmlElement carouselNext = page.getFirstByXPath(
            "//div[@class='carousel-control']/a[@class='next']");
        
        if (carouselNext == null) {
            log("[ERROR] carouselNext is null!", true);
        }
        
        if (carouselNext instanceof HtmlAnchor) {
            try {
                page = ((HtmlAnchor) carouselNext).click();
            } catch (IOException e) {
                // TODO handle this exception
                e.printStackTrace();
            }
            
            client.waitForBackgroundJavaScript(1000);
        } else {
            log("[ERROR] carouselNext is not an HtmlAnchor!", true);
        }
        
        return getOddsGridContainer(page);
    }
    
    /* */
    private HtmlPage checkShowRotationsCheckBox(HtmlPage page) {
        HtmlCheckBoxInput rotCheckBox = (HtmlCheckBoxInput) page
            .getElementById("usersetting_SHOW_ROTATION");
        rotCheckBox.setChecked(true);
        
        log("checkShowRotationsCheckBox: ", " - checked it");
        return page;
    }
    
    /* */
    private HtmlPage clickNumberSortRotText(HtmlPage page) throws IOException {
        List<DomElement> divSortLink = page.getElementsById("rotText");
        
        if (divSortLink.isEmpty()) {
            log("No matches being displayed at this time.", true);
            return null;
        }
        
        DomElement first = divSortLink.get(0);
        HtmlAnchor sortAnchor = (HtmlAnchor) first.getFirstElementChild();
        
        HtmlPage newPage = sortAnchor.click();
        
        log("clickNumberSortRotText: ", " - clicked it");
        
        return newPage;
    }
    
    /* */
    private HtmlPage selectUserSettingsOddsFormatDec(HtmlPage page)
        throws IOException {
        HtmlSelect userSettingsOddsFormatSelect = (HtmlSelect) page
            .getElementById("usersetting_ODDS_FORMAT");
        
        HtmlOption option = userSettingsOddsFormatSelect.getOptionByValue("2");
        HtmlPage newPage = userSettingsOddsFormatSelect
            .setSelectedAttribute(option, true);
        
        option = userSettingsOddsFormatSelect.getOptionByValue("1");
        newPage = userSettingsOddsFormatSelect.setSelectedAttribute(option,
            false);
        
        client.waitForBackgroundJavaScript(1000);
        
        log("selectUserSettingsOddsFormatDec: ", " - selected DEC");
        return newPage;
    }
    
    /* */
    private Document getOddsGridContainer(HtmlPage page) {
        return Jsoup
            .parse(page.getHtmlElementById("oddsGridContainer").asXml());
    }
    
    
    /* */
    private List<DateGroup> scrapeMatches(HtmlPage page, int numBookies)
        throws IOException {
        if (page == null) {
            log("scrapeMatches: page is null", true);
        } else {
            
        }
        
        Document doc = Jsoup.parse(page.asXml());
        
        List<DateGroup> tmpMatches = new ArrayList<DateGroup>();
        
        Element prev = doc.selectFirst(".carousel-control.prev");
        Element next = doc.selectFirst(".carousel-control.next");
        
        Elements dateGroup = doc.select(".data .dateGroup");
        log("dataGroup.size() =", dateGroup.size());
        
        for (Element group : dateGroup) {
            String date = group.select(".date").text().trim();
            DateGroup mg = new DateGroup(date);
            
            System.out.println(date);
            parseEventLine(group);
        }
        return null;
    }
    
    /* */
    private List<DateGroup> scrapeMatchesHU(HtmlPage page, int numBookies)
        throws IOException {
        if (page == null) {
            log("scrapeMatchesHU: page is null", true);
            return null;
        }
        
        // Document doc = Jsoup.parse(page.asXml());
        
        List<DateGroup> tmpMatches = new ArrayList<DateGroup>();
        
        // Elements dateGroup = doc.select(".data .dateGroup");
        // HtmlElement data = page.getFirstByXPath("//div[@class='data']");
        // log("data.asXml() = \n", data.asXml());
        
        HtmlDivision divSport4 = (HtmlDivision) page.getElementById("sport-4");
        String dateGroupsXPath = "./div[1]/div[@class='dateGroup']";
        
        // log("divSport4.asXml() = \n", divSport4.asXml());
        
        List<HtmlElement> dateGroups = divSport4.getByXPath(dateGroupsXPath);
        
        log("dateGroups.size() =", dateGroups.size());
        
        List<Match> matches = new ArrayList<Match>();
        
        for (HtmlElement dateGroup : dateGroups) {
            
            // String date = group.select(".date").text().trim();
            HtmlDivision dateDiv = (HtmlDivision) dateGroup
                .getFirstByXPath(".//div[@class='date']");
            String date = dateDiv.asText().strip();
            
            log("dateGroup date: " + date);
            // parseEventLine(group);
            
            matches.addAll(getAllMatchInfo(dateGroup, date));
        }
        
        for (Match m : matches) {
            log("logging match", m.toString());
        }
        
        return null;
    }
    
    private List<Match> getAllMatchInfo(HtmlElement dateGroup, String date) {
        String conSchedXpath = ".//div[@class='content-scheduled content-pre-game ']";
        HtmlElement conSched = dateGroup.getFirstByXPath(conSchedXpath);
        
        List<Match> dateMatches = new ArrayList<Match>();
        for (DomNode match : conSched.getChildren()) {
            DomNode holder = match.getFirstChild();
            
            dateMatches.add(getMatchInfo(holder, date));
        }
        
        return dateMatches;
    }
    
    private Match getMatchInfo(DomNode holder, String date) {
        MatchBuilder mb = Match.createMatch(date, getBookies().size());
        
        scrapeTeamsAndUrl(holder, mb);
        scrapeOpener(holder, mb);
        
        return mb.build();
    }
    
    private void scrapeTeamsAndUrl(DomNode holder, MatchBuilder mb) {
        HtmlElement teams = holder
            .getFirstByXPath("./div[@class='el-div eventLine-team']");
        HtmlAnchor home = (HtmlAnchor) teams.getFirstByXPath("./div[1]/span/a");
        HtmlAnchor away = (HtmlAnchor) teams.getFirstByXPath("./div[2]/span/a");
        
        mb.home(home.getTextContent().strip())
            .away(away.getTextContent().strip()).url(home.getHrefAttribute());
        
    }
    
    private void scrapeOpener(DomNode holder, MatchBuilder mb) {
        HtmlElement opener = holder
            .getFirstByXPath("./div[@class='el-div eventLine-opener']");
        HtmlElement overEle = opener.getFirstByXPath("./div[1]");
        HtmlElement underEle = opener.getFirstByXPath("./div[2]");
        
        String sover = overEle.getTextContent().strip();
        String sunder = underEle.getTextContent().strip();
        
        double over = (sover.isEmpty()) ? 0.0 : Double.parseDouble(sover);
        double under = (sunder.isEmpty()) ? 0.0 : Double.parseDouble(sunder);
        
        mb.opener(over, under);
    }
    
    private List<DateGroup> parseEventLine(Element dateGroup) {
        List<DateGroup> tmpMatches = new ArrayList<DateGroup>();
        
        Element eventLines = dateGroup.getElementsByClass("eventLines").first();
        Element contentScheduled = eventLines
            .getElementsByClass("content-scheduled").first();
        
        for (Element game : contentScheduled.children()) {
            Element data = game.children().first();
            
            Elements teamNames = getTeamNames(data);
            String home = teamNames.first().child(0).text();
            String away = teamNames.last().child(0).text();
            String url = teamNames.first().child(0).attr("href");
            
            Element opener = getOpener(data);
            String over = opener.child(0).text();
            String under = opener.child(1).text();
            
            log("teams and opener",
                String.format("home: %s  away: %s%n over: %s  under: %s%n",
                    home, away, over, under));
        }
        
        return null;
    }
    
    /* */
    private Elements getTeamNames(Element cs) {
        return cs.getElementsByClass("team-name");
    }
    
    /* */
    private Element getOpener(Element cs) {
        return cs.getElementsByClass("eventLine-opener").first();
    }
    
}
