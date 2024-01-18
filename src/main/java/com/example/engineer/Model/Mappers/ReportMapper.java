package com.example.engineer.Model.Mappers;

import com.example.engineer.Model.DTO.Report.ReportDTO;
import com.example.engineer.Model.ReportEntity;


public class ReportMapper {

    public static ReportDTO mapToDto(ReportEntity report) {
        return ReportDTO.builder()
                .id(report.getId())
                .content(report.getContent())
                .task(TaskMapper.mapToDto(report.getTask()))
                .build();
    }

    public static ReportEntity mapToEntity(ReportDTO reportDTO) {
        ReportEntity report = new ReportEntity();
        report.setId(reportDTO.getId());
        report.setContent(reportDTO.getContent());
        report.setTask(TaskMapper.mapToEntity(reportDTO.getTask()));
        return report;
    }
}
