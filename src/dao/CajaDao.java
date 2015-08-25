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
import entidad.Caja;


/**
 *
 * @author cepardov
 */
public class CajaDao {    
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getCaja(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM caja");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][8];
//idCaja,idUsuario,fecha,totalDinero,totalDocumentos,subtotal,faltante,sobrante
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idCaja,"
                    + "idUsuario,"
                    + "fecha,"
                    + "totalDinero,"
                    + "totalDocumentos,"
                    + "subtotal,"
                    + "faltante,"
                    + "sobrante"
                    + " FROM caja ORDER BY idCaja");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    String estIdCaja = res.getString("idCaja");
                    String estIdUsuario = res.getString("idUsuario");
                    String estFecha = res.getString("fecha");
                    String estTotalDinero = res.getString("totalDinero");
                    String estTotalDocumentos = res.getString("totalDocumentos");
                    String estSubtotal = res.getString("subtotal");
                    String estFaltante = res.getString("faltante");
                    String estSobrante = res.getString("sobrante");

                    data[increment][0] = estIdCaja;
                    data[increment][1] = estIdUsuario;
                    data[increment][2] = estFecha;
                    data[increment][3] = estTotalDinero;
                    data[increment][4] = estTotalDocumentos;
                    data[increment][5] = estSubtotal;
                    data[increment][6] = estFaltante;
                    data[increment][7] = estSobrante;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Caja caja) {
        PreparedStatement getCaja;
        ResultSet result = null;
        try {
            getCaja = getConnection().prepareStatement("SELECT * FROM caja WHERE idCaja = ?");
            getCaja.setString(1, caja.getIdCaja());
            result = getCaja.executeQuery();
            if (result.next()) {
//idCaja,idUsuario,fecha,totalDinero,totalDocumentos,subtotal,faltante,sobrante
                caja.setIdCaja("idCaja");
                caja.setIdUsuario("idUsuario");
                caja.setFecha("fecha");
                caja.setTotalDinero("totalDinero");
                caja.setTotalDocumentos("totalDocumentos");
                caja.setSubtotal("subtotal");
                caja.setFaltante("faltante");
                caja.setSobrante("sobrante");
                
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
    
    public boolean save(Caja caja) {
        PreparedStatement saveCaja;
        try {
            saveCaja = getConnection().prepareStatement(
                    "INSERT INTO caja ("
                    + "idCaja,"
                    + "idUsuario,"
                    + "fecha,"
                    + "totalDinero,"
                    + "totalDocumentos,"
                    + "subtotal,"
                    + "faltante,"
                    + "sobrante)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//idCaja,idUsuario,fecha,totalDinero,totalDocumentos,subtotal,faltante,sobrante
            saveCaja.setString(1, caja.getIdCaja());
            saveCaja.setString(2, caja.getIdUsuario());
            saveCaja.setString(3, caja.getFecha());
            saveCaja.setString(4, caja.getTotalDinero());
            saveCaja.setString(5, caja.getTotalDocumentos());
            saveCaja.setString(6, caja.getSubtotal());
            saveCaja.setString(7, caja.getFaltante());
            saveCaja.setString(8, caja.getSobrante());
            
            saveCaja.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveCaja(): "+se.getMessage());
            caja.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Caja caja) {
        PreparedStatement saveCaja;
        try {
            saveCaja = getConnection().prepareStatement("UPDATE caja SET "
                   + "idCaja=?,"
                    + "idUsuario=?,"
                    + "fecha=?,"
                    + "totalDinero=?,"
                    + "totalDocumentos=?,"
                    + "subtotal=?,"
                    + "faltante=?,"
                    + "sobrante=?"
                    + " WHERE idCaja=?");
            saveCaja.setString(1, caja.getIdCaja());
            saveCaja.setString(2, caja.getIdUsuario());
            saveCaja.setString(3, caja.getFecha());
            saveCaja.setString(4, caja.getTotalDinero());
            saveCaja.setString(5, caja.getTotalDocumentos());
            saveCaja.setString(6, caja.getSubtotal());
            saveCaja.setString(7, caja.getFaltante());
            saveCaja.setString(8, caja.getSobrante());
            
            saveCaja.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateCaja(): "+se.getMessage());
            caja.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Caja caja) {
        PreparedStatement delCaja;
        try {
            if (caja.getIdCaja() != null) {
                delCaja = getConnection().prepareStatement(
                        "DELETE FROM caja WHERE idCaja=?");
                delCaja.setString(1, caja.getIdCaja());
                delCaja.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteCaja(): "+se.getMessage());
            caja.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
