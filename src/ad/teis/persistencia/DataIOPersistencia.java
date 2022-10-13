/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mfernandez
 */
public class DataIOPersistencia implements IPersistencia {

    @Override
    public void escribirPersona(Persona persona, String ruta) {

        if (persona != null) {

            try ( FileOutputStream fos = new FileOutputStream(ruta);  DataOutputStream dos = new DataOutputStream(fos);) {

                dos.writeLong(persona.getId());
                dos.writeChars(persona.getDni());
                dos.writeUTF(persona.getDni());
                dos.writeInt(persona.getEdad());
                dos.writeFloat(persona.getSalario());

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
        }
    }

    @Override
    public Persona leerDatos(String ruta) {

        long id = 0;
        char caracter;
        String dni = "";
        StringBuilder sb = new StringBuilder();
        String dniUTF = "";
        int edad = 0;
        float salario = 0;
        Persona persona = null;

        try (
                 FileInputStream fis = new FileInputStream(ruta);  DataInputStream dis = new DataInputStream(fis);) {

            id = dis.readLong();

            for (int i = 0; i < 9; i++) {
                caracter = dis.readChar();
                sb.append(caracter);
            }

            dniUTF = dis.readUTF();
            edad = dis.readInt();
            salario = dis.readFloat();

            persona = new Persona(id, sb.toString(), edad, salario);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
        }
        return persona;
    }

}
