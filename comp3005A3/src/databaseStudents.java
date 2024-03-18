import java.sql.*;

public class dataMain {

    public static void main(String[] args) {
        // Database connection properties
        String url = "jdbc:postgresql://localhost:5432/students";
        String user = "postgres";
        String password = "Mahaveer@2001";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Create the students table
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                        "student_id SERIAL PRIMARY KEY," +
                        "first_name TEXT NOT NULL," +
                        "last_name TEXT NOT NULL," +
                        "email TEXT NOT NULL UNIQUE," +
                        "enrollment_date DATE" +
                        ")");
            }

            // Insert initial data
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO students (first_name, last_name, email, enrollment_date)" +
                        "VALUES" +
                        "('John', 'Doe', 'john.doe@example.com', '2023-09-01')," +
                        "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01')," +
                        "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02')");
            }

            // Define CRUD operations
            getAllStudents(connection);
            addStudent(connection, "Alice", "Johnson", "alice.johnson@example.com", "2023-09-03");
            updateStudentEmail(connection, 1, "john.doe.new@example.com");
            deleteStudent(connection, 3);
            getAllStudents(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getAllStudents(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("student_id") + " | " +
                        resultSet.getString("first_name") + " | " +
                        resultSet.getString("last_name") + " | " +
                        resultSet.getString("email") + " | " +
                        resultSet.getString("enrollment_date"));
            }
        }
    }

    private static void addStudent(Connection connection, String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setDate(4, Date.valueOf(enrollmentDate));
            statement.executeUpdate();
        }
    }

    private static void updateStudentEmail(Connection connection, int studentId, String newEmail) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE students SET email = ? WHERE student_id = ?")) {
            statement.setString(1, newEmail);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }

    private static void deleteStudent(Connection connection, int studentId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
    }
}
