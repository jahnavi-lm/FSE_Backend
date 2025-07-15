package com.fse.FSE_Backend_Proj.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompositeRule {
    private List<ConditionGroup> conditionGroups; // for grouped logic
    private List<Condition> conditions;           // for flat conditions (optional)
    private String action;
}

