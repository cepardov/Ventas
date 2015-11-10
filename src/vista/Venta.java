/*
 * Copyright (C) 2015 cepardov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package vista;

import beans.DetalleBeans;
import beans.ProductoBeans;
import beans.TransaccionBeans;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import cl.cepardov.encriptar.Encript;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.apache.commons.lang3.SystemUtils;
import utilidades.NumeroLetra;

/**
 *
 * @author cepardov
 */
public class Venta extends javax.swing.JInternalFrame {
    ProductoBeans productobeans = new ProductoBeans();
    NumeroLetra numeroletra=new NumeroLetra();
    TransaccionBeans transaccionbeans=new TransaccionBeans();
    DetalleBeans detallebeans=new DetalleBeans();
    
    public boolean instanciaVenta;
    
    boolean onLogin = false;
    boolean onAnular = false;
    boolean onPagos = false;
    DefaultTableModel modeloTablaDetalle = new DefaultTableModel();
    DefaultTableModel modeloTablaPagos = new DefaultTableModel();
    
    //Datos internos de compra
    int cantidad=1;
    int totalVenta=0;
    
    //Direccion ejecutable
    String path = System.getProperty("user.dir");
    String separador = System.getProperty("file.separator");
    
    //Datos de usuario
    String loginId = null;
    String loginNombre = null;
    String loginApellido = null;
    String loginUsuario = null;
    String loginClave = null;//Encriptado
    String loginPrivilegio = null;
    
    //Datos de la transaccion
    String token = null;
//    String txtIdTransaccion = null;
    String txtTipoTransaccion = "VENTA";
    String txtNumTransaccion = "1";
    boolean finalizadaTransaccion = false;
//    String txtIddetalle = null;
    
    
    /**
     * Creates new form Venta
     * @param idUsuario
     * @param nombre
     * @param apellido
     * @param usuario
     * @param clave
     * @param privilegio
     */
    public Venta(String idUsuario, String nombre, String apellido, String usuario, String clave, String privilegio) {
        initComponents();
        loginId = idUsuario;
        loginNombre = nombre;
        loginApellido = apellido;
        loginUsuario = usuario;
        loginClave = clave;
        loginPrivilegio = privilegio;
        this.inicializa();
    }
    
    public Venta() {}

    public boolean isInstanciaVenta() {
        return instanciaVenta;
    }

    public void setInstanciaVenta(boolean instanciaVenta) {
        this.instanciaVenta = instanciaVenta;
    }
    
    
    
    private void inicializa(){
        //set totales
        this.txtSubTotal.setValue(0);
        this.txtIva.setValue(0);
        this.txtTotal.setValue(0);
        this.txtRecaudacion.setValue(0);
        this.txtVuelto.setValue(0);
        this.lblIdTransaccion.setText("Por Definir");
        //Bloquear Terminal
        this.pnlBuscar.setVisible(false);
        this.pnlPagos.setVisible(false);
        this.pnlCompra.setVisible(false);
        this.pnlValores.setVisible(false);
        
        this.bloqSesion(true);
        //Prepara tabla detalles
        String[] columNamesDetalle = {"Cant.","Código","Descripción","P. Unitario","Descuento","Total"};
        modeloTablaDetalle.setColumnIdentifiers(columNamesDetalle);
        this.tablaDetalle.setModel(modeloTablaDetalle);
        
        //Prepara Tabla pagos
        String[] columNamesPagos = {"Forma Pago","Pagado"};
        modeloTablaPagos.setColumnIdentifiers(columNamesPagos);
//        this.tablaPagos.setModel(modeloTablaPagos);
        
        this.token = UUID.randomUUID().toString().toUpperCase();
        System.out.println("\n\tTOKEN="+this.token+"\n");
        //botonera de acciones
    }
    
