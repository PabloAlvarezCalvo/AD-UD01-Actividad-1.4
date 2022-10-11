/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ad.teis;

import ad.teis.model.Persona;
import ad.teis.persistencia.DataIOPersistencia;
import ad.teis.persistencia.RandomAccessPersistencia;
import java.util.ArrayList;

/**
 *
 * @author a21pabloac1
 */
public class Acvitividad14 {
    
    private static final String RUTA_PERSONA = "persona.dat";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataIOPersistencia dataIOPersistencia = new DataIOPersistencia();
        Persona persona1 = new Persona(1, "12345678A", 18, 1000.0f);
        Persona persona2 = new Persona(2, "12345678B", 19, 2000.0f);
        Persona persona3 = new Persona(3, "12345678C", 20, 3000.0f);
        
        dataIOPersistencia.escribirPersona(persona1, RUTA_PERSONA);
        Persona ps1 = dataIOPersistencia.leerDatos(RUTA_PERSONA);
        System.out.println("Se ha recuperado: " + ps1);

               
        //Tarea 1.4
        RandomAccessPersistencia random = new RandomAccessPersistencia();
        ArrayList<Persona> personas = new ArrayList<>();
        ArrayList<Persona> personasRecuperadas = new ArrayList<>();
        
        personas.add(persona1);
        personas.add(persona2);
        personas.add(persona3);
        random.escribirPersonas(personas, RUTA_PERSONA);
        
        personasRecuperadas = random.leerTodo(RUTA_PERSONA);
        
        int pos = 0;
        Persona psrecuperada = random.leerPersona(pos, RUTA_PERSONA);
        if (psrecuperada != null) {
            System.out.printf("Se ha recuperado la persona %s en la posicion %d \n", psrecuperada, pos);
        } else {
            System.out.println("Fallo al recuperar");
        }
        
    }

}
