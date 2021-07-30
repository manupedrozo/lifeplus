package com.lifeplus.lifeplus.model.form;

import com.lifeplus.lifeplus.model.TeamRole;

public class TeamUserForm {
    private int userId;
    private TeamRole role;
    private int teamId;

    public TeamUserForm() {
    }

    public TeamUserForm(int userId, TeamRole role, int teamId) {
        this.userId = userId;
        this.role = role;
        this.teamId = teamId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TeamRole getRole() {
        return role;
    }

    public void setRole(TeamRole role) {
        this.role = role;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