    private void bloqSesion(boolean enabled){
        this.onLogin = enabled;
        if(enabled){
            this.txtEntrada.setVisible(false);
            this.txtEntradaOculta.setVisible(true);
            this.txtEntradaOculta.requestFocusInWindow();
            this.pnlEntrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Contraseña Vendedor"));
            this.lblSalida.setText("Terminal Bloqueado");
            
            this.pnlCompra.setVisible(false);
            this.pnlValores.setVisible(false);
        } else {
            this.txtEntradaOculta.setVisible(false);
            this.txtEntrada.setVisible(true);
            this.txtEntrada.requestFocusInWindow();
            this.pnlCompra.setVisible(true);
            this.pnlValores.setVisible(true);
            this.pnlEntrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Cod. Producto"));
            this.lblSalida.setText("");
            
//            this.btnBuscar.setEnabled(true);
        }
    }
    
    private String isTarjeta(String cadena){
        try{
            int indexOf = cadena.indexOf("%");
            System.out.println("indexOf="+indexOf);
            if(indexOf>-1){
//                String nombre = nombre_fiscal.substring(0,nombre_fiscal.indexOf(" "));
//        String apellido = nombre_fiscal.substring(nombre_fiscal.indexOf(" ")+1,nombre_fiscal.length());
                String dt = cadena.substring(cadena.indexOf("%")+1,cadena.length());
                System.out.println("dt="+dt);
                return dt;
            } else {
                return cadena;
            }
        } catch (Exception se){
            System.out.println("Error Tarjeta="+se);
            return cadena;
        }
    }
    
    private void desbloaquear(){
        String clave = Encript.Encriptar(this.isTarjeta(this.txtEntradaOculta.getText()));
        
        if(clave.equals(loginClave)){
            this.txtEntradaOculta.setText("");
            this.bloqSesion(false);
//            this.nuevaTransaccion();
        } else {
            this.bloqSesion(true);
            this.txtEntradaOculta.setText("");
            JOptionPane.showMessageDialog(null,"Su contraseña no coincide con el usuario que ha iniciado sesión en la aplicación", "Inicio de Sesión", JOptionPane.WARNING_MESSAGE);
        }
    }
    
//"Cant.","Código","Descripción","P. Unitario","Descuento","Total"
    private void getProductoByEan(){
        String eanInput = this.txtEntrada.getText();
        productobeans.setEan(eanInput);

        if(!eanInput.isEmpty()){
            if(productobeans.findByEan()){
                int rowValue = this.existenciaTablaDetalle();
                if(rowValue>0){
                    int cantidadActual = Integer.parseInt(this.tablaDetalle.getValueAt(rowValue-1, 0).toString());
                    this.tablaDetalle.setValueAt(cantidadActual+this.cantidad, rowValue-1, 0);
                    
                    int cant = Integer.parseInt(this.tablaDetalle.getValueAt(rowValue-1, 0).toString());
                    this.tablaDetalle.setValueAt(this.calcularTotalNeto(cant), rowValue-1, 5);
                    
                    this.lblSalida.setText(this.tablaDetalle.getValueAt(rowValue-1, 2).toString());
                } else {
                    this.lblSalida.setText(productobeans.getNombre());
                    String datos[]=new String[6];

                    datos[0]= this.cantidad+"";
                    datos[1]= productobeans.getEan();
                    datos[2]= productobeans.getNombre();
                    datos[3]= productobeans.getValorNeto();
                    datos[4]= productobeans.getPorcentajeDescuento()+"%";
                    datos[5]= this.calcularTotalNeto(1)+"";
                    modeloTablaDetalle.addRow(datos);
                }
            } else {
                this.lblSalida.setText("Producto no encontrado...");
            }
        }  
    }
    
    private double calcularTotalNeto(int cant){
        //(neto*cantidad)
        int neto = Integer.parseInt(productobeans.getValorNeto());
        int porcentajeDescuento = Integer.parseInt(productobeans.getPorcentajeDescuento());
        double tot = neto*cant;
        double totalDescuento = (tot*porcentajeDescuento)/100;
        
        double totalNeto=tot-totalDescuento;
        
        System.out.println("velorNeto="+neto);
        System.out.println("porDesceunto="+porcentajeDescuento);
        return Math.round(totalNeto);
    }
    
