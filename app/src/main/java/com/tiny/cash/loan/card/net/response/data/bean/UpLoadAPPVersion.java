package com.tiny.cash.loan.card.net.response.data.bean;

public class UpLoadAPPVersion {

    /**
     "comingSoonSwitch":"0",
     "comingSoonUrl":"",
     "updateContent":"option content",
     "updateTitle":"option title",
     "updateType":0 不升级 1 建议升级 2强制升级
     */

    private String comingSoonSwitch;
    private String comingSoonUrl;
    private String updateContent;
    private String updateType;
    private String updateTitle;
    private String updateUrl;

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getComingSoonSwitch() {
        return comingSoonSwitch;
    }

    public void setComingSoonSwitch(String comingSoonSwitch) {
        this.comingSoonSwitch = comingSoonSwitch;
    }

    public String getComingSoonUrl() {
        return comingSoonUrl;
    }

    public void setComingSoonUrl(String comingSoonUrl) {
        this.comingSoonUrl = comingSoonUrl;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }
}
