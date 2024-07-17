package com.team_service.service;

import com.team_service.entity.Team;
import com.team_service.exception.ResourceNotFoundException;
import com.team_service.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private Team updatedTeam;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Original Team");
        team.setCaptain("Original Captain");

        updatedTeam = new Team();
        updatedTeam.setName("Updated Team");
        updatedTeam.setCaptain("Updated Captain");
    }

    @Test
    void testGetAllTeams() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamPage = new PageImpl<>(Collections.singletonList(new Team()));
        when(teamRepository.findAll(pageable)).thenReturn(teamPage);

        Page<Team> result = teamService.getAllTeams(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetTeamById() {
        Team team = new Team();
        team.setId(1L);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team found = teamService.getTeamById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testSave() {
        Team team = new Team();
        team.setName("India");
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        Team savedTeam = teamService.save(team);

        assertNotNull(savedTeam);
        verify(teamRepository, times(1)).save(team);
        assertEquals("India", savedTeam.getName());
    }

    @Test
    void testUpdate() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);

        Team result = teamService.update(1L, updatedTeam);

        assertNotNull(result);
        assertEquals("Updated Team", result.getName());
        assertEquals("Updated Captain", result.getCaptain());

        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testUpdateTeamNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
             teamService.update(1L, updatedTeam);
        });

        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(0)).save(any(Team.class));
    }

    @Test
    public void testDeleteTeamById() {
        doNothing().when(teamRepository).deleteById(1L);
        teamService.deleteTeamById(1L);
        verify(teamRepository, times(1)).deleteById(1L);
    }

}