    private double getSubTotal(){
        double subtotal = 0;
        int i = 0;
        int rows = this.tablaDetalle.getRowCount();//Obtiene el total de filas de la tabla
        
        if(rows>0){
            System.out.println("rows>0");
            
            while(i<rows){
//                boolean res= this.tablaDetalle.getValueAt(i, 1).toString().equals(productobeans.getEan());
                double totalNeto = Double.parseDouble(this.tablaDetalle.getValueAt(i, 5).toString());
                subtotal = subtotal+totalNeto;
                i=i+1;
//                if(res){
//                    System.out.println("if res");
//                    return i;
//                } else {}
            }
            return Math.round(subtotal);
        }
        return 0;
    }
    
    private double getIva(){
        return (this.getSubTotal()*19)/100;
    }
    
    private double getTotal(){
        return Math.round(this.getSubTotal()+this.getIva());
    }
    
    private void removeItemProduct(){
        String eanInput = this.txtEntrada.getText();
        productobeans.setEan(eanInput);
        
        if(!eanInput.isEmpty()){
            int rowValue = this.existenciaTablaDetalle();
            if(rowValue>0){
                int cantidadActual = Integer.parseInt(this.tablaDetalle.getValueAt(rowValue-1, 0).toString());
                this.tablaDetalle.setValueAt(cantidadActual-this.cantidad, rowValue-1, 0);
                this.lblSalida.setText(this.tablaDetalle.getValueAt(rowValue-1, 2).toString()+" [Anulado]");
                
                int cant = Integer.parseInt(this.tablaDetalle.getValueAt(rowValue-1, 0).toString());
                this.tablaDetalle.setValueAt(this.calcularTotalNeto(cant), rowValue-1, 5);
                this.onAnular = false;
                int cantidadActualPost = Integer.parseInt(this.tablaDetalle.getValueAt(rowValue-1, 0).toString());
                if(cantidadActualPost==0){
                    modeloTablaDetalle.removeRow(rowValue-1);
                    this.onAnular = false;
               }
            }
        }
    }
    
    private void search(String query) {
    //Create new table sorter for the table
    TableRowSorter sorter = new TableRowSorter(this.tablaDetalle.getModel());
    //Add row filter to the tablerowsorter (regex)
    sorter.setRowFilter(RowFilter.regexFilter("(?i).*\\Q"+query+"\\E.*") );
    //Apply the results to the output table
    this.tablaDetalle.setRowSorter(sorter);

    }
//    int rowValue=0;
    
    private int existenciaTablaDetalle(){
        int i = 0;
        int rows = this.tablaDetalle.getRowCount();//Obtiene el total de filas de la tabla
        System.out.println("Cantidad filas="+rows);
        
        if(rows>0){
            System.out.println("rows>0");
            
            while(i<rows){
                System.out.println("Ciclo while="+i);
                boolean res= this.tablaDetalle.getValueAt(i, 1).toString().equals(productobeans.getEan());
                i=i+1;
                if(res){
                    System.out.println("if res");
                    return i;
                } else {}
            }
        }
        return 0;
    }
    
    public String obtenerSerial(){
        try {
            Properties propiedades = new Properties();
            propiedades.load(
                    new FileInputStream(path+separador+"serial.properties"));
            String p= propiedades.getProperty(SystemUtils.OS_NAME);
            System.out.println("Prop="+p);
            return p;
        } catch (FileNotFoundException e) {
         System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
         System.out.println("Error, No se puede leer el archivo");
        }
        return null;
    }
    
    private void comandos(){
        String comando = this.txtEntrada.getText();
        switch (comando){
            case "exit":
                dispose();
                break;
            case "bloq":
                this.bloqSesion(true);
                break;
            case "openg":
                this.abrirGaveta();
                break;
            default:
                //enviar texto
                if(this.onAnular){
                    this.removeItemProduct();
                }else if (this.onPagos){
                    
                }else{
                    this.verificaDatosTransaccion();
                    this.getProductoByEan();
                }
                this.txtSubTotal.setValue(this.getSubTotal());
                this.txtIva.setValue(this.getIva());
                this.txtTotal.setValue(this.getTotal());
                break;
        }
        this.txtEntrada.setText("");
    }
    
