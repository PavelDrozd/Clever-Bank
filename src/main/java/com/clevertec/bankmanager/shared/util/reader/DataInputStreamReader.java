package com.clevertec.bankmanager.shared.util.reader;

import com.clevertec.bankmanager.shared.exception.service.ServiceIOException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * Util class for read InputStream
 */
@Slf4j
public class DataInputStreamReader {


    public static String getString(InputStream is) {
        try {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = is.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("DATA INPUT STREAM READER - SERVICE IO EXCEPTION: " + e.getMessage());
            throw new ServiceIOException(e);
        } finally {
            closeInputStream(is);
        }
    }

    private static void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            log.error("DATA INPUT STREAM READER - CAN NOT CLOSE INPUT STREAM: " + e.getMessage());
        }
    }
}
