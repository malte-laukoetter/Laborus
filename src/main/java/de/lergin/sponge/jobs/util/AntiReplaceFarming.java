package de.lergin.sponge.jobs.util;

import de.lergin.sponge.jobs.JobsMain;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public final class AntiReplaceFarming {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:./" + JobsMain.instance().configDir.getParent().toString() + "/jobs",
                "",
                ""
        );
    }

    public static void myMethodThatQueries() {
        Connection conn = null;
        try {
            conn = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("sad");

        setupDataBase();

        try {
            try {
                    conn.prepareStatement("SELECT * FROM anti_replace_farming;").execute();
            } finally {
                    conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;

    }

    public static boolean testLocation(Location<World> loc){
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement getData = conn.prepareStatement("SELECT blockstate, time FROM anti_replace_farming WHERE world=? AND x=? AND y=? AND z=?;");

            getData.setString(1, loc.getExtent().getUniqueId().toString());
            getData.setInt(2, loc.getBlockX());
            getData.setInt(3, loc.getBlockY());
            getData.setInt(4, loc.getBlockZ());

            getData.execute();

            conn.close();
            //TODO: do something with the data ;)
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void addLocation(Location<World> loc){
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement getData = conn.prepareStatement("INSERT INTO anti_replace_farming(world, x, y, z, blockstate) VALUES (?, ?, ?, ?, ?);");

            getData.setString(1, loc.getExtent().getUniqueId().toString());
            getData.setInt(2, loc.getBlockX());
            getData.setInt(3, loc.getBlockY());
            getData.setInt(4, loc.getBlockZ());
            getData.setString(5, loc.getBlock().toString());

            System.out.println(getData.execute());

            conn.commit();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setupDataBase(){
        Connection conn = null;
        try {
            conn = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.prepareStatement("CREATE TABLE anti_replace_farming (id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, world VARCHAR(255) NOT NULL , x INTEGER NOT NULL , y INTEGER NOT NULL , z INTEGER NOT NULL , blockstate VARCHAR(255), time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP());").execute();
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}