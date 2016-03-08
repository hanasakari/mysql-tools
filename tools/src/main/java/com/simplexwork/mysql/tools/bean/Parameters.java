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

/**
 * @author code.huanglei at gmail.com
 * @date Jun 27, 2014
 * @tags
 */
public final class Parameters {
	private String host = "localhost";
	private String user = "root";
	private String port = "3306";// 默认值
	private String password;
	private String database;
	private String table = "_NULL_";
	private String path = "";
	private String packages = "org.simplexwork.";

	public Parameters() {
	}

	public Parameters(String host, String user, String port, String password,
			String database, String table, String path) {
		this.host = host;
		this.user = user;
		this.port = port;
		this.password = password;
		this.database = database;
		this.table = table;
		this.path = path;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
		return "Parameters [host=" + host + ", user=" + user + ", port=" + port
				+ ", password=" + password + ", database=" + database
				+ ", table=" + table + ", path=" + path + "]";
	}

}
