import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

public class practicaEjerciciosJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/guille";  //?allowMultiQueries=true";  - permite enviar varias sentencias SQL separadas por punto y coma en una sola llamada.
        String user = "gad";
        String contraseña = "gad";
        String crearTablaDep = """
                CREATE TABLE IF NOT EXISTS Dep (
                    NumDep smallint unsigned not null primary key,
                    Nombre varchar(20) not null,
                    Ala varchar(10) not null
                );
                """;
        String crearTablaPer = """
                CREATE TABLE IF NOT EXISTS Persona(
                    DNI char(9) not null primary key,
                    Nombre varchar(40) not null,
                    Apellidos varchar(60) not null,
                    Edad tinyint unsigned not null,
                    NumDep smallint unsigned not null,
                    Foreign key (NumDep) references Dep(NumDep)
                );
                """;
        String consultarPersona = "select p.Nombre, p.Apellidos from Persona p join Dep d On p.numDep = d.numDep Where d.Nombre = 'Ventas'";
        String eliminarPersona = "DELETE FROM Persona WHERE numDep = 2";
        String cambioEdad = "Update Persona set Edad = 18 where Edad < 18";

        try (Connection con = DriverManager.getConnection(url, user, contraseña);
        Statement sentencia = con.createStatement()){
            sentencia.executeUpdate(crearTablaDep);
            sentencia.executeUpdate(crearTablaPer);
            //1.a. Inserta un Departamento con los datos que quieras.
            insertarDep(con, 1, "Informatica", "Norte");
            //1.b. Inserta una Persona con los datos que quieras.
            insertarPersona(con, "12345678A","Juan", "Nauj Auj", 43, 1);
            insertarPersona(con, "34567891B", "Pepe", "Epe", 17, 1);
            insertarPersona(con, "56789123F", "Maria", "del Olmo", 17, 1);
            //1.c. Intenta insertar una persona con un NumDep que no existe, y soluciona dicho problema
            try {
                insertarPersona(con, "23456789D", "Luis", "Suil Liu", 34, 2);
            } catch (SQLException ex) {
                System.out.println("ERROR, el número de departamento no existe: " + ex);
                insertarPersona(con, "23456789D", "Luis", "Suil Liu", 34, 1);
            }
            //1.d. Realiza una consulta que te devuelva el nombre y apellido de las personas
            //     que trabajan en el departamento Ventas
            ResultSet resultado = sentencia.executeQuery(consultarPersona);
            while (resultado.next()){
                System.out.println(resultado.getString("Nombre") + " "
                        + resultado.getString("Apellidos"));
            }
            //esta parte la hice 100% IA, me queda revisar lo que tiene a fondo
            //1.e. Elimina los datos con número de departamento 002.
            try (PreparedStatement ps = con.prepareStatement(eliminarPersona)) {

                ps.setInt(1, 2); // Sustituye el primer ? por el número de departamento 2 (002).

                int filasBorradas = ps.executeUpdate(); // Ejecuta el DELETE y guarda cuántas filas eliminó.

                System.out.println("Personas eliminadas: " + filasBorradas); // Muestra el resultado.
            } // Cierra automáticamente el PreparedStatement.

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertarDep(Connection con, int numDep, String nombre, String ala) throws SQLException{
        String sql = "insert into Dep (NumDep, Nombre, Ala) values (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numDep);
            ps.setString(2, nombre);
            ps.setString(3, ala);
            ps.executeUpdate();
        }
    }

    public static void insertarPersona(Connection con, String dni, String nombre, String apellidos, int edad, int numDep) throws SQLException{
        String sql = "INSERT INTO Persona(DNI, Nombre, Apellidos, Edad, NumDep) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellidos);
            ps.setInt(4, edad);
            ps.setInt(5,numDep);
            ps.executeUpdate();
        }
    }
}