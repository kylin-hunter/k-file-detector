package io.github.kylinhunter.tools.file.detector.file.bean;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author BiJi'an
 * @description
 * @date 2022-10-02 16:24
 **/
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdjustFileType {
    /* ==== from yaml  ===*/
    @EqualsAndHashCode.Include
    String id;
    private List<String> extensions;
    private String desc;
    private boolean create;
    private String sameRef;

}