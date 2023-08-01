package com.c2ray.idea.plugin.sqllog.utils;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;

/**
 * @author c2ray
 */
public class ActionUtils {

    public static AnAction getAction(String id) {
        return ActionManager.getInstance().getAction(id);
    }

    public static AnAction getAction(ActionEnum action) {
        return getAction(action.getId());
    }

    public enum ActionEnum {
        MYBATIS_ATTACH_RECERNT_PROCESS_ACTION("MybatisAttachRecentProcessAction");
        private final String id;

        ActionEnum(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
