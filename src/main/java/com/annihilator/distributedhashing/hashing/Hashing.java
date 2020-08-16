package com.annihilator.distributedhashing.hashing;

import com.annihilator.distributedhashing.dto.ResponseStructure;

public interface Hashing {

  public ResponseStructure addNode(String node);

  public ResponseStructure removeNode(String node);

  public ResponseStructure getDestination(String reqId);
}