    private void abrirGaveta(){
        if(SystemUtils.IS_OS_LINUX){
            this.EjecutaAperturaGaveta("/bin/sh "+path+"/./s.sh",null);
        } else if(SystemUtils.IS_OS_WINDOWS){
            this.EjecutaAperturaGaveta("echo p > ",this.obtenerSerial());
        } else {
            System.out.println("No se pudo identificar el SO");
        }
    }
    
    private void EjecutaAperturaGaveta(String cmd, String serial){
        String s = null;
        try{
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                this.msgErrorGaveta();
                System.out.println(s);
            }
        } catch(Exception se){
            System.out.println("de");
            System.out.println(se);
        }
    }
    
    private void msgErrorGaveta(){
        JOptionPane.showMessageDialog(null,"Al parecer no podemos obtener comunicación con la Gaveta de Dinero.\nRealice la apertura manualmente.", "Driver Gaveta", JOptionPane.WARNING_MESSAGE);
    }
    
//    //Registrar Venta
//    int transaccionState= 1;
    boolean isSaveTransaccion = false;
    boolean isSaveDetalle = false;
//    
//    private void transaccion(){
//        this.verificaDatosTransaccion();
//        if(isSaveTransaccion){
//            this.isSaveTransaccion = this.saveDatosTransaccion();
//            this.obtenerIdTransaccion();
//            System.out.println("obtenerIdTransaccion()="+this.txtIdTransaccion);
//        } else {
//            if(this.updateDatosTransaccion()){
//                this.obtenerDatosDetalle();
//            } else {
//                this.transaccionState = 1;
//            }
//        }
////                this.isSaveTransaccion = false;
//    }
//    private void transaccion(){
//        switch (transaccionState) {
//            case 1:
//                this.verificaDatosTransaccion();
//                if(isSaveTransaccion){
//                    if(this.saveDatosTransaccion()){
//                    this.transaccionState = 2;
//                        System.out.println("SetTransaccionState="+this.transaccionState);
//                    } else {
//                        this.transaccionState = 1;
//                        System.out.println("SetTransaccionState="+this.transaccionState);
//                    }
//                } else {
//                    if(this.updateDatosTransaccion()){
//                        this.transaccionState = 2;
//                        System.out.println("SetTransaccionState="+this.transaccionState);
//                    } else {
//                        this.transaccionState = 1;
//                        System.out.println("SetTransaccionState="+this.transaccionState);
//                    }
//                }
//                this.isSaveTransaccion = false;
//                break;
//            case 2:
//                if(this.obtenerIdTransaccion()){
////                    this.transaccionState = 3;
//                } else {
//                    this.transaccionState = 1;
//                }
//                break;
//            case 3:
//                break;
//        }
//    }
    
    private boolean obtenerIdTransaccion(){
        transaccionbeans.setToken(token);
        
        if(transaccionbeans.findByToken()==true){
//            this.txtIdTransaccion=transaccionbeans.getIdTransaccion();
            this.lblIdTransaccion.setText(transaccionbeans.getIdTransaccion());
            return true;
        } else {
//            this.inicializa();
            return false;
        }
    }
    
    private void verificaDatosTransaccion(){
        //DAtos
        String idTransaccion = this.lblIdTransaccion.getText();
        String tipo = this.txtTipoTransaccion;
        String num = this.txtNumTransaccion;
        String fecha = "2015-10-01";
        String totalNeto = this.txtSubTotal.getValue().toString();
        String totalIva = this.txtIva.getValue().toString();
        String total = this.txtTotal.getValue().toString();
        String vuelto = this.txtVuelto.getValue().toString();
        String credito = "0";
        String efectivo = this.txtRecaudacion.getValue().toString();
        String idVendedor = this.loginId;
        String idCliente = null;
        String finalizada = String.valueOf(this.finalizadaTransaccion);
        
        if(idTransaccion.equals("Por Definir")){
            idTransaccion = null;
        }
        
        transaccionbeans.setIdTransaccion(idTransaccion);
        transaccionbeans.setTipo(tipo);
        transaccionbeans.setNum(num);
        transaccionbeans.setFecha(fecha);
        transaccionbeans.setTotalNeto(totalNeto);
        transaccionbeans.setTotalIva(totalIva);
        transaccionbeans.setTotal(total);
        transaccionbeans.setVuelto(vuelto);
        transaccionbeans.setCredito(credito);
        transaccionbeans.setEfectivo(efectivo);
        transaccionbeans.setIdVendedor(idVendedor);
        transaccionbeans.setIdCliente(idCliente);
        transaccionbeans.setFinalizada(finalizada);
        transaccionbeans.setToken(token);
        
        if(!this.isSaveTransaccion){
            System.out.println("Save Transaccion");
            this.isSaveTransaccion = this.saveDatosTransaccion();
        } else {
            System.out.println("Update Transaccion");
            this.updateDatosTransaccion();
        }
        
    }
    
    private boolean saveDatosTransaccion(){
        if(transaccionbeans.save()){
            this.obtenerIdTransaccion();
            System.out.println("ID Transaccion="+this.lblIdTransaccion.getText());
            return true;
        } else {
            System.out.println("[!] "+transaccionbeans.getError());
            return false;
        }
    }
    
    private boolean updateDatosTransaccion(){
        if(transaccionbeans.update()==false){
            System.out.println("[!] "+transaccionbeans.getError());
            return false;
        } else {
            return true;
        }
    }
    
    private void obtenerDatosDetalle(){
        int filas = this.tablaDetalle.getRowCount();
        int i;
        for (i = 0; i < filas; i++) {
            String colCantidad = this.tablaDetalle.getValueAt(i, 0).toString();
            String colEan = this.tablaDetalle.getValueAt(i, 1).toString();
//            String colNombre = (String) this.tablaDetalle.getValueAt(i, 2);
            String colUnitario = this.tablaDetalle.getValueAt(i, 3).toString();
            String colDescuento = this.tablaDetalle.getValueAt(i, 4).toString().replace("%", "");
            String colTotal = this.tablaDetalle.getValueAt(i, 5).toString();
            this.verificarDatosDetalle(colEan, colCantidad, colUnitario, colDescuento, colTotal);
        }
    }
    
    private void verificarDatosDetalle(
            String idProducto,
            String cantidad,
            String unitario,
            String descuento,
            String total){
        String idDetalle = "Por Definir";
        String idTransaccion = this.lblIdTransaccion.getText();
        
        if(idDetalle.equals("Por Definir")){
            idDetalle = null;
        }
        
        detallebeans.setIdDetalle(idDetalle);
        detallebeans.setIdTransaccion(idTransaccion);
        detallebeans.setIdProducto(idProducto);
        detallebeans.setCantidad(cantidad);
        detallebeans.setUnitario(unitario);
        detallebeans.setDescuento(descuento);
        detallebeans.setTotal(total);
        
//        if(!this.isSaveDetalle){
//            System.out.println("Save Detalle");
//            this.isSaveDetalle = this.saveDatosDetalle();
//        } else {
//            System.out.println("Update Detalle");
//            this.updateDatosDetalle();
//        }
        this.verificaDatosTransaccion();
        this.saveDatosDetalle();
        this.isSaveTransaccion = false;
    }
    
    private boolean saveDatosDetalle(){
        if(detallebeans.save()){
            return true;
        } else {
            System.out.println("[!] "+detallebeans.getError());
            return false;
        }
    }
    
    private boolean updateDatosDetalle(){
        if(detallebeans.update()){
            return true;
        } else {
            System.out.println("[!] "+detallebeans.getError());
            return false;
        }
    }
    
