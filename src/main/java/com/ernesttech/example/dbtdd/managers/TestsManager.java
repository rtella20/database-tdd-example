package com.ernesttech.example.dbtdd.managers;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import org.jooq.Table;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ernesttech.example.dbtdd.domain.jooq.generated.Tables.TESTS;


@Repository
public class TestsManager extends AbstractJooqManager<TestsRecord> {

    public List<TestsRecord> findAllTests() {
        return context
                .select()
                .from(TESTS)
                .fetchInto(TestsRecord.class);
    }

    @Override
    public Table getTable() {
        return TESTS;
    }
}
