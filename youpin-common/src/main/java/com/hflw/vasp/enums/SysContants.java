package com.hflw.vasp.enums;

import java.text.MessageFormat;

/**
 * 常量列表
 */
public interface SysContants {

    /**
     * 超级管理员的登录id
     */
    public static final long ADMIN_LOGIN_ID = 1;

    /**
     * 获取代码量
     *
     * @return
     */
    Integer getCode();

    /**
     * 获取值
     *
     * @return
     */
    String getValue();

    /**
     * 启用状态
     */
    public enum EnableStatus implements SysContants {
        initialize {
            @Override
            public Integer getCode() {
                return 0;
            }

            @Override
            public String getValue() {
                return "审核中";
            }
        },
        enable {
            @Override
            public Integer getCode() {
                return 1;
            }

            @Override
            public String getValue() {
                return "启用";
            }
        }, disable {
            @Override
            public Integer getCode() {
                return 2;
            }

            @Override
            public String getValue() {
                return "停用";
            }
        };
    }

    /**
     * 删除状态
     */
    public enum DeleteStatus implements SysContants {
        normal {
            @Override
            public Integer getCode() {
                return 0;
            }

            @Override
            public String getValue() {
                return "正常";
            }
        }, delete {
            @Override
            public Integer getCode() {
                return -1;
            }

            @Override
            public String getValue() {
                return "删除";
            }
        };
    }

    /**
     * 资源是否为父级
     */
    public enum ResourceIsParent implements SysContants {
        yes {
            @Override
            public Integer getCode() {
                return 1;
            }

            @Override
            public String getValue() {
                return "是";
            }
        },
        no {
            @Override
            public Integer getCode() {
                return 2;
            }

            @Override
            public String getValue() {
                return "不是";
            }
        }
    }

    /**
     * 规则状态
     */
    public enum RuleStatus implements SysContants {
        enable {
            @Override
            public Integer getCode() {
                return 1;
            }

            @Override
            public String getValue() {
                return "已生效";
            }
        }, disable {
            @Override
            public Integer getCode() {
                return 2;
            }

            @Override
            public String getValue() {
                return "未生效";
            }
        }
    }


    /**
     * 性别
     */
    public enum Gender implements SysContants {
        man {
            @Override
            public Integer getCode() {
                return 1;
            }

            @Override
            public String getValue() {
                return "男";
            }
        },
        woman {
            @Override
            public Integer getCode() {
                return 2;
            }

            @Override
            public String getValue() {
                return "女";
            }
        }
    }

    public enum RealNameState implements SysContants {
        yes(1, "实名"), no(2, "未实名");
        private Integer code;
        private String value;

        RealNameState(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum ShortMessage {
        SMS_CODE("【广联赛讯】您本次操作的验证码为：{0}（3分钟内有效），欢迎使用，切勿将验证码透漏给他人。");

        ShortMessage(String content) {
            this.content = content;
        }

        private String content;

        public String toFormatterString(String... args) {
            return MessageFormat.format(content, args);
        }
    }

}
