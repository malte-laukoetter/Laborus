package de.lergin.sponge.laborus.util;

import de.lergin.sponge.laborus.Laborus;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.*;

/**
 * class for all the stuff that has to do with blocking the "exploiding" of the jobsystem that has to do with destroying
 * and replacing of blocks
 */
public final class AntiReplaceFarming {
    private static boolean USE_ANTI_REPLACE = Laborus.instance().config.base.useAntiReplace;

    public static void setUseAntiReplace(boolean useAntiReplace) {
        USE_ANTI_REPLACE = useAntiReplace;
    }

    /**
     * creates a connection with the database
     *
     * @return the DB-Connection
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:" + Laborus.instance().getConfigDir().getParent().toString() + "/laborus",
                "",
                ""
        );
    }

    /**
     * tests if the {@link Location<World>} has a database entry with the {@link BlockState} and Action
     *
     * @param loc        the {@link Location<World>} that should be tested
     * @param blockState the {@link BlockState} that the {@link Location<World>} should have
     * @param action     the Action that should be tested
     * @return true if a database entry exist or antiReplaceFarming is deactivated
     */
    public static boolean testLocation(Location<World> loc, BlockState blockState, String action) {
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
            getData.setString(5, action);
            getData.setString(6, blockState.toString());
            getData.setInt(7, Laborus.instance().config.base.antiReplaceTime);

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
     * adds the {@link Location<World>} with the {@link BlockState} and Action to the antiReplaceFarming-DB
     *
     * @param loc        the {@link Location<World>} that should be added
     * @param blockState the {@link BlockState} that was/is at the position
     * @param action     the Action that has happened
     */
    public static void addLocation(Location<World> loc, BlockState blockState, String action) {
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
            getData.setString(6, action);

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