package com.kylinhunter.plat.file.detector.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.kylinhunter.plat.file.detector.common.component.Component;
import com.kylinhunter.plat.file.detector.common.util.ResourceHelper;
import com.kylinhunter.plat.file.detector.config.FileType;
import com.kylinhunter.plat.file.detector.exception.DetectException;
import com.kylinhunter.plat.file.detector.parse.ParseContext;
import com.kylinhunter.plat.file.detector.parse.ParseMagic;
import com.kylinhunter.plat.file.detector.parse.ParseMagicHelper;
import com.kylinhunter.plat.file.detector.parse.YamlMessage;

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
    protected List<ParseMagic> parse(ParseContext parseContext) {

        Document document = Jsoup.parse(readHtml());
        Elements trs = document.select("tr:has(td)");
        parseContext.setTrNums(trs.size());
        for (Element tr : trs) {
            processTr(parseContext, tr);
        }
        List<ParseMagic> revise = revise(parseContext);
        log.info("parseContext=>{}", parseContext);
        return revise;
    }

    /**
     * @return void
     * @title create
     * @description
     * @author BiJi'an
     * @date 2022-10-28 02:04
     */
    public YamlMessage toYaml(File file) {
        YamlMessage yamlMessage = new YamlMessage();
        ParseContext parseContext = yamlMessage.getParseContext();
        List<ParseMagic> parseMagics = this.parse(parseContext);
        yamlMessage.setParseMagics(parseMagics);

        List<String> lines = Lists.newArrayList();
        lines.add("magics: ");
        parseMagics.forEach(magic -> {
            lines.add("  - number: " + magic.getNumber());
            lines.add("    offset: " + magic.getOffset());

            lines.add("    desc: '" + magic.getDesc().replace("'", "''") + "'");

            lines.add("    fileTypes:  ");

            magic.getFileTypes().forEach(fileType -> {
                lines.add("      - id: " + fileType.getId());
                lines.add("        extension: \"" + fileType.getExtension() + "\"");
                lines.add("        desc: '" + fileType.getDesc().replace("'", "''") + "'");
            });

        });
        try {
            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            throw new DetectException("write yaml to file error", e);
        }
        return yamlMessage;
    }

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
            parseContext.incrementValidTrNums();
            processTds(parseContext, tds);
        } else {
            parseContext.incrementInvalidTrNums();
            log.warn("invalid tr(tds.size={}),trText=>{} ", tds.size(),
                    StringUtils.substring(tr.text(), 0, 30));
        }

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
        String td0Text = elementTd0.text().trim();
        String td0Html = elementTd0.html().trim();
        String td1Text = tds.get(1).text().trim();
        String td2Text = tds.get(2).text().trim();
        //                log.info(td0Text + ":" + td1Text + ":" + td2Text);
        Preconditions.checkArgument(StringUtils.isBlank(td1Text), "td1Text must be empty");
        Preconditions.checkArgument(!StringUtils.isBlank(td2Text), "td2Text can't be  empty");

        if (td0Html.indexOf(TR_MAGIC_NUMBER_TAG) > 0) { // is magic number
            processTdsMagicNumber(parseContext, td0Text, td2Text);
        } else {
            processTdsFileType(parseContext, td0Text, td2Text);
        }
    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title processTdsMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-28 17:11
     */
    private void processTdsMagicNumber(ParseContext parseContext, String td0Text, String td2Text) {
        ParseMagic parseMagic = parseMagicNumber(td0Text);
        parseMagic.setDesc(td2Text);
        parseContext.setCurrentParseMagic(parseMagic);
        parseContext.add(parseMagic);
    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title processTdsMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-28 17:11
     */
    private void processTdsFileType(ParseContext parseContext, String td0Text, String td2Text) {
        ParseMagic parseMagic = parseContext.getCurrentParseMagic();
        if (parseMagic != null && parseMagic.isValid()) {
            if (!StringUtils.isEmpty(td0Text) && !td0Text.contains("n/a")) {
                String extension = td0Text.toLowerCase();
                int extensionLen = extension.length();
                if (extensionLen < 2) {
                    throw new DetectException("invalid extension,td0Text=> " + td0Text);
                } else if (extensionLen <= 4) {
                    addFile(parseContext, td0Text, td2Text, parseMagic, extension, td2Text);

                } else {
                    String[] extensions = StringUtils.split(extension, ",");
                    for (String ex : extensions) {
                        extension = ex.trim();
                        extensionLen = extension.length();
                        if (extensionLen < 2) {
                            throw new DetectException("invalid extension,td0Text=> " + td0Text);
                        } else if (extensionLen <= 5) {
                            addFile(parseContext, td0Text, td2Text, parseMagic, extension, td2Text);

                        } else {
                            extension = ParseMagicHelper.explainExtension(extension);
                            addFile(parseContext, td0Text, td2Text, parseMagic, extension, td2Text);

                        }
                    }
                }
            } else {
                addFile(parseContext, td0Text, td2Text, parseMagic, "", td2Text);
            }
        }
    }

    private void addFile(ParseContext parseContext, String td0Text, String td2Text, ParseMagic parseMagic,
                         String extension,
                         String desc) {
        if (StringUtils.isEmpty(extension)) {
            parseContext.incrementNoExtensionNums();
            log.warn("extenion is empty,td0Text=> " + td0Text + ",td2Text=> " + StringUtils.substring(td2Text, 0,
                    20));
        } else {
            if (!ParseMagicHelper.isValidExtension(extension)) {
                throw new DetectException("invalid extension,td0Text=> " + td0Text);
            }
            parseContext.incrementExtensionNums();
        }

        FileType fileType = new FileType();
        fileType.setExtension(extension);
        fileType.setDesc(desc);
        parseMagic.getFileTypes().add(fileType);
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
            if (ParseMagicHelper.isSkipParseMagicNumber(td0Text)) {
                parseMagic.setValid(false);
                log.warn("skip magic number,td0Text=>" + td0Text);
                return parseMagic;
            } else {
                parseMagicNumberByReg(parseMagic);
                if (parseMagic.isValid()) {
                    return parseMagic;
                } else {

                    throw new DetectException("can't parse  magic number, td0Text=>" + td0Text);

                }
            }

        }

    }

    /**
     * @param parseMagic currentParseMagic
     * @return void
     * @title parseMagicNumberDefault
     * @description
     * @author BiJi'an
     * @date 2022-10-28 00:32
     */
    private void parseMagicNumberDefault(ParseMagic parseMagic) {
        String td0Text = parseMagic.getTd0Text();
        String magicNumber = td0Text.replace(" ", "").replace("or", "");
        if (ParseMagicHelper.isValidMagicNumber(magicNumber)) {
            parseMagic.setOffset(0);
            parseMagic.setNumber(magicNumber);
            parseMagic.setValid(true);
        }

    }

    /**
     * @param parseMagic currentParseMagic
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
            if (ParseMagicHelper.isValidMagicNumber(magicNumber)) {
                parseMagic.setValid(true);
                int offset = NumberUtils.toInt(matcher.group(1).replace(",", ""));
                Preconditions.checkArgument(offset > 0 && offset < 1024,
                        "invalid offset=>" + offset + ",td0Text" + td0Text);
                parseMagic.setOffset(offset);
                parseMagic.setNumber(magicNumber);
                log.info("offset={},magicNumber={},td0Text={}", offset, magicNumber, td0Text);
            }
        }

    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title revise
     * @description
     * @author BiJi'an
     * @date 2022-10-27 01:06
     */
    private List<ParseMagic> revise(ParseContext parseContext) {

        reviseFileType(parseContext);
        Map<String, ParseMagic> numberMaps = parseContext.getNumberMaps();

        numberMaps.forEach((magicNumber, parseMagic) -> {
            String sameMagicNumber = ParseMagicHelper.getSameMagicNumber(magicNumber);
            if (!StringUtils.isEmpty(sameMagicNumber)) {
                ParseMagic value = numberMaps.get(sameMagicNumber);
                ParseMagic cloneParseMagic = value.clone();
                cloneParseMagic.setNumber(magicNumber);
                numberMaps.put(magicNumber, cloneParseMagic);

            }
        });
        numberMaps.forEach((magicNumber, parseMagic) -> {
            if (parseMagic.getFileTypes().isEmpty()) {
                throw new DetectException(" file type can't be empty for number=>" + magicNumber);
            }
        });
        return parseContext.getNumberMaps().values().stream().sorted()
                .collect(Collectors.toList());
    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title reviseFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-28 11:28
     */
    private void reviseFileType(ParseContext parseContext) {
        parseContext.getNumberMaps().values().forEach(parseMagic -> {
                    parseMagic.getFileTypes()
                            .forEach(fileType -> fileType.setId(parseContext.nextFileTypeId()));
                    parseMagic.setId(parseContext.nextMagicId());

                }

        );
    }

}
