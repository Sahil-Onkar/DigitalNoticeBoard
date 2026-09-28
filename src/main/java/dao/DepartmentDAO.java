package dao;

import model.Department;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public List<Department> getAllDepartments() {

        List<Department> departments = new ArrayList<>();

        String sql =
                "SELECT * FROM departments ORDER BY department_id";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Department department =
                        new Department();

                department.setDepartmentId(
                        resultSet.getInt("department_id")
                );

                department.setDepartmentName(
                        resultSet.getString("department_name")
                );

                department.setDepartmentCode(
                        resultSet.getString("department_code")
                );

                departments.add(department);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return departments;
    }
}