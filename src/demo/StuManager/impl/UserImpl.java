package demo.StuManager.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import Util.DButil;
import demo.StuManager.dao.UserDao;
import test.Bean.User;

public class UserImpl implements UserDao {

	@Override
	public boolean loginIn(String username, String password) {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		try {
			User user = queryRunner.query("select * from user where id  = ? and password =?",
					new BeanHandler<User>(User.class), username, password);
			return user != null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Test
	public void run() {
		if (loginIn("admin", "aa12321.")) {
			System.out.println("==========login in========");
		} else {
			System.out.println("============failed==============");
		}
		if (loginIn("admin", "123456")) {
			System.out.println("==========login in========");
		} else {
			System.out.println("============failed==============");

		}

	}

}
