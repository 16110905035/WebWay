package Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class DButil {

	static String driverClass = null;
	static String url = null;
	static String user = null;
	static String password = null;
	static {
		try {
			Properties properties = new Properties();
			//类加载器的方式，
			InputStream in = DButil.class.getClassLoader().getResourceAsStream("jdbc.properties"); 
			
//			FileReader in = new FileReader("./jdbc.properties"); // 不成功 ，获取文件路径失败
			properties.load(in);
		
			driverClass = properties.getProperty("driverClass");
			url = properties.getProperty("url");
			user = properties.getProperty("name");
			password = properties.getProperty("password");
		} catch (Exception e) {
		}
	}
	// java 实训代码
	public static Connection open() {
		try {
			Class.forName(driverClass);
			return DriverManager.getConnection(url, user, password);
			
		}catch (Exception e) {
			System.out.println("can't link to DataBase");
			e.printStackTrace();
		}
		return null ;
		
	}
	public static void close (Connection con) {
		if(con ==null) {
			return ; 
		}else {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @description: 数据库的备份
	 * @param: host,username,password,savePath,fileName,databaseName;
	 * @return: true or false
	 * @author:initial-mind
	 * @time:2018年7月7日
	 */
	public static synchronized boolean backup(String host, String userName, String password, String savePath,
			String fileName, String databaseName) {
		File saveFile = new File(savePath);
		if (!saveFile.exists()) {
			saveFile.mkdirs();
		}
		if (!savePath.endsWith(File.separator)) {
			savePath = savePath + File.separator;
		}
		StringBuilder command = new StringBuilder();
		command.append("mysqldump --opt -h").append(host).append(" --user=").append(userName).append(" --password=")
				.append(password).append(" --lock-all-tables=true ").append(" --result-file=")
				.append(savePath + fileName).append("  --default-character-set=utf8 ").append(databaseName);
//		
//		command.append("mysqldump -h").append(host).append(" -u").append(userName).append(" -p").append(password)
//				.append(" ").append(databaseName).append(" > ").append(savePath).append(fileName);
//		System.out.println(command);
		String[] cmd = { "sh", "-c", command.toString() };
		System.out.println(cmd);
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			if (process.waitFor() == 0) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @description:数据库的恢复
	 * @param:userName,password,databaseName,filePath,fileName
	 * @return: true or false
	 * @author:initial-mind
	 * @time:2018年7月7日
	 */
	public synchronized static boolean load(String userName, String password, String databaseName, 
//			String filePath,
			String fileName) {
		StringBuilder loadDB = new StringBuilder();
		loadDB.append("mysql -u").append(userName).append(" -p").append(password).append(" ").append(databaseName)
				.append(" < ").append(
//						filePath + File.separator +
						fileName);

		System.out.println(loadDB.toString());
		String[] cmd = { "sh", "-c", loadDB.toString() };
		try {
			Process load = Runtime.getRuntime().exec(cmd);
			if (load.waitFor() == 0)
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}


//	@Test
//	 public void run() {
//		System.out.println(url + user + password);
//	}
	@Test
	public void rn() {
		Connection con = null; 
		PreparedStatement pe = null;
		ResultSet rs  = null ;
		String sql = "select * from user";
		con =DButil.open();
		try {
			pe = con.prepareStatement(sql);
			rs= pe.executeQuery();
			while(rs.next()) {
				String userID = rs.getString(1);
				String password =rs.getString(2);
				System.out.println(userID+" "+ password);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

}
