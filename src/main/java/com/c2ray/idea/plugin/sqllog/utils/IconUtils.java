package com.c2ray.idea.plugin.sqllog.utils;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author c2ray
 * @since 2023/7/31
 */
public class IconUtils {

    public enum IconEnum {
        STOP("/icons/stop_21.png"),
        MYBATIS("/icons/mybatis_21.png");

        private final String PATH;

        IconEnum(String PATH) {
            this.PATH = PATH;
        }

        public String getPATH() {
            return PATH;
        }
    }

    public static Icon getIcon(IconEnum icon) {
        return IconLoader.getIcon(icon.getPATH(), IconUtils.class);
    }

}
