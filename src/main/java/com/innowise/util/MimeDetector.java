package com.innowise.util;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class MimeDetector {
    public String deetectMimeType(byte[] file) {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
