/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.TransaccionDao;
import entidad.Transaccion;

/**
 * Transaccion
 * @author cepardov
 */
public class TransaccionBeans extends Transaccion{
    TransaccionDao transaccionDao=new TransaccionDao();
    
    public boolean save(){
        return transaccionDao.save(this);
    }
    public boolean delete(){
        return transaccionDao.delete(this);
    }
    public boolean update(){
        return transaccionDao.update(this);
    }
    public boolean findByToken(){
        return transaccionDao.findByToken(this);
    }
    public boolean findByID(){
        return transaccionDao.findById(this);
    }
    public Object[][] getTransaccion(){
        return transaccionDao.getTransaccion();
    }
}
