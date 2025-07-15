package com.fse.FSE_Backend_Proj.parser;

import com.fse.FSE_Backend_Proj.model.CompositeRule;
import com.fse.FSE_Backend_Proj.model.Condition;
import com.fse.FSE_Backend_Proj.model.ConditionGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScriptParserService {

    public List<CompositeRule> parse(String script) {
        List<CompositeRule> rules = new ArrayList<>();
        String[] lines = script.split("\\n");

        Pattern pattern = Pattern.compile(
                "(\\w+)(?:\\((\\d+)\\))?\\s*(>=|<=|==|!=|>|<)\\s*(\\w+)?(?:\\((\\d+)\\))?"
        );

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+WHEN\\s+");
            if (parts.length != 2)
                throw new IllegalArgumentException("Invalid syntax: " + line);

            String action = parts[0].trim();
            String conditionExpr = parts[1].trim();

            List<ConditionGroup> conditionGroups = new ArrayList<>();

            String[] orGroups = conditionExpr.split("(?i)\\s+OR\\s+");
            for (String orGroup : orGroups) {
                String[] andConditions = orGroup.split("(?i)\\s+AND\\s+");
                List<Condition> conditions = new ArrayList<>();

                for (String condStr : andConditions) {
                    Matcher matcher = pattern.matcher(condStr.trim());

                    if (matcher.find()) {
                        String left = matcher.group(1);
                        Integer leftArg = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : null;
                        String operator = matcher.group(3);
                        String right = matcher.group(4);
                        Integer rightArg = matcher.group(5) != null ? Integer.parseInt(matcher.group(5)) : null;

                        if ((right == null || right.isEmpty()) && condStr.matches(".*\\d+.*")) {
                            right = "VALUE";
                            rightArg = Integer.parseInt(condStr.replaceAll("[^0-9-]", ""));
                        }

                        conditions.add(Condition.builder()
                                .left(left)
                                .leftArg(leftArg)
                                .operator(operator)
                                .right(right)
                                .rightArg(rightArg)
                                .build());
                    } else {
                        throw new IllegalArgumentException("Invalid condition: " + condStr);
                    }
                }

                conditionGroups.add(ConditionGroup.builder()
                        .operator("AND")
                        .conditions(conditions)
                        .build());
            }

            rules.add(CompositeRule.builder()
                    .action(action)
                    .conditionGroups(conditionGroups)
                    .build());
        }

        return rules;
    }
}
