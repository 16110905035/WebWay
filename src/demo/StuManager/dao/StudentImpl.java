package demo.StuManager.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.mysql.cj.exceptions.DeadlockTimeoutRollbackMarker;

import Util.DButil;
import demo.StuManager.domain.Page;
import demo.StuManager.domain.Student;

public class StudentImpl implements StudentDao {

	@Override
	public List<Student> findAll() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		return queryRunner.query("select * from student ", new BeanListHandler<Student>(Student.class));

	}

	@Override
	public List<Student> findByName(String name) throws SQLException { // like 查找
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		return queryRunner.query("select * from student where sname like  ?",
				new BeanListHandler<Student>(Student.class), "%" + name + "%");
	}

	@Override
	public void add(Student stu) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		queryRunner.update(
				"insert into student(sname,age,address,gender,birthday,info,tel,hobby) values (?,?,?,?,?,?,?,?)",
				stu.getSname(), stu.getAge(), stu.getAddress(), stu.getGender(), stu.getBirthday(), stu.getInfo(),
				stu.getTel(), stu.getHobby());
	}

	@Override
	public void delete(int sid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		queryRunner.update("delete from student where sid=?", sid);

	}

	@Override
	public void update(Student stu) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		String sql = "update student set sname =? ,age = ? ,gender = ?,address  =? ,tel =? ,hobby =? ,info =?  ,birthday =? where sid =?";
		queryRunner.update(sql, stu.getSname(), stu.getAge(), stu.getGender(), stu.getAddress(), stu.getTel(),
				stu.getHobby(), stu.getInfo(), stu.getBirthday(), stu.getSid());

	}

	@Override
	public Student findBySid(int sid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		return queryRunner.query("select * from student where sid = ?", new BeanHandler<Student>(Student.class), sid);

	}

	@Override
	public List<Student> findByStudent() {

		return null;
	}

	@Override
	public List<Student> search(String sql) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		return queryRunner.query(sql, new BeanListHandler<Student>(Student.class));
	}

	@Override
	public List<Student> findByPage(String sql, int currentPage) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		return queryRunner.query(sql + " limit ? offset ?", new BeanListHandler<Student>(Student.class), PAGE_SIZE,
				(currentPage - 1) * PAGE_SIZE);
	}

	@Override
	public int findStudentNum() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		Object res = queryRunner.query("select  count(*) from student", new ScalarHandler());
		return Integer.parseInt(res.toString());
	}

	@Override
	public int searchStudentNum(String sql) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DButil.getDataBase());
		Object res = queryRunner.query(sql, new ScalarHandler());
		return Integer.parseInt(res.toString());
	}

}
