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

import beans.ProductoBeans;
import cl.cepardov.encriptar.Decript;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import cl.cepardov.encriptar.Encript;
import java.awt.Color;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author cepardov
 */
public class Venta extends javax.swing.JInternalFrame {
    ProductoBeans productobeans = new ProductoBeans();
    boolean onLogin = false;
    DefaultTableModel modeloTablaDetalle = new DefaultTableModel();
    DefaultTableModel modeloTablaPagos = new DefaultTableModel();
    
    //Datos internos de compra
    int cantidad=0;
    int total=0;
    
    //Datos de usuario
    String loginNombre = null;
    String loginApellido = null;
    String loginUsuario = null;
    String loginClave = null;//Encriptado
    String loginPrivilegio = null;
    /**
     * Creates new form Venta
     * @param nombre
     * @param apellido
     * @param usuario
     * @param clave
     * @param privilegio
     */
    public Venta(String nombre, String apellido, String usuario, String clave, String privilegio) {
        initComponents();
        loginNombre = nombre;
        loginApellido = apellido;
        loginUsuario = usuario;
        loginClave = clave;
        loginPrivilegio = privilegio;
        this.inicializa();
    }
    
    public Venta() {}
    
    private void inicializa(){
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
        this.tablaPagos.setModel(modeloTablaPagos);
        
        //botonera de acciones
//        this.btnSubTotal.setVisible(false);
//        this.btnTotal.setVisible(false);
//        this.btnBuscar.setVisible(false);
//        this.btnPagos.setVisible(false);
//        this.btnCantidad.setVisible(false);
//        this.btnAnular.setVisible(false);
//        this.btnCerrar.setVisible(true);
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
        } else {
            this.bloqSesion(true);
            this.txtEntradaOculta.setText("");
            JOptionPane.showMessageDialog(null,"Su contraseña no coincide con el usuario que ha iniciado sesión en la aplicación", "Inicio de Sesión", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void subTotal(){
        
    }
//"Cant.","Código","Descripción","P. Unitario","Descuento","Total"
    private void getProductoByEan(){
        String ean = this.txtEntrada.getText();
        if(!ean.isEmpty()){
            productobeans.setEan(ean);
            if(productobeans.findByEan()){
                this.lblSalida.setText(productobeans.getNombre());
                String datos[]=new String[6];
                
                datos[0]= this.cantidad+"";
                datos[1]= productobeans.getEan();
                datos[2]= productobeans.getNombre();
                datos[3]= productobeans.getValorNeto();
                datos[4]= productobeans.getPorcentajeDescuento();
                datos[5]= this.total+"";
                modeloTablaDetalle.addRow(datos);
                
                
            }
            
        }
    }
    
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.modeloTablaDetalle);
    private void sds(){
        sorter.setRowFilter(RowFilter.regexFilter("7802820600100"));
        sorter.
        System.out.println("ds"+sorter);
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
            default:
                //enviar texto
                this.getProductoByEan();
                break;
        }
        this.txtEntrada.setText("");
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
        jButton1 = new javax.swing.JButton();
        pnlValores = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        txtSubTotal = new javax.swing.JFormattedTextField();
        jPanel13 = new javax.swing.JPanel();
        txtDescuento = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        txtTotal = new javax.swing.JFormattedTextField();
        jPanel15 = new javax.swing.JPanel();
        lblFormaPago = new javax.swing.JLabel();
        txtRecaudacion = new javax.swing.JFormattedTextField();
        jPanel16 = new javax.swing.JPanel();
        txtVuelto = new javax.swing.JFormattedTextField();
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
        jPanel17 = new javax.swing.JPanel();
        rbtnEfectivo = new javax.swing.JRadioButton();
        rbtnCredito = new javax.swing.JRadioButton();
        btnAddFormaPago = new javax.swing.JButton();
        btnDelFormaPago = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPagos = new javax.swing.JTable();
        pnlCompra = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();

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

        jButton1.setText("jButton1");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(btnCerrar))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCerrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlValores.setBorder(new elaprendiz.gui.util.DropShadowBorder());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sub-Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N
        jPanel12.setToolTipText("");

        txtSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtSubTotal.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N

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

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descuentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        txtDescuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtDescuento.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescuento)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtTotal.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N

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
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlEntrada.setBorder(javax.swing.BorderFactory.createTitledBorder("Código Barras"));

        txtEntrada.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
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
        lblSalida.setDistanciaDeSombra(2);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
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

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione Forma de Pago"));

        rbtnEfectivo.setText("Efectivo (F1)");

        rbtnCredito.setText("Crédito (F2)");

        btnAddFormaPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/add.png"))); // NOI18N

        btnDelFormaPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/remove.png"))); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtnEfectivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnCredito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddFormaPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelFormaPago)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAddFormaPago)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rbtnEfectivo)
                .addComponent(rbtnCredito))
            .addComponent(btnDelFormaPago)
        );

        tablaPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tablaPagos);

        javax.swing.GroupLayout pnlPagosLayout = new javax.swing.GroupLayout(pnlPagos);
        pnlPagos.setLayout(pnlPagosLayout);
        pnlPagosLayout.setHorizontalGroup(
            pnlPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlPagosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        pnlPagosLayout.setVerticalGroup(
            pnlPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPagosLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(pnlBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlPagos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
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
    }//GEN-LAST:event_txtEntradaKeyPressed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.sds();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFormaPago;
    private javax.swing.JButton btnBuscarBuscar;
    private javax.swing.JButton btnBuscarCerrar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDelFormaPago;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblFormaPago;
    private elaprendiz.gui.label.LabelMetric lblSalida;
    private javax.swing.JPanel pnlBuscar;
    private javax.swing.JPanel pnlCompra;
    private javax.swing.JPanel pnlEntrada;
    private javax.swing.JPanel pnlPagos;
    private javax.swing.JPanel pnlValores;
    private javax.swing.JRadioButton rbtnCredito;
    private javax.swing.JRadioButton rbtnDescripcion;
    private javax.swing.JRadioButton rbtnEfectivo;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnReferencia;
    private javax.swing.JTable tabla;
    private javax.swing.JTable tablaDetalle;
    private javax.swing.JTable tablaPagos;
    private javax.swing.JFormattedTextField txtDescuento;
    private javax.swing.JTextField txtEntrada;
    private javax.swing.JPasswordField txtEntradaOculta;
    private javax.swing.JFormattedTextField txtRecaudacion;
    private javax.swing.JFormattedTextField txtSubTotal;
    private javax.swing.JFormattedTextField txtTotal;
    private javax.swing.JFormattedTextField txtVuelto;
    // End of variables declaration//GEN-END:variables
}
