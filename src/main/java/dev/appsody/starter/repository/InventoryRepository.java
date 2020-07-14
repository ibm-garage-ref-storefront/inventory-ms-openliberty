package dev.appsody.starter.repository;


import dev.appsody.starter.model.Inventory;
import dev.appsody.starter.utils.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryRepository {

    /**
     * Method is responsible for retrieving the inventory details from the database.
     * @return List of type Inventory
     */
    public List<Inventory> getInventoryDetails() {

        List<Inventory> inventoryItems = new ArrayList<>();

        JDBCConnection jdbcConnection = new JDBCConnection();

        Connection connection = jdbcConnection.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "select id,stock,price,img_alt,img,name,description from inventorydb.items");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Inventory item = new Inventory();
                item.setId(rs.getLong("id"));
                item.setName(rs.getString("name"));
                item.setStock(rs.getInt("stock"));
                item.setPrice(rs.getInt("price"));
                item.setImg_alt(rs.getString("img_alt"));
                item.setImg(rs.getString("img"));
                item.setDescription(rs.getString("description"));
                inventoryItems.add(item);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inventoryItems;
    }
}
