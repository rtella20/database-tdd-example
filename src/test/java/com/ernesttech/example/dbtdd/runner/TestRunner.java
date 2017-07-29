package com.ernesttech.example.dbtdd.runner;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestQueryManager;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestRunner {

    @Autowired
    private TestsManager testManager;

    @Autowired
    private TestQueryManager testQueryManager;

    @TestFactory
    public Collection<DynamicTest> executeTests() {

        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        List<TestsRecord> testCases = testManager.findAllTests();

        for (TestsRecord testsRecord : testCases) {

            Executable executable = () -> {

                Result<Record> results = testQueryManager.executeQuery(testsRecord.getTest());

                assertThat(getActualResult(results), is(getExpectedResult(testsRecord)));

            };

            DynamicTest dynamicTest = dynamicTest(testsRecord.getName(), executable);

            dynamicTests.add(dynamicTest);
        }

        return dynamicTests;
    }


    private String getActualResult(final Result<Record> results) {
        return String.valueOf(results.getValues(0).get(0));
    }

    private String getExpectedResult(final TestsRecord testsRecord) {
        return String.valueOf(testsRecord.getExpected());
    }

}