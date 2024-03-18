import java.sql.*;

public class databaseStudents {

    public static void main(String[] args) {
        // Database connection properties
        String url = "jdbc:postgresql://localhost:5432/students";
        String user = "postgres";
        String password = "tanayShah";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            //Drop the Table if it already existed, allows the code to run without recreating the database
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE IF EXISTS students");
            }
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

            // Define CRUD operations
            addStudent(connection, "John", "Doe", "john.doe@example.com", "2023-09-01");
            addStudent(connection, "Jane", "Smith", "jane.smith@example.com", "2023-09-01");
            addStudent(connection, "Jim", "Beam", "jim.beam@example.com", "2023-09-02");
//            getAllStudents(connection);
            addStudent(connection, "Alice", "Johnson", "alice.johnson@example.com", "2023-09-03");
            updateStudentEmail(connection, 1, "john.doe.new@example.com");
            deleteStudent(connection, 3);
            getAllStudents(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using the command  SELECT * FROM students  it will print all the students in the database
     * @param connection
     * @throws SQLException
     */
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

    /**
     *
     * Adds a student using INSERT INTO
     * @param connection
     * @param firstName -- Name of student
     * @param lastName -- last name of the student
     * @param email -- email id of the student
     * @param enrollmentDate -- date of enrollment
     * @throws SQLException
     */
    private static void addStudent(Connection connection, String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setDate(4, Date.valueOf(enrollmentDate));
            statement.executeUpdate();
        }
    }

    /**
     * Updates the emailid of the student whos id is passed in the function
     * @param connection
     * @param studentId
     * @param newEmail
     * @throws SQLException
     */
    private static void updateStudentEmail(Connection connection, int studentId, String newEmail) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE students SET email = ? WHERE student_id = ?")) {
            statement.setString(1, newEmail);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }

    /**
     * deletes the student with the given student ID
     * @param connection
     * @param studentId
     * @throws SQLException
     */
    private static void deleteStudent(Connection connection, int studentId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?")) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
    }
}
