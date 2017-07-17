package com.ernesttech.example.dbtdd;

import com.ernesttech.example.dbtdd.domain.jooq.generated.tables.records.TestsRecord;
import com.ernesttech.example.dbtdd.managers.TestsManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRunner {

    @Autowired
    private TestsManager testManager;

    private List<TestsRecord> testCases;

    @Before
    public void setUp() throws Exception {
        if (testCases == null) {
            testCases = testManager.findAllTests();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void run() {

        for (TestsRecord testsRecord : testCases) {

        }

    }

}