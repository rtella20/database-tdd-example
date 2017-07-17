package com.ernesttech.example.dbtdd.managers;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public abstract class AbstractJooqManager<R extends UpdatableRecord<R>> {

    @Autowired
    protected DSLContext context;

    public abstract Table getTable();

}
