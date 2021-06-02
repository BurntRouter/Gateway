package com.gateway.bot.database;

import net.dv8tion.jda.api.entities.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {
    private MySQL mysql;

    public AccountManager(MySQL mysql) {
        this.setMysql(mysql);
    }

    public int getBalance(String userid) throws SQLException {
        int bal = 0;
        PreparedStatement preparedStatement = this.mysql.getStatement("SELECT balance FROM users WHERE userid = ?");
        preparedStatement.setString(1, userid);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            bal = resultSet.getInt("balance");
        }
        resultSet.close();
        preparedStatement.close();
        return bal;
    }

    public void openTicket(String userid, String channelid, int bounty) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("INSERT INTO tickets (userid, channelid, bounty) VALUES (?, ?, ?)");
        preparedStatement.setString(1, userid);
        preparedStatement.setString(2, channelid);
        preparedStatement.setInt(3, bounty);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public MySQL getMysql() {
        return mysql;
    }

    public void setMysql(MySQL mysql) {
        this.mysql = mysql;
    }
}
