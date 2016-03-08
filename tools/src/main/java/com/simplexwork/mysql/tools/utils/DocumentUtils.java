/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplexwork.mysql.tools.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import com.simplexwork.mysql.tools.bean.Parameters;
import com.simplexwork.mysql.tools.bean.ProjectInfo;
import com.simplexwork.mysql.tools.bean.TableInfo;
import com.simplexwork.mysql.tools.bean.TableInfo.Column;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author code.huanglei at gmail.com
 * @date Jun 27, 2014
 * @tags
 */
public final class DocumentUtils {

	static {
		Properties properties = new Properties();
		properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		properties.setProperty(Velocity.RESOURCE_LOADER, "class");
		properties
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(properties);
	}

    /*private static String pkgMapper = "com.simplexwork.we.teach.persistence"; // mapper-java的包名
    private static  String pkgBean = "com.simplexwork.we.teach.bean"; // Bean-java的包名
    private static String pkgService = "com.simplexwork.we.teach.service"; // Service的包名
    private static String pkgServiceImpl = "com.simplexwork.we.teach.service.impl"; // Service的包名
    private static String maiPath = "E:/sql-xiuyou/"; // 生成文件的主文件夹路径*/

    /*private static String pkgMapper = "com.aoeai.picture.shock.persistence"; // mapper-java的包名
    private static  String pkgBean = "com.aoeai.picture.shock.bean"; // Bean-java的包名
    private static String pkgService = "com.aoeai.picture.shock.service"; // Service的包名
    private static String pkgServiceImpl = "com.aoeai.picture.shock.service.impl"; // Service的包名
    private static String generatedPath = "E:/sql/picture_shock/"; // 生成文件的主文件夹路径*/

    /**
     * mapper-java的包名
     */
    private static String pkgMapper;

    /**
     * Bean-java的包名
     */
    private static  String pkgBean;

    /**
     * Service的包名
     */
    private static String pkgService;

    /**
     * Service的包名
     */
    private static String pkgServiceImpl;

    /**
     * 生成文件的主文件夹路径
     */
    private static String generatedPath;

    /**
     * 生成Java文件时需要过滤掉的表名前缀
     */
    private static String[] table_prefix;

    public static void setProjectInfo(ProjectInfo projectInfo){
        pkgMapper = projectInfo.getPkgMapper();
        pkgBean = projectInfo.getPkgBean();
        pkgService = projectInfo.getPkgService();
        pkgServiceImpl = projectInfo.getPkgServiceImpl();
        generatedPath = projectInfo.getGeneratedPath();
        table_prefix = projectInfo.getTable_prefix();
    }

	private static Parameters parameters;

	public static Parameters getParameters() {
		return parameters;
	}

	public static void setParameters(Parameters parameters) {
		DocumentUtils.parameters = parameters;
	}

	/**
	 * 生成数据库word文档
	 * 
	 * @param tablesMap
	 * @throws Exception
	 */
	public static void productDatabaseDoc(Map<String, TableInfo> tablesMap)
			throws Exception {
		XWPFDocument xwpfDocument = new XWPFDocument();

		for (Entry<String, TableInfo> entry : tablesMap.entrySet()) {
			TableInfo tableInfo = entry.getValue();

			XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
			XWPFRun xwpfRun = xwpfParagraph.createRun();
			xwpfRun.setText(tableInfo.getTableName() + "("
					+ tableInfo.getTableComment() + ")");
			xwpfRun.setFontSize(18);
			xwpfRun.setTextPosition(10);

			XWPFTable xwpfTable = xwpfDocument.createTable(tableInfo
					.getColumns().size() + 1, 6);

			int i = 0;

			xwpfTable.getRow(i).getCell(0).setText("列名");
			xwpfTable.getRow(i).getCell(1).setText("类型");
			xwpfTable.getRow(i).getCell(2).setText("主键");
			xwpfTable.getRow(i).getCell(3).setText("空值");
			xwpfTable.getRow(i).getCell(4).setText("注释");
			xwpfTable.getRow(i).getCell(5).setText("附加信息");

			for (Column column : tableInfo.getColumns()) {
				int j = 0;
				i++;
				xwpfTable.getRow(i).getCell(j++).setText(column.getName());
				xwpfTable.getRow(i).getCell(j++).setText(column.getType());
				xwpfTable.getRow(i).getCell(j++)
						.setText(column.isKey() ? "是" : "");
				xwpfTable.getRow(i).getCell(j++)
						.setText(column.isNullable() ? "允许" : "");
				xwpfTable.getRow(i).getCell(j++).setText(column.getComment());
				xwpfTable.getRow(i).getCell(j++).setText(column.getExtra());
			}

		}

        File file = new File(generatedPath);
        if(!file.exists()){
            file.mkdirs();
        }

		FileOutputStream fos = new FileOutputStream(
                generatedPath + "database.docx");
		xwpfDocument.write(fos);
		fos.close();

	}

