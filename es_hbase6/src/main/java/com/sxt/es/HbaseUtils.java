package com.sxt.es;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HbaseUtils {
	
	/**
	 * HBASE 表名称
	 */
	public  final String TABLE_NAME = "doc";
	/**
	 * 列簇1 文章信息
	 */
	public  final String COLUMNFAMILY_1 = "cf1";
	/**
	 * 列簇1中的列
	 */
	public  final String COLUMNFAMILY_1_TITLE = "title";
	public  final String COLUMNFAMILY_1_AUTHOR = "author";
	public  final String COLUMNFAMILY_1_CONTENT = "content";
	public  final String COLUMNFAMILY_1_DESCRIBE = "describe";
	
	public static Admin admin = null;
	public static Configuration conf = null;
	public static Connection conn = null;
	
	/**
	 * 构造函数加载配置
	 */
	public HbaseUtils() {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.205.153:2181");
		conf.set("hbase.rootdir", "hdfs://h153:9000/hbase");	
		try {
			conn = ConnectionFactory.createConnection(conf);
			admin = conn.getAdmin();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 读取一条记录
	@SuppressWarnings({ "deprecation", "resource" })
	public Doc get(String tableName, String row) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(row.getBytes());
		Doc Doc = null;
		try {
			
			Result result = table.get(get);
			KeyValue[] raw = result.raw();
			if (raw.length == 4) {
				Doc = new Doc();
				Doc.setId(row);
				Doc.setTitle(new String(raw[3].getValue()));
				Doc.setAuthor(new String(raw[0].getValue()));
				Doc.setContent(new String(raw[1].getValue()));
				Doc.setDescribe(new String(raw[2].getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Doc;
	}
	
	// 添加一条记录
	public  void put(String tableName, String row, String columnFamily,
			String column, String data) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Put p1 = new Put(Bytes.toBytes(row));
		p1.addColumn(columnFamily.getBytes(),column.getBytes(),data.getBytes());
		table.put(p1);
		System.out.println("put'" + row + "'," + columnFamily + ":" + column
				+ "','" + data + "'");
	}
}
