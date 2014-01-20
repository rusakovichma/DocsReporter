package by.creepid.docsreporter.context;

import java.lang.reflect.Field;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import by.creepid.docsreporter.formatter.DocTypesFormatter;
import by.creepid.docsreporter.utils.ClassUtil;
import by.creepid.docsreporter.utils.SimpleTypes;
import fr.opensagres.xdocreport.template.IContext;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.util.ClassUtils;

@Component
public class DocContextProcessor implements ContextProcessor {

    @Resource(name = "formatters")
    private List<DocTypesFormatter> formatters;
    private String emptyField;
    private IContext context = null;

    private void checkContext() {
        if (context == null) {
            throw new IllegalStateException("IContext instance must be set!");
        }
    }

    public Object get(String key) {
        checkContext();

        return context.get(key);
    }

    protected String setFirstCharUpper(String fieldname) {
        StringBuilder builder = new StringBuilder(fieldname);

        String firstUpper = fieldname.substring(0, 1).toUpperCase();

        builder = builder.deleteCharAt(0);
        builder.insert(0, firstUpper);

        return builder.toString();
    }

    protected String getContextFieldName(String context, String field) {
        StringBuilder contextField = new StringBuilder(context)
                .append(".")
                .append(setFirstCharUpper(field));

        return contextField.toString();
    }

    private DocTypesFormatter getFormatter(Class<?> classTo) {

        DocTypesFormatter formatter = null;

        if (formatters != null) {
            for (DocTypesFormatter form : formatters) {
                if (form != null && form.getFromClass() == classTo) {
                    formatter = form;
                }
            }
        }

        return formatter;
    }

    protected String getString(Object fieldObj) {

        DocTypesFormatter formatter = getFormatter(fieldObj.getClass());

        if (formatter != null) {
            return (String) formatter.format(fieldObj);
        }

        return fieldObj.toString();
    }

    private void processObjectFields(String contextStr, Object obj) {

        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] fields = ClassUtil.getDeclaredFields(clazz, true);

            for (Field field : fields) {
                try {
                    field.setAccessible(true);

                    Object fieldObj = field.get(obj);
                    String contextFieldname = getContextFieldName(
                            contextStr, field.getName());

                    this.put(contextFieldname, fieldObj);

                } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(DocContextProcessor.class.getName()).log(
                            Level.ERROR, null, ex);
                }
            }

        }
    }

    private void processSimpleType(String string, Object obj) {

        String value = (obj != null)
                ? getString(obj)
                : emptyField;

        context.put(string, value);
    }

    public Object put(String string, Object obj) {
        checkContext();

        if (obj == null || SimpleTypes.isSimple(obj.getClass())) {
            processSimpleType(string, obj);
        } else {
            processObjectFields(string, obj);
        }

        return obj;
    }

    @Override
    public void setContext(IContext context) {
        this.context = context;
    }

    public void setFormatters(List<DocTypesFormatter> formatters) {
        this.formatters = formatters;
    }

    public void setEmptyField(String emptyField) {
        this.emptyField = emptyField;
    }
}
