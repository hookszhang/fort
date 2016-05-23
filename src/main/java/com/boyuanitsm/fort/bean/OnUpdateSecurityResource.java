package com.boyuanitsm.fort.bean;

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass;
import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;

/**
 * Message of the on update security resource.
 *
 * @author zhanghua on 5/23/16.
 */
public class OnUpdateSecurityResource {

    public OnUpdateSecurityResource() {
    }

    public OnUpdateSecurityResource(OnUpdateSecurityResourceOption option, OnUpdateSecurityResourceClass resourceClass, Object data) {
        this.option = option;
        this.resourceClass = resourceClass;
        this.data = data;
    }

    /**
     * Option. RESTful style.
     */
    private OnUpdateSecurityResourceOption option;

    /**
     * Update resource class.
     */
    private OnUpdateSecurityResourceClass resourceClass;

    /**
     * Data.
     */
    private Object data;

    public OnUpdateSecurityResourceOption getOption() {
        return option;
    }

    public void setOption(OnUpdateSecurityResourceOption option) {
        this.option = option;
    }

    public OnUpdateSecurityResourceClass getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(OnUpdateSecurityResourceClass resourceClass) {
        this.resourceClass = resourceClass;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
