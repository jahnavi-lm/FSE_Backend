package com.fse.FSE_Backend_Proj.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConditionGroup {
    private String operator; // "AND" or "OR"
    private List<Condition> conditions;
}
