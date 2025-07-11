package com.fse.FSE_Backend_Proj.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionRule {

    private String action;     // "BUY" or "SELL"
    private Condition condition;
}
