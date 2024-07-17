package com.team_service.service;

import com.team_service.entity.Team;
import com.team_service.exception.ResourceNotFoundException;
import com.team_service.repository.TeamRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public Page<Team> getAllTeams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return teamRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElse(null);
    }

    @Transactional
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Transactional
    public Team update(Long teamId, Team team) {
        Team dbTeam = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found for id :: " + teamId));
        BeanUtils.copyProperties(team, dbTeam, "id");
        return teamRepository.save(dbTeam);
    }

    @Transactional
    public void deleteTeamById(Long teamId) {
        teamRepository.deleteById(teamId);
    }
}
