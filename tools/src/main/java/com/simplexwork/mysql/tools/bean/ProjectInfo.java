package com.simplexwork.mysql.tools.bean;

/**
 * 项目信息
 * @author aoe
 * @date 15-3-16
 * @tags
 */
public class ProjectInfo {

    /**
     * mapper-java的包名
     */
    private String pkgMapper;

    /**
     * Bean-java的包名
     */
    private  String pkgBean;

    /**
     * Service的包名
     */
    private String pkgService;

    /**
     * Service的包名
     */
    private String pkgServiceImpl;

    /**
     * 生成文件的主文件夹路径
     */
    private String generatedPath;

    /**
     * 生成Java文件时需要过滤掉的表名前缀
     */
    private String[] table_prefix;

    public String getPkgMapper() {
        return pkgMapper;
    }

    public void setPkgMapper(String pkgMapper) {
        this.pkgMapper = pkgMapper;
    }

    public String getPkgBean() {
        return pkgBean;
    }

    public void setPkgBean(String pkgBean) {
        this.pkgBean = pkgBean;
    }

    public String getPkgService() {
        return pkgService;
    }

    public void setPkgService(String pkgService) {
        this.pkgService = pkgService;
    }

    public String getPkgServiceImpl() {
        return pkgServiceImpl;
    }

    public void setPkgServiceImpl(String pkgServiceImpl) {
        this.pkgServiceImpl = pkgServiceImpl;
    }

    public String getGeneratedPath() {
        return generatedPath;
    }

    public void setGeneratedPath(String generatedPath) {
        this.generatedPath = generatedPath;
    }

    public String[] getTable_prefix() {
        return table_prefix;
    }

    public void setTable_prefix(String[] table_prefix) {
        this.table_prefix = table_prefix;
    }
}
