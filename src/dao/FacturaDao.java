/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.DataBaseInstance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import entidad.Factura;


/**
 *
 * @author cepardov
 */
public class FacturaDao {    
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getFactura(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM factura");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][10];
//idFactura,num,fecha,totalNeto,totalIva,total,pagado,vuelto,idVendedor,idCliente,idPago
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idFactura,"
                    + "num,"
                    + "fecha,"
                    + "totalNeto,"
                    + "totalIva,"
                    + "total,"
                    + "pagado,"
                    + "vuelto,"
                    + "idVendedor,"
                    + "idCliente,"
                    + "idPago"
                    + " FROM factura ORDER BY idFactura");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    String estIdFactura = res.getString("idFactura");
                    String estNum = res.getString("num");
                    String estFecha = res.getString("fecha");
                    String estTotalNeto = res.getString("totalNeto");
                    String estTotalIva = res.getString("totalIva");
                    String estTotal = res.getString("total");
                    String estPagado = res.getString("pagado");
                    String estVuelto = res.getString("vuelto");
                    String estIdVendedor = res.getString("idVendedor");
                    String estIdCliente = res.getString("idCliente");
                    String estIdPago = res.getString("idPago");

                    data[increment][0] = estIdFactura;
                    data[increment][1] = estNum;
                    data[increment][2] = estFecha;
                    data[increment][3] = estTotalNeto;
                    data[increment][4] = estTotalIva;
                    data[increment][5] = estTotal;
                    data[increment][6] = estPagado;
                    data[increment][7] = estVuelto;
                    data[increment][8] = estIdVendedor;
                    data[increment][9] = estIdCliente;
                    data[increment][10] = estIdPago;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Factura factura) {
        PreparedStatement getFactura;
        ResultSet result = null;
        try {
            getFactura = getConnection().prepareStatement("SELECT * FROM factura WHERE idFactura = ?");
            getFactura.setString(1, factura.getIdFactura());
            result = getFactura.executeQuery();
            if (result.next()) {
//idFactura,num,fecha,totalNeto,totalIva,total,pagado,vuelto,idVendedor,idCliente,idPago
                factura.setIdFactura("idFactura");
                factura.setNum("num");
                factura.setFecha("fecha");
                factura.setTotalNeto("totalNeto");
                factura.setTotalIva("totalIva");
                factura.setTotal("total");
                factura.setPagado("pagado");
                factura.setVuelto("vuelto");
                factura.setIdVendedor("idVendedor");
                factura.setIdCliente("idCliente");
                factura.setIdPago("idPago");
                
                result.close();
            } else {
                return false;
            }
            closeConnection();
            return true;
        } catch (SQLException se) {
            System.err.println("Se ha producido un error de BD.");
            System.err.println(se.getMessage());
            return false;
        }
    }
    
    public boolean save(Factura factura) {
        PreparedStatement saveFactura;
        try {
            saveFactura = getConnection().prepareStatement(
                    "INSERT INTO factura ("
                    + "idFactura,"
                    + "num,"
                    + "fecha,"
                    + "totalNeto,"
                    + "totalIva,"
                    + "total,"
                    + "pagado,"
                    + "vuelto,"
                    + "idVendedor,"
                    + "idCliente,"
                    + "idPago)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            saveFactura.setString(1, factura.getIdFactura());
            saveFactura.setString(2, factura.getNum());
            saveFactura.setString(3, factura.getFecha());
            saveFactura.setString(4, factura.getTotalNeto());
            saveFactura.setString(5, factura.getTotalIva());
            saveFactura.setString(6, factura.getTotal());
            saveFactura.setString(7, factura.getPagado());
            saveFactura.setString(8, factura.getVuelto());
            saveFactura.setString(9, factura.getIdVendedor());
            saveFactura.setString(10, factura.getIdCliente());
            saveFactura.setString(11, factura.getIdPago());
            
            saveFactura.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveFactura(): "+se.getMessage());
            factura.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Factura factura) {
        PreparedStatement saveFactura;
        try {
            saveFactura = getConnection().prepareStatement("UPDATE factura SET "
                    + "idFactura=?,"
                    + "num=?,"
                    + "fecha=?,"
                    + "totalNeto=?,"
                    + "totalIva=?,"
                    + "total=?,"
                    + "pagado=?,"
                    + "vuelto=?,"
                    + "idVendedor=?,"
                    + "idCliente=?,"
                    + "idPago=?"
                    + " WHERE idFactura=?");
            saveFactura.setString(1, factura.getIdFactura());
            saveFactura.setString(2, factura.getNum());
            saveFactura.setString(3, factura.getFecha());
            saveFactura.setString(4, factura.getTotalNeto());
            saveFactura.setString(5, factura.getTotalIva());
            saveFactura.setString(6, factura.getTotal());
            saveFactura.setString(7, factura.getPagado());
            saveFactura.setString(8, factura.getVuelto());
            saveFactura.setString(9, factura.getIdVendedor());
            saveFactura.setString(10, factura.getIdCliente());
            saveFactura.setString(11, factura.getIdPago());
            
            saveFactura.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateFactura(): "+se.getMessage());
            factura.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Factura factura) {
        PreparedStatement delFactura;
        try {
            if (factura.getIdFactura() != null) {
                delFactura = getConnection().prepareStatement(
                        "DELETE FROM factura WHERE idFactura=?");
                delFactura.setString(1, factura.getIdFactura());
                delFactura.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteFactura(): "+se.getMessage());
            factura.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
