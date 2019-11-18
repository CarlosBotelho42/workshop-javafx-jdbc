package model.services;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class SellerServices {

    private SellerDao dao = DaoFactory.createSellerDao(); // injeção de dependencia

    public List<Seller> FindAll(){
        return dao.findAll();

    }

    public void  saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);

        }
        else {
            dao.update(obj);

        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());

    }
}
