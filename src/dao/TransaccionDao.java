
package dao;

import conexion.DataBaseInstance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import entidad.Transaccion;

/**
 *
 * @author cepardov
 */
public class TransaccionDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getTransaccion(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM transaccion");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null,"Error recopilando datos de transaccion"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        }
        Object[][] data = new String[posid][14];//Numero de datos
//idTransaccion,tipo,num,fecha,totalNeto,totalIva,total,vuelto,credito,efectivo,idVendedor,idCliente,finalizada
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idTransaccion,"
                    + "tipo,"
                    + "num,"
                    + "fecha,"
                    + "totalNeto,"
                    + "totalIva,"
                    + "total,"
                    + "vuelto,"
                    + "credito,"
                    + "efectivo,"
                    + "idVendedor,"
                    + "idCliente,"
                    + "finalizada,"
                    + "token"
                    + " FROM transaccion ORDER BY idTransaccion");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    String estIdTransaccion = res.getString("idTransaccion");
                    String estTipo = res.getString("tipo");
                    String estNum = res.getString("num");
                    String estFecha = res.getString("fecha");
                    String estTotalNeto = res.getString("totalNeto");
                    String estTotalIva = res.getString("totalIva");
                    String estTotal = res.getString("total");
                    String estVuelto = res.getString("vuelto");
                    String estCredito = res.getString("credito");
                    String estEfectivo = res.getString("efectivo");
                    String estIdVendedor = res.getString("idVendedor");
                    String estIdCliente = res.getString("idCliente");
                    String estFinalizada = res.getString("finalizada");
                    String estToken = res.getString("token");
                    
                    data[increment][0] = estIdTransaccion;
                    data[increment][1] = estTipo;
                    data[increment][2] = estNum;
                    data[increment][3] = estFecha;
                    data[increment][4] = estTotalNeto;
                    data[increment][5] = estTotalIva;
                    data[increment][6] = estTotal;
                    data[increment][7] = estVuelto;
                    data[increment][8] = estCredito;
                    data[increment][9] = estEfectivo;
                    data[increment][10] = estIdVendedor;
                    data[increment][11] = estIdCliente;
                    data[increment][12] = estFinalizada;
                    data[increment][13] = estToken;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null,"Error al transportar datos de la transacción a la interfaz"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error="+se);
            }
        return data;
    }
    
    public boolean findById(Transaccion transaccion) {
        PreparedStatement getTransaccion;
        ResultSet result = null;
        try {
            getTransaccion = getConnection().prepareStatement("SELECT * FROM transaccion WHERE idTransaccion = ?");
            getTransaccion.setString(1, transaccion.getIdTransaccion());
            result = getTransaccion.executeQuery();
            if (result.next()) {
//idTransaccion,tipo,num,fecha,totalNeto,totalIva,total,vuelto,credito,efectivo,idVendedor,idCliente,finalizada
                transaccion.setIdTransaccion(result.getString("idTransaccion"));
                transaccion.setTipo(result.getString("tipo"));
                transaccion.setNum(result.getString("num"));
                transaccion.setFecha(result.getString("fecha"));
                transaccion.setTotalNeto(result.getString("totalNeto"));
                transaccion.setTotalIva(result.getString("totalIva"));
                transaccion.setTotal(result.getString("total"));
                transaccion.setVuelto(result.getString("vuelto"));
                transaccion.setCredito(result.getString("credito"));
                transaccion.setEfectivo(result.getString("efectivo"));
                transaccion.setIdVendedor(result.getString("idVendedor"));
                transaccion.setIdCliente(result.getString("idCliente"));
                transaccion.setFinalizada(result.getString("finalizada"));
                transaccion.setToken(result.getString("token"));
                
                result.close();
            } else {
                return false;
            }
            closeConnection();
            return true;
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(null,"Error al intentar buscar una transacción por id"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            System.err.println(se.getMessage());
            return false;
        }
    }
    
    public boolean findByToken(Transaccion transaccion) {
        PreparedStatement getTransaccion;
        ResultSet result = null;
        try {
            getTransaccion = getConnection().prepareStatement("SELECT * FROM transaccion WHERE token = ?");
            getTransaccion.setString(1, transaccion.getToken());
            result = getTransaccion.executeQuery();
            if (result.next()) {
//idTransaccion,tipo,num,fecha,totalNeto,totalIva,total,vuelto,credito,efectivo,idVendedor,idCliente,finalizada
                transaccion.setIdTransaccion(result.getString("idTransaccion"));
                transaccion.setTipo(result.getString("tipo"));
                transaccion.setNum(result.getString("num"));
                transaccion.setFecha(result.getString("fecha"));
                transaccion.setTotalNeto(result.getString("totalNeto"));
                transaccion.setTotalIva(result.getString("totalIva"));
                transaccion.setTotal(result.getString("total"));
                transaccion.setVuelto(result.getString("vuelto"));
                transaccion.setCredito(result.getString("credito"));
                transaccion.setEfectivo(result.getString("efectivo"));
                transaccion.setIdVendedor(result.getString("idVendedor"));
                transaccion.setIdCliente(result.getString("idCliente"));
                transaccion.setFinalizada(result.getString("finalizada"));
//                transaccion.setToken(result.getString("token"));
                
                result.close();
            } else {
                return false;
            }
            closeConnection();
            return true;
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(null,"Error al intentar buscar una transacción por id"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            System.err.println(se.getMessage());
            return false;
        }
    }
    
    public boolean save(Transaccion transaccion) {
        PreparedStatement saveTransaccion;
        try {
            saveTransaccion = getConnection().prepareStatement(
                    "INSERT INTO transaccion ("
                    + "idTransaccion,"
                    + "tipo,"
                    + "num,"
                    + "fecha,"
                    + "totalNeto,"
                    + "totalIva,"
                    + "total,"
                    + "vuelto,"
                    + "credito,"
                    + "efectivo,"
                    + "idVendedor,"
                    + "idCliente,"
                    + "finalizada,"
                    + "token)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//idTransaccion,tipo,num,fecha,totalNeto,totalIva,total,vuelto,credito,efectivo,idVendedor,idCliente,finalizada
            saveTransaccion.setString(1, transaccion.getIdTransaccion());
            saveTransaccion.setString(2, transaccion.getTipo());
            saveTransaccion.setString(3, transaccion.getNum());
            saveTransaccion.setString(4, transaccion.getFecha());
            saveTransaccion.setString(5, transaccion.getTotalNeto());
            saveTransaccion.setString(6, transaccion.getTotalIva());
            saveTransaccion.setString(7, transaccion.getTotal());
            saveTransaccion.setString(8, transaccion.getVuelto());
            saveTransaccion.setString(9, transaccion.getCredito());
            saveTransaccion.setString(10, transaccion.getEfectivo());
            saveTransaccion.setString(11, transaccion.getIdVendedor());
            saveTransaccion.setString(12, transaccion.getIdCliente());
            saveTransaccion.setString(13, transaccion.getFinalizada());
            saveTransaccion.setString(14, transaccion.getToken());

            saveTransaccion.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveTransaccion(): "+se.getMessage());
            transaccion.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Transaccion transaccion) {
        PreparedStatement saveTransaccion;
        try {
            saveTransaccion = getConnection().prepareStatement("UPDATE transaccion SET "
                    + "idTransaccion=?,"
                    + "tipo=?,"
                    + "num=?,"
                    + "fecha=?,"
                    + "totalNeto=?,"
                    + "totalIva=?,"
                    + "total=?,"
                    + "vuelto=?,"
                    + "credito=?,"
                    + "efectivo=?,"
                    + "idVendedor=?,"
                    + "idCliente=?,"
                    + "finalizada=?,"
                    + "token=?"
                    + " WHERE idTransaccion=?");
            saveTransaccion.setString(1, transaccion.getIdTransaccion());
            saveTransaccion.setString(2, transaccion.getTipo());
            saveTransaccion.setString(3, transaccion.getNum());
            saveTransaccion.setString(4, transaccion.getFecha());
            saveTransaccion.setString(5, transaccion.getTotalNeto());
            saveTransaccion.setString(6, transaccion.getTotalIva());
            saveTransaccion.setString(7, transaccion.getTotal());
            saveTransaccion.setString(8, transaccion.getVuelto());
            saveTransaccion.setString(9, transaccion.getCredito());
            saveTransaccion.setString(10, transaccion.getEfectivo());
            saveTransaccion.setString(11, transaccion.getIdVendedor());
            saveTransaccion.setString(12, transaccion.getIdCliente());
            saveTransaccion.setString(13, transaccion.getFinalizada());
            saveTransaccion.setString(14, transaccion.getToken());
            saveTransaccion.setString(15, transaccion.getIdTransaccion());
            
            saveTransaccion.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateTransaccion(): "+se.getMessage());
            transaccion.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Transaccion transaccion) {
        PreparedStatement delTransaccion;
        try {

            if (transaccion.getIdTransaccion() != null) {
                delTransaccion = getConnection().prepareStatement(
                        "DELETE FROM transaccion WHERE idTransaccion=?");
                delTransaccion.setString(1, transaccion.getIdTransaccion());
                delTransaccion.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteTransaccion(): "+se.getMessage());
            transaccion.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