//    private boolean existencia(){
//        String nombre = this.txtNombre.getText().toLowerCase();
//        
//        categoriasbeans.setNombre(nombre);
//        
//        if(categoriasbeans.findByName()!=false){
//            JOptionPane.showMessageDialog(null,"La categoŕia \""+nombre+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
////            this.clean();
//            this.btnGuardar.setEnabled(true);
//            this.btnModificar.setEnabled(false);
//            this.btnEliminar.setEnabled(false);
//        } else {
//            return true;
//        }        
//        return false;
//    }
    
    private void formPagoOn(){
        double txtValor=Double.parseDouble(this.txtTotal.getText())-Double.parseDouble(this.txtRecaudacion.getText());
        
        this.lblSalida.setText("Son "+this.numeroletra.Convertir(""+txtValor, false));
        this.pnlPagos.setVisible(true);
        this.onPagos = true;
        this.txtEntradaEfectivo.requestFocusInWindow();
    }
    
    private void formPagoOff(){
        this.lblSalida.setText("");
        this.txtEntradaEfectivo.setText("");
        this.pnlPagos.setVisible(false);
        this.onPagos = false;
        this.txtEntrada.requestFocusInWindow();
    }
    
    private void calcularRecaudacion(){
//        double ingresoRecaudacion = Double.parseDouble(this.txtEntradaEfectivo.getValue().toString());
        double recaudacion = Double.parseDouble(this.txtRecaudacion.getValue().toString());
        
        this.txtRecaudacion.setValue(Math.round(recaudacion+this.getRecaudacion()));
        this.formPagoOff();
    }
    
    private void calcularVuelto(){
        double valorTotal = Double.parseDouble(this.txtTotal.getText());
        double valorRecaudacion = Double.parseDouble(this.txtRecaudacion.getText());
        double vuelto = 0;
        
        if(valorTotal<=valorRecaudacion){
            vuelto = valorRecaudacion-valorTotal;
            this.txtVuelto.setValue(vuelto);
            //Abrir gaveta
            this.obtenerDatosDetalle();
            this.abrirGaveta();
            this.limpiar();
            if(vuelto>0){
                String a = this.lblSalida.getText();
                this.lblSalida.setText(a+" | Ultima venta $"+valorTotal+".-, Vuelto $"+vuelto+".-");
            }
        } else {
            vuelto = valorTotal-valorRecaudacion;
            this.lblSalida.setText("Faltan "+this.numeroletra.Convertir(""+vuelto, false)+" para completar la venta");
        }
    }
    
    private double getRecaudacion(){
        String recaudacion = this.txtEntradaEfectivo.getText();
        try{
            return Double.parseDouble(recaudacion);
        } catch (Exception se){
            System.out.println("Error getRecaudacion");
        }
        return 0;
    }
    
    private void limpiar(){
        this.borrarTabla();
        this.inicializa();
    }
    
    private void borrarTabla(){
       for (int i = 0; i < this.tablaDetalle.getRowCount(); i++) {
           this.modeloTablaDetalle.removeRow(i);
           i-=1;
       }
   }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        pnlValores = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        txtSubTotal = new javax.swing.JFormattedTextField();
        jPanel13 = new javax.swing.JPanel();
        txtIva = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        txtTotal = new javax.swing.JFormattedTextField();
        jPanel15 = new javax.swing.JPanel();
        lblFormaPago = new javax.swing.JLabel();
        txtRecaudacion = new javax.swing.JFormattedTextField();
        jPanel16 = new javax.swing.JPanel();
        txtVuelto = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        lblIdTransaccion = new javax.swing.JLabel();
        pnlEntrada = new javax.swing.JPanel();
        txtEntrada = new javax.swing.JTextField();
        txtEntradaOculta = new javax.swing.JPasswordField();
        lblSalida = new elaprendiz.gui.label.LabelMetric();
        pnlBuscar = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        rbtnNombre = new javax.swing.JRadioButton();
        rbtnReferencia = new javax.swing.JRadioButton();
        rbtnDescripcion = new javax.swing.JRadioButton();
        btnBuscarBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnBuscarCerrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        pnlPagos = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtEntradaEfectivo = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        pnlCompra = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();

        setResizable(true);

        jPanel1.setBorder(new elaprendiz.gui.util.DropShadowBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(180, 503));

        btnCerrar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cerrar.png"))); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/remove-product.png"))); // NOI18N
        jButton4.setText("Anular");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/total.png"))); // NOI18N
        jButton1.setText("Pagos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCerrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlValores.setBorder(new elaprendiz.gui.util.DropShadowBorder());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sub-Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N
        jPanel12.setToolTipText("");

        txtSubTotal.setEditable(false);
        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtSubTotal.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSubTotal)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "I.V.A", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        txtIva.setEditable(false);
        txtIva.setForeground(new java.awt.Color(0, 0, 204));
        txtIva.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtIva.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtIva)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        txtTotal.setEditable(false);
        txtTotal.setForeground(new java.awt.Color(204, 0, 0));
        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtTotal.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTotal)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recaudación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        lblFormaPago.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lblFormaPago.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFormaPago.setText("Forma Pago");

        txtRecaudacion.setEditable(false);
        txtRecaudacion.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtRecaudacion.setFocusable(false);
        txtRecaudacion.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFormaPago, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
            .addComponent(txtRecaudacion)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(lblFormaPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRecaudacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vuelto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        txtVuelto.setEditable(false);
        txtVuelto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtVuelto.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtVuelto)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtVuelto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel4.setText("ID:");

        lblIdTransaccion.setText("Por Definir");

        javax.swing.GroupLayout pnlValoresLayout = new javax.swing.GroupLayout(pnlValores);
        pnlValores.setLayout(pnlValoresLayout);
        pnlValoresLayout.setHorizontalGroup(
            pnlValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlValoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlValoresLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblIdTransaccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlValoresLayout.setVerticalGroup(
            pnlValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlValoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlValoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblIdTransaccion))
                .addContainerGap())
        );

        pnlEntrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Código Barras"));

        txtEntrada.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        txtEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntradaActionPerformed(evt);
            }
        });
        txtEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEntradaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntradaKeyReleased(evt);
            }
        });

        txtEntradaOculta.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtEntradaOculta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEntradaOcultaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlEntradaLayout = new javax.swing.GroupLayout(pnlEntrada);
        pnlEntrada.setLayout(pnlEntradaLayout);
        pnlEntradaLayout.setHorizontalGroup(
            pnlEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntradaLayout.createSequentialGroup()
                .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEntradaOculta, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEntradaLayout.setVerticalGroup(
            pnlEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntradaLayout.createSequentialGroup()
                .addGroup(pnlEntradaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEntradaOculta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        lblSalida.setForeground(new java.awt.Color(0, 0, 0));
        lblSalida.setText("lblSalida");
        lblSalida.setColorDeSombra(new java.awt.Color(102, 102, 102));
        lblSalida.setDistanciaDeSombra(1);
        lblSalida.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N

        pnlBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Producto"));

        rbtnNombre.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        rbtnNombre.setText("Nombre (N)");

        rbtnReferencia.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        rbtnReferencia.setText("Referencia (R)");

        rbtnDescripcion.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        rbtnDescripcion.setText("Descripción (D)");

        btnBuscarBuscar.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        btnBuscarBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscarx.png"))); // NOI18N
        btnBuscarBuscar.setText("Buscar");

        jLabel1.setText("Ingrese el termino de búsqueda y presione enter o haga clic en buscar");

        btnBuscarCerrar.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        btnBuscarCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cerrarx.png"))); // NOI18N
        btnBuscarCerrar.setText("Cerrar");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtnNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnReferencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnDescripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscarBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarCerrar)
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbtnNombre)
                        .addComponent(btnBuscarCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(rbtnReferencia)
                        .addComponent(rbtnDescripcion)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout pnlBuscarLayout = new javax.swing.GroupLayout(pnlBuscar);
        pnlBuscar.setLayout(pnlBuscarLayout);
        pnlBuscarLayout.setHorizontalGroup(
            pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBuscarLayout.setVerticalGroup(
            pnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBuscarLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlPagos.setBorder(javax.swing.BorderFactory.createTitledBorder("Forma de Pago"));

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Monto Recibido $");

        txtEntradaEfectivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtEntradaEfectivo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtEntradaEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEntradaEfectivoKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Ingrese pago en efectivo");
        jLabel3.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEntradaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(210, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEntradaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Efectivo (F1)", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Crédito (F2)", jPanel3);

        javax.swing.GroupLayout pnlPagosLayout = new javax.swing.GroupLayout(pnlPagos);
        pnlPagos.setLayout(pnlPagosLayout);
        pnlPagosLayout.setHorizontalGroup(
            pnlPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPagosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        pnlPagosLayout.setVerticalGroup(
            pnlPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPagosLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCompra.setBorder(javax.swing.BorderFactory.createTitledBorder("Productos"));

        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tablaDetalle);

        javax.swing.GroupLayout pnlCompraLayout = new javax.swing.GroupLayout(pnlCompra);
        pnlCompra.setLayout(pnlCompraLayout);
        pnlCompraLayout.setHorizontalGroup(
            pnlCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCompraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        pnlCompraLayout.setVerticalGroup(
            pnlCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlValores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(pnlValores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEntradaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaKeyReleased
        // TODO add your handling code here:
        //aqui
        
    }//GEN-LAST:event_txtEntradaKeyReleased

    private void txtEntradaOcultaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaOcultaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if(this.onLogin){
                this.desbloaquear();
            } else {
                
            }
        }
    }//GEN-LAST:event_txtEntradaOcultaKeyPressed

    private void txtEntradaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.comandos();
        }
        if(evt.getKeyCode() == KeyEvent.VK_F1) {
//            this.rbtnEfectivo.setSelected(true);
//            this.rbtnCredito.setSelected(false);
        }
        if(evt.getKeyCode() == KeyEvent.VK_F2) {
//            this.rbtnEfectivo.setSelected(false);
//            this.rbtnCredito.setSelected(true);
        }
        
    }//GEN-LAST:event_txtEntradaKeyPressed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.lblSalida.setText("Anulación de productos");
        this.onAnular = true;
        this.txtEntrada.requestFocusInWindow();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        double getTotal = Double.parseDouble(this.txtTotal.getValue().toString());
        if(getTotal==0){
            JOptionPane.showMessageDialog(null,"Primero debe agregar productos para esta venta", "Validar Recaudación", JOptionPane.WARNING_MESSAGE);
            this.formPagoOff();
        } else {
//            this.verificaDatosTransaccion();
            this.formPagoOn();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtEntradaEfectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaEfectivoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.calcularRecaudacion();
            this.calcularVuelto();
        }
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.formPagoOff();
        }
    }//GEN-LAST:event_txtEntradaEfectivoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarBuscar;
    private javax.swing.JButton btnBuscarCerrar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblFormaPago;
    private javax.swing.JLabel lblIdTransaccion;
    private elaprendiz.gui.label.LabelMetric lblSalida;
    private javax.swing.JPanel pnlBuscar;
    private javax.swing.JPanel pnlCompra;
    private javax.swing.JPanel pnlEntrada;
    private javax.swing.JPanel pnlPagos;
    private javax.swing.JPanel pnlValores;
    private javax.swing.JRadioButton rbtnDescripcion;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnReferencia;
    private javax.swing.JTable tabla;
    private javax.swing.JTable tablaDetalle;
    private javax.swing.JTextField txtEntrada;
    private javax.swing.JFormattedTextField txtEntradaEfectivo;
    private javax.swing.JPasswordField txtEntradaOculta;
    private javax.swing.JFormattedTextField txtIva;
    private javax.swing.JFormattedTextField txtRecaudacion;
    private javax.swing.JFormattedTextField txtSubTotal;
    private javax.swing.JFormattedTextField txtTotal;
    private javax.swing.JFormattedTextField txtVuelto;
    // End of variables declaration//GEN-END:variables
}