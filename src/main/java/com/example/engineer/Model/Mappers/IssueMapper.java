package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Issue.IssueDTO;
import com.example.engineer.Model.DTO.Issue.IssueDTODashboard;
import com.example.engineer.Model.IssueEntity;
import com.example.engineer.Model.TaskEntity;

import java.util.HashSet;
import java.util.Set;


public class IssueMapper {
    public static IssueDTO mapToDto(IssueEntity issue) {
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setId(issue.getId());
        issueDTO.setName(issue.getName());
        issueDTO.setDescription(issue.getDescription());
        if (issue.getTask() != null) {
            issueDTO.setTask(TaskMapper.mapToDto(issue.getTask()));
        }
        return issueDTO;
    }

    public static IssueEntity mapToEntity(IssueDTO issueDTO) {
        IssueEntity issue = new IssueEntity();
        if (issueDTO != null) {
            issue.setId(issueDTO.getId());
            issue.setName(issueDTO.getName());
            issue.setDescription(issueDTO.getDescription());

            if (issueDTO.getTask() != null) {
                issue.setTask(TaskMapper.mapToEntity(issueDTO.getTask()));
            }
        }
        return issue;
    }

    public static Set<IssueDTO> issueDtoList(TaskEntity task) {
        Set<IssueEntity> issues = task.getIssues();
        Set<IssueDTO> issueSet = new HashSet<>();

        if (issues.isEmpty()) {
            return null;
        }
        for (IssueEntity issueEntity : issues) {
            issueSet.add(issueDtoWithoutRelations(issueEntity));
        }

        return issueSet;
    }

    public static IssueDTO issueDtoWithoutRelations(IssueEntity issue) {
        return IssueDTO.builder()
                .id(issue.getId())
                .name(issue.getName())
                .description(issue.getDescription())
                .build();
    }

    public static IssueDTODashboard mapToDtoForDashboard(IssueEntity issue) {
        return IssueDTODashboard.builder()
                .id(issue.getId())
                .name(issue.getName())
                .description(issue.getName())
                .build();
    }

}
