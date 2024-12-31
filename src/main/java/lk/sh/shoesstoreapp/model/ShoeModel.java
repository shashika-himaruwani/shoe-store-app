package lk.sh.shoesstoreapp.model;


import lk.sh.shoesstoreapp.db.DBConnection;
import lk.sh.shoesstoreapp.dto.ShoeDto;
import lk.sh.shoesstoreapp.tm.ShoeTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoeModel {

    public static boolean saveShoe(ShoeDto shoeDto){
        try{
            //object  return
            Connection connection = DBConnection.getDBConnection().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("insert into shoes values (?,?,?,?,?,?)");
            // Set the placeholder .
            preparedStatement.setObject(1, shoeDto.getId());
            preparedStatement.setObject(2,shoeDto.getBrand());
            preparedStatement.setObject(3,shoeDto.getGender());
            preparedStatement.setObject(4,shoeDto.getSize());
            preparedStatement.setObject(5,shoeDto.getStock_quantity());
            preparedStatement.setObject(6,shoeDto.getPrice());

            int saved = preparedStatement.executeUpdate();

            if (saved>0){
                return true;
            }
            else {
                return false;
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static String updateShoe(ShoeDto shoeDto){

        try {
            //object ekk return wenne
            Connection connection = DBConnection.getDBConnection().getConnection();

            // Write sql query to update
            PreparedStatement preparedStatement = connection.prepareStatement("update shoes set brand=? , gender=? , size=? , stock_quantity =? , price=?  where id=?");



            preparedStatement.setObject(1,shoeDto.getBrand());
            preparedStatement.setObject(2,shoeDto.getGender());
            preparedStatement.setObject(3,shoeDto.getSize());
            preparedStatement.setObject(4,shoeDto.getStock_quantity());
            preparedStatement.setObject(5,shoeDto.getPrice());

            preparedStatement.setObject(6, shoeDto.getId());

            // Execute the query and get the number of affected rows.
            int i = preparedStatement.executeUpdate();

            // Check if the insertion was successful.
            if (i > 0) {
                return "Data Updated";



            }
            else {
                return "Data Not Updated";
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean deleteShoe(int id){
        ShoeDto shoeDto = null;
        try {
            boolean status = false;

            Connection connection = DBConnection.getDBConnection().getConnection();

            // Write SQL query for deleting a record
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shoes WHERE id=?");

            preparedStatement.setObject(1, id);

            int i = preparedStatement.executeUpdate();

            if (i > 0) {
                status=true;
            }
            return status;


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<ShoeTM> getAllShoe(){

        try {

            Connection connection = DBConnection.getDBConnection().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("select * from shoes ");


            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<ShoeTM> tms = new ArrayList<>();

            while (resultSet.next()) {

                tms.add(new ShoeTM(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getDouble(6))
                );

            }

            return tms;


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ShoeDto searchShoeById(int id){
        try {
            ShoeDto shoeDto = null;

            Connection connection = DBConnection.getDBConnection().getConnection();

            //write sql query...
            PreparedStatement preparedStatement = connection.prepareStatement("select * from shoes where id=?");
            preparedStatement.setObject(1, id); //

            ResultSet resultSet = preparedStatement.executeQuery(); // data get


            while (resultSet.next()){
                shoeDto = new ShoeDto(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getDouble(6)
                );
            }
            return shoeDto;

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<ShoeDto> getAllShoes() {
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shoes");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<ShoeDto> shoes = new ArrayList<>();
            while (resultSet.next()) {
                shoes.add(new ShoeDto(
                        resultSet.getInt("id"),
                        resultSet.getString("brand"),
                        resultSet.getString("gender"),
                        resultSet.getInt("size"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getDouble("price")
                ));
            }
            return shoes;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}