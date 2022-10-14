/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author a21pabloac1
 */
public class RandomAccessPersistencia implements IPersistencia {
    
    private static final boolean MODO_UTF = false;
    
    private static final int MAX_LENGTH_NOMBRE = 100;
    
    private static final int BYTES_ID = 8;
    private static final int BYTES_DNI = 18;
    private static final int BYTES_EDAD = 4;
    private static final int BYTES_SALARIO = 4;
    private static final int BYTES_BORRADO = 1;
    private static final int BYTES_NOMBRE = MAX_LENGTH_NOMBRE * 2;
    
    private static final int OFFSET_ID = 0; //0, [0 - 7]
    private static final int OFFSET_DNI = OFFSET_ID + BYTES_ID; //0 + 8 = 8, [8 - 25]
    private static final int OFFSET_EDAD = OFFSET_DNI + BYTES_DNI; //8 + 18 = 26, [26 - 29]
    private static final int OFFSET_SALARIO = OFFSET_EDAD + BYTES_EDAD; //26 + 4 = 30, [30 - 33]
    private static final int OFFSET_BORRADO = OFFSET_SALARIO + BYTES_SALARIO; //30 + 4 = 34, [34]
    private static final int OFFSET_NOMBRE = OFFSET_BORRADO + BYTES_BORRADO ; //34 + 1 = 35, [35 - 235]
    
    private static final int LONG_BYTES_PERSONA = BYTES_ID + BYTES_DNI + BYTES_EDAD + BYTES_SALARIO + BYTES_BORRADO + BYTES_NOMBRE;

