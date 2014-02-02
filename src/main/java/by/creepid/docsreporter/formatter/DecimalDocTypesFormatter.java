package by.creepid.docsreporter.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class DecimalDocTypesFormatter implements DocTypesFormatter<BigDecimal, String> {

    private static final char DECIMAL_SEPARATOR_DEFAULT = ',';
    private static final char GROUPING_SEPARATOR_DEFAULT = ' ';
    private char decimalSeparator = DECIMAL_SEPARATOR_DEFAULT;
    private char groupingSeparator = GROUPING_SEPARATOR_DEFAULT;
    private DecimalFormat formatter;

    @PostConstruct
    public void init() {
        Locale locale = Locale.getDefault();

        formatter = (DecimalFormat) NumberFormat.getInstance(locale);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(groupingSeparator);

        formatter.setDecimalFormatSymbols(symbols);
    }

    @Autowired(required = false)
    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator.charAt(0);
    }

    @Autowired(required = false)
    public void setGroupingSeparator(String groupingSeparator) {
        this.groupingSeparator = groupingSeparator.charAt(0);
    }

    public synchronized String format(BigDecimal f) {
        return (f != null) ? formatter.format(f) : null;
    }

    public Class<String> getToClass() {
        return String.class;
    }

    public Class<BigDecimal> getFromClass() {
        return BigDecimal.class;
    }
}