	/**
	 * 生成java bean
	 * 
	 * @param tablesMap
	 * @throws Exception
	 */
	public static void productJavaBean(Map<String, TableInfo> tablesMap)
			throws Exception {

		VelocityContext context = new VelocityContext();
		Template template = Velocity.getTemplate("javabean.vm");

		for (Entry<String, TableInfo> entry : tablesMap.entrySet()) {

			TableInfo tableInfo = entry.getValue();

			String javaBeanName = getJavaBeanName(StringUtils.capitalize(tableInfo
                    .getTableName()));

			List<Object> fileds = new ArrayList<>();

			for (Column column : tableInfo.getColumns()) {
				Map<String, String> field = new HashMap<>();
				String columnType = column.getType();
				if (columnType.contains("(")) {
					columnType = columnType.substring(0,
							columnType.indexOf("("));
				}
				field.put("comment", column.getComment());
				field.put("type", MysqlUtils.getPojoType(columnType,column.getName()));
				field.put("name", fixName(column.getName()));
				fileds.add(field);
			}

            context.put("tableComment", tableInfo.getTableComment());
			context.put("className", javaBeanName);
			context.put("fields", fileds);
            context.put("pkgBean", pkgBean);

			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			FileUtils.writeStringToFile(new File(
                    generatedPath + "bean/" + javaBeanName + ".java"),
					writer.toString(), "UTF-8");

		}

	}

    /**
     * 根据表名获取JavaBean的名字
     * @param tableName 表名
     * @return
     */
    private static String getJavaBeanName(String tableName){
        if (table_prefix == null) return tableName;

        for (String str : table_prefix){
            tableName = tableName.replaceAll(str, "");
        }

        return fixName(StringUtils.capitalize(tableName));
    }

	/**
	 * 生成mybatis数据库mapper文件
	 * 
	 * @param tablesMap
	 * @throws Exception
	 */
	public static void productMyBatisMapper(Map<String, TableInfo> tablesMap)
			throws Exception {

		VelocityContext context = new VelocityContext();

		Template template1 = Velocity
				.getTemplate("mapper-java.vm"); // org/simplexwork/mysql/tools/velocity/mapper-java.vm

		Template template2 = Velocity
				.getTemplate("mapper-xml.vm");

		for (Entry<String, TableInfo> entry : tablesMap.entrySet()) {

			TableInfo tableInfo = entry.getValue();

			String tableName = getJavaBeanName(StringUtils.capitalize(tableInfo
                    .getTableName()));

			String interfaceName = tableName  + "Mapper";

			context.put("interfaceName", interfaceName);
			context.put("beanName", tableName);
			context.put("resultMapId", getStartSmallName(tableName + "Map"));
			context.put("beanVarName", StringUtils.uncapitalize(tableName));
            context.put("pkgMapper", pkgMapper);
            context.put("pkgBean", pkgBean);
            context.put("tableComment", tableInfo.getTableComment());

			StringWriter writer1 = new StringWriter();
			template1.merge(context, writer1);

			FileUtils.writeStringToFile(new File(
					generatedPath + "mapper/"
							+ interfaceName + ".java"), writer1.toString(),
					"UTF-8");

            String keyInBean = ""; // 主键在Bean中的变量名
            String keyInColoum = ""; // 主键在表中的名
			List<Object> fileds = new ArrayList<>();
			for (Column column : tableInfo.getColumns()) {
                if(column.isKey()){
                    keyInBean = fixName(column.getName());
                    keyInColoum = column.getName();
                }

				Map<String, Object> field = new HashMap<>();
				String columnType = column.getType();
				if (columnType.contains("(")) {
					columnType = columnType.substring(0,
							columnType.indexOf("("));
				}
				field.put("isKey", column.isKey());
				field.put("sqlName", column.getName());
				field.put("name", fixName(column.getName()));
				fileds.add(field);
			}

			context.put("tableName", tableInfo.getTableName());
            context.put("keyInBean", keyInBean);
            context.put("keyInColoum", keyInColoum);
			context.put("fields", fileds);

			StringWriter writer2 = new StringWriter();
			template2.merge(context, writer2);
			FileUtils.writeStringToFile(new File(
					generatedPath
							+ getMapperXmlName(interfaceName) + ".xml"), writer2.toString(),
					"UTF-8");
		}
	}

