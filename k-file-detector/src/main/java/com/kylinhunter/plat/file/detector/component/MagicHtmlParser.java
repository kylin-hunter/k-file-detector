package com.kylinhunter.plat.file.detector.component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.FileType;
import com.kylinhunter.plat.file.detector.config.Magic;
import com.kylinhunter.plat.file.detector.exception.DetectException;

import lombok.Data;
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
     * @param parseContext parseContext
     * @param tr           tr
     * @return void
     * @title processTr
     * @description
     * @author BiJi'an
     * @date 2022-10-27 00:52
     */
    private void processTr(ParseContext parseContext, Element tr) {
        Elements tds = tr.select("td");
        if (tds.size() == 3) {
            parseContext.validTrNums++;
            processTds(parseContext, tds);
        } else {
            parseContext.invalidTrNums++;
            log.warn("invalid tr ({}) content: {} ", tds.size(), tr.text());
        }

    }

    /**
     * @param elementTd0 elementTd0
     * @return void
     * @title isMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-27 00:56
     */
    private boolean isMagicNumber(Element elementTd0) {
        if (elementTd0.html().indexOf("face=\"courier\"") > 0) {
            return true;
        }
        return false;
    }

    /**
     * @param parseContext parseContext
     * @param tds          tds
     * @return void
     * @title processTds
     * @description
     * @author BiJi'an
     * @date 2022-10-27 01:22
     */
    private void processTds(ParseContext parseContext, Elements tds) {
        Element elementTd0 = tds.get(0);
        String td0Text = elementTd0.text().replace(" ", "");
        String td1Text = tds.get(1).text();
        String td2Text = tds.get(2).text();

        if (StringUtils.isNotBlank(td1Text)) {
            throw new DetectException("td1Text must be empty");
        }
        if (StringUtils.isEmpty(td2Text)) {
            throw new DetectException("td2Text can't be  empty");
        }
        //        log.info(td0Text + ":" + td1Text + ":" + td2Text);

        if (isMagicNumber(elementTd0)) { // is magic number
            if (StringUtils.isEmpty(td0Text)) {
                throw new DetectException("td0Text can't be  empty");
            }
            parseContext.curNumber = td0Text;
            Magic magic = new Magic(parseContext.curNumber);
            magic.setFileTypes(Lists.newArrayList());
            parseContext.numberMaps.put(parseContext.curNumber, magic);
        } else {

            if (!StringUtils.isEmpty(parseContext.curNumber)) {

                String extension = td0Text;
                if (StringUtils.isEmpty(td0Text) || td0Text.contains("n/a")) {
                    log.error("invalid format=> " + td0Text);
                    extension = "";
                }
                Magic magic = parseContext.numberMaps.get(parseContext.curNumber);
                if (magic == null) {
                    throw new DetectException("magic is null" + parseContext.curNumber);
                }

                FileType fileType = new FileType();
                fileType.setId(++parseContext.nextFileTypeId + "");
                fileType.setExtension(extension);
                fileType.setDesc(td2Text);
                magic.getFileTypes().add(fileType);

            }

        }
    }

    /**
     * @return java.lang.String
     * @title readHtml
     * @description
     * @author BiJi'an
     * @date 2022-10-27 00:47
     */
    private String readHtml() {
        try {
            InputStream inputStreamInClassPath =
                    ResourceHelper.getInputStreamInClassPath("/signature/File-Signatures-2022-10-26.html");
            return IOUtils.toString(inputStreamInClassPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DetectException("read html error", e);
        }

    }

    /**
     * @return com.kylinhunter.plat.file.detector.config.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    public List<Magic> parse() {
        Document document = Jsoup.parse(readHtml());
        ParseContext parseContext = new ParseContext();
        Elements trs = document.select("tr:has(td)");
        parseContext.trNums = trs.size();
        for (Element tr : trs) {
            processTr(parseContext, tr);
        }
        revise(parseContext);
        log.info(parseContext.toString());
        return parseContext.numberMaps.values().stream().collect(Collectors.toList());
    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title revise
     * @description
     * @author BiJi'an
     * @date 2022-10-27 01:06
     */
    private void revise(ParseContext parseContext) {

        Map<String, Magic> numberMaps1 = parseContext.numberMaps;

        numberMaps1.forEach((k, v) -> {
            //            System.out.println(k + "/" + v);
            if (v.getFileTypes().isEmpty()) {
                if (!k.equals("04000000xxxxxxxxxxxxxxxx20030000or")) {
                    if (!k.equals("00004949585052or")) {
                        if (!k.equals("9500or")) {
                            if (!k.equals("31BEor")) {
                                if (!k.equals("[512(0x200)byteoffset]FDFFFFFFnn00")) {
                                    if (!k.equals("4A47030Eor")) {
                                        if (!k.equals("E9or")) {
                                            if (!k.equals("E8or")) {
                                                if (!k.equals("5B5645525Dor")) {
                                                    if (!k.equals("FFEx")) {
                                                        if (!k.equals("474946383761or")) {
                                                            if (!k.equals("474946383761or")) {
                                                                if (!k.equals("46726F6D203F3F3For")) {
                                                                    if (!k.equals("46726F6D202020or")) {
                                                                        throw new DetectException("dd: " + k);

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }

            }
        });
    }

    /**
     * @author BiJi'an
     * @description
     * @date 2022-10-02 14:08
     **/
    @Slf4j
    @Data
    public static class ParseContext {
        public int trNums;
        public int invalidTrNums;
        public int validTrNums;
        public String curNumber;
        public int nextFileTypeId;

        public Map<String, Magic> numberMaps = Maps.newTreeMap();

        @Override
        public String toString() {
            return new StringJoiner(", ", ParseContext.class.getSimpleName() + "[", "]")
                    .add("trNums=" + trNums)
                    .add("invalidTrNums=" + invalidTrNums)
                    .add("validTrNums=" + validTrNums)
                    .add("curNumber='" + curNumber + "'")
                    .add("numberMaps=" + numberMaps.size())
                    .toString();
        }
    }

}
