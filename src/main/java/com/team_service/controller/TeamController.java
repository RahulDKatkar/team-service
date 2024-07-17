package com.team_service.controller;

import com.team_service.entity.Team;
import com.team_service.exception.ResourceNotFoundException;
import com.team_service.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Team Service", description = "Operations for Team System")
public class TeamController {

    private Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity<Page<Team>> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching all Teams.");
        Page<Team> teams = teamService.getAllTeams(page, size);
        if (teams.getTotalElements() == 0) {
            logger.warn("No Teams not found.");
        }
        logger.info(String.format("Total ['%s'] number of Teams found.", teams.getTotalElements()));
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long teamId) {
        logger.info(String.format("Finding Team for id ['%s']", teamId));
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            throw new ResourceNotFoundException(String.format("Team not found for id ['%s'] ", teamId));
        }
        logger.info(String.format("Team ['%s'] found for id ['%s']", team.getName(), teamId));
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Team> save(@RequestBody Team team) {
        logger.info(String.format("Creating Team for Name ['%s']", team.getName()));
        Team createdTeam = teamService.save(team);
        logger.info(String.format("Successfully created Team for Name ['%s']", team.getName()));
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> update(@PathVariable Long id, @RequestBody Team team) {
        logger.info(String.format("Updating Team for Name ['%s']", team.getName()));
        Team updatedTeam = teamService.update(id, team);
        logger.info(String.format("Successfully updated Team for Name ['%s']", team.getName()));
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        teamService.deleteTeamById(id);
        logger.info(String.format("Successfully Deleted Team for Id ['%s']", id));
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
