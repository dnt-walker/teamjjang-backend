package com.example.taskmanager.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class ModifiedTimeDto extends RegisteredTimeDto implements Serializable {

    protected String modifiedDate;

}
