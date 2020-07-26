package com.annihilator.hashing.dto;

import java.util.Map;

public class ResponseStructure {

  private Result result;

  private String message;

  private Map<String, String> body;

  public ResponseStructure(Result result, String message) {
    this.result = result;
    this.message = message;
  }

  public ResponseStructure(Result result, String message, Map<String, String> body) {
    this.result = result;
    this.message = message;
    this.body = body;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, String> getBody() {
    return body;
  }

  public void setBody(Map<String, String> body) {
    this.body = body;
  }

  public static ResponseStructure getNodeAddedResponse() {
    return new ResponseStructure(Result.SUCCEEDED, "Node added to the cluster");
  }

  public static ResponseStructure getNodeRemovedResponse() {
    return new ResponseStructure(Result.SUCCEEDED, "Node successfully removed from the cluster");
  }

  public static ResponseStructure getRingFullErrorResponse() {
    return new ResponseStructure(Result.FAILED, "There is no space left in Ring");
  }

  public static ResponseStructure getNodeExistErrorResponse() {
    return new ResponseStructure(Result.FAILED, "Node already exist");
  }

  public static ResponseStructure getNodeNotFoundErrorResponse() {
    return new ResponseStructure(Result.FAILED, "Node not found");
  }

  public static ResponseStructure getNoNodeExistInClusterErrorResponse() {
    return new ResponseStructure(Result.FAILED, "No node is connected to the cluster");
  }
}
