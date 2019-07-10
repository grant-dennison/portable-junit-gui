package com.codehousing.junitgui.gui;

import org.junit.runner.Description;
import org.junit.runner.Request;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

public class TestTreeNode {

    private final Description description;

    public TestTreeNode(Class<?> clazz) {
        Request request = Request.aClass(clazz);
        this.description = request.getRunner().getDescription();
    }

    private TestTreeNode(Description description) {
        this.description = description;
    }

    public String getDescription() {
        if(!this.isLeaf()) {
            Class<?> clazz = this.description.getTestClass();
            if(clazz.isAnnotationPresent(TestDescription.class)) {
                return clazz.getAnnotation(TestDescription.class).value();
            }
            return clazz.getSimpleName();
        } else {
            Method m = this.getTestMethod();
            if(m.isAnnotationPresent(TestDescription.class)) {
                return m.getAnnotation(TestDescription.class).value();
            }
            return m.getName();
        }
    }

    public Iterable<TestTreeNode> getChildren() {
        return this.description.getChildren().stream().map(TestTreeNode::new).collect(Collectors.toList());
    }

    public Class<?> getTestClass() {
        return this.description.getTestClass();
    }

    public Method getTestMethod() {
        try {
            return this.description.getTestClass().getMethod(this.description.getMethodName());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public boolean isLeaf() {
        return !this.description.isSuite();
        // return this.description.isTest();
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
