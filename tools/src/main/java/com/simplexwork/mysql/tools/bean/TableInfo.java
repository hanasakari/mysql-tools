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

package com.simplexwork.mysql.tools.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code.huanglei at gmail.com
 * @date Jun 27, 2014
 * @tags
 */
public class TableInfo {
	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表注释
	 */
	private String tableComment;
	/**
	 * 字段
	 */
	private List<Column> columns = new ArrayList<>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	/**
	 * @return the tableComment
	 */
	public String getTableComment() {
		return tableComment;
	}

	/**
	 * @param tableComment
	 *            the tableComment to set
	 */
	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	@Override
	public String toString() {
		return "TableInfo [tableName=" + tableName + ", columns=" + columns
				+ "]";
	}

	public static class Column {
		/**
		 * 字段名
		 */
		String name;
		/**
		 * 字段类型
		 */
		String type;
		/**
		 * 是否主键
		 */
		boolean key;
		/**
		 * 
		 */
		String extra;
		boolean isNullable;
		/**
		 * 注释
		 */
		String comment;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isKey() {
			return key;
		}

		public void setKey(boolean key) {
			this.key = key;
		}

		public String getExtra() {
			return extra;
		}

		public void setExtra(String extra) {
			this.extra = extra;
		}

		public boolean isNullable() {
			return isNullable;
		}

		public void setNullable(boolean isNullable) {
			this.isNullable = isNullable;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		@Override
		public String toString() {
			return "Column [name=" + name + ", type=" + type + ", key=" + key
					+ ", extra=" + extra + ", isNullable=" + isNullable
					+ ", comment=" + comment + "]";
		}

	}
}
