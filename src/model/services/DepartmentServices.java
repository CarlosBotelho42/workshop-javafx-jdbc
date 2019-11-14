package model.services;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentServices  {

    private DepartmentDao dao = DaoFactory.createDepartmentDao(); // injeção de dependencia

    public List<Department> FindAll(){
        return dao.findAll();

    }

}
