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
    public static final String URL = "jdbc:mysql://mintic.mysql.database.azure.com:3306/ciclo2?useSSL=true";
    public static final String USER = "estudiante";
    public static final String CLAVE = "mintic2023.";



    //LOCALHOTS LOCAL
    //public static final String URL = "jdbc:mysql://localhost:3306/ciclo2";
    //public static final String USER = "root";
    //public static final String CLAVE = "";

    public BaseDeDatos() {
        this.lsEstudiantes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
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

    public void modificarEst(int id, modelo est) {
        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE estudiantes SET "
                    + "nombre = '" + est.getNombre() + "', "
                    + "apellido = '" + est.getApellido() + "', "
                    + "telefono = '" + est.getTelefono() + "', "
                    + "correo = '" + est.getCorreo() + "', "
                    + "programa = '" + est.getPrograma() + "' "
                    + "WHERE cedula = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.println("Estudiante modificado");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al modificar el estudiante");
        }
    }

    public void eliminar(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM estudiantes WHERE cedula = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.println("Estudiante eliminado");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el estudiante");
        }
    }

    public void SQLtoList() {
        ArrayList<modelo> temporal = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM estudiantes;";
            var rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String programa = rs.getString("programa");
                modelo est = new modelo(Integer.toString(id), nombre, apellido, telefono, correo, programa);
                temporal.add(est);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al cargar la lista");
        }
        this.lsEstudiantes = temporal;
    }

    public void exportarCSV() {
        String csvFilePath = "Infoestudiantes.csv";

        try (Connection conn = DriverManager.getConnection(URL, USER, CLAVE);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM estudiantes;";
            var rs = stmt.executeQuery(sql);
            BufferedWriter lapiz = new BufferedWriter(new FileWriter(csvFilePath));

            lapiz.write("cedula,nombre,apellido,telefono,correo,programa");

            while (rs.next()) {
                int id = rs.getInt("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String programa = rs.getString("programa");
                lapiz.newLine();
                lapiz.write(id + "," + nombre + "," + apellido + "," + telefono + "," + correo + "," + programa);
            }
            lapiz.close();
            rs.close();
            stmt.close();

            System.out.println("Exportado a CSV exitosamente");
        } catch (SQLException | IOException e) {
            System.out.println("Error al exportar a CSV");
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
