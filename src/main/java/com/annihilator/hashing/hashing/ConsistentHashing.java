package com.annihilator.hashing.hashing;

import com.annihilator.hashing.configuration.ConsistentHashingConfiguration;
import com.annihilator.hashing.dto.Node;
import com.annihilator.hashing.dto.NodeType;
import com.annihilator.hashing.dto.ResponseStructure;
import com.annihilator.hashing.dto.Result;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsistentHashing implements Hashing {

  private static final Logger logger = LoggerFactory.getLogger(ConsistentHashing.class);

  private static volatile ConsistentHashing instance;
  private final int RING_SIZE;
  private final List<Node> ring;
  private final Map<String, List<Integer>> connectedNodes;
  private final ConsistentHashingConfiguration config;

  private ConsistentHashing(ConsistentHashingConfiguration config) {

    this.RING_SIZE = config.getSizeOfRing();
    this.ring = new LinkedList<>();
    this.connectedNodes = new HashMap<>();
    this.config = config;

    setUpHashing();
  }

  public static ConsistentHashing getInstance(ConsistentHashingConfiguration config) {

    if (instance == null) {
      synchronized (ConsistentHashing.class) {
        if (instance == null) {
          instance = new ConsistentHashing(config);
        }
      }
    }

    return instance;
  }

  private void setUpHashing() {
    int count = 0;
    while (count < RING_SIZE) {
      ring.add(null);
      count++;
    }
  }

  private long getMurmur3_128Hashing(String node) {
    return com.google.common.hash.Hashing.murmur3_128().hashString(node, Charset.forName("UTF-8"))
        .asLong();
  }

  private int getNodeOrReqPosition(String node) {

    Long hashValue = getMurmur3_128Hashing(node);
    int remainder = (int) (hashValue % RING_SIZE);

    return Math.abs(remainder);
  }

  private void logRingStatus() {
    for (String node : connectedNodes.keySet()) {
      logger.info("Node: " + node + " Position:" + connectedNodes.get(node));
    }
  }

  private int checkIfNodeCanBeAdded(String node) {
    int totalNodeInTheCluster = connectedNodes.size();
    int noOfVirtualNodesPerNode = config.getNoOfVirtualNodes();

    if (totalNodeInTheCluster * (noOfVirtualNodesPerNode + 1) == RING_SIZE) {
      logger.error("Ring is full can't add more node to the cluster");

      return -1;
    }

    if (connectedNodes.keySet().contains(node)) {
      logger.error("node already added to the network");

      return 0;
    }

    return 1;
  }

  @Override
  public ResponseStructure addNode(String nodeName) {

    int status = checkIfNodeCanBeAdded(nodeName);

    if(status == 0) {
      return ResponseStructure.getNodeExistErrorResponse();
    } else if (status == -1) {
      return ResponseStructure.getRingFullErrorResponse();
    }

    List<Integer> positions = new ArrayList<>();
    connectedNodes.put(nodeName, positions);
    String tempNode = nodeName;

    boolean isActualServerPlaced = false;

    while (true) {

      int pos = getNodeOrReqPosition(tempNode);

      if (null == ring.get(pos)) {

        Node node = new Node();

        if(isActualServerPlaced) {

          node.setAddress(nodeName);
          node.setType(NodeType.REAL);
          isActualServerPlaced = true;
        } else {

          node.setAddress(nodeName);
          node.setType(NodeType.VIRTUAL);
        }

        ring.set(pos, node);

        positions.add(pos);

        if(positions.size() == config.getNoOfVirtualNodes() + 1) { // one is for the actual node
          break;
        }
      }
      tempNode += "-";
    }

    if (config.isDebugging()) {
      logRingStatus();
    }

    return ResponseStructure.getNodeAddedResponse();
  }

  @Override
  public ResponseStructure getDestination(String reqId) {

    Map<String, String> body = new HashMap<>();

    if (connectedNodes.isEmpty()) {
      logger.error("No node is connected to the cluster");

      return ResponseStructure.getNoNodeExistInClusterErrorResponse();
    }

    int pos = getNodeOrReqPosition(reqId);

    while (true) {

      if (null != ring.get(pos)) {
        body.put("node", ring.get(pos).getAddress());

        return new ResponseStructure(Result.SUCCEEDED, "Node found in the ring", body);
      }

      pos++;

      if (pos == RING_SIZE) {
        pos = 0;
      }
    }
  }

  @Override
  public ResponseStructure removeNode(String node) {

    if (!connectedNodes.keySet().contains(node)) {
      logger.error("There is no node: " + node + " connected to the cluster");

      return ResponseStructure.getNodeNotFoundErrorResponse();
    }

    List<Integer> positions = connectedNodes.get(node);
    connectedNodes.remove(node);

    for(Integer pos: positions) {
      ring.set(pos, null);
    }

    if(config.isDebugging()) {
      logRingStatus();
    }

    return ResponseStructure.getNodeRemovedResponse();
  }
}
