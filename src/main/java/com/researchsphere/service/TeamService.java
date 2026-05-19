package com.researchsphere.service;

import com.researchsphere.dao.TeamDao;
import com.researchsphere.dao.TeamMemberDao;
import com.researchsphere.dao.UserDao;
import com.researchsphere.entity.Team;
import com.researchsphere.entity.TeamMember;
import com.researchsphere.entity.User;

import java.util.List;

public class TeamService {

    private final TeamDao teamDao = new TeamDao();
    private final TeamMemberDao teamMemberDao = new TeamMemberDao();
    private final UserDao userDao = new UserDao();
    private final ActivityService activityService = new ActivityService();

    public List<Team> listAll() {
        return teamDao.findAll();
    }

    public Team get(Long id) {
        return teamDao.findById(id);
    }

    public List<TeamMember> members(Long teamId) {
        return teamMemberDao.findByTeam(teamId);
    }

    public List<User> allUsers() {
        return userDao.findAll();
    }

    public void createTeam(Team team, User actor) {
        teamDao.save(team);
        TeamMember leader = new TeamMember();
        leader.setTeamId(team.getId());
        leader.setUserId(team.getLeaderId());
        leader.setMemberRole("LEADER");
        teamMemberDao.save(leader);
        activityService.log(actor, "CREATE_TEAM", "TEAM", team.getId(), "Created team: " + team.getName());
    }

    public void addMember(Long teamId, Long userId, String role, User actor) {
        if (teamMemberDao.findByTeamAndUser(teamId, userId) != null) {
            return;
        }
        TeamMember m = new TeamMember();
        m.setTeamId(teamId);
        m.setUserId(userId);
        m.setMemberRole(role != null ? role : "MEMBER");
        teamMemberDao.save(m);
        activityService.log(actor, "ADD_TEAM_MEMBER", "TEAM", teamId, "Added user " + userId);
    }

    public void removeMember(Long teamId, Long userId, User actor) {
        TeamMember tm = teamMemberDao.findByTeamAndUser(teamId, userId);
        if (tm != null) {
            teamMemberDao.delete(tm);
            activityService.log(actor, "REMOVE_TEAM_MEMBER", "TEAM", teamId, "Removed user " + userId);
        }
    }

    public void updateTeam(Long id, String name, String description, Long leaderId, User actor) {
        Team team = teamDao.findById(id);
        if (team == null) {
            return;
        }
        if (name != null && !name.isBlank()) {
            team.setName(name);
        }
        team.setDescription(description);
        if (leaderId != null) {
            team.setLeaderId(leaderId);
        }
        teamDao.save(team);
        activityService.log(actor, "UPDATE_TEAM", "TEAM", id, "Updated team: " + team.getName());
    }

    public void deleteTeam(Long id, User actor) {
        Team team = teamDao.findById(id);
        if (team == null) {
            return;
        }
        for (TeamMember m : teamMemberDao.findByTeam(id)) {
            teamMemberDao.delete(m);
        }
        teamDao.delete(team);
        activityService.log(actor, "DELETE_TEAM", "TEAM", id, "Deleted team");
    }
}
