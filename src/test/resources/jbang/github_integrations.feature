Feature: Test Github integrations

  Background:
    Given camel jbang is installed

  Scenario: User run route hosted on github
   Given user execute "camel run github:apache:camel-kamelets-examples:jbang/hello-java/Hey.java" in parallel
    Then integration "Cool" logs "Hello from Camel"
    And user execute "camel stop Cool"

  Scenario: User run route from gist
    Given user execute "camel run https://gist.github.com/davsclaus/477ddff5cdeb1ae03619aa544ce47e92" in parallel
    Then integration "mybeer" logs "Hello Camel from xml"
    And user execute "camel stop mybeer"

  Scenario: User download routes hosted on github
    Given user execute "camel init https://github.com/apache/camel-kamelets-examples/tree/main/jbang/dependency-injection --directory=app"
    When user execute "camel run app/Echo.java app/Hello.java" in parallel
    Then integration "Echo" logs "JackJack!! from Echo"
    And user execute "camel stop Echo"