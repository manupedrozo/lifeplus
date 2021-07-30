package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.Patient;
import com.lifeplus.lifeplus.model.Team;
import com.lifeplus.lifeplus.model.form.TeamForm;
import com.lifeplus.lifeplus.service.TeamService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/paged")
    public ResponseEntity<Page<Team>> getAllTeamsPagedAndFiltered(
            @ApiParam(value = "Query param for 'page number'") @Valid @RequestParam(value = "page") int page,
            @ApiParam(value = "Query param for 'page size'") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @ApiParam(value = "Query param for 'name' filter") @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        if (size == 0) size = 10;
        Page<Team> teamPage = teamService.findAllPagedAndFiltered(page, size, name);
        return ResponseEntity.ok(teamPage);
    }

    @GetMapping("/user/{id}")
    @Secured("ROLE_ADMIN")
    public List<Team> getByUserId(@PathVariable("id") int id) {
        return teamService.findByUserId(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") int id) {
        final Optional<Team> optional = teamService.findById(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/me")
    public List<Team> getCurrentTeams() {
        return teamService.findCurrentTeams();
    }

    @GetMapping(value = "/me/patient")
    @Secured({"ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<Patient> getCurrentPatients() {
        return teamService.findCurrentPatients();
    }

    @GetMapping(value = "/me/paged")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public ResponseEntity<Page<Team>> getCurrentTeamsPagedAndFiltered(
            @ApiParam(value = "Query param for 'page number'") @Valid @RequestParam(value = "page") int page,
            @ApiParam(value = "Query param for 'page size'") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @ApiParam(value = "Query param for 'name' filter") @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        if (size == 0) size = 10;
        Page<Team> teamPage = teamService.findCurrentTeamsPagedAndFiltered(page, size, name);
        return ResponseEntity.ok(teamPage);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity modifyTeamById(@PathVariable("id") int id, @Valid @RequestBody TeamForm teamForm) {
        return teamService.update(id, teamForm) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/leave/{id}")
    public ResponseEntity leaveTeam(@PathVariable("id") int teamId) {
        return teamService.removeCurrentUserFromTeam(teamId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
