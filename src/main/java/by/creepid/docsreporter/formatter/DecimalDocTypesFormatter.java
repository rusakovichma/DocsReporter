package by.creepid.docsreporter.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalDocTypesFormatter implements DocTypesFormatter<BigDecimal, String> {

    private static final DecimalFormat formatter;

    static {
        Locale locale = Locale.getDefault();

        formatter = (DecimalFormat) NumberFormat.getInstance(locale);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');

        formatter.setDecimalFormatSymbols(symbols);
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
