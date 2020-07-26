package com.annihilator.hashing.hashing;

import com.annihilator.hashing.dto.ResponseStructure;

public interface Hashing {

  public ResponseStructure addNode(String node);

  public ResponseStructure removeNode(String node);

  public ResponseStructure getDestination(String reqId);
}
