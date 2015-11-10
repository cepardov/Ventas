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
package entidad;

/**
 *
 * @author cepardov
 */
public class Transaccion {
    protected String idTransaccion;
    protected String idTransaccionUpdate;
    protected String tipo;
    protected String num;
    protected String fecha;
    protected String totalNeto;
    protected String totalIva;
    protected String total;
    protected String vuelto;
    protected String credito;
    protected String efectivo;
    protected String idVendedor;
    protected String idCliente;
    protected String finalizada;
    protected String token;
    protected String error;

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getIdTransaccionUpdate() {
        return idTransaccionUpdate;
    }

    public void setIdTransaccionUpdate(String idTransaccionUpdate) {
        this.idTransaccionUpdate = idTransaccionUpdate;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(String totalNeto) {
        this.totalNeto = totalNeto;
    }

    public String getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(String totalIva) {
        this.totalIva = totalIva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVuelto() {
        return vuelto;
    }

    public void setVuelto(String vuelto) {
        this.vuelto = vuelto;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(String finalizada) {
        this.finalizada = finalizada;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
}
