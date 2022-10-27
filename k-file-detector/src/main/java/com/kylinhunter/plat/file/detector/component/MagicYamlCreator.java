package com.kylinhunter.plat.file.detector.component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.parse.ParseMagic;
import com.kylinhunter.plat.file.detector.parse.ParseResult;
import com.kylinhunter.plat.file.detector.parse.ParseSpecialConfigHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description https://www.garykessler.net/library/file_sigs.html
 * @date 2022-10-02 14:08
 **/
@Slf4j
@Component
public class MagicYamlCreator {
    private static final String HTML_PATH = "/signature/File-Signatures-2022-10-26.html";
    private static final String TR_MAGIC_NUMBER_TAG = "face=\"courier\"";
    private static final Pattern PATTERN_NUMBER = Pattern.compile("^\\[\\s*(\\d+|\\d+,\\d+)[\\s\\w\\(\\)]+\\](.*$)");
    private static final Pattern PATTERN_HEX = Pattern.compile("^[A-Fa-f0-9xn]+$");

    /**
     * @return java.lang.String
     * @title readHtml
     * @description
     * @author BiJi'an
     * @date 2022-10-27 00:47
     */
    private String readHtml() {
        try {
            InputStream inputStreamInClassPath = ResourceHelper.getInputStreamInClassPath(HTML_PATH);
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
    public ParseResult parse() {
        ParseResult parseResult = new ParseResult();
        Document document = Jsoup.parse(readHtml());
        Elements trs = document.select("tr:has(td)");
        parseResult.setTrNums(trs.size());
        for (Element tr : trs) {
            processTr(parseResult, tr);
        }
        revise(parseResult);
        log.info(parseResult.toString());
        return parseResult;
    }

    /**
     * @return void
     * @title create
     * @description
     * @author BiJi'an
     * @date 2022-10-28 02:04
     */
    public List<String> toYaml(List<ParseMagic> parseMagics) {
        if (parseMagics == null) {
            parseMagics = this.parse().getParseMagics();
        }

        List<String> lines = Lists.newArrayList();
        lines.add("magics: ");
        parseMagics.forEach(magic -> {
            lines.add("  - number: " + magic.getNumber());

            lines.add("    desc: '" + magic.getDesc().replace("'", "''") + "'");

            lines.add("    fileTypes:  ");

            magic.getFileTypes().forEach(fileType -> {
                lines.add("      - id: " + fileType.getId());
                lines.add("        extension: \"" + fileType.getExtension() + "\"");
                lines.add("        desc: '" + fileType.getDesc().replace("'", "''") + "'");
            });

        });
        return lines;
    }

    /**
     * @param parseResult parseResult
     * @param tr          tr
     * @return void
     * @title processTr
     * @description
     * @author BiJi'an
     * @date 2022-10-27 00:52
     */
    private void processTr(ParseResult parseResult, Element tr) {
        Elements tds = tr.select("td");
        if (tds.size() == 3) {
            parseResult.incrementValidTrNums();
            processTds(parseResult, tds);
        } else {
            parseResult.incrementInvalidTrNums();
            log.warn("invalid tr  tds.size={} ,content: {} ", tds.size(), tr.text());
        }

    }

    /**
     * @param parseResult parseResult
     * @param tds         tds
     * @return void
     * @title processTds
     * @description
     * @author BiJi'an
     * @date 2022-10-27 01:22
     */
    private void processTds(ParseResult parseResult, Elements tds) {
        Element elementTd0 = tds.get(0);
        String td0Text = elementTd0.text().trim();
        String td0Html = elementTd0.html().trim();
        String td1Text = tds.get(1).text().trim();
        String td2Text = tds.get(2).text().trim();
        //                log.info(td0Text + ":" + td1Text + ":" + td2Text);

        if (StringUtils.isNotBlank(td1Text)) {
            throw new DetectException("td1Text must be empty");
        }
        if (StringUtils.isEmpty(td2Text)) {
            throw new DetectException("td2Text can't be  empty");
        }

        if (td0Html.indexOf(TR_MAGIC_NUMBER_TAG) > 0) { // is magic number
            ParseMagic parseMagic = parseMagicNumber(td0Text);
            parseMagic.setDesc(td2Text);
            parseResult.setParseMagic(parseMagic);
            parseResult.add(parseMagic);
        } else {
            ParseMagic parseMagic = parseResult.getParseMagic();
            if (parseMagic != null) {
                String extension = td0Text;
                if (StringUtils.isEmpty(td0Text) || td0Text.contains("n/a")) {
                    log.warn("invalid format=> " + td0Text);
                    extension = "";
                }
                FileType fileType = new FileType();
                fileType.setExtension(extension);
                fileType.setDesc(td2Text);
                parseMagic.getFileTypes().add(fileType);
            }

        }
    }

    /**
     * @param td0Text td0Text
     * @return com.kylinhunter.plat.file.detector.parse.ParseMagic
     * @title parseMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-28 00:09
     */
    public ParseMagic parseMagicNumber(String td0Text) {
        ParseMagic parseMagic = new ParseMagic(td0Text);
        parseMagicNumberDefault(parseMagic);
        if (parseMagic.isValid()) {
            return parseMagic;
        } else {
            parseMagicNumberByReg(parseMagic);
            if (parseMagic.isValid()) {
                return parseMagic;
            } else {
                if (ParseSpecialConfigHelper.invalidNumberContent(td0Text)) {
                    parseMagic.setValid(false);
                    log.warn("invalid magic number,td0Text=>" + td0Text);
                    return parseMagic;
                }
            }
            throw new DetectException("can't parse  magic number, td0Text=>" + td0Text);
        }

    }

    /**
     * @param parseMagic parseMagic
     * @return void
     * @title parseMagicNumberDefault
     * @description
     * @author BiJi'an
     * @date 2022-10-28 00:32
     */
    private void parseMagicNumberDefault(ParseMagic parseMagic) {
        String td0Text = parseMagic.getTd0Text();
        String magicNumber = td0Text.replace(" ", "").replace("or", "");
        if (isValidMagicNumber(magicNumber)) {
            parseMagic.setOffset("0");
            parseMagic.setNumber(magicNumber);
            parseMagic.setValid(true);
        }

    }

    /**
     * @param parseMagic parseMagic
     * @return java.lang.String
     * @title readMagicNumberByReg
     * @description
     * @author BiJi'an
     * @date 2022-10-27 17:23
     */
    private void parseMagicNumberByReg(ParseMagic parseMagic) {
        String td0Text = parseMagic.getTd0Text();

        Matcher matcher = PATTERN_NUMBER.matcher(td0Text);
        int groupCount = matcher.groupCount();
        if (matcher.find() && groupCount == 2) {
            String magicNumber = matcher.group(2).replace(" ", "");
            if (isValidMagicNumber(magicNumber)) {
                parseMagic.setValid(true);
                parseMagic.setOffset(matcher.group(1));
                parseMagic.setNumber(magicNumber);
            }
        }

    }

    /**
     * @param str str
     * @return boolean
     * @title isHexStr
     * @description
     * @author BiJi'an
     * @date 2022-10-27 16:33
     */
    public static boolean isValidMagicNumber(String str) {
        return !StringUtils.isEmpty(str) && PATTERN_HEX.matcher(str).matches();
    }

    /**
     * @param parseResult parseResult
     * @return void
     * @title revise
     * @description
     * @author BiJi'an
     * @date 2022-10-27 01:06
     */
    private void revise(ParseResult parseResult) {

        Map<String, ParseMagic> numberMaps = parseResult.getNumberMaps();

        numberMaps.forEach((magicNumber, parseMagic) -> {
            String sameMagicNumber = ParseSpecialConfigHelper.getSameMagicNumber(magicNumber);
            if (!StringUtils.isEmpty(sameMagicNumber)) {
                numberMaps.put(magicNumber, numberMaps.get(sameMagicNumber));
            }
        });
        numberMaps.forEach((magicNumber, parseMagic) -> {
            if (parseMagic.getFileTypes().isEmpty()) {
                throw new DetectException(" file type can't be empty for number=>" + magicNumber);
            }
        });
        parseResult.setParseMagics(
                parseResult.getNumberMaps().values().stream().sorted()
                        .peek(parseMagic -> parseMagic.getFileTypes()
                                .forEach(f -> f.setId(parseResult.nextFileTypeId())))
                        .collect(Collectors.toList()));
    }

}
