package com.lifeplus.lifeplus.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"patient_id"})
)
public class Team implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @NotNull
    @OneToOne
    private User patient;

    @OneToMany(mappedBy = "team", targetEntity = TeamUser.class)
    private List<TeamUser> teamUsers;

    public Team() {
        teamUsers = new ArrayList<>();
    }

    public Team(User patient) {
        this.patient = patient;
        teamUsers = new ArrayList<>();
    }

    public Team(@NotNull User patient, List<TeamUser> teamUsers) {
        this.patient = patient;
        this.teamUsers = teamUsers;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }
}
