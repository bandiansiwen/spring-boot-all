package com.imp.all.download.progress;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2023/11/29 19:52
 * @description
 */

@Slf4j
public class MultiThreadDownloadProgressPrinter implements DownloadProgressPrinter {

    private final PartProgress[] partProgresses;
    private long contentLength;

    public MultiThreadDownloadProgressPrinter(int threadNum) {
        this.partProgresses = new PartProgress[threadNum];
        for (int i = 0; i < threadNum; i++) {
            PartProgress part = new PartProgress();
            this.partProgresses[i] = part;
        }
    }

    @Override
    public void print(String task, long contentLength, long alreadyDownloadLength, long speed) {
        PartProgress partProgress = this.getPartProgress(task);
        partProgress.setAlreadyDownloadLength(alreadyDownloadLength);
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public long getAlreadyDownloadLength() {
//        for (int i=0; i< partProgresses.length; i++) {
//            PartProgress part = partProgresses[i];
//            log.info("part index:{} => contentLength: {}", i, part.getAlreadyDownloadLength());
//        }
        return Arrays.stream(partProgresses)
                .filter(Objects::nonNull)
                .map(PartProgress::getAlreadyDownloadLength)
                .reduce(Long::sum)
                .orElse(0L);
    }

    public long setContentLength(long contentLength) {
        return this.contentLength = contentLength;
    }

    private PartProgress getPartProgress(String indexText) {
        int i = Integer.parseInt(indexText);
        if (i < this.partProgresses.length) {
            return this.partProgresses[i];
        }
        throw new RuntimeException("partProgresses don't have element by index: " + indexText);
    }

    static class PartProgress {
        private long alreadyDownloadLength;

        public void setAlreadyDownloadLength(long alreadyDownloadLength) {
            this.alreadyDownloadLength = alreadyDownloadLength;
        }

        public long getAlreadyDownloadLength() {
            return alreadyDownloadLength;
        }
    }
}
