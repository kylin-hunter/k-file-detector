package io.github.kylinhunter.tools.file.detector.content;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;
import io.github.kylinhunter.tools.file.detector.common.component.C;
import io.github.kylinhunter.tools.file.detector.content.bean.DetectConext;
import io.github.kylinhunter.tools.file.detector.content.constant.ContentDetectType;
import io.github.kylinhunter.tools.file.detector.content.content.ContentDetector;
import io.github.kylinhunter.tools.file.detector.file.bean.FileType;
import io.github.kylinhunter.tools.file.detector.magic.bean.Magic;
import io.github.kylinhunter.tools.file.detector.magic.bean.ReadMagic;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BiJi'an
 * @description
 * @date 2022-11-07 16:43
 **/
@Data
@C
@Slf4j
public class MixContentDetector {

    private Map<String, ContentDetector> magicContentDetectors = Maps.newHashMap();
    private List<ContentDetector> defaultContentDetectors = Lists.newArrayList();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10,
            10L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

    public MixContentDetector(List<ContentDetector> detectors) {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        for (ContentDetector contentDetector : detectors) {
            ContentDetectType contentDetectType = contentDetector.getContentDetectType();
            if (contentDetectType.isRelatedMagic()) {
                magicContentDetectors.put(contentDetectType.getMagicNumer(), contentDetector);
            } else {
                defaultContentDetectors.add(contentDetector);
            }

        }
    }

    public DetectConext detect(ReadMagic readMagic) {
        DetectConext detectConext = new DetectConext(readMagic);

        List<DetectFutureTask> futureTasks = Lists.newArrayList();
        FileType[] fileTypes = null;
        if (readMagic.isDetectContent()) {

            for (Magic magic : readMagic.getContentMagics()) {
                ContentDetector contentDetector = magicContentDetectors.get(magic.getNumber());
                if (contentDetector != null) {
                    fileTypes = contentDetector.detect(detectConext);
                    if (fileTypes != null && fileTypes.length > 0) {
                        for (FileType fileType : fileTypes) {
                            detectConext.addContentFileType(fileType);
                        }
                        break;
                    }
                }
            }
            if (fileTypes == null || fileTypes.length <= 0) {
                defaultContentDetectors.forEach(contentDetector -> {
                    DetectFutureTask futureTask = new DetectFutureTask(new DetectTask(contentDetector, detectConext));
                    futureTasks.add(futureTask);
                    threadPoolExecutor.submit(futureTask);

                });
                for (DetectFutureTask futureTask : futureTasks) {
                    try {
                        FileType[] tmpFileTypes = futureTask.get();
                        for (FileType fileType : tmpFileTypes) {
                            detectConext.addContentFileType(fileType);
                        }
                        ContentDetectType contentDetectType =
                                futureTask.detectTask.getContentDetector().getContentDetectType();
                        log.info("futureTask({}) detect result={}", contentDetectType, Arrays.toString(tmpFileTypes));
                    } catch (Exception e) {
                        log.error("futureTask error", e);
                    }
                }
            }
        }

        return detectConext;
    }

    @Data
    static class DetectTask implements Callable<FileType[]> {
        private final ContentDetector contentDetector;
        private final DetectConext detectConext;

        @Override
        public FileType[] call() {
            return contentDetector.detect(detectConext);
        }
    }

    @Getter
    static class DetectFutureTask extends FutureTask<FileType[]> {

        private final DetectTask detectTask;

        public DetectFutureTask(DetectTask detectTask) {
            super(detectTask);
            this.detectTask = detectTask;
        }
    }
}
