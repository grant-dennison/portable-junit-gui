package com.codehousing.example.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ExampleTest.class,
        AnotherTest.class
})
public class ExampleSuite {
}
