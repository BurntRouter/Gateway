package com.gateway.bot.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void openTicket(String userid, String channelid) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("INSERT INTO tickets (userid, channelid) VALUES (?, ?)");
        preparedStatement.setString(1, userid);
        preparedStatement.setString(2, channelid);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void updateBalance(String userid, int change) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("UPDATE users SET balance = balance + ? WHERE userid = ?");
        preparedStatement.setInt(1, change);
        preparedStatement.setString(2, userid);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public String getTicketOwner(String channelid) throws SQLException {
        String userid = null;
        PreparedStatement preparedStatement = this.mysql.getStatement("SELECT userid FROM tickets WHERE channelid = ?");
        preparedStatement.setString(1, channelid);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            userid = resultSet.getString("userid");
        }
        resultSet.close();
        preparedStatement.close();
        return userid;
    }

    public void closeTicket(String channelid) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("DELETE FROM tickets WHERE channelid = ?");
        preparedStatement.setString(1, channelid);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void setFreelancer(String channelid, String freelancerid) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("UPDATE tickets SET freelancer = ? WHERE channelid = ?");
        preparedStatement.setString(1, freelancerid);
        preparedStatement.setString(2, channelid);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public String getFreelancer(String channelid) throws SQLException {
        String freelancer = null;
        PreparedStatement preparedStatement = this.mysql.getStatement("SELECT freelancer FROM tickets WHERE channelid = ?");
        preparedStatement.setString(1, channelid);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            freelancer = resultSet.getString("freelancer");
        }
        resultSet.close();
        preparedStatement.close();
        return freelancer;
    }

    public int getBounty(String channelid) throws SQLException {
        int bounty = 0;
        PreparedStatement preparedStatement = this.mysql.getStatement("SELECT bounty FROM tickets WHERE channelid = ?");
        preparedStatement.setString(1, channelid);

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            bounty = resultSet.getInt("bounty");
        }
        resultSet.close();
        preparedStatement.close();
        return bounty;
    }

    public void setBounty(String channelid, int bounty) throws SQLException {
        PreparedStatement preparedStatement = this.mysql.getStatement("UPDATE tickets SET bounty = ? WHERE channelid = ?");
        preparedStatement.setInt(1, bounty);
        preparedStatement.setString(2, channelid);
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
