package com.kylinhunter.plat.file.detector.component;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 14:08
 **/
@Slf4j
@Component
public class MagicHtmlParser {

    /**
     * @return com.kylinhunter.plat.file.detector.config.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public void parset() throws IOException {
        InputStream inputStreamInClassPath =
                ResourceHelper.getInputStreamInClassPath("/signature/File-Signatures-2022-10-26.html");
        String html = IOUtils.toString(inputStreamInClassPath, "Utf-8");
        Document document = Jsoup.parse(html);

        Elements trs = document.select("tr:has(td)");
        int validMagic = 0;
        for (Element element : trs) {
            Elements tds = element.select("td");
            if (tds.size() == 3) {
                validMagic++;
                String td1 = tds.next().text();
                String td2 = tds.next().text();
                String td3 = tds.next().text();
                System.out.println(td1 + td2 + td3);

            } else {
                System.out.println("tr============>" + tds.size() + "/" + element.text());

            }

        }

        System.out.println("trs.size():" + trs.size());
        System.out.println("validMagic:" + validMagic);
    }

    public static void main(String[] args) throws IOException {
        MagicHtmlParser magicHtmlParser = new MagicHtmlParser();
        magicHtmlParser.parset();
    }

}
