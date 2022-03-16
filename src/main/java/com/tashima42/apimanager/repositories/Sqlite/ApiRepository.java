/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tashima42.apimanager.repositories.Sqlite;

import com.tashima42.apimanager.main.Api;
import com.tashima42.apimanager.main.Team;
import com.tashima42.apimanager.main.User;
import com.tashima42.apimanager.repositories.IApiRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author tashima
 */
public class ApiRepository implements IApiRepository {
    private final Connection conn;

    public ApiRepository() {
        this.conn = Connect.connect();
    }

    @Override
    public ArrayList<Api> getAll() {
        String sql = "SELECT api.name AS name, api.description, user.name AS owner, team.name AS team \n"
                + "FROM api JOIN user, team WHERE api.owner = user.id AND api.team = team.id LIMIT 1;";
        ArrayList<Api> apis = new ArrayList<>();
        try ( PreparedStatement ptsmt = this.conn.prepareStatement(sql)) {
            ResultSet res = ptsmt.executeQuery();

            while (res.next()) {
                String apiName = res.getString("name");
                String apiDescription = res.getString("description");
                String ownerName = res.getString("owner");
                String teamName = res.getString("team");

                User owner = new User(ownerName, null, null);
                Team team = new Team(teamName, null);
                Api api = new Api(apiName, apiDescription, owner, team);
                System.out.println(api);
                apis.add(api);
            }
            return apis;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new Error("Failed to get apis");
        }
    }
}