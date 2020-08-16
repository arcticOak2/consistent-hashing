package com.annihilator.distributedhashing.hashing;

import com.annihilator.distributedhashing.configuration.ConsistentHashingConfiguration;
import com.annihilator.distributedhashing.dto.ResponseStructure;
import com.annihilator.distributedhashing.dto.Result;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ConsistentHashingTest {

  @Mock
  ConsistentHashingConfiguration configuration;
  private ConsistentHashing consistentHashing;

  @Before
  public void initialise() {

    Mockito.when(configuration.getSizeOfRing()).thenReturn(2);
    Mockito.when(configuration.isDebugging()).thenReturn(false);
    Mockito.when(configuration.getNoOfVirtualNodes()).thenReturn(0);
    consistentHashing = ConsistentHashing.getInstance(configuration);
  }

  @After
  public void tearDown() {

    Whitebox.setInternalState(consistentHashing, "instance", null);
  }

  @Test
  public void testAddNodeWhenListIsNotFull() {

    ResponseStructure structure = consistentHashing
        .addNode("10.0.0.19:9200");

    Assert.assertEquals(Result.SUCCEEDED, structure.getResult());
  }

  @Test
  public void testWhenSameNodeIsIngestedTwice() {

    consistentHashing
        .addNode("10.0.0.19:9200");

    ResponseStructure structure = consistentHashing
        .addNode("10.0.0.19:9200");

    Assert.assertEquals(Result.FAILED, structure.getResult());
  }

  @Test
  public void testAddNodeWhenListIsFull() {

    consistentHashing
        .addNode("10.0.0.19:9200");
    consistentHashing
        .addNode("10.0.0.20:9200");

    ResponseStructure structure = consistentHashing
        .addNode("10.0.0.21:9200");

    Assert.assertEquals(Result.FAILED, structure.getResult());
  }

  @Test
  public void testGetDestinationWhenNodeExist() {

    consistentHashing
        .addNode("10.0.0.19:9200");
    consistentHashing
        .addNode("10.0.0.20:9200");
    ResponseStructure dest = consistentHashing.getDestination("dummyRequestId");

    Assert.assertEquals(Result.SUCCEEDED, dest.getResult());
  }

  @Test
  public void testGetDestinationWhenNoNodeExist() {

    ResponseStructure dest = consistentHashing.getDestination("dummyRequestId");

    Assert.assertEquals(Result.FAILED, dest.getResult());
  }

  @Test
  public void testRemoveNodeWhenNodeExist() {

    consistentHashing.addNode("10.0.0.19:9200");
    ResponseStructure response = consistentHashing.removeNode("10.0.0.19:9200");

    Assert.assertEquals(Result.SUCCEEDED, response.getResult());
  }

  @Test
  public void testRemoveNodeWhenNoNodeExist() {

    ResponseStructure response = consistentHashing.removeNode("10.0.0.19:9200");

    Assert.assertEquals(Result.FAILED, response.getResult());
  }
}
