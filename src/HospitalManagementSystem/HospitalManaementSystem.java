package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManaementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password = "@learningsql1234";

    //HospitalManaementSystem: Defines the main class of the project.
    //url, username, password: Constants for connecting to the MySQL database. Replace these with your actual database details.

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("com.mysql.cj.jdbc.Driver"): Loads the MySQL JDBC driver class.
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        //connecting to database
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            //Connection connection = DriverManager.getConnection(url, username, password): Establishes a connection to the MySQL database
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                        //Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid Choice");
                }
            }


        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void bookAppointment(Patient patient ,Doctor doctor , Connection connection,Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (yyyy-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId)&& doctor.getDoctorById(doctorId)){
            // Checks if the patient and doctor exist in the database.
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    //Prepares the SQL query to insert the appointment.
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }
        else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Prepares the SQL query.
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Executes the query and retrieves the result.
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
