/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DetalleDao;
import entidad.Detalle;

/**
 * Detalle
 * @author cepardov
 */
public class DetalleBeans extends Detalle{
    DetalleDao detalleDao=new DetalleDao();
    
    public boolean save(){
        return detalleDao.save(this);
    }
    public boolean delete(){
        return detalleDao.delete(this);
    }
    public boolean update(){
        return detalleDao.update(this);
    }
    public boolean findByID(){
        return detalleDao.findById(this);
    }
    public Object[][] getDetalle(){
        return detalleDao.getDetalle();
    }
}
