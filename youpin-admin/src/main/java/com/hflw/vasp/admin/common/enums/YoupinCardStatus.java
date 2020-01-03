package com.hflw.vasp.admin.common.enums;

public enum YoupinCardStatus {

    NM(0, "未开通"),//normal
    VD(1, "已开通"),//valid
    IV(2, "未开通");//invalid，未开通/已失效

    private YoupinCardStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Byte value) {
        if (value == null) return "";

        YoupinCardStatus[] statuses = YoupinCardStatus.values();
        for (YoupinCardStatus status : statuses) {
            if (status.value == value) {
                return status.desc;
            }
        }
        return "";
    }

}
