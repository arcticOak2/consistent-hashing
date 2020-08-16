package com.annihilator.distributedhashing.application;

import com.annihilator.distributedhashing.configuration.ConsistentHashingConfiguration;
import com.annihilator.distributedhashing.controller.ConsistentHashingController;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ConsistentHashingApplication extends Application<ConsistentHashingConfiguration> {

  public static void main(final String[] args) throws Exception {
    new ConsistentHashingApplication().run(args);
  }

  @Override
  public String getName() {
    return "annihilator-consistent-hashing";
  }

  @Override
  public void run(ConsistentHashingConfiguration config, Environment environment) {
    environment.jersey().register(new ConsistentHashingController(config));
  }
}

