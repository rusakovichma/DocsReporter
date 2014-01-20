package by.creepid.docsreporter.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDocTypesFormatter implements DocTypesFormatter<Date, String> {

    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    }

    public synchronized String format(Date f) {
        return DATE_FORMAT.format(f);
    }

    public Class<String> getToClass() {
        return String.class;
    }

    public Class<Date> getFromClass() {
        return Date.class;
    }

}
