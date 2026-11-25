public class practicaEjerciciosJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/guille";  //?allowMultiQueries=true";  - permite enviar varias sentencias SQL separadas por punto y coma en una sola llamada.
        String user = "gad";
        String contraseña = "gad";
        String crearTablaDep = """
                CREATE TABLE IF NOT EXISTS Dep (
                    NumDep smallint unsigned not null primary key,
                    Nombre varchar(20) not null,
                    Ala varchar(10) not null,
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
        String consultarDep = "select * from Dep";

        try (Connection con = DriverManager.getConnection(url, user, contraseña);
        Statement sentencia = con.createStatement()){
            sentencia.executeUpdate(crearTablaDep);
            sentencia.executeUpdate(crearTablaPer);
            insertarDep(con, 1, "informatica", "Norte");
            insertarPersona(con, "12345678A","Juan", "Nauj Auj", 43, 1);
            insertarPersona(con, "23456789D", "Luis", "Suil Liu", 34, 2);
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