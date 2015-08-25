/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CajaDao;
import entidad.Caja;

/**
 * Caja
 * @author cepardov
 */
public class CajaBeans extends Caja{
    CajaDao cajaDao=new CajaDao();
    
    public boolean save(){
        return cajaDao.save(this);
    }
    public boolean delete(){
        return cajaDao.delete(this);
    }
    public boolean update(){
        return cajaDao.update(this);
    }
    public boolean findByID(){
        return cajaDao.findById(this);
    }
    public Object[][] getCaja(){
        return cajaDao.getCaja();
    }
}
