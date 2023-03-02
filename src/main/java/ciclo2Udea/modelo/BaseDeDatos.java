package ciclo2Udea.modelo;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class BaseDeDatos {

    private ArrayList<modelo> lsEstudiantes;
    public static final String URL = "jdbc:mysql://localhost:3306/ciclo2";
    public static final String USER = "root";
    public static final String CLAVE = "";

    public BaseDeDatos() {
        this.lsEstudiantes = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
            Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS estudiantes ("
                    + "cedula INT(12) NOT NULL,"
                    + "nombre VARCHAR(50) NOT NULL,"
                    + "apellido VARCHAR(50) NOT NULL,"
                    + "telefono VARCHAR(50) NOT NULL,"
                    + "correo VARCHAR(50) NOT NULL,"
                    + "programa VARCHAR(50) NOT NULL,"
                    + "PRIMARY KEY (cedula)"
                    + ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla creada");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla");
        }
    }

    public void guardarEstudiante(modelo estudainte) {
        int id = Integer.parseInt(estudainte.getId());
        String sql = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
             Statement stmt = conn.createStatement()) {
            sql = "INSERT INTO estudiantes (cedula, nombre, apellido, telefono, correo, programa) VALUES ("
                    + id + ", '"
                    + estudainte.getNombre() + "', '"
                    + estudainte.getApellido() + "', '"
                    + estudainte.getTelefono() + "', '"
                    + estudainte.getCorreo() + "', '"
                    + estudainte.getPrograma() + "');";
            stmt.executeUpdate(sql);
            System.out.println("Estudiante guardado");
        } catch (SQLException e) {
            System.out.println(sql);
            System.out.println("Error al guardar el estudiante");
            System.out.println(e.getMessage());
        }


    }

    public modelo buscarEstudiante(int id) {
        modelo result = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM estudiantes WHERE cedula = " + id + ";";
            var rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String programa = rs.getString("programa");
                result = new modelo(Integer.toString(id), nombre, apellido, telefono, correo, programa);
                System.out.println("Estudiante encontrado");
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el estudiante");
        }
        return result;
    }

    public void modificarEst(String id, modelo est) {
        for (int i = 0; i < this.lsEstudiantes.size(); i++) {
            if (this.lsEstudiantes.get(i).getId().equals(id)) {
                this.lsEstudiantes.get(i).setNombre(est.getNombre());
                this.lsEstudiantes.get(i).setApellido(est.getApellido());
                this.lsEstudiantes.get(i).setTelefono(est.getTelefono());
                this.lsEstudiantes.get(i).setCorreo(est.getCorreo());
                this.lsEstudiantes.get(i).setPrograma(est.getPrograma());
                System.out.println("Estudiante modificado");
            }
        }
    }

    public void eliminar(int id) {
        for (int i = 0; i < this.lsEstudiantes.size(); i++) {
            if (this.lsEstudiantes.get(i).getId().equals(id)) {
                this.lsEstudiantes.remove(i);
                System.out.println("Estudiante eliminado");
            }
        }
    }

    // Trabajamos con archivos .dat
    public void guardarArchivo() {
        try {
            FileOutputStream archivo = new FileOutputStream("src\\main\\estudiantes.dat"); //crea el archivo externo
            ObjectOutputStream salida = new ObjectOutputStream(archivo); // crea un lector de objetos que lee el archivo externo

            salida.writeObject(this.lsEstudiantes);
            salida.close();
            archivo.close();
//            for (int i = 0; i < this.lsEstudiantes.size(); i++) { // recorre la lista de estudiantes
//                modelo est = this.lsEstudiantes.get(i); // obtiene el estudiante de la lista
//                salida.writeObject(est); // escribe el estudiante en el archivo externo
//            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo crear o encontrar el archivo");
        } catch (IOException e) {
            System.out.println("Hubo un error en el sistema");
            e.printStackTrace();
        }
    }

    public void recuperarArchivo() throws ClassNotFoundException  {
        try {
            FileInputStream archivo = new FileInputStream("src\\main\\estudiantes.dat");
            ObjectInputStream entrada = new ObjectInputStream(archivo);

            this.lsEstudiantes = (ArrayList) entrada.readObject();
            entrada.close();
            archivo.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo crear o encontrar el archivo");
        } catch (IOException e) {
            System.out.println("Hubo un error en el sistema");
        }
//        catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    // Trabajamos con archivos .csv
    public void guardarCVS() {
        String csvFileName = "archivoCSV.csv";
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] procesador = new CellProcessor[]{
            new NotNull(), //ID
            new NotNull(), //Nombre
            new NotNull(), //Apellido
            new NotNull(), //Telefono
            new NotNull(), //Correo
            new NotNull(), //Programa
        };
        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"ID", "Nombre", "Apellido", "Telefono", "Correo", "Programa"};
            beanWriter.writeHeader(header);

            for (modelo estudiantes : this.lsEstudiantes) {
                beanWriter.write(estudiantes, header, procesador);
            }

            System.out.println("Archivo CSV creado");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo CSV");
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el archivo CSV");
                }
            }
        }
    }

    public void leerCSV() throws FileNotFoundException {
        String line1 = null; // Ignorando la primera linea
        String splitBy = ","; // Delimitador es una coma
        try {
            BufferedReader br = new BufferedReader(new FileReader("archivoCSV.csv"));
            br.readLine();
            while ((line1 = br.readLine()) != null) {
                String[] estudiante = line1.split(splitBy);
                String ID = estudiante[0];
                String nombre = estudiante[1];
                String apellido = estudiante[2];
                String telefono = estudiante[3];
                String correo = estudiante[4];
                String programa = estudiante[5];
                modelo student = new modelo(ID, nombre, apellido, telefono, correo, programa);
                this.lsEstudiantes.add(student);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "BaseDeDatos{" + "lsEstudiantes=" + lsEstudiantes + '}';
    }

    public ArrayList<modelo> getLsEstudiantes() {
        return lsEstudiantes;
    }

    public void setLsEstudiantes(ArrayList<modelo> lsEstudiantes) {
        this.lsEstudiantes = lsEstudiantes;
    }
}
