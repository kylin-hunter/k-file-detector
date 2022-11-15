package io.github.kylinhunter.tools.file.detector.prepared;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.common.util.ResourceHelper;
import io.github.kylinhunter.tools.file.detector.exception.DetectException;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.prepared.parse.MagicYamlWriter;
import io.github.kylinhunter.tools.file.detector.prepared.parse.ParseMagicHelper;
import io.github.kylinhunter.tools.file.detector.prepared.parse.bean.ParseContext;
import io.github.kylinhunter.tools.file.detector.prepared.parse.bean.ParseMagic;
import io.github.kylinhunter.tools.file.detector.prepared.parse.bean.YamlMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description https://www.garykessler.net/library/file_sigs.html
 * @date 2022-10-02 14:08
 **/
@Slf4j
@C
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
     * @return io.github.kylinhunter.plat.file.detector.magic.bean.Magic
     * @title checkExtensionConsistent
     * @description
     * @author BiJi'an
     * @date 2022-10-24 02:35
     */
    protected YamlMessage parse() {

        ParseContext parseContext = new ParseContext();
        Document document = Jsoup.parse(readHtml());
        Elements trs = document.select("tr:has(td)");
        parseContext.getParseStat().setTrNums(trs.size());
        for (Element tr : trs) {
            processTr(parseContext, tr);
        }
        revise(parseContext);
        log.info("parseContext=>{}", parseContext);
        return toYamlMessage(parseContext);
    }

    /**
     * @param parseContext parseContext
     * @return io.github.kylinhunter.plat.file.detector.init.parse.bean.YamlMessage
     * @title toYamlMessage
     * @description
     * @author BiJi'an
     * @date 2022-10-29 15:06
     */
    private YamlMessage toYamlMessage(ParseContext parseContext) {
        YamlMessage yamlMessage = new YamlMessage(parseContext);
        Map<String, ParseMagic> numberMaps = parseContext.getNumberMaps();
        yamlMessage.setParseMagics(numberMaps.values().stream().sorted()
                .collect(Collectors.toList()));

        yamlMessage.setFileTypes(numberMaps.values().stream().map(ParseMagic::getFileTypes)
                .flatMap(Collection::stream).distinct().sorted().collect(Collectors.toList()));
        return yamlMessage;
    }

    /**
     * @param fileMagic     fileMagic
     * @param fileFileTypes fileFileTypes
     * @return io.github.kylinhunter.plat.file.detector.init.parse.bean.YamlMessage
     * @title toYaml
     * @description
     * @author BiJi'an
     * @date 2022-10-29 15:08
     */
    public YamlMessage toYaml(File fileMagic, File fileFileTypes) {

        YamlMessage yamlMessage = this.parse();
        MagicYamlWriter.writeMagics(yamlMessage, fileMagic);
        MagicYamlWriter.writeFileType(yamlMessage, fileFileTypes);
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
            parseContext.getParseStat().incrementTrValidNum();
            processTds(parseContext, tds);
        } else {
            parseContext.getParseStat().incrementTrInvalidNum();
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
            processTdsMagicDefault(parseContext, td0Text, td2Text);
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
    private void processTdsMagicDefault(ParseContext parseContext, String td0Text, String td2Text) {
        ParseMagic parseMagic = processTdsMagicDefault(td0Text);
        parseMagic.setDesc(td2Text);
        parseContext.setCurrentParseMagic(parseMagic);
        parseContext.add(parseMagic);
    }

    /**
     * @param td0Text td0Text
     * @return io.github.kylinhunter.plat.file.detector.init.parse.bean.ParseMagic
     * @title parseMagicNumber
     * @description
     * @author BiJi'an
     * @date 2022-10-28 00:09
     */
    public ParseMagic processTdsMagicDefault(String td0Text) {
        ParseMagic parseMagic = new ParseMagic(td0Text);
        if (ParseMagicHelper.isSkipParseMagicNumberBySplit(td0Text)) {
            parseMagic.setValid(false);
            log.warn("skip magic number,td0Text=>" + td0Text);
            return parseMagic;
        } else {
            processTdsMagicDefault(parseMagic);
            if (parseMagic.isValid()) {
                if (ParseMagicHelper.isSkipParseMagicNumber(parseMagic.getNumber())) {
                    parseMagic.setValid(false);
                    log.warn("skip magic number,td0Text=>" + td0Text);
                    return parseMagic;
                }
                return parseMagic;
            } else {
                processTdsMagicByReg(parseMagic);
                if (parseMagic.isValid()) {

                    if (ParseMagicHelper.isSkipParseMagicNumber(parseMagic.getNumber())) {
                        parseMagic.setValid(false);
                        log.warn("skip magic number,td0Text=>" + td0Text);
                        return parseMagic;
                    }
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
    private void processTdsMagicDefault(ParseMagic parseMagic) {
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
    private void processTdsMagicByReg(ParseMagic parseMagic) {
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
                addFile(parseContext, td0Text, td2Text, parseMagic, extension, td2Text);

            } else {
                addFile(parseContext, td0Text, td2Text, parseMagic, "", td2Text);
            }
        }
    }

    private void addFile(ParseContext parseContext, String td0Text, String td2Text, ParseMagic parseMagic,
                         String extension,
                         String desc) {

        List<String> extensions = Lists.newArrayList();

        if (StringUtils.isEmpty(extension)) {
            parseContext.getParseStat().incrementExtensionNoneNum();
            log.warn("extenion is empty,td0Text=> " + td0Text + ",td2Text=> "
                    + StringUtils.substring(td2Text, 0, 20));
        } else {
            extension = ParseMagicHelper.speicalExtension(extension);

            String[] extensionArr = StringUtils.split(extension, ',');
            for (String ext : extensionArr) {
                ext = ext.replaceAll("\\s", "");
                if (ext.length() < 2) {
                    throw new DetectException("invalid extension,td0Text=> " + td0Text);
                } else {
                    if (ParseMagicHelper.isValidExtension(ext)) {
                        extensions.add(ext);

                    } else {
                        throw new DetectException("invalid extension,td0Text=> " + td0Text);
                    }
                }

            }

        }
        FileType fileType = new FileType();
        fileType.setExtensions(extensions);
        fileType.setDesc(desc);
        parseMagic.getFileTypes().add(fileType);
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

        reviseFileTypes(parseContext);
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

    }

    /**
     * @param parseContext parseContext
     * @return void
     * @title reviseFileType
     * @description
     * @author BiJi'an
     * @date 2022-10-28 11:28
     */
    private void reviseFileTypes(ParseContext parseContext) {
        Map<String, ParseMagic> numberMaps = parseContext.getNumberMaps();
        Map<String, FileType> duplicateFileTypes = Maps.newHashMap();

        List<ParseMagic> parseMagics = numberMaps.values().stream().sorted().collect(Collectors.toList());
        FileTypeIdGenerator fileTypeIdGenerator = new FileTypeIdGenerator();
        parseMagics.forEach(parseMagic -> {
                    List<FileType> newFileTypes = parseMagic.getFileTypes().stream().map(fileType -> {

                        String id = fileTypeIdGenerator.generateId(parseMagic.getNumber(), fileType);

                        FileType distFileType = duplicateFileTypes.get(id);
                        if (distFileType != null) {
                            log.info("duplicate file type=>{}", distFileType);
                            parseContext.getParseStat().incrementFileTypeDuplicateNum();
                            return distFileType;
                        } else {
                            fileType.setId(id);
                            duplicateFileTypes.put(id, fileType);
                            parseContext.getParseStat().incrementFileTypeNum();
                            return fileType;

                        }
                    }).collect(Collectors.toList());
                    parseMagic.setFileTypes(newFileTypes);

                }

        );

    }

}
