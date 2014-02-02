package by.creepid.docsreporter.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class DateDocTypesFormatter implements DocTypesFormatter<Date, String> {

    private static final String DEFAULT_DATE_PATTERN;
    private String datePattern = DEFAULT_DATE_PATTERN;
    
    private SimpleDateFormat dateFormat;

    static {
        DEFAULT_DATE_PATTERN = "dd.MM.yyyy";
    }

    public String getDatePattern() {
        return datePattern;
    }

    @Autowired(required = false)
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    @PostConstruct
    public void init() {
        dateFormat = new SimpleDateFormat(datePattern);
    }

    public synchronized String format(Date f) {
        return dateFormat.format(f);
    }

    public Class<String> getToClass() {
        return String.class;
    }

    public Class<Date> getFromClass() {
        return Date.class;
    }
}
