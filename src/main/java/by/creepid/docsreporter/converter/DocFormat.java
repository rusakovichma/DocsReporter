/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.converter;

/**
 *
 * @author rusakovich
 */
public enum DocFormat {

    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new String[]{"docx"}),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", new String[]{"pptx"}),
    PDF("application/pdf", new String[]{"pdf"}),
    ODT("application/vnd.oasis.opendocument.text", new String[]{"odt"}),
    XHTML("application/xhtml+xml", new String[]{"htm", "html", "xhtml"}),
    UNSUPPORTED("text/html", new String[]{""});
    
    private final String mimeType;
    private final String[] exts;

    private DocFormat(String mimeType, String[] exts) {
        this.mimeType = mimeType;
        this.exts = exts;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String[] getExts() {
        return exts;
    }

    public String getExtsString(String separator) {
        StringBuilder builder = new StringBuilder();

        String[] extArr = this.getExts();
        for (int i = 0; i < extArr.length; i++) {
            builder.append(".");
            builder.append(extArr[i]);

            if (i != extArr.length - 1) {
                builder.append(separator);
            }

        }

        return builder.toString();
    }

    public String changeExt(String path) {
        StringBuilder builder = new StringBuilder(path);

        int dotIndex = path.lastIndexOf(".");

        builder.delete(dotIndex + 1, path.length())
                .append(this.getExts()[0]);

        return builder.toString();
    }

    public static DocFormat getFormat(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is null!");
        }

        int dotIndex = path.lastIndexOf(".");
        if (dotIndex == -1) {
            return UNSUPPORTED;
        }

        String form = path.substring(dotIndex + 1, path.length());

        for (DocFormat curr : DocFormat.values()) {

            String[] exts = curr.getExts();
            for (String ext : exts) {
                if (ext.equalsIgnoreCase(form)) {
                    return curr;
                }
            }
        }

        return UNSUPPORTED;
    }
}
