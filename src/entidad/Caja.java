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
public class Caja {
    protected String idCaja;
    protected String idCajaUpdate;
    protected String idUsuario;
    protected String fecha;
    protected String totalDinero;
    protected String totalDocumentos;
    protected String subtotal;
    protected String faltante;
    protected String sobrante;
    protected String error;

    public String getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(String idCaja) {
        this.idCaja = idCaja;
    }

    public String getIdCajaUpdate() {
        return idCajaUpdate;
    }

    public void setIdCajaUpdate(String idCajaUpdate) {
        this.idCajaUpdate = idCajaUpdate;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotalDinero() {
        return totalDinero;
    }

    public void setTotalDinero(String totalDinero) {
        this.totalDinero = totalDinero;
    }

    public String getTotalDocumentos() {
        return totalDocumentos;
    }

    public void setTotalDocumentos(String totalDocumentos) {
        this.totalDocumentos = totalDocumentos;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getFaltante() {
        return faltante;
    }

    public void setFaltante(String faltante) {
        this.faltante = faltante;
    }

    public String getSobrante() {
        return sobrante;
    }

    public void setSobrante(String sobrante) {
        this.sobrante = sobrante;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
}
