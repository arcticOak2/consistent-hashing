package com.annihilator.hashing.application;

import com.annihilator.hashing.configuration.ConsistentHashingConfiguration;
import com.annihilator.hashing.controller.ConsistentHashingController;
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

