package by.creepid.docsreporter.context;

import by.creepid.docsreporter.context.annotations.ImageField;
import java.lang.reflect.Field;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import by.creepid.docsreporter.formatter.DocTypesFormatter;
import by.creepid.docsreporter.utils.ClassUtil;
import by.creepid.docsreporter.utils.FieldHelper;
import by.creepid.docsreporter.utils.SimpleTypes;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

@Component
public class DocContextProcessor implements ContextProcessor {

    @Resource(name = "formatters")
    private List<DocTypesFormatter> formatters;
    private String emptyField;
    private IContext context = null;
    private static final Class<? extends Annotation> imageAnnotatioin = ImageField.class;

    private void checkContext() {
        if (context == null) {
            throw new IllegalStateException("IContext instance must be set!");
        }
    }

    public Object get(String key) {
        checkContext();

        return context.get(key);
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

    private void processImage(String imageMark, byte[] image, Annotation imageAnnotatioin) {
        IImageProvider imageProvider = new ByteArrayImageProvider(image, false);
        context.put(imageMark, imageProvider);
    }

    protected void processObjectFields(String contextStr, Object obj) {
        
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] fields = FieldHelper.getDeclaredFields(clazz, true);

            for (Field field : fields) {
                try {
                    field.setAccessible(true);

                    Object fieldObj = field.get(obj);
                    String contextFieldname = FieldHelper.getFieldPath(
                            contextStr, field.getName());

                    if (field.isAnnotationPresent(imageAnnotatioin)) {
                        processImage(contextFieldname, (byte[]) fieldObj,
                                field.getAnnotation(imageAnnotatioin));
                    } else {
                        this.put(contextFieldname, fieldObj);
                    }

                } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(DocContextProcessor.class.getName()).log(
                            Level.ERROR, null, ex);
                }
            }

        }
    }

    protected void processSimpleType(String string, Object obj) {
        
        String value = (obj != null)
                ? getString(obj)
                : emptyField;

        context.put(string, value);
    }

    protected void processCollection(String string, Object obj) {
        context.put(string, obj);
    }

    public Object put(String string, Object obj) {
        checkContext();
        
        if (obj == null || SimpleTypes.isSimple(obj.getClass())) {
            processSimpleType(string, obj);
        } else if (obj instanceof Collection) {
            processCollection(string, obj);
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

    public void putMap(Map<String, Object> map) {
        context.putMap(map);
    }

    public Map<String, Object> getContextMap() {
        return context.getContextMap();
    }
}
