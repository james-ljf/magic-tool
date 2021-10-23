package com.magictool.web.domain.res;

import java.io.Serializable;

/**
 * 自定义业务状态接口
 * @author lijf
 */
public interface Code extends Serializable {

    /**
     * 状态码
     *
     * @return String
     */
    String getCode();

    /**
     * 信息
     *
     * @return String
     */
    String getMsg();

    Code SUCCESS = new Code() {
        @Override
        public String getCode() {
            return "8888";
        }

        @Override
        public String getMsg() {
            return "SUCCESS";
        }
    };

    Code ERROR_PARAMS = new Code() {
        @Override
        public String getCode() {
            return "8010";
        }

        @Override
        public String getMsg() {
            return "参数不正确";
        }
    };

    Code ERROR_TOKEN = new Code() {
        @Override
        public String getCode() {
            return "9010";
        }

        @Override
        public String getMsg() {
            return "用户登陆令牌失效";
        }
    };

    /**
     * 自定义Code
     *
     * @return Code
     */
    static Code business(String code, String msg) {
        return new Code() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        };
    }
}