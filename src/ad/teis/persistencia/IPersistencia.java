/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;

/**
 *
 * @author mfernandez
 */
public interface IPersistencia {
    
    void escribirPersona(Persona persona, String ruta);
    Persona leerDatos(String ruta);
    
}
