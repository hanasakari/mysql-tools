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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.simplexwork.mysql.tools.bean.Parameters;
import com.simplexwork.mysql.tools.bean.TableInfo;
import com.simplexwork.mysql.tools.bean.TableInfo.Column;

/**
 * @author code.huanglei at gmail.com
 * @date Jun 27, 2014
 * @tags
 */
public final class MysqlUtils {

	private static Map<String, String> DataTypeMap;

	static {
		DataTypeMap = new HashMap<String, String>();
		DataTypeMap.put("varchar", "String");
		DataTypeMap.put("int", "Integer");
		DataTypeMap.put("datetime", "Date");
		DataTypeMap.put("nvarchar", "String");
		DataTypeMap.put("char", "String");
		DataTypeMap.put("uniqueidentifier", "String");
		DataTypeMap.put("bigint", "Long");
		DataTypeMap.put("tinyint", "Integer");
		DataTypeMap.put("smallint", "Integer");
		DataTypeMap.put("text", "String");
		DataTypeMap.put("float", "Float");
        DataTypeMap.put("decimal", "BigDecimal");
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getPojoType(String columnType, String columnName) {

        // 需要转换为Long的字段
        String[] longTypes = new String[]{"add_time","update_time"};
        for(String name : longTypes){
            if(name.equals(columnName)){
                return "Long";
            }
        }

		String tmp = columnType.toLowerCase();
		StringTokenizer st = new StringTokenizer(tmp);
		return DataTypeMap.get(st.nextToken());
	}

	public static Map<String, TableInfo> getDatabaseInfo(Parameters parameters) {
		String sql = "select table_name , column_name ,  column_type , column_key , extra , is_nullable ,column_comment, "
				+ "( select tables.table_comment from tables where tables.table_name = columns.table_name and tables.table_schema = '"
				+ parameters.getDatabase()
				+ "') as table_comment"
				+ " from columns where table_schema='"
				+ parameters.getDatabase() + "' order by table_name";

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Map<String, TableInfo> tablesMap = new LinkedHashMap<>();

		try {
			connection = DriverManager.getConnection("jdbc:mysql://"
					+ parameters.getHost() + ":" + parameters.getPort()
					+ "/information_schema", parameters.getUser(),
					parameters.getPassword());
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				String tableName = resultSet.getString("table_name");

				TableInfo tableInfo = tablesMap.get(tableName);

				if (tableInfo == null) {
					tableInfo = new TableInfo();
					tableInfo.setTableName(tableName);
					tableInfo.setTableComment(resultSet
							.getString("table_comment"));
				}

				Column column = new Column();
				column.setName(resultSet.getString("column_name"));
				column.setType(resultSet.getString("column_type"));
				column.setKey(resultSet.getString("column_key").equals("PRI"));
				column.setExtra(resultSet.getString("extra"));
				column.setNullable(resultSet.getString("is_nullable").equals(
						"YES"));
				column.setComment(resultSet.getString("column_comment"));

				tableInfo.addColumn(column);

				tablesMap.put(tableName, tableInfo);

			}

		} catch (SQLException sqlException) {
			sqlException.printStackTrace(System.err);
		} finally {
			try {
				if (resultSet != null && !resultSet.isClosed()) {
					resultSet.close();
				}

				if (statement != null && !statement.isClosed()) {
					statement.close();
				}

				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException exception) {

			}
		}

		return tablesMap;
	}
}
