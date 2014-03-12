package by.creepid.docsreporter.context;

import by.creepid.docsreporter.context.annotations.NullValue;
import by.creepid.docsreporter.context.annotations.Image;
import by.creepid.docsreporter.context.meta.MetadataFillerChain;
import by.creepid.docsreporter.converter.images.ImageConverter;
import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

import by.creepid.docsreporter.formatter.DocTypesFormatter;
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
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DocContextProcessor implements ContextProcessor {

    private static final Class<? extends Annotation> imageAnnotatioin = Image.class;
    private static final Class<? extends Annotation> emptyValueAnnotatioin = NullValue.class;

    private static final String EMPTY_FIELD_DEFAULT = "";

    @Resource(name = "formatters")
    private List<DocTypesFormatter> formatters;

    private String emptyField = EMPTY_FIELD_DEFAULT;

    @Autowired
    private ImageConverter imageConverter;
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

        if (!imageConverter.isImage(image) || !imageConverter.isSupportedImageType(image)) {
            return;
        }

        Image imageAnnot = (Image) imageAnnotatioin;

        boolean useImageSize = !imageAnnot.useTemplateSize();

        IImageProvider imageProvider;

        float width = imageAnnot.width();
        float height = imageAnnot.height();

        if (!imageAnnot.useRatioSize()
                && width != Float.MIN_VALUE
                && height != Float.MIN_VALUE) {

            try {
                image = imageConverter.convertPhotoToPreview(image,
                        Math.round(width), Math.round(height));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            imageProvider = new ByteArrayImageProvider(image, useImageSize);
            imageProvider.setSize(width, height);

        } else if (imageAnnot.useRatioSize()
                && (width != Float.MIN_VALUE || height != Float.MIN_VALUE)) {

            if (width != Float.MIN_VALUE) {

                try {
                    image = imageConverter.convertPhotoToPreview(image,
                            Math.round(width), Integer.MIN_VALUE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                imageProvider = new ByteArrayImageProvider(image, useImageSize);

                imageProvider.setUseImageSize(true);
                imageProvider.setWidth(width);
            } else {

                try {
                    image = imageConverter.convertPhotoToPreview(image,
                            Integer.MIN_VALUE, Math.round(height));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                imageProvider = new ByteArrayImageProvider(image, useImageSize);

                imageProvider.setUseImageSize(true);
                imageProvider.setHeight(height);
            }

            imageProvider.setResize(true);
        } else {
            imageProvider = new ByteArrayImageProvider(image, useImageSize);
        }

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

                    } else if (field.isAnnotationPresent(emptyValueAnnotatioin) && fieldObj == null) {

                        NullValue emptyValueAnnot = (NullValue) field.
                                getAnnotation(emptyValueAnnotatioin);
                        String emptyValue = emptyValueAnnot.value();

                        processSimpleType(contextFieldname, emptyValue, fieldObj);

                    } else if (Enum.class.isAssignableFrom(field.getType())) {

                        Enum enumField = (Enum) fieldObj;
                        this.put(contextFieldname, enumField.name());

                    } else {
                        this.put(contextFieldname, fieldObj);
                    }

                } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    protected void processSimpleType(String string, String defauleValue, Object obj) {

        String value = (obj != null)
                ? getString(obj)
                : defauleValue;

        context.put(string, value);
    }

    private void processSimpleType(String string, Object obj) {
        processSimpleType(string, emptyField, obj);
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

    public void setImageConverter(ImageConverter imageConverter) {
        this.imageConverter = imageConverter;
    }

}