    /**
     * 生成Service和ServiceIml文件
     *
     * @param tablesMap
     * @throws Exception
     */
    public static void productService(Map<String, TableInfo> tablesMap)
            throws Exception {

        VelocityContext context = new VelocityContext();

        Template templateService = Velocity.getTemplate("java-service.vm");
        Template template2 = Velocity.getTemplate("java-service-impl.vm");

        for (Entry<String, TableInfo> entry : tablesMap.entrySet()) {

            TableInfo tableInfo = entry.getValue();

            String tableName = getJavaBeanName(StringUtils.capitalize(tableInfo
                    .getTableName()));

            String serviceName = tableName + "Service";

            context.put("serviceName", serviceName);
            context.put("beanName", tableName);
            context.put("beanNameTuoFeng", getStartSmallName(tableName));
            context.put("mapperTuoFeng", getStartSmallName(tableName) + "Mapper");
            context.put("beanVarName", StringUtils.uncapitalize(tableName));
            context.put("pkgMapper", pkgMapper);
            context.put("pkgBean", pkgBean);
            context.put("pkgService", pkgService);
            context.put("pkgServiceImpl", pkgServiceImpl);
            context.put("tableComment", tableInfo.getTableComment());

            String keyInBean = ""; // 主键在Bean中的变量名
            String keyInColoum = ""; // 主键在表中的名
            List<Object> fileds = new ArrayList<>();
            for (Column column : tableInfo.getColumns()) {
                if(column.isKey()){
                    keyInBean = fixName(column.getName());
                    keyInColoum = column.getName();
                }

                Map<String, Object> field = new HashMap<>();
                String columnType = column.getType();
                if (columnType.contains("(")) {
                    columnType = columnType.substring(0,
                            columnType.indexOf("("));
                }
                String name = fixName(column.getName());
                field.put("isKey", column.isKey());
                field.put("sqlName", column.getName());
                field.put("name", name);
                field.put("nameStartBig", getStartBigName(name));
                field.put("comment",column.getComment());
                field.put("isNullable", column.isNullable());
                fileds.add(field);
            }

            context.put("tableName", tableInfo.getTableName());
            context.put("keyInBean", keyInBean);
            context.put("keyInColoum", keyInColoum);
            context.put("fields", fileds);

            StringWriter writerService = new StringWriter();
            templateService.merge(context, writerService);
            FileUtils.writeStringToFile(new File(
                    generatedPath + "service/"
                            + serviceName + ".java"), writerService.toString(),
                    "UTF-8");

            StringWriter writer2 = new StringWriter();
            template2.merge(context, writer2);
            FileUtils.writeStringToFile(new File(
                    generatedPath + "serviceImpl/"
                            + serviceName + "Impl.java"), writer2.toString(),
                    "UTF-8");
        }
    }

	public static String fixName(String name) {
		while (name.contains("_")) {
			int i = name.indexOf("_");
			name = name.substring(0, i)
					+ name.substring(++i, ++i).toUpperCase()
					+ name.subSequence(i, name.length());
		}

		return name;

	}

    /**
     * 获得开头是小写的驼峰式命名
     * @param name
     * @return
     */
    private static String getStartSmallName(String name){
        name = name.substring(0,1).toLowerCase() + name.substring(1);

        return name;
    }

    /**
     * 获得开头是大写的驼峰式命名
     * @param name
     * @return
     */
    private static String getStartBigName(String name){
        name = name.substring(0,1).toUpperCase() + name.substring(1);

        return name;
    }

    /**
     * 获得mapper的xml文件名称
     * @param str
     * @return
     */
    private static String getMapperXmlName(String str){
        str = getStartSmallName(str);
        for(int i=0;i<str.length();i++) {
            char c = str.charAt(i);
            if(Character.isUpperCase(c)){
                String upper = c + "";
                String lower = "-" + upper.toLowerCase();
                str = str.replace(upper, lower);
            }
        }
            return str;
    }

}
