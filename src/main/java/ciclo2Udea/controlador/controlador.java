package ciclo2Udea.controlador;

import ciclo2Udea.modelo.BaseDeDatos;
import ciclo2Udea.modelo.modelo;
import ciclo2Udea.vista.vista1;
import ciclo2Udea.vista.vista2;
import ciclo2Udea.vista.vista3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import vista.vista4;
import vista.vista5;

public class controlador implements ActionListener {

    // Atributos
    modelo model;
    vista1 menu;
    vista2 ingresar;
    vista3 buscar;
    vista4 modificar;
    vista5 listar;
    BaseDeDatos db;

    public controlador() { // constructor
        this.menu = new vista1();
        this.ingresar = new vista2();
        this.buscar = new vista3();
        this.db = new BaseDeDatos();
        this.modificar = new vista4();
        this.listar = new vista5();
        menu.getBtn_ingresar().addActionListener(this);
        menu.getBtn_salir().addActionListener(this);
        menu.getBtn_buscar().addActionListener(this);
        menu.getBtn_modificar().addActionListener(this);
        menu.getBtn_eliminar().addActionListener(this);
        menu.getBtn_listar().addActionListener(this);
        menu.getBtn_listar().addActionListener(this);
        ingresar.getBtn_guardar().addActionListener(this);
        ingresar.getBtn_cancelar().addActionListener(this);
        buscar.getBtn_buscar().addActionListener(this);
        buscar.getBtn_cancelar().addActionListener(this);
        modificar.getBtn_buscar().addActionListener(this);
        modificar.getBtn_modificar().addActionListener(this);
        modificar.getBtn_cancelar().addActionListener(this);
        listar.getAtras().addActionListener(this);
    }

    public void iniciar() throws IOException {
        menu.setVisible(true);
    }

    @Override
    //Darle vida a los botones
    public void actionPerformed(ActionEvent e) {
        // aqui botones del menu

        if (e.getSource() == menu.getBtn_buscar()) {
            buscar.setVisible(true);
            menu.dispose();
        }

        if (e.getSource() == menu.getBtn_eliminar()) {
            int idElim = Integer.parseInt(JOptionPane.showInputDialog(menu, "Ingrese el id del estudiante a eliminar", "Eliminar Estudiante", 2));
            eliminar(idElim);
        }

        if (e.getSource() == menu.getBtn_listar()) {
            listar.setVisible(true);
            listar();
            menu.dispose();
        }

        if (e.getSource() == menu.getBtn_modificar()) {
            modificar.setVisible(true);
            menu.dispose();
        }

        if (e.getSource() == menu.getBtn_ingresar()) {
            ingresar.setVisible(true);
            menu.dispose();
        }
        if (e.getSource() == menu.getBtn_salir()) {
            db.exportarCSV();
            System.exit(0);
        }

        // Aqui los botones de Vista_ingresar
        if (e.getSource() == ingresar.getBtn_guardar()) {
            guardar();
        }

        // aqui los botones de buscar
        if (e.getSource() == buscar.getBtn_buscar()) {
            buscar();
        }
        // aqui los botones de modificar
        if (e.getSource() == modificar.getBtn_buscar()) {
            mod_buscar();
        }

        if (e.getSource() == modificar.getBtn_modificar()) {
            modificar();
        }

        // aqui los botones de cancelar
        if (e.getSource() == ingresar.getBtn_cancelar() || e.getSource() == buscar.getBtn_cancelar() || e.getSource() == modificar.getBtn_cancelar() || e.getSource() == listar.getAtras()) {
            ingresar.dispose();
            buscar.dispose();
            modificar.dispose();
            listar.dispose();
            menu.setVisible(true);
        }

    }

    public void guardar() {
        String id = ingresar.getTxt_cedula().getText();
        String nombre = ingresar.getTxt_nombre().getText();
        String apellido = ingresar.getTxt_apellido().getText();
        String telefono = ingresar.getTxt_telefono().getText();
        String correo = ingresar.getTxt_correo().getText();
        String programa = ingresar.getTxt_programa().getText();

        modelo nuevoEst = new modelo(id, nombre, apellido, telefono, correo, programa);
        db.guardarEstudiante(nuevoEst);
        JOptionPane.showMessageDialog(null, "Estudiante guardado");
        // Limpiar los campos
        ingresar.getTxt_cedula().setText("");
        ingresar.getTxt_nombre().setText("");
        ingresar.getTxt_apellido().setText("");
        ingresar.getTxt_telefono().setText("");
        ingresar.getTxt_correo().setText("");
        ingresar.getTxt_programa().setText("");

    }