    @Override
    public void escribirPersona(Persona persona, String ruta) {

        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "rw");
            )
        {
            raf.writeLong(persona.getId());
            StringBuilder sb = new StringBuilder(persona.getDni());
            sb.setLength(9);
            raf.writeChars(sb.toString());   
            raf.writeInt(persona.getEdad());
            raf.writeFloat(persona.getSalario());
            raf.writeBoolean(persona.isBorrado());
            
            sb = new StringBuilder(persona.getNombre());
            sb.setLength(100);
            if (MODO_UTF){
                raf.writeUTF(sb.toString());
            } else {
                raf.writeChars(sb.toString());
            }
            

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
    }

    @Override
    public Persona leerDatos(String ruta) {

        long id = 0;
        String dni = "";
        int edad = 0;
        float salario = 0;
        boolean borrado;
        String nombre;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;
        try (
                 RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            )
        {

            id = raf.readLong();
            for (int i = 0; i <= 8; i++) {
                sb.append(raf.readChar());
            }

            dni = sb.toString();

            edad = raf.readInt();
            salario = raf.readFloat();
            borrado = raf.readBoolean();
            
            if (MODO_UTF){
                //Original
                nombre = raf.readUTF();
            } else {
                //Con chars
                sb = new StringBuilder();
                for (int i = 0; i < 100; i++) {
                    sb.append(raf.readChar());
                }
                nombre = sb.toString();
            }

            persona = new Persona(id, dni, edad, salario, nombre);
            persona.setBorrado(borrado);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return persona;

    }

    public void escribirPersonas(ArrayList<Persona> personas, String ruta) {
        long longitudBytes = 0;
        if (personas != null) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(ruta, "rw");
                )
            {

                longitudBytes = raf.length();
                raf.seek(longitudBytes);
                for (Persona persona : personas) {
                    raf.writeLong(persona.getId());
                    StringBuilder sb = new StringBuilder(persona.getDni());
                    sb.setLength(9);
                    raf.writeChars(sb.toString());
                    //raf.writeUTF(sb.toString());

                    raf.writeInt(persona.getEdad());
                    raf.writeFloat(persona.getSalario());
                    raf.writeBoolean(persona.isBorrado());
                    sb = new StringBuilder(persona.getNombre());
                    sb.setLength(100);
                    if (MODO_UTF){
                        raf.writeUTF(sb.toString());
                    } else {
                        raf.writeChars(sb.toString());
                    }
                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Se ha producido una excepción: " + ex.getMessage());
            }
        }

    }

    public ArrayList<Persona> leerTodo(String ruta) {
        long id ;
        String dni ;
        int edad;
        float salario;
        boolean borrado;
        String nombre;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;
        ArrayList<Persona> personas = new ArrayList<>();
        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            )
        {

            do {
                id = raf.readLong();
                sb = new StringBuilder();
                for (int i = 0; i <= 8; i++) {
                    sb.append(raf.readChar());
                }

                dni = sb.toString();

                edad = raf.readInt();
                salario = raf.readFloat();
                borrado = raf.readBoolean();
                if (MODO_UTF){
                //Original
                    nombre = raf.readUTF();
                } else {
                    //Con chars
                    sb = new StringBuilder();
                    for (int i = 0; i < 100; i++) {
                        sb.append(raf.readChar());
                    }
                    nombre = sb.toString();
                }


                persona = new Persona(id, dni, edad, salario, nombre);
                persona.setBorrado(borrado);
                personas.add(persona);

            } while (raf.getFilePointer() < raf.length());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return personas;

    }

    public Persona leerPersona(int posicion, String ruta) {
        long id = 0;
        String dni = "";
        int edad = 0;
        float salario = 0;
        boolean borrado;
        String nombre;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;

        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            )
        {

            raf.seek(convertPositionToBytes(posicion));
            id = raf.readLong();
            for (int i = 0; i <= 8; i++) {
                sb.append(raf.readChar());
            }

            dni = sb.toString();

            edad = raf.readInt();
            salario = raf.readFloat();
            borrado = raf.readBoolean();
            
            
            if (MODO_UTF){
                //Original
                nombre = raf.readUTF();
            } else {
                //Con chars
                sb = new StringBuilder();
                for (int i = 0; i < 100; i++) {
                    sb.append(raf.readChar());
                }
                nombre = sb.toString();
            }

            persona = new Persona(id, dni, edad, salario, nombre);
            persona.setBorrado(borrado);

        } catch (EOFException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }

        return persona;
    }

    private long convertPositionToBytes(int posicion) {
        if (posicion == 0) {
            return posicion;
        } else {
            return LONG_BYTES_PERSONA * (posicion - 1);
        }
    }

    public Persona add(int posicion, String ruta, Persona persona) {
        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "rw");
            )
        {

            raf.seek(convertPositionToBytes(posicion));

            raf.writeLong(persona.getId());
            StringBuilder sb = new StringBuilder(persona.getDni());
            sb.setLength(9);
            raf.writeChars(sb.toString());

            raf.writeInt(persona.getEdad());
            raf.writeFloat(persona.getSalario());
            raf.writeBoolean(persona.isBorrado());
            sb = new StringBuilder(persona.getNombre());
            sb.setLength(100);
            if (MODO_UTF){
                raf.writeUTF(sb.toString());
            } else {
                raf.writeChars(sb.toString());
            }

        } catch (FileNotFoundException ex) {
            persona = null;
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        } catch (IOException ex) {
            persona = null;
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }
        return persona;
    }
    
    public float sumarSalario(int posicion, String ruta, float incremento) {
        //Debe situarse en la Persona que ocupa la posicion indicada
        //(mismas consideraciones que en los apartados anteriores con posición), 
        //leer su salario e incrementarlo en la cantidad incremento.
        //Devuelve el nuevo salario.
        float nuevoSalario;
        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "rw");
            )
        {

            //(0) long id (8 bytes)
            //(4) String dni (9x2 bytes);
            //(22) int edad (4 bytes);
            //(26) float salario (4 bytes);
            raf.seek(convertPositionToBytes(posicion) + OFFSET_SALARIO);
            nuevoSalario = raf.readFloat() + incremento;
            
            raf.seek(convertPositionToBytes(posicion) + OFFSET_SALARIO);
            raf.writeFloat(nuevoSalario);
            
            return nuevoSalario;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
        }

        return -1;
    }
    
    public boolean borrar(int posicion, String ruta, boolean borrado){
        //En lugar de borrar un registro físicamente, vamos a marcarlo como borrado 
        //con un campo booleano indicando si está borrado o no. Para ello,
        //tendremos que añadir un atributo borrado a la clase Persona y
        //modificar los métodos que hemos creado para escribir y leer ese campo.
        //Tendremos que tener en cuenta que se modifica la longitud en bytes
        //del registro de una objeto Persona. El método borrar buscará en el
        //fichero indicado por ruta, la Persona en la posición indicada en el
        //parámetro y establecerá el valor borrado en el fichero.
        boolean exito = false;
        
        try ( RandomAccessFile raf = new RandomAccessFile(ruta, "rw");){
            raf.seek(convertPositionToBytes(posicion) + OFFSET_BORRADO);
            raf.writeBoolean(borrado);

            
        } catch (IOException e) {
            
        }
        return false;
    }
    
    public Persona leerPersonaDebug(int posicion, String ruta) {
        long id = 0;
        String dni = "";
        int edad = 0;
        float salario = 0;
        boolean borrado;
        String nombre;
        StringBuilder sb = new StringBuilder();
        Persona persona = null;

        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            )
        {

            raf.seek(posicion);
            id = raf.readLong();
            for (int i = 0; i <= 8; i++) {
                sb.append(raf.readChar());
            }

            dni = sb.toString();

            edad = raf.readInt();
            salario = raf.readFloat();
            borrado = raf.readBoolean();
            
            
            if (MODO_UTF){
                //Original
                nombre = raf.readUTF();
            } else {
                //Con chars
                sb = new StringBuilder();
                for (int i = 0; i < 100; i++) {
                    sb.append(raf.readChar());
                }
                nombre = sb.toString();
            }   

            persona = new Persona(id, dni, edad, salario, nombre);
            persona.setBorrado(borrado);

        } catch (EOFException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Se ha producido una excepción: " + ex.getMessage());
        }

        return persona;
    }
}
