package com.annihilator.distributedhashing.dto;

public class Node {

  private String address;

  private NodeType type;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public NodeType getType() {
    return type;
  }

  public void setType(NodeType type) {
    this.type = type;
  }
}
