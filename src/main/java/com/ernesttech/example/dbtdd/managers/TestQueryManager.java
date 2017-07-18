package com.ernesttech.example.dbtdd.managers;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TestQueryManager {

    @Autowired
    protected DSLContext context;

    public Result<Record> executeQuery(final String query) {
        return context.fetch(query);
    }

}
