package de.lergin.sponge.laborus.util;

import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.job.JobAction;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.*;

/**
 * class for all the stuff that has to do with blocking the "exploiding" of the jobsystem that has to do with destroying
 * and replacing of blocks
 */
public final class AntiReplaceFarming {
    private final static boolean USE_ANTI_REPLACE = ConfigHelper.getNode("setting", "use_anti_replace").getBoolean(false);

    /**
     * creates a connection with the database
     *
     * @return the DB-Connection
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:./" + JobsMain.instance().getConfigDir().getParent().toString() + "/laborus",
                "",
                ""
        );
    }

    /**
     * tests if the {@link Location<World>} has a database entry with the {@link BlockState} and {@link JobAction}
     *
     * @param loc        the {@link Location<World>} that should be tested
     * @param blockState the {@link BlockState} that the {@link Location<World>} should have
     * @param action     the {@link JobAction} that should be tested
     * @return true if a database entry exist or antiReplaceFarming is deactivated
     */
    public static boolean testLocation(Location<World> loc, BlockState blockState, JobAction action) {
        if (!USE_ANTI_REPLACE)
            return true;

        Connection conn;
        try {
            conn = getConnection();

            PreparedStatement getData = conn.prepareStatement("SELECT id FROM anti_replace_farming WHERE world=? AND x=? AND y=? AND z=? AND jobaction=? AND blockState=? AND time >  TIMESTAMPADD(HOUR, ?, NOW()) LIMIT 1");

            getData.setString(1, loc.getExtent().getUniqueId().toString());
            getData.setInt(2, loc.getBlockX());
            getData.setInt(3, loc.getBlockY());
            getData.setInt(4, loc.getBlockZ());
            getData.setString(5, action.name());
            getData.setString(6, blockState.toString());
            getData.setInt(7, -ConfigHelper.getNode("setting", "antiReplaceTime").getInt(48));

            ResultSet res = getData.executeQuery();

            if (res.next()) {
                conn.close();
                return false;
            } else {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * adds the {@link Location<World>} with the {@link BlockState} and {@link JobAction} to the antiReplaceFarming-DB
     *
     * @param loc        the {@link Location<World>} that should be added
     * @param blockState the {@link BlockState} that was/is at the position
     * @param action     the {@link JobAction} that has happened
     */
    public static void addLocation(Location<World> loc, BlockState blockState, JobAction action) {
        if (!USE_ANTI_REPLACE)
            return;

        Connection conn;
        try {
            conn = getConnection();
            PreparedStatement getData = conn.prepareStatement("INSERT INTO anti_replace_farming(world, x, y, z, blockstate, jobaction) VALUES (?, ?, ?, ?, ?, ?)");

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

    /**
     * creates the DataBase
     */
    public static void setupDataBase() {
        Connection conn = null;
        try {
            conn = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.prepareStatement("DROP TABLE IF EXISTS anti_replace_farming").execute();
                conn.prepareStatement("CREATE TABLE IF NOT EXISTS anti_replace_farming (id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, world CHAR(36) NOT NULL , x INTEGER NOT NULL , y INTEGER NOT NULL , z INTEGER NOT NULL , blockstate VARCHAR(255), jobaction VARCHAR(127), time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP());").execute();
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}