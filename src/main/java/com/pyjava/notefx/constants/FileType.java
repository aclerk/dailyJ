package com.pyjava.notefx.constants;

/**
 * <p>描述: 文件类型 </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/11 23:40
 */
public enum FileType {
    /**
     * markdown文件
     */
    MD(".md", "markdown"),
    /**
     * 文本文件
     */
    TXT(".txt", "txt")
    ;

    private final String suffix;
    private final String type;

    FileType(String suffix, String type) {
        this.suffix = suffix;
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getType() {
        return type;
    }
}