    public void buscar() {
        int id = Integer.parseInt(buscar.getTxt_cedula().getText());
        modelo resultado = db.buscarEstudiante(id);
        // limpia los campos
        buscar.getTxt_nombre().setText("");
        buscar.getTxt_apellido().setText("");
        buscar.getTxt_telefono().setText("");
        buscar.getTxt_correo().setText("");
        buscar.getTxt_programa().setText("");

        if (resultado != null) {
            buscar.getTxt_nombre().setText(resultado.getNombre());
            buscar.getTxt_apellido().setText(resultado.getApellido());
            buscar.getTxt_telefono().setText(resultado.getTelefono());
            buscar.getTxt_correo().setText(resultado.getCorreo());
            buscar.getTxt_programa().setText(resultado.getPrograma());
        } else {
            JOptionPane.showMessageDialog(null, "Estudiante no encontrado");
        }


    }

    public void mod_buscar() {
        int id = Integer.parseInt(modificar.getTxt_cedula().getText());
        modelo resultado = db.buscarEstudiante(id);

        if (resultado != null) {
            modificar.getTxt_nombre().setText(resultado.getNombre());
            modificar.getTxt_apellido().setText(resultado.getApellido());
            modificar.getTxt_telefono().setText(resultado.getTelefono());
            modificar.getTxt_correo().setText(resultado.getCorreo());
            modificar.getTxt_programa().setText(resultado.getPrograma());
        } else {
            JOptionPane.showMessageDialog(null, "Estudiante no encontrado");
        }

    }

    public void modificar() {
        int id = Integer.parseInt(modificar.getTxt_cedula().getText());
        String nombre = modificar.getTxt_nombre().getText();
        String apellido = modificar.getTxt_apellido().getText();
        String telefono = modificar.getTxt_telefono().getText();
        String correo = modificar.getTxt_correo().getText();
        String programa = modificar.getTxt_programa().getText();

        if ("".equals(nombre) || "".equals(apellido) || "".equals(telefono) || "".equals(correo) || "".equals(programa)) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
        } else {
            modelo nuevoEst = new modelo(Integer.toString(id), nombre, apellido, telefono, correo, programa);
            db.modificarEst(id, nuevoEst);
            JOptionPane.showMessageDialog(null, "Estudiante modificado");
            // Limpiar los campos
            modificar.getTxt_cedula().setText("");
            modificar.getTxt_nombre().setText("");
            modificar.getTxt_apellido().setText("");
            modificar.getTxt_telefono().setText("");
            modificar.getTxt_correo().setText("");
            modificar.getTxt_programa().setText("");
        }
    }

    public void eliminar(int id) {
        modelo estudiante = db.buscarEstudiante(id);
        if (estudiante != null) {
            db.eliminar(id);
            JOptionPane.showMessageDialog(menu, "Registro eliminado correctamente", "All ok", 0);
        } else {
            JOptionPane.showMessageDialog(menu, "No se encontro el estudiante por el id " + id, "Error", 0);
        }

    }

    public void listar() {
        db.SQLtoList();
        ArrayList<modelo> Estudiantes = db.getLsEstudiantes();
        ArrayList<Object[]> list = new ArrayList();
        for (int i = 0; i < Estudiantes.size(); i++) {
            list.add(new Object[]{
                    Estudiantes.get(i).getId(),
                    Estudiantes.get(i).getNombre(),
                    Estudiantes.get(i).getApellido(),
                    Estudiantes.get(i).getTelefono(),
                    Estudiantes.get(i).getCorreo(),
                    Estudiantes.get(i).getPrograma()
            });
            listar.getjTable1().setModel(new DefaultTableModel(
                    list.toArray(new Object[list.size()][]),
                    new String[]{
                            "ID", "Nombre", "Apellido", "Telefono", "Correo", "Programa"
                    }
            ));
        }

    }

}
