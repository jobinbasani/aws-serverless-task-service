package com.jobinbasani.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskRequest extends Task{
    private String action;
}
