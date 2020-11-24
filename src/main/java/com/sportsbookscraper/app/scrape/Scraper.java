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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
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
            
            // set page up for scraping
            // try {
            log("scrape: enabling correct options", true);
            page = enableCorrectOptionsOnPage(page);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            
            // get list of all bookies
            bookies = scrapeBookies(page);
            
            
            if (page == null) {
                /* TODO actually check why page is null, does it actually not
                 * contain any matches */
                // the page probably doesn't have any matches, so return
                return;
            }
            
            // try {
            // scrapeMatches(page, bookies.size());
            scrapeMatches(page, bookies.size());
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
    private List<Bookie> scrapeBookies(HtmlPage page) {
        List<Bookie> tmpBookies = new ArrayList<Bookie>();
        DomElement divElementsColumn = page.getElementById("booksCarousel");
        
        
        int index = 0;
        for (DomNode node : divElementsColumn.getChildren()) {
            String bookieName = node.getTextContent().strip();
            if (bookieName.isEmpty())
                continue;
            
            tmpBookies.add(new Bookie(bookieName, index));
            
            index += 1;
        }
        
        return tmpBookies;
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
    private List<DateGroup> scrapeMatches(HtmlPage page, int numBookies)
        throws IOException {
        if (page == null) {
            log("scrapeMatches: page is null", true);
            return null;
        }
        DomNodeList<DomNode> dateGroupDivs = getDateGroupDivs(page);
        log("dateGroupDivs.size() =" + dateGroupDivs.size(), true);
        
        List<DateGroup> dateGroups = new ArrayList<DateGroup>();
        
        // create date groups with initial matches, not containing bookie odds
        for (DomNode dateGroupDiv : dateGroupDivs) {
            String date = scrapeDateFromDateGroupsDateDiv(dateGroupDiv);
            
            log("dateGroup date: " + date, true);
            
            // create and add a date group with initial matches
            dateGroups.add(createAllDateGroupMatches(dateGroupDiv, date));
        }
        
        log("Created initial matches, now scraping bookie odds", true);
        
        // now we scrape each bookies shown odds, then click next and repeat
        scrapeBookieOverUnders(page, dateGroups);
        
        for (DateGroup dg : dateGroups) {
            log("logging date group\n", dg.toString());
        }
        
        return null;
    }
    
    /* the number of bookies listed per next click */
    private static final int BOOKIES_PER_NEXT_CLICK = 10;
    
    /* */
    private void scrapeBookieOverUnders(HtmlPage page,
        List<DateGroup> dateGroups) {
        
        int bookiesSize = getBookies().size();
        int skipCount = BOOKIES_PER_NEXT_CLICK
            - (bookiesSize % BOOKIES_PER_NEXT_CLICK);
        
        // integer division means ((29/10 == 2) * 10) == 20
        int needSkip = ((bookiesSize / BOOKIES_PER_NEXT_CLICK)
            * BOOKIES_PER_NEXT_CLICK) - 1;
        int prevCount = bookiesSize / BOOKIES_PER_NEXT_CLICK;
        
        log(String.format(
            "scrapeBookieOverUnders: bsize=%d  skipCount=%d  needSkip=%d  prevCount=%d",
            bookiesSize, skipCount, needSkip, prevCount), true);
        
        DomNodeList<DomNode> dateGroupDivs = getDateGroupDivs(page);
        
        int whileRuns = 0;
        int bIndex = 0; // bookie index
        while (bIndex < bookiesSize) {
            whileRuns += 1;
            log("Scraping batch " + whileRuns + " of bookies", true);
            
            int bScraped = 0; // number of bookies scraped
            for (int dgi = 0, n = dateGroupDivs.size(); dgi < n; dgi++) {
                DomNode dgdiv = dateGroupDivs.get(dgi);
                DateGroup dg = dateGroups.get(dgi);
                
                // don't skip unless bIndex is to the last next carousel page
                int skip = (bIndex >= needSkip) ? skipCount : 0;
                
                bScraped = scrapeOddsFromDateGroupDiv(dgdiv, dg, bIndex, skip);
            }
            
            log("Scraped " + bScraped + " bookies this batch, total scraped is "
                + bIndex, true);
            
            bIndex += bScraped;
            
            try {
                logger.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            dateGroupDivs = clickCarouselNextAndGetDateGroupDivs(page);
        }
        
    }
    
    /* */
    private int scrapeOddsFromDateGroupDiv(DomNode dgdiv, DateGroup dg,
        int bindex, int skipCount) {
        DomNodeList<DomNode> matchHolders = getContentScheduledDiv(dgdiv)
            .getChildNodes();
        
        if (matchHolders.size() != dg.size()) {
            log("[ERROR] matchHolders.size() != dg.size()", true);
        }
        
        int oddsScraped = 0;
        // 'mi' is match index
        for (int mi = 0; mi < matchHolders.size(); mi++) {
            DomNode matchDiv = matchHolders.get(mi).getFirstChild();
            List<HtmlDivision> odds = matchDiv
                .getByXPath("./div[@class='el-div eventLine-book']");
            // log(" ODDS DIV SIZE = " + odds.size(), true);
            Match match = dg.getMatch(mi);
            
            oddsScraped = scrapeOddsToMatch(odds, match, bindex, skipCount);
            
        }
        
        return oddsScraped;
    }
    
    /* */
    private int scrapeOddsToMatch(List<HtmlDivision> odds, Match match,
        int bindex, int skipCount) {
        
        int oddsScraped = 0;
        // 'oi' is odds index
        for (int oi = 0, n = odds.size(); oi < n; oi++) {
            // skip already seen bookies on last next click
            if (skipCount > 0) {
                log("skipping bookie " + skipCount, true);
                skipCount -= 1;
                continue;
            }
            
            scrapeSingleBookieOdds(odds.get(oi), match, bindex + oddsScraped);
            oddsScraped += 1;
        }
        
        return oddsScraped;
    }
    
    /* helper function used to scrape a single bookie odds */
    private void scrapeSingleBookieOdds(HtmlDivision overUnder, Match match,
        int bIndex) {
        DomNode eOver = overUnder.getFirstChild();
        DomNode eUnder = overUnder.getLastChild();
        
        String over = eOver.getTextContent().strip();
        String under = eUnder.getTextContent().strip();
        
        match.setBookieOdds(bIndex, over, under);
    }
    
    private String scrapeDateFromDateGroupsDateDiv(DomNode dateGroupDiv) {
        DomNode dateDiv = dateGroupDiv.getFirstByXPath(".//div[@class='date']");
        
        return dateDiv.getTextContent().strip();
    }
    
    /* gets the date group divs from the <div id='sport-4'> div */
    private DomNodeList<DomNode> getDateGroupDivs(HtmlPage page) {
        return page.getElementById("sport-4").getFirstChild().getChildNodes();
    }
    
    /* gets the content scheduled div where all of the matches are */
    private DomNode getContentScheduledDiv(DomNode dateGroupDiv) {
        String conSchedXpath = ".//div[@class='content-scheduled content-pre-game ']";
        return dateGroupDiv.getFirstByXPath(conSchedXpath);
    }
    
    /* */
    private DateGroup createAllDateGroupMatches(DomNode dateGroupDiv,
        String date) {
        DomNode conSched = getContentScheduledDiv(dateGroupDiv);
        
        // create a new date group to store matches in
        DateGroup dateGroup = new DateGroup(date);
        for (DomNode match : conSched.getChildren()) {
            // scrape each match in the date group div and add it
            dateGroup.addMatch(scrapeAllButBookieOddsAndCreateMatch(match));
        }
        
        return dateGroup;
    }
    
    /* scrapes and creates a match from everything but bookie odds */
    private Match scrapeAllButBookieOddsAndCreateMatch(DomNode matchDiv) {
        MatchBuilder mb = Match.createMatch(getBookies().size());
        
        DomNode holder = matchDiv.getFirstChild();
        scrapeRotNumbers(holder, mb);
        scrapeMatchTime(holder, mb);
        scrapeTeamsAndUrl(holder, mb);
        scrapeOpener(holder, mb);
        
        return mb.build();
    }
    
    /* scrapes and sets the home and away ROT numbers */
    private void scrapeRotNumbers(DomNode holder, MatchBuilder mb) {
        HtmlDivision rots = holder
            .getFirstByXPath("./div[@class='el-div eventLine-rotation']");
        
        
        // HtmlDivision homeRot = rot.getFirstByXPath("./div[1]");
        String homeRot = rots.getChildNodes().get(1).getTextContent().strip();
        // HtmlDivision awayRot = teams.getFirstByXPath("./div[2]");
        String awayRot = rots.getChildNodes().get(2).getTextContent().strip();
        
        int hrot = 0;
        try {
            hrot = Integer.parseInt(homeRot);
        } catch (NumberFormatException e) {}
        
        int arot = 0;
        try {
            arot = Integer.parseInt(homeRot);
        } catch (NumberFormatException e) {}
        
        mb.homeRot(hrot).awayRot(arot);
    }
    
    /* scrapes and set the match time */
    private void scrapeMatchTime(DomNode holder, MatchBuilder mb) {
        HtmlDivision matchTime = holder
            .getFirstByXPath("./div[@class='el-div eventLine-time']");
        String time = matchTime.getFirstChild().getTextContent().strip();
        
        mb.time(time);
    }
    
    /* scrapes and sets the team names as well as the match's URL */
    private void scrapeTeamsAndUrl(DomNode holder, MatchBuilder mb) {
        HtmlElement teams = holder
            .getFirstByXPath("./div[@class='el-div eventLine-team']");
        
        HtmlAnchor home = (HtmlAnchor) teams.getFirstByXPath("./div[1]/span/a");
        HtmlAnchor away = (HtmlAnchor) teams.getFirstByXPath("./div[2]/span/a");
        
        mb.home(home.getTextContent().strip());
        mb.away(away.getTextContent().strip());
        mb.url(home.getHrefAttribute());
        
    }
    
    /* scrapes and sets the opener's over-under odds */
    private void scrapeOpener(DomNode holder, MatchBuilder mb) {
        HtmlElement opener = holder
            .getFirstByXPath("./div[@class='el-div eventLine-opener']");
        HtmlElement eOver = opener.getFirstByXPath("./div[1]");
        HtmlElement eUnder = opener.getFirstByXPath("./div[2]");
        
        String over = eOver.getTextContent().strip();
        String under = eUnder.getTextContent().strip();
        
        mb.opener(over, under);
    }
    
    /* */
    private DomNodeList<DomNode> clickCarouselNextAndGetDateGroupDivs(
        HtmlPage page) {
        DomElement carouselNext = page.getElementById("feedHeaderCarousel")
            .getFirstElementChild().getFirstByXPath("./a[2]");
        
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
        
        return getDateGroupDivs(page);
    }
    
    /* */
    private HtmlElement clickCarouselPrev(HtmlPage page) {
        DomElement carouselPrev = page.getElementById("feedHeaderCarousel")
            .getFirstElementChild().getFirstByXPath("./a[1]");
        
        if (carouselPrev == null) {
            log("[ERROR] carouselPrev is null!", true);
        }
        
        if (carouselPrev instanceof HtmlAnchor) {
            try {
                page = ((HtmlAnchor) carouselPrev).click();
            } catch (IOException e) {
                // TODO handle this exception
                e.printStackTrace();
            }
            
            client.waitForBackgroundJavaScript(1000);
        } else {
            log("[ERROR] carouselPrev is not an HtmlAnchor!", true);
        }
        
        // String conSchedXpath = "//div[@class='content-scheduled
        // content-pre-game ']";
        
        // return page.getFirstByXPath(conSchedXpath);
        return null;
    }
    
    /* returns a DomNode's text content as a double, or 0.0 if no text */
    private double domNodeTextToDouble(DomNode node) {
        String nodeText = node.getTextContent().strip();
        return nodeText.isEmpty() ? 0.0 : Double.parseDouble(nodeText);
    }
    
}
