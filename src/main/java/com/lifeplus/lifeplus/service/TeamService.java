package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.*;
import com.lifeplus.lifeplus.model.form.TeamForm;
import com.lifeplus.lifeplus.model.form.TeamUserForm;
import com.lifeplus.lifeplus.repository.PatientRepository;
import com.lifeplus.lifeplus.repository.TeamRepository;
import com.lifeplus.lifeplus.repository.TeamUserRepository;
import com.lifeplus.lifeplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private UserRepository userRepository;
    private TeamUserRepository teamUserRepository;
    private PatientRepository patientRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, TeamUserRepository teamUserRepository, PatientRepository patientRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamUserRepository = teamUserRepository;
        this.patientRepository = patientRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAllByPatient_ActiveIsTrue();
    }

    public Page<Team> findAllPagedAndFiltered(int page, int size, String name) {
        return teamRepository.findPagedAndFilteredAndSorted(PageRequest.of(page, size), name);
    }

    public Optional<Team> findById(int id) {
        return teamRepository.findFirstByIdAndPatient_ActiveIsTrue(id);
    }

    public List<Team> findByUserId(int id) {
        Optional<User> userOptional = userRepository.findFirstByIdAndActiveIsTrue(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getType() == UserType.USER) {
                return teamRepository.findAllByUserPatientId(id);
            } else {
                return teamRepository.findAllByUserTeamUserId(id);
            }
        } else {
            return new ArrayList<>();
        }
    }

    private Page<Team> findByUserIdPaged(int id, int page, int size, String name) {
        return teamRepository.findPagedAndFilteredAndSorted(PageRequest.of(page, size), id, name);
    }

    public List<Team> findCurrentTeams() {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return findByUserId(user.getId());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Patient> findCurrentPatients() {
        List<Team> teams = findCurrentTeams();
        List<Patient> result = new ArrayList<>();
        for (Team team: teams) {
            result.add(
                    patientRepository.findByUser_IdAndUser_ActiveIsTrue(team.getPatient().getId())
                            .orElseThrow(() -> new NoSuchElementException(String.format("Patient: %d does not exist.", team.getPatient().getId()))
            ));
        }
        return result;
    }

    public Page<Team> findCurrentTeamsPagedAndFiltered(int page, int size, String name) {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return findByUserIdPaged(user.getId(), page, size, name);
        } else {
            return Page.empty();
        }
    }

    private void updateTeamFromForm(Team team, TeamForm teamForm) {
        List<TeamUser> oldTeamUsers = team.getTeamUsers();
        List<TeamUserForm> teamUserForms = teamForm.getTeamUserForms();
        List<TeamUser> newTeamUsers = new ArrayList<>();

        if (!teamUserForms.isEmpty()) {
            teamUserForms.forEach(u -> {
                User user = getUser(u.getUserId());
                Optional<TeamUser> teamUser = oldTeamUsers.stream()
                        .filter(t -> t.getUser().getId() == u.getUserId()).findFirst();

                if (teamUser.isPresent()) {
                    TeamUser previous = teamUser.get();
                    if (previous.getRole() != u.getRole()) {
                        previous.setRole(u.getRole());
                        teamUserRepository.save(previous);
                    }
                    newTeamUsers.add(previous);
                } else {
                    TeamUser newTeamUser = new TeamUser(user, u.getRole(), team);
                    teamUserRepository.save(newTeamUser);
                    newTeamUsers.add(newTeamUser);
                }
            });
        }
        team.getTeamUsers().forEach(u -> {
            if (!newTeamUsers.contains(u)) teamUserRepository.delete(u);
        });
        team.setTeamUsers(newTeamUsers);
    }

    private User getUser(int id) {
        return userRepository.findFirstByIdAndActiveIsTrue(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User: %d does not exist.", id)));
    }

    // Helper
    public int save(Team team) {
        teamRepository.save(team);
        return team.getId();
    }

    public int save(TeamForm teamForm) {
        Team team = new Team();
        updateTeamFromForm(team, teamForm);
        teamRepository.save(team);
        return team.getId();
    }

    public boolean update(int id, TeamForm teamForm) {
        final Optional<Team> byId = findById(id);
        if (byId.isPresent()) {
            Team team = byId.get();
            updateTeamFromForm(team, teamForm);
            teamRepository.save(team);
            return true;
        }
        return false;
    }

    public boolean removeCurrentUserFromTeam(int teamId) {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);
        if (userOptional.isPresent()) {
            List<TeamUser> teamUsers = teamUserRepository.findAllByUser_IdAndUser_ActiveIsTrue(userOptional.get().getId());
            if (teamUsers.size() > 0) {
                Optional<TeamUser> teamUserOptional = teamUsers.stream().filter(u -> u.getTeam().getId() == teamId).findFirst();
                if (teamUserOptional.isPresent()) {
                    teamUserRepository.delete(teamUserOptional.get());
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean deleteAllTeamUsersByUserId(int id) {
        teamUserRepository.findAllByUser_Id(id).forEach(tu -> teamUserRepository.delete(tu));
        return true;
    }
}
