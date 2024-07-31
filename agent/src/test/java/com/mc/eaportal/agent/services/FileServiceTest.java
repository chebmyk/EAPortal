package com.mc.eaportal.agent.services;

import com.mc.eaportal.agent.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FileServiceTest {

    @Test
    void getFolderTree() {
        FileService fileService = new FileService();
        Node node = fileService.getFolderTree("/Users/michael/Documents/Projects/Java/EAPortal/agent/src/main/java/com/mc/eaportal/agent");
        assertEquals("agent", node.getName());
//        assertTrue(node.isDirectory());
//        assertEquals("src/test/resources", node.getPath());
//        assertEquals(2, node.getChildren().size());
//        Node child1 = node.getChildren().stream().filter(n -> n.getName().equals("file1.txt")).findFirst().get();
//        assertFalse(child1.isDirectory());
//        assertEquals("src/test/resources/file1.txt", child1.getPath());
//        Node child2 = node.getChildren().stream().filter(n -> n.getName().equals("folder1")).findFirst().get();
//        assertTrue(child2.isDirectory());
//        assertEquals("src/test/resources/folder1", child2.getPath());
//        assertEquals(1, child2.getChildren().size());
//        Node child3 = child2.getChildren().stream().filter(n -> n.getName().equals("file2.txt")).findFirst().get();
//        assertFalse(child3.isDirectory());
//        assertEquals("src/test/resources/folder1/file2.txt", child3.getPath());
    }

}