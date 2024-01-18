package com.example.engineer.Service;

import com.example.engineer.Model.DTO.Issue.IssueDTO;
import com.example.engineer.Model.DTO.Issue.IssueDTODashboard;
import com.example.engineer.Model.DTO.Task.TaskDTO;
import com.example.engineer.Model.IssueEntity;
import com.example.engineer.Model.Mappers.IssueMapper;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.TaskEntity;
import com.example.engineer.Repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

    public IssueDTO create(IssueDTO issue) {
        IssueEntity mappedIssue = IssueMapper.mapToEntity(issue);
        IssueEntity savedIssue = issueRepository.save(mappedIssue);
        return IssueMapper.mapToDto(savedIssue);
    }

    public Optional<IssueDTO> findById(Long id) {
        return issueRepository
                .findById(id)
                .map(IssueMapper::mapToDto);
    }

    public Set<IssueDTODashboard> findAllGlobalIssue() {
        Set<IssueDTODashboard> globalIssues = new HashSet<>();
        Set<IssueEntity> issues = issueRepository.findAllByTaskIsNull();

        if (!issues.isEmpty()) {
            for (IssueEntity issue : issues) {
                globalIssues.add(IssueMapper.mapToDtoForDashboard(issue));
            }
        }
        return globalIssues;
    }


    public void addIssueToTask(IssueDTO issueDTO, TaskDTO taskDTO) {
        IssueEntity issue = IssueMapper.mapToEntity(issueDTO);
        TaskEntity task = TaskMapper.mapToEntity(taskDTO);

        issue.setTask(task);
        issueRepository.save(issue);
    }

    @Transactional
    public void delete(IssueDTO issueDTO) {
        IssueEntity issue = IssueMapper.mapToEntity(issueDTO);
        issue.setTask(null);

        issueRepository.save(issue);
        issueRepository.delete(issue);
    }

    public void update(Long id, IssueDTO issueDTO) {
        issueDTO.setId(id);
        IssueEntity issue = IssueMapper.mapToEntity(issueDTO);
        issueRepository.save(issue);
    }
}
