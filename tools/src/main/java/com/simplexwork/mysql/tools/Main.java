package com.simplexwork.mysql.tools;

import java.util.Map;

import com.simplexwork.mysql.tools.utils.DocumentUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import com.simplexwork.mysql.tools.bean.Parameters;
import com.simplexwork.mysql.tools.bean.TableInfo;
import com.simplexwork.mysql.tools.utils.MysqlUtils;

public class Main {

	private final static String OPT_HOST = "host";

	private final static String OPT_PORT = "port";
	private final static String OPT_DATABASE = "database";
	private final static String OPT_OUT = "out";
	private final static String OPT_PACKAGE = "package";

	private final static String OPT_DATABASEDOC = "d";
	private final static String OPT_JAVABEAN = "j";
	private final static String OPT_MYBATIS = "m";

	public static void main(String[] args) {
		Options options = new Options();

		options.addOption(OPT_HOST, true, "mysql host");
		options.addOption(OPT_PORT, true, "mysql port");
		options.addOption(OPT_DATABASE, true, "database");
		options.addOption(OPT_OUT, true, "out put file");
		options.addOption(OPT_PACKAGE, true, "package");
		options.addOption(OPT_DATABASEDOC,false,"databases document");
		options.addOption(OPT_JAVABEAN,false,"java bean");
		options.addOption(OPT_MYBATIS,false,"mybatis");

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new PosixParser();

		CommandLine cl = null;
		try {
			cl = parser.parse(options, args);
		} catch (ParseException e) {
			formatter.printHelp("", options);
		}

		Parameters parameters = new Parameters();

		if (cl.hasOption(OPT_HOST)) {
			parameters.setHost(cl.getOptionValue(OPT_HOST));
		}
		if (cl.hasOption(OPT_PORT)) {
			parameters.setPort(cl.getOptionValue(OPT_PORT));
		}
		if (cl.hasOption(OPT_DATABASE)) {
			parameters.setDatabase(cl.getOptionValue(OPT_DATABASE));
		}
		if (cl.hasOption(OPT_PACKAGE)) {
			parameters.setPath(cl.getOptionValue(OPT_PACKAGE));
		}
		System.out.println(parameters);

		System.out.println("获取数据库信息...");

		Map<String, TableInfo> tables = MysqlUtils.getDatabaseInfo(parameters);

		try {

			if (cl.hasOption(OPT_DATABASEDOC)) {
				System.out.println("生成数据库文档...");
				DocumentUtils.productDatabaseDoc(tables);
			}

			if (cl.hasOption(OPT_JAVABEAN)) {
				System.out.println("生成Bean...");
				DocumentUtils.productJavaBean(tables);
			}

			if (cl.hasOption(OPT_MYBATIS)) {
				System.out.println("生成mybatis文件");
				DocumentUtils.productMyBatisMapper(tables);
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
}
