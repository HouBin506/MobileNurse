package com.herenit.mobilenurse.api;

/**
 * author: HouBin
 * date: 2019/1/4 10:07
 * desc: 存放一些API相关的东西，网络请求的域名，响应结果码等
 */
public interface Api {
    String IP_PORT = "129.88.199.157:8080";//服务器IP+端口（浙江省人民医院，正式库）;
    //    String IP_PORT = "10.10.76.78:8083";//服务器IP+端口（公司测试库）;
    String PROTOCOL = "http://";//http协议
    String DOMAIN = "/heren.com/";//服务器域名
    String BASE_URL = PROTOCOL + IP_PORT + DOMAIN;//服务器的基础地址
    //下载App安装包地址
    String DOWNLOAD_APP_FILE_PATH = "mobileNurse/versionHandler/getApp";
    //登录地址
    String LOGIN_PATH = "mobileNurse/loginHandler/login";
    //检查报告页面地址
    String EXAM_REPORT_PAGER_PATH = "webSite/viewsHandler/examReport";
    //检验报告页面地址
    String LAB_REPORT_PAGER_PATH = "webSite/viewsHandler/labReport";
    //微生物检验报告页面地址
    String MICROORGANISM_LAB_REPORT_PAGER_PATH = "webSite/viewsHandler/bioLabReport";
    Integer CODE_SUCCESS = 0;
    Integer CODE_FAILURE = -1;
    Integer CODE_FAILURE_NO_PERMISSION = -2;
}
