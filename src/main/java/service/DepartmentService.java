package service;

import dao.DepartmentDAO;
import model.Department;

import java.util.List;

public class DepartmentService {

    private DepartmentDAO departmentDAO;

    public DepartmentService() {
        departmentDAO = new DepartmentDAO();
    }

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    public String getDepartmentNameById(int departmentId) {

        List<Department> departments =
                departmentDAO.getAllDepartments();

        for (Department department : departments) {

            if (department.getDepartmentId() == departmentId) {
                return department.getDepartmentName();
            }
        }

        return "Unknown";
    }
}