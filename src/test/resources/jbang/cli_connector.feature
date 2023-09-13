@cli_connector
Feature: Test camel-cli-connector

  Background:
    Given camel jbang is installed

  Scenario: User execute e2e jbang with csb productized artifacts
    Given user configure camel property "deps" with value "org.apache.camel.springboot:camel-timer-starter,camel:management,camel:cli-connector"
    And user configure camel property "runtime" with value "spring-boot"
    And user configure camel property "gav" with value "com.foo:acme:1.0-SNAPSHOT"
    And user configure camel property "camel-version" with passed value
    And user configure camel property "additional-properties=openshift-maven-plugin-version" with passed value
    And user configure camel property "camel-spring-boot-version" with passed value
    And user execute "camel init test.yaml --directory=app"
    Then file "test.yaml" is created
    When user execute "camel run app/test.yaml" in parallel
    Then integration "test" logs expected camel version
    And user execute "mkdir -p app/export"
    And user execute "camel export --dir app/export"
    When user execute "cat app/export/pom.xml"
    Then check the SB platform BOM gav
    And user execute "camel stop test"
