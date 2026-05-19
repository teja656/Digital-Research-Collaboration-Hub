package com.researchsphere.dao;

import com.researchsphere.entity.TeamMember;

import java.util.List;

public class TeamMemberDao extends GenericDao<TeamMember> {

    public TeamMemberDao() {
        super(TeamMember.class);
    }

    public List<TeamMember> findByTeam(Long teamId) {
        return executeList(session -> session.createQuery(
                "from TeamMember tm where tm.teamId = :tid", TeamMember.class)
                .setParameter("tid", teamId)
                .list());
    }

    public List<TeamMember> findByUser(Long userId) {
        return executeList(session -> session.createQuery(
                "from TeamMember tm where tm.userId = :uid", TeamMember.class)
                .setParameter("uid", userId)
                .list());
    }

    public TeamMember findByTeamAndUser(Long teamId, Long userId) {
        return execute(session -> session.createQuery(
                "from TeamMember tm where tm.teamId = :tid and tm.userId = :uid", TeamMember.class)
                .setParameter("tid", teamId)
                .setParameter("uid", userId)
                .uniqueResult());
    }

    public void deleteByTeamAndUser(Long teamId, Long userId) {
        executeVoid(session -> {
            TeamMember tm = findByTeamAndUser(teamId, userId);
            if (tm != null) {
                session.delete(session.contains(tm) ? tm : session.merge(tm));
            }
            return null;
        });
    }
}
