
package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    // A Connection object that connects to the database.
    private Scanner scanner;
   // A Scanner object for reading user input.

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Add Patient Method
    public void addPatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            // It prepares an SQL INSERT statement to add the patient details into the patients table.
            //The ? placeholders will be replaced by the actual values from the user input.
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //This line creates a PreparedStatement object using the connection object and the SQL query string.
            // PreparedStatement is used to execute parameterized SQL queries.
            //connection.prepareStatement(query): This part calls a method on the connection object (likely representing a database connection) to
            // prepare the SQL query specified in the query string.
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
         //This line executes the SQL INSERT statement. executeUpdate returns the number of rows affected by the query,
            // which is stored in the variable affectedRows

            if (affectedRows > 0) {
                System.out.println("Patient Added Successfully!!");
            } else {
                System.out.println("Failed to add Patient!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //View Patient (Just Read Operation --> we will just fetch the data from database and print it)
    public void viewPatients(){
        String query = "select * from patients";
        try{
           PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //This line executes the prepared SQL query and stores the results in a ResultSet object named resultSet.
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }
            //The resultSet.next() method checks if there's a next row and returns true if so,
            // positioning the cursor on that row. Otherwise, it returns false, signaling the end of the loop.


        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    
    //Check Patient is Present or not
    public boolean getPatientById(int id){
        String query = "Select * from patients where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            //Sets the value of the first (and only) placeholder in the SQL query to the value of the id parameter passed to the method.
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                //if(resultSet.next()): Checks if the result set contains at least one row (i.e., a patient with the specified ID exists).
                //resultSet.next(): Moves the cursor to the next row of the result set. If there is a row, it returns true; otherwise, it returns false.
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    return false;

    }


}






