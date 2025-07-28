package com.weaver.accurate.common;

public enum BizCode implements Code {

    /**************************计费模块错误码******************************/
    GIT_OPERATED_FAIlED(20000, "git拉取代码失败", "git拉取代码失败"),
    GET_DIFF_CLASS_ERROR(20001, "获取差异类失败", "获取差异类失败，请查看日志文件"),
    CREATE_JOB_FAIL(20002, "创建job失败", "请联系管理员"),
    PARSE_BRANCH_ERROR(20003, "解析分支失败", "请确认分支正常"),
    PARSE_JAVA_FILE(20004, "解析java类失败", "请确认类是否有语法错误"),
    GIT_AUTH_FAILED(20005, "git认证失败", "git认证失败"),
    LOAD_CLASS_FAIL(20006, "读取java类失败", "读取java类失败，请稍后再试"),
    UNKNOWN_REPOSITY_URL(20007, "未知仓库地址", "请检查仓库url"),
    GET_METHOD_INVOKE_LINK_FAIL(20008, "获取方法调用链失败", "获取方法调用链失败"),

    COMPILE_CODE_FAIL(20009, "编译源码失败", "获取classes文件失败"),
    POM_NOT_EXIST_FAIL(20010, "pom文件不存在", "编译源码失败,pom文件不存在"),
    JACOCO_EXEC_NOT_EXIST(20011, "exec文件不存在", "exec文件不存在,请检查目录"),
    JACOCO_REPORT_FAIL(20012, "生成jacoco报告失败", "生成jacoco报告失败"),

    GET_CODE_FAIL(20013, "获取代码失败", "获取代码失败，请排查日志"),
    ;
    private final int code;
    private final String info;
    private final String fixTips;

    BizCode(int code, String info, String fixTips) {
        this.code = code;
        this.info = info;
        this.fixTips = fixTips;
    }

    /**
     * 错误码
     * eg: 200xx gateway
     * 300xx user
     * 400xx order
     * 500xx core
     * 600xx operator
     * 700xx admin
     * <p>
     * universal:
     * 10000 ~ 19999
     * 10000 success
     * 10001 system error
     * 10002 timed out
     * 10003 params error
     * 10004 rpc timeout
     * 10005 rpc invoke error
     *
     * @return int
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 错误码说明(内部日志，统计，查看使用)
     *
     * @return String
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * 错误码描述，对外输出
     *
     * @return String
     */
    @Override
    public String getFixTips() {
        return this.fixTips;
    }
}
