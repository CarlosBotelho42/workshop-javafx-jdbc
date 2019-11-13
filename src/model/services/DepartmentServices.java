package model.services;

import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentServices  {

    public List<Department> FindAll(){
        List<Department> list = new ArrayList<>();
        list.add(new Department(1,"Livros"));
        list.add(new Department(2,"Filmes"));
        list.add(new Department(3,"CDs"));

        return list;
    }

}
