package com.lifeplus.lifeplus.model.form;

import java.util.List;

public class TeamForm {
    private List<TeamUserForm> teamUserForms;

    public TeamForm() {
    }

    public TeamForm(List<TeamUserForm> teamUserForms) {
        this.teamUserForms = teamUserForms;
    }

    public List<TeamUserForm> getTeamUserForms() {
        return teamUserForms;
    }

    public void setTeamUserForms(List<TeamUserForm> teamUserForms) {
        this.teamUserForms = teamUserForms;
    }
}
