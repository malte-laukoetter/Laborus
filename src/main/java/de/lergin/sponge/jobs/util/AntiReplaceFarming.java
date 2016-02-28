package de.lergin.sponge.jobs.util;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.*;

public final class AntiReplaceFarming {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:./" + JobsMain.instance().configDir.getParent().toString() + "/jobs",
                "",
                ""
        );
    }

    public static boolean testLocation(Location<World> loc, JobAction action){
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement getData = conn.prepareStatement("SELECT blockState FROM anti_replace_farming WHERE world=? AND x=? AND y=? AND z=? AND action=? AND time >  TIMESTAMPADD(DAY, -2, NOW())");

            getData.setString(1, loc.getExtent().getUniqueId().toString());
            getData.setInt(2, loc.getBlockX());
            getData.setInt(3, loc.getBlockY());
            getData.setInt(4, loc.getBlockZ());
            getData.setString(5, action.name());

            ResultSet res = getData.executeQuery();

            System.out.println(res);

            if (res.next()) {
                conn.close();
                return false;
            }else{
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void addLocation(Location<World> loc, BlockState blockState, JobAction action){
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement getData = conn.prepareStatement("INSERT INTO anti_replace_farming(world, x, y, z, blockstate, action) VALUES (?, ?, ?, ?, ?, ?)");

            getData.setString(1, loc.getExtent().getUniqueId().toString());
            getData.setInt(2, loc.getBlockX());
            getData.setInt(3, loc.getBlockY());
            getData.setInt(4, loc.getBlockZ());
            getData.setString(5, blockState.toString());
            getData.setString(6, action.name());

            getData.execute();

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
                //TODO stop dropping table in release ;)
                conn.prepareStatement("DROP TABLE IF EXISTS anti_replace_farming").execute();
                conn.prepareStatement("CREATE TABLE IF NOT EXISTS anti_replace_farming (id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, world CHAR(36) NOT NULL , x INTEGER NOT NULL , y INTEGER NOT NULL , z INTEGER NOT NULL , blockstate VARCHAR(255), action VARCHAR(127), time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP());").execute();
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}