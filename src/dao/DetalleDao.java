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
import entidad.Detalle;


/**
 *
 * @author cepardov
 */
public class DetalleDao {    
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getDetalle(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM detalle");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][7];
//idDetalle,idFactura,idProducto,cantidad,unitario,descuento,total
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idDetalle,"
                    + "idFactura,"
                    + "idProducto,"
                    + "cantidad,"
                    + "unitario,"
                    + "descuento,"
                    + "total"
                    + " FROM detalle ORDER BY idDetalle");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    String estIdDetalle = res.getString("idDetalle");
                    String estIdFactura = res.getString("idFactura");
                    String estIdProducto = res.getString("idProducto");
                    String estCantidad = res.getString("cantidad");
                    String estUnitario = res.getString("unitario");
                    String estDescuento = res.getString("descuento");
                    String estTotal = res.getString("total");

                    data[increment][0] = estIdDetalle;
                    data[increment][1] = estIdFactura;
                    data[increment][2] = estIdProducto;
                    data[increment][3] = estCantidad;
                    data[increment][4] = estUnitario;
                    data[increment][5] = estDescuento;
                    data[increment][6] = estTotal;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Detalle detalle) {
        PreparedStatement getDetalle;
        ResultSet result = null;
        try {
            getDetalle = getConnection().prepareStatement("SELECT * FROM detalle WHERE idDetalle = ?");
            getDetalle.setString(1, detalle.getIdDetalle());
            result = getDetalle.executeQuery();
            if (result.next()) {
//idDetalle,idFactura,idProducto,cantidad,unitario,descuento,total
                detalle.setIdDetalle("idDetalle");
                detalle.setIdFactura("idFactura");
                detalle.setIdProducto("idProducto");
                detalle.setCantidad("cantidad");
                detalle.setUnitario("unitario");
                detalle.setDescuento("descuento");
                detalle.setTotal("total");
                
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
    
    public boolean save(Detalle detalle) {
        PreparedStatement saveDetalle;
        try {
            saveDetalle = getConnection().prepareStatement(
                    "INSERT INTO detalle ("
                    + "idDetalle,"
                    + "idFactura,"
                    + "idProducto,"
                    + "cantidad,"
                    + "unitario,"
                    + "descuento,"
                    + "total)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)");
//idDetalle,idFactura,idProducto,cantidad,unitario,descuento,total
            saveDetalle.setString(1, detalle.getIdDetalle());
            saveDetalle.setString(2, detalle.getIdFactura());
            saveDetalle.setString(3, detalle.getIdProducto());
            saveDetalle.setString(4, detalle.getCantidad());
            saveDetalle.setString(5, detalle.getUnitario());
            saveDetalle.setString(6, detalle.getDescuento());
            saveDetalle.setString(7, detalle.getTotal());
            
            saveDetalle.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveDetalle(): "+se.getMessage());
            detalle.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Detalle detalle) {
        PreparedStatement saveDetalle;
        try {
            saveDetalle = getConnection().prepareStatement("UPDATE detalle SET "
                    + "idDetalle=?,"
                    + "idFactura=?,"
                    + "idProducto=?,"
                    + "cantidad=?,"
                    + "unitario=?,"
                    + "descuento=?,"
                    + "total=?"
                    + " WHERE idDetalle=?");
            saveDetalle.setString(1, detalle.getIdDetalle());
            saveDetalle.setString(2, detalle.getIdFactura());
            saveDetalle.setString(3, detalle.getIdProducto());
            saveDetalle.setString(4, detalle.getCantidad());
            saveDetalle.setString(5, detalle.getUnitario());
            saveDetalle.setString(6, detalle.getDescuento());
            saveDetalle.setString(7, detalle.getTotal());
            
            saveDetalle.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateDetalle(): "+se.getMessage());
            detalle.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Detalle detalle) {
        PreparedStatement delDetalle;
        try {
            if (detalle.getIdDetalle() != null) {
                delDetalle = getConnection().prepareStatement(
                        "DELETE FROM detalle WHERE idDetalle=?");
                delDetalle.setString(1, detalle.getIdDetalle());
                delDetalle.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteDetalle(): "+se.getMessage());
            detalle.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
