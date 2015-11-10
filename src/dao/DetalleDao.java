
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
                JOptionPane.showMessageDialog(null,"Error recopilando datos de detalle"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        }
        Object[][] data = new String[posid][0];//Numero de datos
//idDetalle,idTransaccion,idProducto,cantidad,unitario,descuento,total
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idDetalle,"
                    + "idTransaccion,"
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
                    String estIdTransaccion = res.getString("idTransaccion");
                    String estIdProducto = res.getString("idProducto");
                    String estCantidad = res.getString("cantidad");
                    String estUnitario = res.getString("unitario");
                    String estDescuento = res.getString("descuento");
                    String estTotal = res.getString("total");
                    
                    data[increment][0] = estIdDetalle;
                    data[increment][1] = estIdTransaccion;
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
                JOptionPane.showMessageDialog(null,"Error al transportar datos de la transacción a la interfaz"+"\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error="+se);
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
//idDetalle,idTransaccion,idProducto,cantidad,unitario,descuento,total
                detalle.setIdDetalle(result.getString("idDetalle"));
                detalle.setIdTransaccion(result.getString("idTransaccion"));
                detalle.setIdProducto(result.getString("idProducto"));
                detalle.setCantidad(result.getString("cantidad"));
                detalle.setUnitario(result.getString("unitario"));
                detalle.setDescuento(result.getString("escuento"));
                detalle.setTotal(result.getString("total"));
                
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
    
    public boolean save(Detalle detalle) {
        PreparedStatement saveDetalle;
        try {
            saveDetalle = getConnection().prepareStatement(
                    "INSERT INTO detalle ("
                    + "idDetalle,"
                    + "idTransaccion,"
                    + "idProducto,"
                    + "cantidad,"
                    + "unitario,"
                    + "descuento,"
                    + "total)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)");
//idDetalle,idTransaccion,idProducto,cantidad,unitario,descuento,total
            saveDetalle.setString(1, detalle.getIdDetalle());
            saveDetalle.setString(2, detalle.getIdTransaccion());
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
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveCategorias(): "+se.getMessage());
            detalle.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Detalle detalle) {
        PreparedStatement saveDetalle;
        try {
            saveDetalle = getConnection().prepareStatement("UPDATE detalle SET "
                    + "idDetalle=?,"
                    + "idTransaccion=?,"
                    + "idProducto=?,"
                    + "cantidad=?,"
                    + "unitario=?,"
                    + "descuento=?,"
                    + "total=?"
                    + " WHERE idDetalle=?");
            saveDetalle.setString(1, detalle.getIdDetalle());
            saveDetalle.setString(2, detalle.getIdTransaccion());
            saveDetalle.setString(3, detalle.getIdProducto());
            saveDetalle.setString(4, detalle.getCantidad());
            saveDetalle.setString(5, detalle.getUnitario());
            saveDetalle.setString(6, detalle.getDescuento());
            saveDetalle.setString(7, detalle.getTotal());
            saveDetalle.setString(8, detalle.getIdDetalle());
            
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
