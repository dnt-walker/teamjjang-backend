package com.example.taskmanager.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTask is a Querydsl query type for Task
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = 2109764920L;

    public static final QTask task = new QTask("task");

    public final SetPath<String, StringPath> assignees = this.<String, StringPath>createSet("assignees", String.class, StringPath.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> completionDate = createDate("completionDate", java.time.LocalDate.class);

    public final StringPath creator = createString("creator");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Job, QJob> jobs = this.<Job, QJob>createList("jobs", Job.class, QJob.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final DatePath<java.time.LocalDate> plannedEndDate = createDate("plannedEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QTask(String variable) {
        super(Task.class, forVariable(variable));
    }

    public QTask(Path<? extends Task> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTask(PathMetadata metadata) {
        super(Task.class, metadata);
    }

}

