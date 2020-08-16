package com.annihilator.distributedhashing.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ConsistentHashingConfiguration extends Configuration {

  @Valid
  @NotNull
  @JsonProperty
  private int sizeOfRing;

  @Valid
  @NotNull
  @JsonProperty
  private int noOfVirtualNodes;

  @Valid
  @NotNull
  @JsonProperty
  private boolean debugging;

  public int getSizeOfRing() {
    return sizeOfRing;
  }

  public void setSizeOfRing(int sizeOfRing) {
    this.sizeOfRing = sizeOfRing;
  }

  public boolean isDebugging() {
    return debugging;
  }

  public void setDebugging(boolean debugging) {
    this.debugging = debugging;
  }

  public int getNoOfVirtualNodes() {
    return noOfVirtualNodes;
  }

  public void setNoOfVirtualNodes(int noOfVirtualNodes) {
    this.noOfVirtualNodes = noOfVirtualNodes;
  }
}
